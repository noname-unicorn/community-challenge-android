package com.miwas.togellenge.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.miwas.togellenge.R
import com.miwas.togellenge.models.Challenge

class FeedChallengeAdapter(
	private val challengesList: Array<Challenge>
) : RecyclerView.Adapter<FeedChallengeAdapter.FeedChallengeViewHolder>() {

	class FeedChallengeViewHolder(view: View) : RecyclerView.ViewHolder(view)

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): FeedChallengeViewHolder {
		val rootView = LayoutInflater.from(parent.context).inflate(R.layout.feed_challenge_item, parent, false)

		return FeedChallengeViewHolder(rootView)
	}

	override fun onBindViewHolder(holder: FeedChallengeViewHolder, position: Int) {

	}

	override fun getItemCount() = challengesList.size
}