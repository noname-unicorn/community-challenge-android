package com.miwas.togellenge.network.listeners

import com.google.firebase.firestore.DocumentReference

interface CreateDocumentResultListener {

	fun onReceived(result: DocumentReference)

	fun onFailure()
}