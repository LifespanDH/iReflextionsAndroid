package com.lifespandh.ireflexions.auth

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.lifespandh.ireflexions.R
import com.lifespandh.ireflexions.base.BaseActivity
import com.lifespandh.ireflexions.utils.livedata.observeFreshly
import com.lifespandh.ireflexions.utils.logs.logD
import com.lifespandh.ireflexions.utils.logs.logE
import com.lifespandh.ireflexions.utils.network.OAUTH_KEY
import com.lifespandh.ireflexions.utils.network.createJsonRequestBody
import com.lifespandh.ireflexions.utils.ui.toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest

    private val authViewModel by viewModels<AuthViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
    }

    private fun init() {
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.your_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.your_web_client_id))
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()

        setListeners()
        setObservers()
    }

    private fun setListeners() {
        signInButton.setOnClickListener {
//            val signInIntent = mGoogleSignInClient.signInIntent
//            startActivityForResult(
//                signInIntent,
//                RC_SIGN_IN
//            )
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this) { result ->
                    try {
                        startIntentSenderForResult(
                            result.pendingIntent.intentSender, 12,
                            null, 0, 0, 0, null)
                    } catch (e: IntentSender.SendIntentException) {
                        logE("Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener(this) { e ->
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    logD(e.localizedMessage)
                    oneTapClient.beginSignIn(signUpRequest)
                        .addOnSuccessListener(this) { result ->
                            try {
                                startIntentSenderForResult(
                                    result.pendingIntent.intentSender, 12,
                                    null, 0, 0, 0)
                            } catch (e: IntentSender.SendIntentException) {
//                                Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                            }
                        }
                        .addOnFailureListener(this) { e ->
                            // No Google Accounts found. Just continue presenting the signed-out UI.
//                            Log.d(TAG, e.localizedMessage)
                        }
                }
        }

        signupbutton.setOnClickListener {
            startActivity(RegistrationActivity.newInstance(this))
        }
    }

    private fun setObservers() {
        authViewModel.tokenLiveData.observeFreshly(this) {
            // We get token here, now we can proceed to the next screen
        }

        authViewModel.errorLiveData.observeFreshly(this) {
            toast(it)
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleSignInResult(task)
//        }
//    }
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    when (requestCode) {
        12 -> {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
                val username = credential.id
                val password = credential.password

                logE("called here $credential $idToken $username $password")
                val requestBody = createJsonRequestBody(OAUTH_KEY to idToken)
                authViewModel.loginUser(requestBody)
                when {
                    idToken != null -> {
                        // Got an ID token from Google. Use it to authenticate
                        // with your backend.
//                        Log.d(TAG, "Got ID token.")
                    }
                    password != null -> {
                        // Got a saved username and password. Use them to authenticate
                        // with your backend.
//                        Log.d(TAG, "Got password.")
                    }
                    else -> {
                        // Shouldn't happen.
//                        Log.d(TAG, "No ID token or password!")
                    }
                }
            } catch (e: ApiException) {
                // ...
            }
        }
    }
}

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.

            val requestBody = createJsonRequestBody(OAUTH_KEY to account.serverAuthCode)
            authViewModel.loginUser(requestBody)
            Log.d("signInResult", "Successful")

        } catch (e: ApiException) {
            Log.d("signInResult:failed code=", ""+ e.statusCode)
        }
    }
}





