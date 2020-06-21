package com.miwas.togellenge.network.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.miwas.togellenge.network.listeners.CreateDocumentResultListener
import com.miwas.togellenge.network.listeners.LoadFileListener
import com.miwas.togellenge.network.listeners.MakeOperationListener
import com.miwas.togellenge.network.listeners.ReceiveDocumentResultListener
import com.miwas.togellenge.network.listeners.ReceiveResultListener
import com.miwas.togellenge.utils.Constants

object FirebaseRequester {
	private var dataBaseFirebase: FirebaseFirestore = Firebase.firestore
	private var fireBaseAuth: FirebaseAuth = Firebase.auth

	fun getUserUid(): String? = fireBaseAuth.uid

	fun getCurrentUser(): FirebaseUser? = fireBaseAuth.currentUser

	fun getChallenge(receiveResultListener: ReceiveDocumentResultListener, documentId: String) {
		dataBaseFirebase
			.collection(Constants.CHALLENGES_COLLECTION)
			.document(documentId)
			.get()
			.addOnSuccessListener { result ->
				receiveResultListener.onReceived(result)
			}
			.addOnFailureListener {
				receiveResultListener.onFailure()
			}
	}

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

	fun createChallenge(createChallengeListener: MakeOperationListener, challenge: HashMap<String, Any?>) {
		dataBaseFirebase.collection(Constants.CHALLENGES_COLLECTION)
			.add(challenge)
			.addOnSuccessListener { documentReference ->
				createChallengeListener.onComplete()
			}
			.addOnFailureListener { e ->
				createChallengeListener.onFailure()
			}
	}

	fun addUserToChallenge(addUserToChallengeListener: MakeOperationListener, challengeId: String) {
		dataBaseFirebase.collection(Constants.CHALLENGES_COLLECTION)
			.document(challengeId)
			.update(Constants.PARTICIPANTS_FIELD, FieldValue.arrayUnion(fireBaseAuth.currentUser?.uid))
			.addOnSuccessListener {
				addUserToChallengeListener.onComplete()
			}
			.addOnFailureListener {
				addUserToChallengeListener.onFailure()
			}

	}

	fun addConfirmationToChallenge(
		addConfirmationToChallengeListener: MakeOperationListener,
		challengeId: String,
		confirmationId: String
	) {
		dataBaseFirebase.collection(Constants.CHALLENGES_COLLECTION)
			.document(challengeId)
			.update(Constants.CONFIRMATIONS_FIELD, FieldValue.arrayUnion(confirmationId))
			.addOnSuccessListener {
				addConfirmationToChallengeListener.onComplete()
			}
			.addOnFailureListener {
				addConfirmationToChallengeListener.onFailure()
			}

	}

	fun removeUserFromChallenge(removeUserFromChallengeListener: MakeOperationListener, challengeId: String) {
		dataBaseFirebase.collection(Constants.CHALLENGES_COLLECTION)
			.document(challengeId)
			.update(Constants.PARTICIPANTS_FIELD, FieldValue.arrayRemove(fireBaseAuth.currentUser?.uid))
			.addOnSuccessListener {
				removeUserFromChallengeListener.onComplete()
			}
			.addOnFailureListener {
				removeUserFromChallengeListener.onFailure()
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

	fun loadFile(loadFileListener: LoadFileListener, loadFileTask: UploadTask) {
		loadFileTask
			.addOnSuccessListener { task ->
				task.metadata?.reference?.downloadUrl?.let { taskUrl ->
					taskUrl
						.addOnSuccessListener { uri ->
							loadFileListener.onComplete(uri.toString())
						}
						.addOnFailureListener {
							loadFileListener.onFailure()
						}
				}

			}
			.addOnFailureListener {
				loadFileListener.onFailure()
			}
	}

	fun createConfirmation(
		createConfirmationListener: CreateDocumentResultListener,
		confirmation: HashMap<String, Comparable<*>?>
	) {
		dataBaseFirebase.collection(Constants.CONFIRMATIONS_COLLECTION)
			.add(confirmation)
			.addOnSuccessListener {
				createConfirmationListener.onReceived(it)
			}
			.addOnFailureListener {
				createConfirmationListener.onFailure()
			}
	}
}