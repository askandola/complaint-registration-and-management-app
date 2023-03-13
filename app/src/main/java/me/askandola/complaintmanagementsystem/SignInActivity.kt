package me.askandola.complaintmanagementsystem

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import me.askandola.complaintmanagementsystem.daos.UserDao
import me.askandola.complaintmanagementsystem.models.User

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInButton: SignInButton
    private lateinit var progressBar: ProgressBar
    private val TAG = "SignInActivity Tag"
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var adminLoginButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth

        progressBar = findViewById(R.id.progressBar)
        googleSignInButton = findViewById(R.id.googleSignInButton)
        adminLoginButton = findViewById(R.id.adminLoginButton)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignInButton.setOnClickListener{
            signIn()
        }

        adminLoginButton.setOnClickListener {
            val adminLoginIntent = Intent(this, AdminSignInActivity::class.java)
            startActivity(adminLoginIntent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }


    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task?.getResult(ApiException::class.java)!!
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e)
            updateUI(null)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        googleSignInButton.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        adminLoginButton.visibility = View.GONE

        GlobalScope.launch(Dispatchers.IO) {
            val authBack = auth.signInWithCredential(credential).await()
            withContext(Dispatchers.Main) {
                updateUI(authBack.user)
            }
        }


    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null) {
            progressBar.visibility = View.VISIBLE
            googleSignInButton.visibility = View.GONE
            adminLoginButton.visibility = View.GONE

            GlobalScope.launch(Dispatchers.IO) {
                val myUser = UserDao().getUserById(user.uid).await().toObject(User::class.java)

                withContext(Dispatchers.Main) {
                    if(myUser!=null && myUser.admin == "true") {
                        val adminIntent = Intent(this@SignInActivity, AdminMainActivity::class.java)
                        startActivity(adminIntent)
                        finish()
                    }
                    else {
                        val dbUser = User(user.uid, user.displayName, user.email, "false")
                        UserDao().setUser(dbUser)

                        val mainActivityIntent = Intent(this@SignInActivity, MainActivity::class.java)
                        startActivity(mainActivityIntent)
                        finish()
                    }
                }
            }
        }
        else {
            progressBar.visibility = View.GONE
            googleSignInButton.visibility = View.VISIBLE
            adminLoginButton.visibility  =View.VISIBLE
        }
    }

}