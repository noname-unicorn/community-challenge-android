package com.miwas.togellenge.ui.fragments.feed

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.miwas.togellenge.MainActivity
import com.miwas.togellenge.R
import com.miwas.togellenge.ui.activities.AuthActivity
import com.miwas.togellenge.ui.fragments.CreateChallengeFragment

class FeedFragment : Fragment() {

	private lateinit var toAuthButton: Button
	private lateinit var createChallengeFab: View

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val root = inflater.inflate(R.layout.fragment_feed, container, false)
		toAuthButton = root.findViewById(R.id.to_auth_button)
		createChallengeFab = root.findViewById(R.id.create_challenge_fab)
		toAuthButton.setOnClickListener {
			val intent = Intent(root.context, AuthActivity::class.java)
			startActivity(intent)
		}

		createChallengeFab.setOnClickListener { view ->
			(activity as MainActivity).replaceFragments(CreateChallengeFragment())
		}

		return root
	}
}
