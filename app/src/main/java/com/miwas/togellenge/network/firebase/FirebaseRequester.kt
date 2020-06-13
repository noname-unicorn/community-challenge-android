package com.miwas.togellenge.network.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.miwas.togellenge.presentation.listeners.AddUserToChallengeListener
import com.miwas.togellenge.network.listeners.ReceiveResultListener
import com.miwas.togellenge.presentation.listeners.RemoveUserFromChallengeListener
import com.miwas.togellenge.utils.Constants

object FirebaseRequester {
	private var dataBaseFirebase: FirebaseFirestore = Firebase.firestore
	private var fireBaseAuth: FirebaseAuth = Firebase.auth

	fun getUserUid(): String? = fireBaseAuth.uid

	fun getCurrentUser(): FirebaseUser? = fireBaseAuth.currentUser

	fun getAllChallenges(receiveResultListener: ReceiveResultListener) {
		dataBaseFirebase
			.collection(Constants.CHALLENGES_COLLECTION)
			.get()
			.addOnSuccessListener { result ->
				receiveResultListener.onReceived(result)
			}
			.addOnFailureListener {
				receiveResultListener.onFailure()
			}
	}

	fun getCreatedChallenges(receiveResultListener: ReceiveResultListener) {

		dataBaseFirebase.collection(Constants.CHALLENGES_COLLECTION)
			.whereEqualTo(Constants.AUTHOR_FIELD, fireBaseAuth.uid)
			.get()
			.addOnSuccessListener { result ->
				receiveResultListener.onReceived(result)
			}
	}

	fun getParticipatedChallenges(receiveResultListener: ReceiveResultListener) {

		dataBaseFirebase.collection(Constants.CHALLENGES_COLLECTION)
			.whereArrayContains(Constants.PARTICIPANTS_FIELD, fireBaseAuth.uid.toString())
			.get()
			.addOnSuccessListener { result ->
				receiveResultListener.onReceived(result)
			}
	}

	fun addUserToChallenge(addUserToChallengeListener: AddUserToChallengeListener, challengeId: String) {
		dataBaseFirebase.collection(Constants.CHALLENGES_COLLECTION)
			.document(challengeId)
			.update(Constants.PARTICIPANTS_FIELD, FieldValue.arrayUnion(fireBaseAuth.currentUser?.uid))
			.addOnSuccessListener {
				addUserToChallengeListener.onAdded()
			}
	}

	fun removeUserFromChallenge(removeUserFromChallengeListener: RemoveUserFromChallengeListener, challengeId: String) {
		dataBaseFirebase.collection(Constants.CHALLENGES_COLLECTION)
			.document(challengeId)
			.update(Constants.PARTICIPANTS_FIELD, FieldValue.arrayRemove(fireBaseAuth.currentUser?.uid))
			.addOnSuccessListener {
				removeUserFromChallengeListener.onRemoved()
			}
	}

	fun getCurrentUserConfirmations(receiveResultListener: ReceiveResultListener) {
		dataBaseFirebase.collection(Constants.CONFIRMATIONS_COLLECTION)
			.whereEqualTo(Constants.USER_ID_FIELD, fireBaseAuth.uid)
			.get()
			.addOnSuccessListener { result ->
				receiveResultListener.onReceived(result)
			}
	}
}