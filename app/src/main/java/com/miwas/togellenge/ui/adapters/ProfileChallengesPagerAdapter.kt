package com.miwas.togellenge.ui.adapters

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.miwas.togellenge.presentation.presenter.ProfilePresenter
import com.miwas.togellenge.ui.fragments.profile.ProfileChallengesFragment

class ProfileChallengesPagerAdapter(
	fragmentManager: FragmentManager,
	private val profilePresenter: ProfilePresenter
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

	private val fragmentsList = mutableListOf<ProfileChallengesFragment>()
	private val PAGES_COUNT = 2
	private val tabTitles = arrayOf("Участник", "Создатель")

	override fun getCount(): Int {
		return PAGES_COUNT
	}

	override fun getItem(position: Int): ProfileChallengesFragment {
		return fragmentsList[position]
	}

	override fun getPageTitle(position: Int): CharSequence? {
		return tabTitles[position]
	}

	fun addFragment(profileChallengesFragment: ProfileChallengesFragment) {
		fragmentsList.add(profileChallengesFragment)
	}

}