package com.miwas.togellenge.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.miwas.togellenge.R

class AuthActivity : AppCompatActivity() {

	private lateinit var auth: FirebaseAuth

	private lateinit var loginButton: Button
	private lateinit var passwordEdit: TextInputEditText
	private lateinit var emailEdit: TextInputEditText
	private lateinit var haventAccount: TextView
	private lateinit var invalidAuthWarning: TextView
	private lateinit var sharedPreferences: SharedPreferences

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_auth)
		window.setFlags(
			WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
			WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
		)

		auth = Firebase.auth
		loginButton = findViewById(R.id.login_button)
		emailEdit = findViewById(R.id.email_edit_text)
		passwordEdit = findViewById(R.id.password_edit_text)
		haventAccount = findViewById(R.id.havent_account)
		invalidAuthWarning = findViewById(R.id.invalid_auth_warning)

		loginButton.setOnClickListener {
			if (checkFields()) {
				login(emailEdit.text.toString(), passwordEdit.text.toString(), it.context)
			}
		}

		haventAccount.setOnClickListener {
			val intent = Intent(this, RegActivity::class.java)
			startActivity(intent)
		}
	}


	private fun checkFields(): Boolean {
		var isInErrorState = false

		if (emailEdit.text.isNullOrEmpty()) {
			isInErrorState = true
			emailEdit.error = "Обязательное поле"
		}

		if (passwordEdit.text.isNullOrEmpty()) {
			isInErrorState = true
			passwordEdit.error = "Обязательное поле"
		}

		return !isInErrorState
	}

	private fun login(email: String, password: String, context: Context) {
		auth.signInWithEmailAndPassword(email, password)
			.addOnCompleteListener(this) { task ->
				if (task.isSuccessful) {
					Log.d("signIn", "signInWithEmail:success")
					finish()
				} else {
					Log.w("signIn", "signInWithEmail:failure", task.exception)
					runOnUiThread {
						if (task.exception is FirebaseAuthInvalidCredentialsException) {
							invalidAuthWarning.text =
								when ((task.exception as FirebaseAuthInvalidCredentialsException).errorCode) {
									"ERROR_INVALID_EMAIL" -> context.getText(R.string.invalid_email)
									"ERROR_WRONG_PASSWORD" -> context.getText(R.string.wrong_password)
									"ERROR_USER_NOT_FOUND" -> context.getText(R.string.user_not_found)
									else -> context.getText(R.string.error_occurred)
								}
							invalidAuthWarning.visibility = VISIBLE
						}
					}
				}

			}
	}

}