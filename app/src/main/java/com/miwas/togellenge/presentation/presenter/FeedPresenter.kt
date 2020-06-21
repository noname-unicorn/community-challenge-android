package com.miwas.togellenge.presentation.presenter

import com.miwas.togellenge.base.BasePresenter
import com.miwas.togellenge.models.Challenge
import com.miwas.togellenge.network.listeners.MakeOperationListener
import com.miwas.togellenge.presentation.interactors.ChallengesInteractor
import com.miwas.togellenge.presentation.interactors.UserInteractor
import com.miwas.togellenge.presentation.listeners.ReadyChallengesListener
import com.miwas.togellenge.presentation.view.FeedView
import com.miwas.togellenge.utils.Constants.EMPTY_STRING
import java.util.ArrayList

class FeedPresenter : BasePresenter<FeedView>() {

	private val challengesInteractor: ChallengesInteractor = ChallengesInteractor()
	private val userInteractor: UserInteractor = UserInteractor()

	override fun viewIsReady() {
		baseView?.initView()
		getChallenges()
	}

	fun onJoinButtonClicked(challenge: Challenge, position: Int) {

		if (userInteractor.isCurrentUser()) {
			addOrDeleteParticipant(challenge, position)
		} else {
			baseView?.goToAuth()
		}
	}

	private fun addOrDeleteParticipant(challenge: Challenge, position: Int) {
		if (challenge.isCurrentUserParticipate) {
			challenge.id?.let { challengeId ->

				val removeUserFromChallengeListener: MakeOperationListener = object : MakeOperationListener {
					override fun onComplete() {
						(challenge.participants as ArrayList).remove(userInteractor.getUserUid())
						challenge.isCurrentUserParticipate = false
						baseView?.updateChallenge(position)
					}

					override fun onFailure() {

					}
				}

				challengesInteractor.removeUserFromChallenge(removeUserFromChallengeListener, challengeId)
			}
		} else {
			challenge.id?.let { challengeId ->

				val addUserToChallengeListener: MakeOperationListener = object : MakeOperationListener {
					override fun onComplete() {
						(challenge.participants as ArrayList).add(userInteractor.getUserUid() ?: return)
						challenge.isCurrentUserParticipate = true
						baseView?.updateChallenge(position)
					}

					override fun onFailure() {

					}
				}
				challengesInteractor.addUserToChallenge(addUserToChallengeListener, challengeId)
			}
		}
	}

	private fun getChallenges() {
		val readyChallengesListener: ReadyChallengesListener = object : ReadyChallengesListener {
			override fun onReady(challenges: MutableList<Challenge>) {
				baseView?.setChallengesList(challenges)
			}
		}

		val userUid = userInteractor.getUserUid() ?: EMPTY_STRING
		challengesInteractor.getAllChallenges(readyChallengesListener, userUid)
	}

}