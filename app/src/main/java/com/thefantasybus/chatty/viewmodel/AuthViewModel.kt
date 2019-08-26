package com.thefantasybus.chatty.viewmodel

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.thefantasybus.chatty.R
import kotlinx.android.synthetic.main.activity_auth.*

class AuthViewModel(application: Application): AndroidViewModel(application) {
    private lateinit var mAuth: FirebaseAuth
    val user = MutableLiveData<FirebaseUser>()
    val authmethod = MutableLiveData<AuthMethod>()


    fun init(){
        mAuth = FirebaseAuth.getInstance()

        // Check if user is signed in (non-null) and update UI accordingly.
        user.postValue(mAuth.currentUser)

        authmethod.postValue(AuthMethod.LOGIN) //set the default to SIGNIN
    }

    fun signUp(name: String, email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            //editText2.error = getString(R.string.error_weak_password);
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val request = UserProfileChangeRequest.Builder().setDisplayName(name).build()
                    mAuth.currentUser!!.updateProfile(request)
                    user.postValue(mAuth.currentUser)
                } else {
                    // If sign in fails, display a message to the user.


                    user.postValue(null)
                }

                // ...
            }
    }

    fun logIn(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    user.postValue(mAuth.currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    user.postValue(null)
                }

                // ...
            }
//            .addOnFailureListener(this) {
//            }
    }

}

enum class AuthMethod{
    LOGIN,
    SIGNUP
}