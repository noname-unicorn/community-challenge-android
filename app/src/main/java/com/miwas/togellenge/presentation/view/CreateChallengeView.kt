package com.miwas.togellenge.presentation.view

import com.miwas.togellenge.base.MvpView

interface CreateChallengeView : MvpView {

	fun changeConfirmationWay(selectedWayNumber: Int)

	fun setTitleError()

	fun setDescriptionError()

	fun setConfirmationError()
}