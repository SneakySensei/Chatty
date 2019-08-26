package com.thefantasybus.chatty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.content.Intent                               //@NOTE IMPORT NEEDED FOR USING INTENTS
import android.graphics.Color
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.*
import com.thefantasybus.chatty.viewmodel.AuthMethod
import com.thefantasybus.chatty.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.activity_auth.*


class AuthActivity : AppCompatActivity() {
    lateinit var viewmodel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        viewmodel = ViewModelProviders.of(this).get(AuthViewModel::class.java)

        viewmodel.init()
        viewmodel.user.observe(this@AuthActivity, Observer {
            if(it==null) {
                return@Observer
            }
            else {
                val intent = Intent(this,ChatsActivity::class.java)
                startActivity(intent)
                finish() //ends the AuthActivity
            }
        })

        viewmodel.authmethod.observe(this@AuthActivity, Observer{
            if(it==null){
                return@Observer
            }
            else{
                updateUI(it)
            }
        })

    }



    fun updateUI(authMethod: AuthMethod){
        when(authMethod){
            AuthMethod.LOGIN -> {showLogin()}
            AuthMethod.SIGNUP -> {showSignup()}
        }
    }

    fun showLogin(){
        editText3.visibility = View.GONE

        button.text = getString(R.string.auth_button_login)
        textView2.text = "Don't have an account? Sign Up"
        textView.text = "Login to Chatty!"
        button.setOnClickListener {
            val email = editText.text.toString()
            val password = editText2.text.toString()
            if(email.isEmpty()) {
                editText.hint = "*Email required"
                editText.setHintTextColor(Color.rgb(200,60,60))
            }
            if(password.isEmpty()) {
                editText2.hint = "*Password required"
                editText2.setHintTextColor(Color.rgb(200,60,60))
            }
            if(email.isNotEmpty() && password.isNotEmpty())
                viewmodel.logIn(email,password)
        }


        textView2.setOnClickListener{
            viewmodel.authmethod.postValue(AuthMethod.SIGNUP)
        }
    }

    fun showSignup(){
        editText3.visibility = View.VISIBLE

        button.text = getString(R.string.auth_button_sign_up)
        textView2.text = "Already have an account? Login Instead"
        textView.text = "Sign Up to Chatty!"
        button.setOnClickListener {
            val name = editText3.text.toString()
            val email = editText.text.toString()
            val password = editText2.text.toString()
            if(name.isEmpty()) {
                editText3.hint = "*Name required"
                editText3.setHintTextColor(Color.rgb(200,100,100))
            }
            if(email.isEmpty()) {
                editText.hint = "*Email required"
                editText.setHintTextColor(Color.rgb(200,100,100))
            }
            if(password.isEmpty()) {
                editText2.hint = "*Password required"
                editText2.setHintTextColor(Color.rgb(200,100,100))
            }
            if(email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty())
                viewmodel.signUp(name, email, password)
        }

        textView2.setOnClickListener{
            viewmodel.authmethod.postValue(AuthMethod.LOGIN)
        }
    }
}
