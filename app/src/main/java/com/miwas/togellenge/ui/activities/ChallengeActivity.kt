package com.miwas.togellenge.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.miwas.togellenge.R
import com.miwas.togellenge.models.Challenge
import com.miwas.togellenge.models.Confirmation
import com.miwas.togellenge.presentation.listeners.ConfirmChallengeListener
import com.miwas.togellenge.presentation.listeners.ReadyConfirmationListener
import com.miwas.togellenge.presentation.presenter.ChallengePresenter
import com.miwas.togellenge.presentation.view.ChallengeView
import com.miwas.togellenge.ui.popups.TextConfirmationDialog
import com.miwas.togellenge.utils.Constants.CHALLENGE_ID_PARAM
import java.text.SimpleDateFormat
import java.util.*


class ChallengeActivity : AppCompatActivity(), ChallengeView {

	private lateinit var challengePresenter: ChallengePresenter
	private lateinit var confirmChallengeButton: Button
	private lateinit var joinButton: Button
	private lateinit var challengeName: TextView
	private lateinit var participantsCount: TextView
	private lateinit var dateOfCreation: TextView
	private lateinit var confirmationMethodImageView: ImageView
	private var isVideoConfirmation: Boolean = false
	private lateinit var textConfirmationDialog: TextConfirmationDialog

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_challenge)
		intent.extras?.let { extras ->
			val challengeId = extras.getString(CHALLENGE_ID_PARAM)
			confirmChallengeButton = findViewById(R.id.confirm_button)
			joinButton = findViewById(R.id.join_button)
			challengeName = findViewById(R.id.name)
			participantsCount = findViewById(R.id.participants_count)
			dateOfCreation = findViewById(R.id.date_of_creation)
			confirmationMethodImageView = findViewById(R.id.confirmation_method)
			textConfirmationDialog = TextConfirmationDialog(this)
			initPresenter(challengeId)
		}
	}

	private fun initPresenter(challengeId: String?) {
		challengePresenter = ChallengePresenter(challengeId)
		challengePresenter.attachView(this)
		challengePresenter.viewIsReady()
	}

	override fun initView() {

		confirmChallengeButton.setOnClickListener {
			challengePresenter.confirmButtonClicked()
		}

		joinButton.setOnClickListener {
			Log.e("joinButton", "clicked")
		}
	}

	override fun startConfirmationByFile(isVideo: Boolean) {
		isVideoConfirmation = isVideo
		if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
			requestPermissions(permissions, PERMISSION_CODE)
		} else {
			if (isVideoConfirmation) {
				pickVideoFromGallery()
			} else {
				pickImageFromGallery()
			}
		}
	}

	override fun showTextConfirmationDialog(confirmChallengeListener: ConfirmChallengeListener) {

		textConfirmationDialog.showPopupWindow(
			confirmChallengeButton,
			confirmChallengeListener
		)
	}

	private fun pickImageFromGallery() {
		val intent = Intent(Intent.ACTION_PICK)
		intent.type = "image/*"
		startActivityForResult(intent, IMAGE_PICK_CODE)
	}

	private fun pickVideoFromGallery() {
		val intent = Intent(Intent.ACTION_PICK)
		intent.type = "video/*"
		startActivityForResult(intent, VIDEO_PICK_CODE)
	}

	companion object {
		private const val IMAGE_PICK_CODE = 1000
		private const val VIDEO_PICK_CODE = 1001
		private const val PERMISSION_CODE = 1002
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		when (requestCode) {
			PERMISSION_CODE -> {
				if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					if (isVideoConfirmation) {
						pickVideoFromGallery()
					} else {
						pickImageFromGallery()
					}
				} else {
					Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
				}
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == Activity.RESULT_OK) {
			when (requestCode) {
				IMAGE_PICK_CODE -> loadPhoto(data)
				VIDEO_PICK_CODE -> loadVideo(data)
			}
		}
	}

	private fun loadVideo(data: Intent?) {
		challengePresenter.loadVideo(data)
	}

	private fun loadPhoto(data: Intent?) {
		challengePresenter.loadPhoto(data)
	}

	override fun setInitialInfo(challenge: Challenge) {

		with(challenge) {
			challengeName.text = name
			participantsCount.text = participants?.count().toString()

			date?.let { timestamp ->
				val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.US)
				val dateString = formatter.format(timestamp.toDate())
				dateOfCreation.text = dateString
			}

			val confirmationMethodImage = when (confirmationMethod) {
				"photo" -> R.drawable.ic_camera
				"video" -> R.drawable.ic_videocamera
				else -> R.drawable.ic_notebook
			}

			confirmationMethodImageView.setImageDrawable(
				confirmationMethodImageView.context.getDrawable(
					confirmationMethodImage
				)
			)

			if (isCurrentUserAuthor) {
				joinButton.visibility = View.GONE
			} else {
				if (challenge.isCurrentUserParticipate) {
					joinButton.background = joinButton.context.getDrawable(R.drawable.rounded_button_red)
					joinButton.text = "Выйти"
					confirmChallengeButton.visibility = VISIBLE
				} else {
					joinButton.background = joinButton.context.getDrawable(R.drawable.rounded_button_active)
					joinButton.text = "Присоединиться"
					confirmChallengeButton.visibility = GONE
				}
			}
		}

	}

	override fun setConfirmations() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun goToAuth() {

	}

}
