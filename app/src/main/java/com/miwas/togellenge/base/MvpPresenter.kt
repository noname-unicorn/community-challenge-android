package com.miwas.togellenge.base

interface MvpPresenter<V : MvpView> {

	fun attachView(mvpView: V)

	fun viewIsReady()

	fun detachView()

	fun destroy()
}