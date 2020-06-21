package com.miwas.togellenge.presentation.listeners

import com.miwas.togellenge.models.Confirmation

interface ReadyConfirmationListener {
	fun onReady(confirmation: Confirmation)
}