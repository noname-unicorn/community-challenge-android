package com.miwas.togellenge.ui.fragments.feed

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.miwas.togellenge.R
import com.miwas.togellenge.models.Challenge
import com.miwas.togellenge.ui.activities.AuthActivity
import com.miwas.togellenge.ui.adapters.FeedChallengeAdapter
import com.miwas.togellenge.utils.Constants.CHALLENGES_COLLECTION
import com.miwas.togellenge.utils.Constants.PARTICIPANTS_FIELD
import java.util.ArrayList

class FeedFragment : Fragment() {

	private lateinit var feedChallengeRecycler: RecyclerView
	private lateinit var dataBaseFirebase: FirebaseFirestore
	private lateinit var fireBaseAuth: FirebaseAuth
	private val challengesArray = mutableListOf<Challenge>()
	private lateinit var feedChallengeAdapter: FeedChallengeAdapter

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val root = inflater.inflate(R.layout.fragment_feed, container, false)
		feedChallengeRecycler = root.findViewById(R.id.feed_challenge_recycler)
		dataBaseFirebase = Firebase.firestore
		fireBaseAuth = Firebase.auth
		requestChallenges()

		val bookClickListener: JoinButtonListener = object : JoinButtonListener {
			override fun onClick(challenge: Challenge, position: Int) {
				if (fireBaseAuth.currentUser != null) {
					addOrDeleteParticipant(challenge, position)
				} else {
					val intent = Intent(context, AuthActivity::class.java)
					startActivity(intent)
				}
			}
		}
		feedChallengeAdapter = FeedChallengeAdapter(bookClickListener)

		feedChallengeRecycler.apply {
			adapter = feedChallengeAdapter
			layoutManager = LinearLayoutManager(context)
		}
		return root
	}

	private fun requestChallenges() {
		dataBaseFirebase
			.collection(CHALLENGES_COLLECTION)
			.get()
			.addOnSuccessListener { result ->
				for (document in result) {
					challengesArray.add(
						Challenge().apply {
							id = document.id
							name =
								if (document.data["name"] == null) null else document.data["name"] as String
							description =
								if (document.data["description"] == null) null else document.data["description"] as String
							date =
								if (document.data["date"] == null) null else document.data["date"] as Timestamp
							participants =
								if (document.data["participants"] == null) null else document.data["participants"] as List<String>
							isCurrentUserParticipate = participants?.contains(fireBaseAuth.uid) ?: false
							authorId =
								if (document.data["author"] == null) null else document.data["author"] as String
							confirmationMethod =
								if (document.data["accept_method"] == null) null else document.data["accept_method"] as String
						}
					)
					Log.e("res", "${document.id} => ${document.data}")
				}
				feedChallengeAdapter.setChallengesList(challengesArray)
			}
	}

	private fun addOrDeleteParticipant(challenge: Challenge, position: Int) {
		if (challenge.isCurrentUserParticipate) {
			challenge.isCurrentUserParticipate = false
			challenge.id?.let { id ->
				dataBaseFirebase.collection(CHALLENGES_COLLECTION)
					.document(id)
					.update(PARTICIPANTS_FIELD, FieldValue.arrayRemove(fireBaseAuth.currentUser?.uid))
					.addOnSuccessListener {
						feedChallengeAdapter.updateChallenge(position)
					}
			}
			(challenge.participants as ArrayList).remove(fireBaseAuth.currentUser?.uid)
		} else {
			challenge.isCurrentUserParticipate = true
			challenge.id?.let { id ->
				dataBaseFirebase.collection(CHALLENGES_COLLECTION)
					.document(id)
					.update(PARTICIPANTS_FIELD, FieldValue.arrayUnion(fireBaseAuth.currentUser?.uid))
					.addOnSuccessListener {
						feedChallengeAdapter.updateChallenge(position)
					}
			}
			(challenge.participants as ArrayList).add((fireBaseAuth.currentUser ?: return).uid)
		}
	}

	interface JoinButtonListener {
		fun onClick(challenge: Challenge, position: Int)
	}
}
