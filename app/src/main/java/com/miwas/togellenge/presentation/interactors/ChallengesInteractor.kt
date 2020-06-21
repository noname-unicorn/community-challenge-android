package com.miwas.togellenge.presentation.interactors

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.miwas.togellenge.models.Challenge
import com.miwas.togellenge.network.firebase.FirebaseRequester
import com.miwas.togellenge.network.listeners.MakeOperationListener
import com.miwas.togellenge.network.listeners.ReceiveDocumentResultListener
import com.miwas.togellenge.presentation.listeners.ReadyChallengesListener
import com.miwas.togellenge.network.listeners.ReceiveResultListener
import com.miwas.togellenge.presentation.listeners.ReadyChallengeListener

class ChallengesInteractor {

	private val firebaseRequester = FirebaseRequester

	fun generateChallenges(result: QuerySnapshot, uid: String): MutableList<Challenge> {
		val challengesArray = mutableListOf<Challenge>()

		for (document in result) {
			challengesArray.add(
				getChallengeByDocument(document, uid)
			)
		}

		return challengesArray
	}

	fun getChallenge(readyChallengeListener: ReadyChallengeListener, documentId: String, uid: String) {

		val receiveDocumentResultListener: ReceiveDocumentResultListener = object : ReceiveDocumentResultListener {
			override fun onReceived(result: DocumentSnapshot) {
				readyChallengeListener.onReady(getChallengeByDocument(result, uid))
			}

			override fun onFailure() {

			}

		}

		firebaseRequester.getChallenge(receiveDocumentResultListener, documentId)
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

	fun createChallenge(createChallengeListener: MakeOperationListener, challenge: HashMap<String, Any?>) {
		firebaseRequester.createChallenge(createChallengeListener, challenge)
	}

	fun addUserToChallenge(addUserToChallengeListener: MakeOperationListener, challengeId: String) {
		firebaseRequester.addUserToChallenge(addUserToChallengeListener, challengeId)
	}

	fun addConfirmationToChallenge(
		addConfirmationToChallengeListener: MakeOperationListener,
		challengeId: String,
		confirmationId: String
	) {
		firebaseRequester.addConfirmationToChallenge(addConfirmationToChallengeListener, challengeId, confirmationId)
	}

	fun removeUserFromChallenge(removeUserFromChallengeListener: MakeOperationListener, challengeId: String) {
		firebaseRequester.removeUserFromChallenge(removeUserFromChallengeListener, challengeId)
	}

	private fun generateReceiveChallengesListener(
		readyChallengesListener: ReadyChallengesListener,
		uid: String
	) = object : ReceiveResultListener {
		override fun onReceived(result: QuerySnapshot) {
			readyChallengesListener.onReady(generateChallenges(result, uid))
		}

		override fun onFailure() {
			//TODO: add errors handling
		}
	}

	private fun getChallengeByDocument(document: DocumentSnapshot, uid: String) = document.data?.let { data ->

		Challenge().apply {
			id = document.id
			name =
				if (data["name"] == null) null else data["name"] as String
			description =
				if (data["description"] == null) null else data["description"] as String
			date =
				if (data["date"] == null) null else data["date"] as Timestamp
			participants =
				if (data["participants"] == null) null else data["participants"] as List<String>
			isCurrentUserParticipate = participants?.contains(uid) ?: false
			authorId =
				if (data["author"] == null) null else data["author"] as String
			isCurrentUserAuthor = authorId == uid

			confirmationMethod =
				if (data["accept_method"] == null) null else data["accept_method"] as String
		}

	} ?: Challenge()

}