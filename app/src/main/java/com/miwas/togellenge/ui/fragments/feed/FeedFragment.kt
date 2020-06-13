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
import com.miwas.togellenge.presentation.listeners.JoinButtonListener
import com.miwas.togellenge.presentation.interactors.ChallengesInteractor
import com.miwas.togellenge.presentation.interactors.UserInteractor
import com.miwas.togellenge.presentation.listeners.AddUserToChallengeListener
import com.miwas.togellenge.presentation.listeners.ReadyChallengesListener
import com.miwas.togellenge.presentation.listeners.RemoveUserFromChallengeListener
import com.miwas.togellenge.ui.activities.AuthActivity
import com.miwas.togellenge.ui.adapters.ChallengesAdapter
import java.util.ArrayList

class FeedFragment : Fragment() {

	private lateinit var feedChallengeRecycler: RecyclerView

	private lateinit var challengesAdapter: ChallengesAdapter
	private val challengesInteractor: ChallengesInteractor = ChallengesInteractor()
	private val userInteractor: UserInteractor = UserInteractor()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val root = inflater.inflate(R.layout.fragment_feed, container, false)
		feedChallengeRecycler = root.findViewById(R.id.feed_challenge_recycler)
		requestChallenges()

		val challengeClickListener: JoinButtonListener = object : JoinButtonListener {
			override fun onClick(challenge: Challenge, position: Int) {
				if (userInteractor.isCurrentUser()) {
					addOrDeleteParticipant(challenge, position)
				} else {
					val intent = Intent(context, AuthActivity::class.java)
					startActivity(intent)
				}
			}
		}
		challengesAdapter = ChallengesAdapter(challengeClickListener)

		feedChallengeRecycler.apply {
			adapter = challengesAdapter
			layoutManager = LinearLayoutManager(context)
		}
		return root
	}

	private fun requestChallenges() {

		val readyChallengesListener: ReadyChallengesListener = object : ReadyChallengesListener {
			override fun onReady(challenges: MutableList<Challenge>) {
				challengesAdapter.setChallengesList(challenges)
			}
		}

		userInteractor.getUserUid()?.let { userUid ->
			challengesInteractor.getAllChallenges(readyChallengesListener, userUid)
		}
	}

	private fun addOrDeleteParticipant(challenge: Challenge, position: Int) {
		if (challenge.isCurrentUserParticipate) {
			challenge.id?.let { challengeId ->

				val removeUserFromChallengeListener: RemoveUserFromChallengeListener =
					object : RemoveUserFromChallengeListener {
						override fun onRemoved() {
							(challenge.participants as ArrayList).remove(userInteractor.getUserUid())
							challenge.isCurrentUserParticipate = false
							challengesAdapter.updateChallenge(position)
						}
					}
				challengesInteractor.removeUserFromChallenge(removeUserFromChallengeListener, challengeId)
			}
		} else {
			challenge.id?.let { challengeId ->

				val addUserToChallengeListener: AddUserToChallengeListener = object : AddUserToChallengeListener {
					override fun onAdded() {
						(challenge.participants as ArrayList).add(userInteractor.getUserUid() ?: return)
						challenge.isCurrentUserParticipate = true
						challengesAdapter.updateChallenge(position)
					}
				}
				challengesInteractor.addUserToChallenge(addUserToChallengeListener, challengeId)
			}
		}
	}
}
