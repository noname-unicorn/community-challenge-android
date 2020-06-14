package com.miwas.togellenge.presentation.view

import com.miwas.togellenge.base.MvpView
import com.miwas.togellenge.models.Challenge

interface ProfileView : MvpView {

	fun setParticipatedList(challenges: MutableList<Challenge>)

	fun setCreatedList(challenges: MutableList<Challenge>)

	fun setParticipantCounter(count: Int)

	fun setCreatedCounter(count: Int)

	fun setConfirmationsCounter(count: Int)
}