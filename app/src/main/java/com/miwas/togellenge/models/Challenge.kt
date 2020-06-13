package com.miwas.togellenge.models

import com.google.firebase.Timestamp

class Challenge {
	var id: String? = null
	var confirmationMethod: String? = null
	var authorId: String? = null
	var date: Timestamp? = null
	var description: String? = null
	var name: String? = null
	var participants: List<String>? = null
	var isCurrentUserParticipate = false
	var isCurrentUserAuthor = false
}