package com.miwas.togellenge.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.miwas.togellenge.R
import com.miwas.togellenge.models.Challenge
import com.miwas.togellenge.presentation.presenter.ProfilePresenter
import com.miwas.togellenge.presentation.view.ProfileView
import com.miwas.togellenge.ui.activities.AuthActivity
import com.miwas.togellenge.ui.adapters.ProfileChallengesPagerAdapter

class ProfileFragment : Fragment(), ProfileView {

	private lateinit var profilePresenter: ProfilePresenter
	private lateinit var viewPager: ViewPager
	private lateinit var tabLayout: TabLayout
	private lateinit var nestedScroll: NestedScrollView
	private lateinit var challengesParticipateCounter: TextView
	private lateinit var challengesCreatedCounter: TextView
	private lateinit var confirmationsCount: TextView
	private lateinit var participatedFragment: ProfileChallengesFragment
	private lateinit var createdFragment: ProfileChallengesFragment

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val root = inflater.inflate(R.layout.fragment_profile, container, false)
		viewPager = root.findViewById(R.id.view_pager)
		tabLayout = root.findViewById(R.id.tab_layout)
		nestedScroll = root.findViewById(R.id.nested_scroll)
		challengesParticipateCounter = root.findViewById(R.id.challenges_participate_counter)
		challengesCreatedCounter = root.findViewById(R.id.challenges_created_counter)
		confirmationsCount = root.findViewById(R.id.confirmations_count)
		initPresenter()
		return root
	}

	private fun initPresenter() {
		profilePresenter = ProfilePresenter()
		profilePresenter.attachView(this)
		profilePresenter.viewIsReady()
	}

	override fun initView() {

		viewPager.offscreenPageLimit = 0
		nestedScroll.isFillViewport = true
		participatedFragment = ProfileChallengesFragment.newInstance(1) ?: return
		participatedFragment.setPresenter(profilePresenter)
		participatedFragment.initializeAdapter()
		createdFragment = ProfileChallengesFragment.newInstance(2) ?: return
		createdFragment.setPresenter(profilePresenter)
		createdFragment.initializeAdapter()

		activity?.supportFragmentManager?.let {
			val adapter = ProfileChallengesPagerAdapter(it, profilePresenter)
			adapter.addFragment(participatedFragment)
			adapter.addFragment(createdFragment)
			viewPager.adapter = adapter
		}

		tabLayout.apply {
			setupWithViewPager(viewPager)
		}
	}

	override fun setParticipatedList(challenges: MutableList<Challenge>) {
		participatedFragment.setChallengesList(challenges)
	}

	override fun setCreatedList(challenges: MutableList<Challenge>) {
		createdFragment.setChallengesList(challenges)
	}

	override fun setParticipantCounter(count: Int) {
		challengesParticipateCounter.text = count.toString()
	}

	override fun setCreatedCounter(count: Int) {
		challengesCreatedCounter.text = count.toString()
	}

	override fun setConfirmationsCounter(count: Int) {
		confirmationsCount.text = count.toString()
	}

	override fun goToAuth() {
		val intent = Intent(context, AuthActivity::class.java)
		startActivity(intent)
	}

}
