package com.miwas.togellenge.ui.fragments.profile

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
import com.miwas.togellenge.presentation.listeners.RemoveUserFromChallengeListener
import com.miwas.togellenge.presentation.presenter.ProfilePresenter
import com.miwas.togellenge.ui.adapters.ChallengesAdapter

class ProfileChallengesFragment : Fragment() {

	private val challengesPageArg = "PROFILE_CHALLENGES_PAGE_ARG"
	private var mPage = 0
	private lateinit var recyclerView: RecyclerView
	private lateinit var challengesAdapter: ChallengesAdapter
	private lateinit var profilePresenter: ProfilePresenter
	private val challengesInteractor: ChallengesInteractor = ChallengesInteractor()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		arguments?.let {
			mPage = it.getInt(challengesPageArg)
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view: View = inflater.inflate(R.layout.recycler_view, container, false)
		recyclerView = view.findViewById(R.id.recycler_view)
		recyclerView.apply {
			adapter = challengesAdapter
			layoutManager = LinearLayoutManager(context)
		}
		return view
	}

	fun initializeAdapter() {
		val challengeClickListener: JoinButtonListener = object : JoinButtonListener {
			override fun onClick(challenge: Challenge, position: Int) {
				challenge.id?.let { challengeId ->
					val removeUserFromChallengeListener: RemoveUserFromChallengeListener =
						object : RemoveUserFromChallengeListener {
							override fun onRemoved() {
								challengesAdapter.removeChallenge(challenge)
							}
						}
					challengesInteractor.removeUserFromChallenge(removeUserFromChallengeListener, challengeId)
				}
			}
		}
		challengesAdapter = ChallengesAdapter(challengeClickListener)
	}

	fun setPresenter(profilePresenter: ProfilePresenter) {
		this.profilePresenter = profilePresenter
	}

	fun setChallengesList(challenges: MutableList<Challenge>) {
		challengesAdapter.setChallengesList(challenges)
	}

	companion object {
		fun newInstance(page: Int): ProfileChallengesFragment? {
			val challengesPageArg = "PROFILE_CHALLENGES_PAGE_ARG"
			val args = Bundle()
			args.putInt(challengesPageArg, page)
			val fragment = ProfileChallengesFragment()
			fragment.arguments = args
			return fragment
		}
	}
}