package com.miwas.togellenge.base


abstract class BasePresenter<V : MvpView> : MvpPresenter<V> {
	var baseView: V? = null

	override fun attachView(mvpView: V) {
		baseView = mvpView
	}

	override fun detachView() {
		baseView = null
	}

	open fun getView(): V? {
		return baseView
	}

	protected open fun isViewAttached(): Boolean {
		return baseView != null
	}

	override fun destroy() {}
}