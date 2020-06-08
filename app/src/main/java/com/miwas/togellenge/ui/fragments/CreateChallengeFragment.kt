package com.miwas.togellenge.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.miwas.togellenge.R
import com.miwas.togellenge.ui.activities.AuthActivity

class CreateChallengeFragment : Fragment() {

	private lateinit var textWayButton: ImageButton
	private lateinit var photoWayButton: ImageButton
	private lateinit var videoWayButton: ImageButton

	private lateinit var readyButton: Button

	private lateinit var titleTextInput: TextInputEditText
	private lateinit var descriptionEditText: TextInputEditText

	private lateinit var confirmationWayWarning: TextView

	private lateinit var dataBaseFirebase: FirebaseFirestore
	private lateinit var fireBaseAuth: FirebaseAuth

	private var selectedWay = -1

	private lateinit var wayButtons: Array<Pair<Int, ImageButton>>

	private val waysOfConfirmation = arrayOf("text", "photo", "video")

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val root = inflater.inflate(R.layout.fragment_create_challenge, container, false)
		textWayButton = root.findViewById(R.id.text_way_button)
		photoWayButton = root.findViewById(R.id.photo_way_button)
		videoWayButton = root.findViewById(R.id.video_way_button)
		titleTextInput = root.findViewById(R.id.title_edit_text)
		descriptionEditText = root.findViewById(R.id.description_edit_text)
		confirmationWayWarning = root.findViewById(R.id.confirmation_way_warning)

		readyButton = root.findViewById(R.id.ready_button)
		wayButtons = arrayOf(Pair(0, textWayButton), Pair(1, photoWayButton), Pair(2, videoWayButton))
		textWayButton.setOnClickListener {
			changeActiveButton(0, root.context)
		}

		photoWayButton.setOnClickListener {
			changeActiveButton(1, root.context)
		}

		videoWayButton.setOnClickListener {
			changeActiveButton(2, root.context)
		}

		readyButton.setOnClickListener {
			if (!checkFields()) {
				addChallenge(titleTextInput.text.toString(), descriptionEditText.text.toString())
			}
		}

		dataBaseFirebase = Firebase.firestore
		fireBaseAuth = Firebase.auth

		return root
	}

	override fun onStart() {
		super.onStart()
		if (fireBaseAuth.currentUser == null) {
			val intent = Intent(context, AuthActivity::class.java)
			startActivity(intent)
		}
	}

	private fun changeActiveButton(selectedWayNumber: Int, context: Context) {
		selectedWay = selectedWayNumber
		confirmationWayWarning.visibility = GONE

		wayButtons.forEach { button ->
			if (button.first == selectedWayNumber) {
				button.second.background = context.getDrawable(R.drawable.circle_button_active)
			} else {
				button.second.background = context.getDrawable(R.drawable.circle_button_inactive)
			}
		}
	}

	private fun addChallenge(name: String, description: String) {

		val challenge = hashMapOf(
			"date" to Timestamp.now(),
			"accept_method" to waysOfConfirmation[selectedWay],
			"description" to description,
			"author" to fireBaseAuth.currentUser?.uid,
			"name" to name,
			"participants" to listOf(fireBaseAuth.currentUser?.uid)
		)

		dataBaseFirebase.collection("challenges")
			.add(challenge)
			.addOnSuccessListener { documentReference ->
				Log.d("DB", "DocumentSnapshot added with ID: ${documentReference.id}")
			}
			.addOnFailureListener { e ->
				Log.w("DB", "Error adding document", e)
			}

	}

	private fun checkFields(): Boolean {
		var isInErrorState = false

		if (titleTextInput.text.isNullOrEmpty()) {
			titleTextInput.error = "Введите название"
			isInErrorState = true
		}

		if (descriptionEditText.text.isNullOrEmpty()) {
			descriptionEditText.error = "Введите описание"
			isInErrorState = true
		}

		if (selectedWay == -1) {
			confirmationWayWarning.visibility = VISIBLE
			isInErrorState = true
		}

		return isInErrorState
	}
}