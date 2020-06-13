package com.miwas.togellenge.presentation.interactors

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import com.miwas.togellenge.models.Confirmation
import com.miwas.togellenge.network.firebase.FirebaseRequester
import com.miwas.togellenge.network.listeners.ReceiveResultListener
import com.miwas.togellenge.presentation.listeners.ReadyConfirmationsListener

class ConfirmationsInteractor {

	private val firebaseRequester = FirebaseRequester

	fun getCurrentChallengeConfirmations() {

	}

	fun getCurrentUserConfirmations(readyConfirmationsListener: ReadyConfirmationsListener, uid: String) {
		firebaseRequester.getCurrentUserConfirmations(
			generateReceiveChallengesListener(readyConfirmationsListener)
		)
	}

	fun generateConfirmations(result: QuerySnapshot): MutableList<Confirmation> {
		val confirmationsArray = mutableListOf<Confirmation>()

		for (document in result) {
			confirmationsArray.add(
				Confirmation().apply {
					id = document.id
					date =
						if (document.data["date"] == null) null else document.data["date"] as Timestamp
					challengeId =
						if (document.data["challenge_id"] == null) null else document.data["challenge_id"] as String
					userId =
						if (document.data["user_id"] == null) null else document.data["user_id"] as String
					confirmation =
						if (document.data["confirmation"] == null) null else document.data["confirmation"] as String
				}
			)
			Log.e("res", "${document.id} => ${document.data}")
		}

		return confirmationsArray
	}

	private fun generateReceiveChallengesListener(
		readyConfirmationsListener: ReadyConfirmationsListener
	) = object :
		ReceiveResultListener {
		override fun onReceived(result: QuerySnapshot) {
			readyConfirmationsListener.onReady(generateConfirmations(result))
		}

		override fun onFailure() {
			//TODO: add errors handling
		}
	}
}