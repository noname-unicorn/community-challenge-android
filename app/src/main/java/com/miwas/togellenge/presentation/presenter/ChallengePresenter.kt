package com.miwas.togellenge.presentation.presenter

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.firebase.Timestamp
import com.miwas.togellenge.base.BasePresenter
import com.miwas.togellenge.models.Challenge
import com.miwas.togellenge.models.Confirmation
import com.miwas.togellenge.network.listeners.LoadFileListener
import com.miwas.togellenge.network.listeners.MakeOperationListener
import com.miwas.togellenge.presentation.interactors.ChallengesInteractor
import com.miwas.togellenge.presentation.interactors.ConfirmationsInteractor
import com.miwas.togellenge.presentation.interactors.UserInteractor
import com.miwas.togellenge.presentation.listeners.ConfirmChallengeListener
import com.miwas.togellenge.presentation.listeners.ReadyChallengeListener
import com.miwas.togellenge.presentation.listeners.ReadyConfirmationListener
import com.miwas.togellenge.presentation.view.ChallengeView
import com.miwas.togellenge.utils.Constants
import java.util.*

class ChallengePresenter(private val challengeId: String?) : BasePresenter<ChallengeView>() {

	private val challengesInteractor: ChallengesInteractor = ChallengesInteractor()
	private val confirmationsInteractor: ConfirmationsInteractor = ConfirmationsInteractor()
	private val userInteractor: UserInteractor = UserInteractor()
	private lateinit var currentChallenge: Challenge
	private lateinit var confirmationMethod: String

	override fun viewIsReady() {
		baseView?.initView()
		getChallenge()
	}

	private fun getChallenge() {

		val readyChallengeListener: ReadyChallengeListener = object : ReadyChallengeListener {
			override fun onReady(challenge: Challenge) {
				currentChallenge = challenge
				baseView?.setInitialInfo(challenge)
				confirmationMethod = challenge.confirmationMethod ?: "text"
			}
		}

		val userUid = userInteractor.getUserUid() ?: Constants.EMPTY_STRING

		challengeId?.let { id ->
			challengesInteractor.getChallenge(readyChallengeListener, id, userUid)
		}
	}

	fun confirmButtonClicked() {
		if (confirmationMethod != "text") {
			baseView?.startConfirmationByFile(confirmationMethod == "video")
		} else {
			val confirmChallengeListener: ConfirmChallengeListener = object : ConfirmChallengeListener {

				override fun onConfirm(text: String) {
					createConfirmation(UUID.randomUUID().toString(), text)
				}
			}
			baseView?.showTextConfirmationDialog(confirmChallengeListener)
		}
	}


	fun loadVideo(data: Intent?) {
		val selectedVideoUri: Uri? = data?.data
		selectedVideoUri?.let { videoUri ->
			val fileName = UUID.randomUUID().toString()
			val loadFileListener: LoadFileListener = object : LoadFileListener {

				override fun onComplete(url: String) {
					createConfirmation(fileName, url)
				}

				override fun onFailure() {

				}
			}

			confirmationsInteractor.loadConfirmation(loadFileListener, fileName, videoUri, "video/mp4")
		}
	}

	fun loadPhoto(data: Intent?) {

		val selectedPhotoUri: Uri? = data?.data
		selectedPhotoUri?.let { photoUri ->
			val fileName = UUID.randomUUID().toString()
			val loadFileListener: LoadFileListener = object : LoadFileListener {

				override fun onComplete(url: String) {
					createConfirmation(fileName, url)
					Log.e("finalUrl", url)
				}

				override fun onFailure() {

				}
			}

			confirmationsInteractor.loadConfirmation(loadFileListener, fileName, photoUri, null)
		}
	}

	fun createConfirmation(confirmationId: String, confirmation: String) {

		val confirmationMap = hashMapOf(
			"challenge_id" to challengeId,
			"confirmation" to confirmation,
			"confirmation_id" to confirmationId,
			"date" to Timestamp.now(),
			"user_id" to userInteractor.getUserUid()
		)

		val readyConfirmationListener: ReadyConfirmationListener = object : ReadyConfirmationListener {
			override fun onReady(confirmation: Confirmation) {
				addConfirmationToChallenge(confirmation)
			}

		}

		confirmationsInteractor.createConfirmation(readyConfirmationListener, confirmationMap)
	}

	fun addConfirmationToChallenge(confirmation: Confirmation) {
		val addConfirmationToChallengeListener: MakeOperationListener = object : MakeOperationListener {

			override fun onComplete() {

			}

			override fun onFailure() {

			}
		}

		challengesInteractor.addConfirmationToChallenge(
			addConfirmationToChallengeListener,
			challengeId.toString(),
			confirmation.id.toString()
		)
	}

}