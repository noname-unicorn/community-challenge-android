package com.miwas.togellenge.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.miwas.togellenge.R

class CreateChallengeFragment : Fragment() {

	private lateinit var textWayButton: ImageButton
	private lateinit var photoWayButton: ImageButton
	private lateinit var videoWayButton: ImageButton

	private lateinit var titleTextInput: TextInputEditText
	private lateinit var descriptionEditText: TextInputEditText

	private var selectedWay = -1

	private lateinit var wayButtons: Array<Pair<Int, ImageButton>>

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

		return root
	}

	private fun changeActiveButton(selectedWayNumber: Int, context: Context) {
		wayButtons.forEach { button ->
			if (button.first == selectedWayNumber) {
				button.second.background = context.getDrawable(R.drawable.circle_button_active)
			} else {
				button.second.background = context.getDrawable(R.drawable.circle_button_inactive)
			}
		}
	}
}