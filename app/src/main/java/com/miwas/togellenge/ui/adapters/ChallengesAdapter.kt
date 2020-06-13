package com.miwas.togellenge.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miwas.togellenge.R
import com.miwas.togellenge.models.Challenge
import com.miwas.togellenge.presentation.listeners.JoinButtonListener

class ChallengesAdapter(
	private val joinClickListener: JoinButtonListener
) : RecyclerView.Adapter<ChallengesAdapter.FeedChallengeViewHolder>() {

	private var challengesList = mutableListOf<Challenge>()

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

		if (challenge.isCurrentUserAuthor) {
			joinButton.visibility = GONE
		} else {
			if (challenge.isCurrentUserParticipate) {
				joinButton.background = holder.itemView.context.getDrawable(R.drawable.rounded_button_red)
				joinButton.text = "-"
			} else {
				joinButton.background = holder.itemView.context.getDrawable(R.drawable.rounded_button_active)
				joinButton.text = "+"
			}
		}

		name.text = challenge.name
		participantsCount.text = challenge.participants?.count().toString()
		val confirmationMethodImage = when (challenge.confirmationMethod) {
			"photo" -> R.drawable.ic_camera
			"video" -> R.drawable.ic_videocamera
			else -> R.drawable.ic_notebook
		}
		confirmationMethod.setImageDrawable(confirmationMethod.context.getDrawable(confirmationMethodImage))
		joinButton.setOnClickListener {
			joinClickListener.onClick(challenge, position)
		}
	}

	fun setChallengesList(challenges: MutableList<Challenge>) {
		this.challengesList = challenges
		notifyDataSetChanged()
	}

	fun updateChallenge(position: Int) {
		notifyItemChanged(position)
	}

	fun removeChallenge(challenge: Challenge) {
		challengesList.remove(challenge)
		notifyDataSetChanged()
	}

	override fun getItemCount() = challengesList.size
}