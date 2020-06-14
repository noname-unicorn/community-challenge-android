package com.miwas.togellenge.presentation.presenter

import android.text.Editable
import com.google.firebase.Timestamp
import com.miwas.togellenge.base.BasePresenter
import com.miwas.togellenge.network.listeners.MakeOperationListener
import com.miwas.togellenge.presentation.interactors.ChallengesInteractor
import com.miwas.togellenge.presentation.interactors.UserInteractor
import com.miwas.togellenge.presentation.view.CreateChallengeView

class CreateChallengePresenter : BasePresenter<CreateChallengeView>() {

	private val challengesInteractor: ChallengesInteractor = ChallengesInteractor()
	private val userInteractor: UserInteractor = UserInteractor()
	private var selectedWay = -1
	private val waysOfConfirmation = arrayOf("text", "photo", "video")

	override fun viewIsReady() {
		baseView?.initView()
		if (!userInteractor.isCurrentUser()) {
			baseView?.goToAuth()
		}
	}

	fun confirmationWayChanged(selectedWayNumber: Int) {
		selectedWay = selectedWayNumber
		baseView?.changeConfirmationWay(selectedWayNumber)
	}

	fun readyButtonClicked(titleText: Editable?, descriptionText: Editable?) {
		if (!checkFields(titleText, descriptionText)) {
			createChallenge(titleText.toString(), descriptionText.toString())
		}
	}

	private fun checkFields(titleText: Editable?, descriptionText: Editable?): Boolean {
		var isInErrorState = false

		if (titleText.isNullOrEmpty()) {
			baseView?.setTitleError()
			isInErrorState = true
		}

		if (descriptionText.isNullOrEmpty()) {
			baseView?.setDescriptionError()
			isInErrorState = true
		}

		if (selectedWay == -1) {
			baseView?.setConfirmationError()
			isInErrorState = true
		}

		return isInErrorState
	}

	private fun createChallenge(name: String, description: String) {

		val challenge = hashMapOf(
			"date" to Timestamp.now(),
			"accept_method" to waysOfConfirmation[selectedWay],
			"description" to description,
			"author" to userInteractor.getUserUid(),
			"name" to name,
			"participants" to listOf(userInteractor.getUserUid())
		)

		val createChallengeListener: MakeOperationListener = object : MakeOperationListener {
			override fun onComplete() {

			}

			override fun onFailure() {

			}
		}

		challengesInteractor.createChallenge(createChallengeListener, challenge)
	}
}