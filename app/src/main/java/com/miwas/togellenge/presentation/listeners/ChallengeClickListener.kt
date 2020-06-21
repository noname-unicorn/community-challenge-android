package com.miwas.togellenge.presentation.listeners

import com.miwas.togellenge.models.Challenge

interface ChallengeClickListener {
	fun onClick(challenge: Challenge, position: Int)
}