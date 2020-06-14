package com.miwas.togellenge.presentation.view

import com.miwas.togellenge.base.MvpView
import com.miwas.togellenge.models.Challenge

interface FeedView : MvpView {

	fun updateChallenge(position: Int)

	fun setChallengesList(challenges: MutableList<Challenge>)

}