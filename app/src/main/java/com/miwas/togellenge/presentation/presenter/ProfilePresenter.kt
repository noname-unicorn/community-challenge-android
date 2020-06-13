package com.miwas.togellenge.presentation.presenter

import com.miwas.togellenge.base.BasePresenter
import com.miwas.togellenge.models.Challenge
import com.miwas.togellenge.models.Confirmation
import com.miwas.togellenge.presentation.interactors.ChallengesInteractor
import com.miwas.togellenge.presentation.interactors.ConfirmationsInteractor
import com.miwas.togellenge.presentation.interactors.UserInteractor
import com.miwas.togellenge.presentation.listeners.ReadyChallengesListener
import com.miwas.togellenge.presentation.listeners.ReadyConfirmationsListener
import com.miwas.togellenge.presentation.view.ProfileView

class ProfilePresenter : BasePresenter<ProfileView>() {

	private val challengesInteractor: ChallengesInteractor = ChallengesInteractor()
	private val confirmationsInteractor: ConfirmationsInteractor = ConfirmationsInteractor()
	private val userInteractor: UserInteractor = UserInteractor()

	override fun viewIsReady() {
		getAllAvailableInfo()
		baseView?.initView()
	}

	private fun getAllAvailableInfo() {
		generateParticipatedChallenges()
		generateCreatedChallenges()
		generateConfirmations()
	}

	private fun generateParticipatedChallenges() {

		val readyChallengesListener: ReadyChallengesListener = object : ReadyChallengesListener {
			override fun onReady(challenges: MutableList<Challenge>) {
				baseView?.setParticipantCounter(challenges.count())
				baseView?.setParticipatedList(challenges)
			}
		}

		userInteractor.getUserUid()?.let { userUid ->
			challengesInteractor.getParticipatedChallenges(readyChallengesListener, userUid)
		}
	}

	private fun generateCreatedChallenges() {

		val readyChallengesListener: ReadyChallengesListener = object : ReadyChallengesListener {
			override fun onReady(challenges: MutableList<Challenge>) {
				baseView?.setCreatedCounter(challenges.count())
				baseView?.setCreatedList(challenges)
			}
		}

		userInteractor.getUserUid()?.let { userUid ->
			challengesInteractor.getCreatedChallenges(readyChallengesListener, userUid)
		}
	}

	private fun generateConfirmations() {

		val readyConfirmationsListener: ReadyConfirmationsListener = object : ReadyConfirmationsListener {
			override fun onReady(confirmations: MutableList<Confirmation>) {
				baseView?.setConfirmationsCounter(confirmations.count())
			}
		}

		userInteractor.getUserUid()?.let { userUid ->
			confirmationsInteractor.getCurrentUserConfirmations(readyConfirmationsListener, userUid)
		}
	}

}