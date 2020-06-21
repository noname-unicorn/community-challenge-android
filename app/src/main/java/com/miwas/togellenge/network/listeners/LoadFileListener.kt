package com.miwas.togellenge.network.listeners

interface LoadFileListener {

	fun onComplete(url: String)

	fun onFailure()
}