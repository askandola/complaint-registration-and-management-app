package me.askandola.complaintmanagementsystem

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import me.askandola.complaintmanagementsystem.daos.UserDao
import me.askandola.complaintmanagementsystem.models.User

class AdminSignInActivity : AppCompatActivity() {

    private lateinit var signInButton: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var emailText: TextView
    private lateinit var passwordText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var credentialsTextView: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_sign_in)

        signInButton = findViewById(R.id.signInButton)
        email = findViewById(R.id.emailField)
        password = findViewById(R.id.passwordField)
        progressBar = findViewById(R.id.progressBar)
        emailText = findViewById(R.id.enterEmailText)
        passwordText = findViewById(R.id.enterPasswordText)
        credentialsTextView = findViewById(R.id.credentialsTextView)

        auth = Firebase.auth

        signInButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {

        progressBar.visibility = View.VISIBLE
        emailText.visibility = View.GONE
        email.visibility = View.GONE
        passwordText.visibility = View.GONE
        password.visibility = View.GONE
        signInButton.visibility = View.GONE
        credentialsTextView.visibility = View.GONE

        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {
            if(it.isSuccessful)
                updateUI(it.result.user)
            else {
                Toast.makeText(this, "Wrong credentials!", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if(firebaseUser==null) {
            progressBar.visibility = View.GONE
            emailText.visibility = View.VISIBLE
            email.visibility = View.VISIBLE
            passwordText.visibility = View.VISIBLE
            password.visibility = View.VISIBLE
            signInButton.visibility = View.VISIBLE
            credentialsTextView.visibility = View.VISIBLE
        }
        else {
            val user = User(firebaseUser.uid, "admin", firebaseUser.email, "true")
            UserDao().setUser(user)

            val intent = Intent(this, AdminMainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val userLoginIntent = Intent(this, SignInActivity::class.java)
        startActivity(userLoginIntent)
        finish()
    }
}