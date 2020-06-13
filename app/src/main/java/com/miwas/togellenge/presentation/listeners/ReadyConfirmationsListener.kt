package com.miwas.togellenge.presentation.listeners

import com.miwas.togellenge.models.Confirmation

interface ReadyConfirmationsListener {
	fun onReady(confirmations: MutableList<Confirmation>)
}