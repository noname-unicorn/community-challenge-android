package com.miwas.togellenge.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miwas.togellenge.R
import com.miwas.togellenge.models.Challenge

class FeedChallengeAdapter(

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
		val name = holder.itemView.findViewById<TextView>(R.id.name)
		val participantsCount = holder.itemView.findViewById<TextView>(R.id.participant_count_text)
		val confirmationMethod = holder.itemView.findViewById<ImageView>(R.id.confirmation_method_image)
		name.text = challengesList[position].name
		participantsCount.text = challengesList[position].participants?.count().toString()
		val confirmationMethodImage = when (challengesList[position].confirmationMethod) {
			"photo" -> R.drawable.ic_camera
			"video" -> R.drawable.ic_videocamera
			else -> R.drawable.ic_notebook
		}
		confirmationMethod.background = confirmationMethod.context.getDrawable(confirmationMethodImage)
	}

	fun setChallengesList(challenges: List<Challenge>) {
		this.challengesList = challenges
		notifyDataSetChanged()
	}

	override fun getItemCount() = challengesList.size
}