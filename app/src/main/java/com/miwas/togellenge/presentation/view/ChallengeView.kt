package com.miwas.togellenge.presentation.view

import com.miwas.togellenge.base.MvpView
import com.miwas.togellenge.models.Challenge

interface ChallengeView : MvpView {

	fun setInitialInfo(challenge: Challenge)

	fun setConfirmations()

	fun startConfirmationByFile(isVideo: Boolean)
}