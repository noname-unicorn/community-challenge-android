package com.miwas.togellenge.presentation.interactors

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import com.miwas.togellenge.models.Challenge
import com.miwas.togellenge.network.firebase.FirebaseRequester
import com.miwas.togellenge.presentation.listeners.AddUserToChallengeListener
import com.miwas.togellenge.presentation.listeners.ReadyChallengesListener
import com.miwas.togellenge.network.listeners.ReceiveResultListener
import com.miwas.togellenge.presentation.listeners.RemoveUserFromChallengeListener

class ChallengesInteractor {

	private val firebaseRequester = FirebaseRequester

	fun generateChallenges(result: QuerySnapshot, uid: String): MutableList<Challenge> {
		val challengesArray = mutableListOf<Challenge>()

		for (document in result) {
			challengesArray.add(
				Challenge().apply {
					id = document.id
					name =
						if (document.data["name"] == null) null else document.data["name"] as String
					description =
						if (document.data["description"] == null) null else document.data["description"] as String
					date =
						if (document.data["date"] == null) null else document.data["date"] as Timestamp
					participants =
						if (document.data["participants"] == null) null else document.data["participants"] as List<String>
					isCurrentUserParticipate = participants?.contains(uid) ?: false
					authorId =
						if (document.data["author"] == null) null else document.data["author"] as String
					isCurrentUserAuthor = authorId == uid

					confirmationMethod =
						if (document.data["accept_method"] == null) null else document.data["accept_method"] as String
				}
			)
			Log.e("res", "${document.id} => ${document.data}")
		}

		return challengesArray
	}

	fun getAllChallenges(readyChallengesListener: ReadyChallengesListener, uid: String) {
		firebaseRequester.getAllChallenges(generateReceiveChallengesListener(readyChallengesListener, uid))
	}

	fun getCreatedChallenges(readyChallengesListener: ReadyChallengesListener, uid: String) {
		firebaseRequester.getCreatedChallenges(generateReceiveChallengesListener(readyChallengesListener, uid))
	}

	fun getParticipatedChallenges(readyChallengesListener: ReadyChallengesListener, uid: String) {
		firebaseRequester.getParticipatedChallenges(generateReceiveChallengesListener(readyChallengesListener, uid))
	}

	fun addUserToChallenge(addUserToChallengeListener: AddUserToChallengeListener, challengeId: String) {
		firebaseRequester.addUserToChallenge(addUserToChallengeListener, challengeId)
	}

	fun removeUserFromChallenge(removeUserFromChallengeListener: RemoveUserFromChallengeListener, challengeId: String) {
		firebaseRequester.removeUserFromChallenge(removeUserFromChallengeListener, challengeId)
	}

	private fun generateReceiveChallengesListener(
		readyChallengesListener: ReadyChallengesListener,
		uid: String
	) = object :
		ReceiveResultListener {
		override fun onReceived(result: QuerySnapshot) {
			readyChallengesListener.onReady(generateChallenges(result, uid))
		}

		override fun onFailure() {
			//TODO: add errors handling
		}
	}
}