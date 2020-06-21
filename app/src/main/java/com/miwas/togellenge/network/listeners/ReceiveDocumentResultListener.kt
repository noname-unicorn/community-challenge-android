package com.miwas.togellenge.network.listeners

import com.google.firebase.firestore.DocumentSnapshot

interface ReceiveDocumentResultListener {

	fun onReceived(result: DocumentSnapshot)

	fun onFailure()
}