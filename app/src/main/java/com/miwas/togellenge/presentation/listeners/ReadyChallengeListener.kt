package com.miwas.togellenge.presentation.listeners

import com.miwas.togellenge.models.Challenge

interface ReadyChallengeListener {
	fun onReady(challenge: Challenge)
}