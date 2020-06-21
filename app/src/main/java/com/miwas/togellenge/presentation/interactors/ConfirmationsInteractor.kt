package com.miwas.togellenge.presentation.interactors

import android.net.Uri
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.miwas.togellenge.models.Confirmation
import com.miwas.togellenge.network.firebase.FirebaseRequester
import com.miwas.togellenge.network.listeners.CreateDocumentResultListener
import com.miwas.togellenge.network.listeners.LoadFileListener
import com.miwas.togellenge.network.listeners.ReceiveResultListener
import com.miwas.togellenge.presentation.listeners.ReadyConfirmationListener
import com.miwas.togellenge.presentation.listeners.ReadyConfirmationsListener
import java.util.*
import kotlin.collections.HashMap

class ConfirmationsInteractor {

	private val firebaseRequester = FirebaseRequester
	private val storage = Firebase.storage

	fun getCurrentChallengeConfirmations() {

	}

	fun getCurrentUserConfirmations(readyConfirmationsListener: ReadyConfirmationsListener, uid: String) {
		firebaseRequester.getCurrentUserConfirmations(
			generateReceiveChallengesListener(readyConfirmationsListener)
		)
	}

	fun loadConfirmation(
		loadFileListener: LoadFileListener,
		fileName: String,
		selectedFileUri: Uri,
		contentType: String?
	) {

		val storageRef = storage.reference
		val ref: StorageReference = storageRef.child(fileName)
		val loadFileTask = if (contentType.isNullOrEmpty()) {
			ref.putFile(selectedFileUri)
		} else {
			val metadata = StorageMetadata.Builder()
				.setContentType(contentType)
				.build()
			ref.putFile(selectedFileUri, metadata)
		}
		firebaseRequester.loadFile(loadFileListener, loadFileTask)
	}

	fun createConfirmation(
		readyConfirmationListener: ReadyConfirmationListener,
		confirmation: HashMap<String, Comparable<*>?>
	) {
		val createDocumentResultListener: CreateDocumentResultListener = object : CreateDocumentResultListener {

			override fun onReceived(result: DocumentReference) {
				readyConfirmationListener.onReady(Confirmation().apply { id = result.id })
			}

			override fun onFailure() {

			}
		}

		firebaseRequester.createConfirmation(createDocumentResultListener, confirmation)
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
	) = object : ReceiveResultListener {
		override fun onReceived(result: QuerySnapshot) {
			readyConfirmationsListener.onReady(generateConfirmations(result))
		}

		override fun onFailure() {
			//TODO: add errors handling
		}
	}
}