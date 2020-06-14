package com.miwas.togellenge.ui.fragments.creation

import android.content.Intent
import android.os.Bundle
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
import com.miwas.togellenge.R
import com.miwas.togellenge.presentation.presenter.CreateChallengePresenter
import com.miwas.togellenge.presentation.view.CreateChallengeView
import com.miwas.togellenge.ui.activities.AuthActivity

class CreateChallengeFragment : Fragment(), CreateChallengeView {

	private lateinit var createChallengePresenter: CreateChallengePresenter
	private lateinit var textWayButton: ImageButton
	private lateinit var photoWayButton: ImageButton
	private lateinit var videoWayButton: ImageButton
	private lateinit var readyButton: Button
	private lateinit var titleTextInput: TextInputEditText
	private lateinit var descriptionEditText: TextInputEditText
	private lateinit var confirmationWayWarning: TextView
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
		confirmationWayWarning = root.findViewById(R.id.confirmation_way_warning)
		readyButton = root.findViewById(R.id.ready_button)
		initPresenter()
		return root
	}

	override fun initView() {
		wayButtons = arrayOf(Pair(0, textWayButton), Pair(1, photoWayButton), Pair(2, videoWayButton))
		textWayButton.setOnClickListener {
			createChallengePresenter.confirmationWayChanged(0)
		}

		photoWayButton.setOnClickListener {
			createChallengePresenter.confirmationWayChanged(1)
		}

		videoWayButton.setOnClickListener {
			createChallengePresenter.confirmationWayChanged(2)
		}

		readyButton.setOnClickListener {
			createChallengePresenter.readyButtonClicked(titleTextInput.text, descriptionEditText.text)
		}
	}

	private fun initPresenter() {
		createChallengePresenter = CreateChallengePresenter()
		createChallengePresenter.attachView(this)
		createChallengePresenter.viewIsReady()
	}

	override fun changeConfirmationWay(selectedWayNumber: Int) {
		confirmationWayWarning.visibility = GONE

		wayButtons.forEach { button ->
			if (button.first == selectedWayNumber) {
				button.second.background = requireContext().getDrawable(R.drawable.circle_button_active)
			} else {
				button.second.background = requireContext().getDrawable(R.drawable.circle_button_inactive)
			}
		}
	}

	override fun setTitleError() {
		titleTextInput.error = "Введите название"
	}

	override fun setDescriptionError() {
		descriptionEditText.error = "Введите описание"
	}

	override fun setConfirmationError() {
		confirmationWayWarning.visibility = VISIBLE
	}

	override fun goToAuth() {
		val intent = Intent(context, AuthActivity::class.java)
		startActivity(intent)
	}
}