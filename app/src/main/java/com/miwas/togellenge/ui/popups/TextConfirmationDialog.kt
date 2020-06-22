package com.miwas.togellenge.ui.popups

import android.app.Activity
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.google.android.material.textfield.TextInputEditText
import com.miwas.togellenge.R
import com.miwas.togellenge.presentation.listeners.ConfirmChallengeListener

class TextConfirmationDialog(private val activity: Activity) {

	private lateinit var confirmationEditText: TextInputEditText
	private lateinit var confirmButton: Button

	fun showPopupWindow(view: View, confirmChallengeListener: ConfirmChallengeListener) {
		val popupView = activity.layoutInflater.inflate(R.layout.text_confirmation_dialog, null)
		confirmationEditText = popupView.findViewById(R.id.confirmation_edit_text)
		confirmButton = popupView.findViewById(R.id.confirm_button)
		val width = LinearLayout.LayoutParams.MATCH_PARENT
		val height = LinearLayout.LayoutParams.MATCH_PARENT
		val focusable = true
		val popupWindow = PopupWindow(popupView, width, height, focusable)
		popupWindow.animationStyle = R.style.InfoWindowAnimation
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
		confirmButton.setOnClickListener {
			confirmChallengeListener.onConfirm(confirmationEditText.text.toString())
			popupWindow.dismiss()
		}
	}

}