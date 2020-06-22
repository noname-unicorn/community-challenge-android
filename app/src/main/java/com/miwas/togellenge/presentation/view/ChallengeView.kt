package com.miwas.togellenge.presentation.view

import com.miwas.togellenge.base.MvpView
import com.miwas.togellenge.models.Challenge
import com.miwas.togellenge.presentation.listeners.ConfirmChallengeListener

interface ChallengeView : MvpView {

	fun setInitialInfo(challenge: Challenge)

	fun setConfirmations()

	fun startConfirmationByFile(isVideo: Boolean)

	fun showTextConfirmationDialog(confirmChallengeListener: ConfirmChallengeListener)
}