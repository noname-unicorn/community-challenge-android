package com.miwas.togellenge.presentation.listeners

import com.miwas.togellenge.models.Challenge

interface ReadyChallengesListener {
	fun onReady(challenges: MutableList<Challenge>)
}