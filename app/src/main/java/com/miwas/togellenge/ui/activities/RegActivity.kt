package com.miwas.togellenge.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.miwas.togellenge.R

class RegActivity : AppCompatActivity() {

	private lateinit var auth: FirebaseAuth

	private lateinit var registerButton: Button
	private lateinit var passwordEdit: EditText
	private lateinit var passwordConfirmEdit: EditText
	private lateinit var emailEdit: EditText
	private lateinit var alreadyHaveAccount: TextView
	private lateinit var sharedPreferences: SharedPreferences

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_reg)
		auth = Firebase.auth
		registerButton = findViewById(R.id.button_register)
		emailEdit = findViewById(R.id.email_edit_register)
		passwordEdit = findViewById(R.id.password_edit_register)
		passwordConfirmEdit = findViewById(R.id.password_confirm_edit_register)
		alreadyHaveAccount = findViewById(R.id.already_have_account)

		//Является ли зареганным
		val currentUser = auth.currentUser
		registerButton.setOnClickListener { reg(emailEdit.text.toString(), passwordEdit.text.toString()) }
		alreadyHaveAccount.setOnClickListener {
			val intent = Intent(this, AuthActivity::class.java)
			startActivity(intent)
		}
	}

	private fun reg(email: String, password: String) {
		auth.createUserWithEmailAndPassword(email, password)
			.addOnCompleteListener(this) { task ->
				if (task.isSuccessful) {
					// Sign in success, update UI with the signed-in user's information
					Log.d("singUp", "createUserWithEmail:success")
					val user = auth.currentUser
				} else {
					// If sign in fails, display a message to the user.
					Log.w("singUp", "createUserWithEmail:failure", task.exception)
					Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
				}
			}
	}
}