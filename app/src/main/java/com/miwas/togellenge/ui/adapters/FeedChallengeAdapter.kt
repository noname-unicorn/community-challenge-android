package com.miwas.togellenge.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miwas.togellenge.R
import com.miwas.togellenge.models.Challenge
import com.miwas.togellenge.ui.fragments.feed.FeedFragment

class FeedChallengeAdapter(
	private val joinClickListener: FeedFragment.JoinButtonListener
) : RecyclerView.Adapter<FeedChallengeAdapter.FeedChallengeViewHolder>() {

	private var challengesList: List<Challenge> = listOf()

	class FeedChallengeViewHolder(view: View) : RecyclerView.ViewHolder(view)

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): FeedChallengeViewHolder {
		val rootView = LayoutInflater.from(parent.context).inflate(R.layout.feed_challenge_item, parent, false)

		return FeedChallengeViewHolder(rootView)
	}

	override fun onBindViewHolder(holder: FeedChallengeViewHolder, position: Int) {

		val challenge = challengesList[position]

		val name = holder.itemView.findViewById<TextView>(R.id.name)
		val participantsCount = holder.itemView.findViewById<TextView>(R.id.participant_count_text)
		val confirmationMethod = holder.itemView.findViewById<ImageView>(R.id.confirmation_method_image)
		val joinButton = holder.itemView.findViewById<Button>(R.id.join_button)
		if (challenge.isCurrentUserParticipate) {
			joinButton.setBackgroundColor(Color.RED)
		}
		name.text = challenge.name
		participantsCount.text = challenge.participants?.count().toString()
		val confirmationMethodImage = when (challenge.confirmationMethod) {
			"photo" -> R.drawable.ic_camera
			"video" -> R.drawable.ic_videocamera
			else -> R.drawable.ic_notebook
		}
		confirmationMethod.background = confirmationMethod.context.getDrawable(confirmationMethodImage)
		joinButton.setOnClickListener {
			joinClickListener.onClick(challenge)
			if (challenge.isCurrentUserParticipate) {
				joinButton.setBackgroundColor(Color.BLUE)
			} else {
				joinButton.setBackgroundColor(Color.RED)
			}
		}
	}

	fun setChallengesList(challenges: List<Challenge>) {
		this.challengesList = challenges
		notifyDataSetChanged()
	}

	override fun getItemCount() = challengesList.size
}