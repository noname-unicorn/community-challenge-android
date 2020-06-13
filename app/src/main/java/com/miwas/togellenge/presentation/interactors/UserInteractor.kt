package com.miwas.togellenge.presentation.interactors

import com.miwas.togellenge.network.firebase.FirebaseRequester

class UserInteractor {

	private val firebaseRequester = FirebaseRequester

	fun isCurrentUser(): Boolean = firebaseRequester.getCurrentUser() != null

	fun getUserUid(): String? = firebaseRequester.getUserUid()
}