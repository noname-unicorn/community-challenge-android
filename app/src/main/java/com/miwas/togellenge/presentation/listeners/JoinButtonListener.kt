package com.miwas.togellenge.presentation.listeners

import com.miwas.togellenge.models.Challenge

interface JoinButtonListener {
	fun onClick(challenge: Challenge, position: Int)
}