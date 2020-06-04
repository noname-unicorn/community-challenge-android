package com.miwas.togellenge.ui.fragments.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.miwas.togellenge.R
import com.miwas.togellenge.models.Challenge
import com.miwas.togellenge.models.User

class FeedFragment : Fragment() {

	private lateinit var toAuthButton: Button
	private lateinit var feedChallengeRecycler: RecyclerView
	private lateinit var dataBaseFirebase: FirebaseFirestore
	private val challengesArray = mutableListOf<Challenge>()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val root = inflater.inflate(R.layout.fragment_feed, container, false)
		feedChallengeRecycler = root.findViewById(R.id.feed_challenge_recycler)
		dataBaseFirebase = Firebase.firestore
		requestChallenges()
		return root
	}

	private fun requestChallenges() {
		dataBaseFirebase
			.collection("challenges")
			.get()
			.addOnSuccessListener { result ->
				for (document in result) {
					challengesArray.add(
						Challenge().apply {
							name =
								if (document.data["name"] == null) null else document.data["name"] as String
							description =
								if (document.data["description"] == null) null else document.data["description"] as String
							date =
								if (document.data["date"] == null) null else document.data["date"] as Timestamp
							participants =
								if (document.data["participants"] == null) null else document.data["participants"] as List<User>
							authorId =
								if (document.data["author"] == null) null else document.data["author"] as String
							confirmationMethod =
								if (document.data["accept_method"] == null) null else document.data["accept_method"] as String
						}
					)
					Log.e("res", "${document.id} => ${document.data}")
				}
				Log.e("res", "")
			}
	}
}
