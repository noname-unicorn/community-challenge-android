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

class AuthActivity : AppCompatActivity() {

	private lateinit var auth: FirebaseAuth

	private lateinit var loginButton: Button
	private lateinit var passwordEdit: EditText
	private lateinit var emailEdit: EditText
	private lateinit var haventAccount: TextView
	private lateinit var sharedPreferences: SharedPreferences

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_auth)
		auth = Firebase.auth
		loginButton = findViewById(R.id.login_button)
		emailEdit = findViewById(R.id.email_edit)
		passwordEdit = findViewById(R.id.password_edit)
		haventAccount = findViewById(R.id.havent_account)
		//Является ли зареганным
		val currentUser = auth.currentUser
		loginButton.setOnClickListener { login(emailEdit.text.toString(), passwordEdit.text.toString()) }

		haventAccount.setOnClickListener {
			val intent = Intent(this, RegActivity::class.java)
			startActivity(intent)
		}
	}

	private fun login(email: String, password: String) {
		auth.signInWithEmailAndPassword(email, password)
			.addOnCompleteListener(this) { task ->
				if (task.isSuccessful) {
					// Sign in success, update UI with the signed-in user's information
					Log.d("signIn", "signInWithEmail:success")
					val user = auth.currentUser
				} else {
					// If sign in fails, display a message to the user.
					Log.w("signIn", "signInWithEmail:failure", task.exception)
					Toast.makeText(
						baseContext, "Authentication failed.",
						Toast.LENGTH_SHORT
					).show()
				}

			}
	}

}