package com.miwas.togellenge.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.miwas.togellenge.R

class RegActivity : AppCompatActivity() {

	private lateinit var auth: FirebaseAuth

	private lateinit var registerButton: Button
	private lateinit var passwordEdit: TextInputEditText
	private lateinit var passwordConfirmEdit: TextInputEditText
	private lateinit var emailEdit: TextInputEditText
	private lateinit var alreadyHaveAccount: TextView
	private lateinit var invalidRegWarning: TextView
	private lateinit var sharedPreferences: SharedPreferences
	private lateinit var dataBaseFirebase: FirebaseFirestore

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_reg)
		window.setFlags(
			WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
			WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
		)
		auth = Firebase.auth
		dataBaseFirebase = Firebase.firestore
		registerButton = findViewById(R.id.button_register)
		emailEdit = findViewById(R.id.email_edit)
		passwordEdit = findViewById(R.id.password_edit)
		passwordConfirmEdit = findViewById(R.id.password_confirm_edit)
		alreadyHaveAccount = findViewById(R.id.already_have_account)
		invalidRegWarning = findViewById(R.id.invalid_reg_warning)

		registerButton.setOnClickListener {
			if (checkFields(it.context)) {
				reg(emailEdit.text.toString(), passwordEdit.text.toString(), it.context)
			}
		}
		alreadyHaveAccount.setOnClickListener {
			val intent = Intent(this, AuthActivity::class.java)
			startActivity(intent)
		}
	}

	private fun checkFields(context: Context): Boolean {
		var isInErrorState = false

		if (emailEdit.text.isNullOrEmpty()) {
			isInErrorState = true
			emailEdit.error = "Обязательное поле"
		}

		if (passwordEdit.text.isNullOrEmpty()) {
			isInErrorState = true
			passwordEdit.error = "Обязательное поле"
		}

		if (passwordConfirmEdit.text.isNullOrEmpty()) {
			isInErrorState = true
			passwordConfirmEdit.error = "Обязательное поле"
		}

		if (passwordEdit.text.toString() != passwordConfirmEdit.text.toString()) {
			isInErrorState = true
			invalidRegWarning.text = context.getText(R.string.passwords_not_match)
			invalidRegWarning.visibility = View.VISIBLE
		}

		return !isInErrorState
	}

	private fun reg(email: String, password: String, context: Context) {
		auth.createUserWithEmailAndPassword(email, password)
			.addOnCompleteListener(this) { task ->
				if (task.isSuccessful) {
					Log.d("singUp", "createUserWithEmail:success")
					addUser(auth.currentUser?.email)
					finish()
				} else {
					Log.w("singUp", "createUserWithEmail:failure", task.exception)
					runOnUiThread {
						if (task.exception is FirebaseAuthInvalidCredentialsException) {
							invalidRegWarning.text =
								when ((task.exception as FirebaseAuthInvalidCredentialsException).errorCode) {
									"ERROR_INVALID_EMAIL" -> context.getText(R.string.invalid_email)
									"ERROR_WEAK_PASSWORD" -> context.getText(R.string.weak_password)
									"ERROR_EMAIL_ALREADY_IN_USE" -> context.getText(R.string.already_has_user)
									else -> context.getText(R.string.error_occurred)
								}
							invalidRegWarning.visibility = View.VISIBLE
						}
					}
				}
			}
	}

	private fun addUser(email: String?) {

		val user = hashMapOf(
			"registration_date" to Timestamp.now(),
			"email" to email,
			"avatar" to "default"
		)

		dataBaseFirebase.collection("users")
			.add(user)
			.addOnSuccessListener { documentReference ->
				Log.d("DB", "DocumentSnapshot added with ID: ${documentReference.id}")
			}
			.addOnFailureListener { e ->
				Log.w("DB", "Error adding document", e)
			}

	}
}