package com.miwas.togellenge.network.listeners

import com.google.firebase.firestore.QuerySnapshot

interface ReceiveResultListener {

	fun onReceived(result: QuerySnapshot)

	fun onFailure()
}