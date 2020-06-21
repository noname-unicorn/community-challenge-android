package com.miwas.togellenge.ui.fragments.feed

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miwas.togellenge.R
import com.miwas.togellenge.models.Challenge
import com.miwas.togellenge.presentation.listeners.ChallengeClickListener
import com.miwas.togellenge.presentation.listeners.JoinButtonListener
import com.miwas.togellenge.presentation.presenter.FeedPresenter
import com.miwas.togellenge.presentation.view.FeedView
import com.miwas.togellenge.ui.activities.AuthActivity
import com.miwas.togellenge.ui.activities.ChallengeActivity
import com.miwas.togellenge.ui.adapters.ChallengesAdapter
import com.miwas.togellenge.utils.Constants.CHALLENGE_ID_PARAM

class FeedFragment : Fragment(), FeedView {

	private lateinit var feedPresenter: FeedPresenter
	private lateinit var feedChallengeRecycler: RecyclerView
	private lateinit var challengesAdapter: ChallengesAdapter

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val root = inflater.inflate(R.layout.fragment_feed, container, false)
		feedChallengeRecycler = root.findViewById(R.id.feed_challenge_recycler)
		initPresenter()
		return root
	}

	private fun initPresenter() {
		feedPresenter = FeedPresenter()
		feedPresenter.attachView(this)
		feedPresenter.viewIsReady()
	}

	override fun initView() {
		val joinChallengeClickListener: JoinButtonListener = object : JoinButtonListener {
			override fun onClick(challenge: Challenge, position: Int) {
				feedPresenter.onJoinButtonClicked(challenge, position)
			}
		}

		val challengeClickListener: ChallengeClickListener = object : ChallengeClickListener {
			override fun onClick(challenge: Challenge, position: Int) {
				val intent = Intent(context, ChallengeActivity::class.java)
				intent.putExtra(CHALLENGE_ID_PARAM, challenge.id)
				startActivity(intent)
			}
		}

		challengesAdapter = ChallengesAdapter(joinChallengeClickListener, challengeClickListener)

		feedChallengeRecycler.apply {
			adapter = challengesAdapter
			layoutManager = LinearLayoutManager(context)
		}
	}

	override fun updateChallenge(position: Int) {
		challengesAdapter.updateChallenge(position)
	}

	override fun setChallengesList(challenges: MutableList<Challenge>) {
		challengesAdapter.setChallengesList(challenges)
	}

	override fun goToAuth() {
		val intent = Intent(context, AuthActivity::class.java)
		startActivity(intent)
	}
}
