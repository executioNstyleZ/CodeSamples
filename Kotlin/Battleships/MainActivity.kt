package com.apps.jaredshaw.hotseatbattleships

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.*
import java.util.*
import kotlin.concurrent.schedule

/**
 * Created by Jared on 11/21/2017.
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var rootLayout: LinearLayout
    private lateinit var welcomeText: TextView
    private lateinit var emailTextBox: EditText
    private lateinit var passwordTextBox: EditText
    private lateinit var repeatedPasswordTextBox: EditText
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rootLayout = LinearLayout(this)
        setContentView(rootLayout)

        rootLayout.orientation = LinearLayout.VERTICAL

        welcomeText = TextView(this)
        welcomeText.setText("Battleships")
        welcomeText.textSize = 30.0f
        welcomeText.gravity = Gravity.CENTER

        emailTextBox = EditText(this)
        emailTextBox.hint = "E-mail"

        passwordTextBox = EditText(this)
        passwordTextBox.hint = "Password"

        repeatedPasswordTextBox = EditText(this)
        repeatedPasswordTextBox.hint = "Repeat password"

        if(Database.isSignedIn()){
            displayLoggedInMenu()
        }else{
            displayLoginMenu()
        }

    }

    @SuppressLint("ResourceType")
    fun displayLoggedInMenu(){
        rootLayout.removeAllViews()

        val welcomeBackText = TextView(this)
        welcomeBackText.setText("Welcome back " + Database.getEmail())
        welcomeBackText.gravity = Gravity.CENTER

        val continueButton = Button(this)
        continueButton.setText("Continue")
        continueButton.setOnClickListener(this)
        continueButton.id = 4

        val logOutButton = Button(this)
        logOutButton.setText("Log Out")
        logOutButton.setOnClickListener(this)
        logOutButton.id = 5

        rootLayout.addView(welcomeText, linearLayout("match", "wrap", 0.5f))
        rootLayout.addView(welcomeBackText, linearLayout("match", "wrap", 1.0f))
        rootLayout.addView(continueButton, linearLayout("match", "wrap", 1.0f))
        rootLayout.addView(logOutButton, linearLayout("match", "wrap", 1.0f))
    }

    @SuppressLint("ResourceType")
    fun displayLoginMenu(){
        rootLayout.removeAllViews()

        val loginButton = Button(this)
        loginButton.setText("Login")
        loginButton.setOnClickListener(this)
        loginButton.id = 1

        val registerNewUserButton = Button(this)
        registerNewUserButton.setText("Register New Account")
        registerNewUserButton.setOnClickListener(this)
        registerNewUserButton.id = 2

        rootLayout.addView(welcomeText, linearLayout("match", "wrap", 0.5f))
        rootLayout.addView(emailTextBox, linearLayout("match", "wrap", 1.0f))
        rootLayout.addView(passwordTextBox, linearLayout("match", "wrap", 1.0f))
        rootLayout.addView(loginButton, linearLayout("match", "wrap", 1.0f))
        rootLayout.addView(registerNewUserButton, linearLayout("match", "wrap", 1.0f))
    }

    @SuppressLint("ResourceType")
    fun displayEmailVerificationMenu(){
        rootLayout.removeAllViews()

        val toasty = Toast.makeText(this, "You must verify your email before continuing", Toast.LENGTH_SHORT)
        toasty.show()

        rootLayout.addView(welcomeText, linearLayout("match", "wrap", 0.5f))

        val verifyText = TextView(this)
        verifyText.setText("Before continuing, you need to verify your email address. Click the button below to send an email verification")
        verifyText.gravity = Gravity.CENTER

        val verifyButton = Button(this)
        verifyButton.setText("Send/Resend Email")
        verifyButton.setOnClickListener(this)
        verifyButton.id = 6

        val continueButton = Button(this)
        continueButton.setText("Continue")
        continueButton.setOnClickListener(this)
        continueButton.id = 4

        rootLayout.addView(verifyText, linearLayout("match", "wrap", 1.0f))
        rootLayout.addView(verifyButton, linearLayout("match", "wrap", 1.0f))
        rootLayout.addView(continueButton, linearLayout("match", "wrap", 1.0f))
    }

    @SuppressLint("ResourceType")
    fun displayRegisterMenu(){
        rootLayout.removeAllViews()

        val registerButton = Button(this)
        registerButton.setOnClickListener(this)
        registerButton.id = 3
        registerButton.setText("Register")

        rootLayout.addView(welcomeText, linearLayout("match", "wrap", 0.5f))
        rootLayout.addView(emailTextBox, linearLayout("match", "wrap", 1.0f))
        rootLayout.addView(passwordTextBox, linearLayout("match", "wrap", 1.0f))
        rootLayout.addView(repeatedPasswordTextBox, linearLayout("match", "wrap", 1.0f))
        rootLayout.addView(registerButton, linearLayout("match", "wrap", 1.0f))
    }

    private fun linearLayout(firstParam: String, secondParam: String, gravity: Float): LinearLayout.LayoutParams{

        var first: Int
        var second: Int

        if(firstParam == "match"){
            first = LinearLayout.LayoutParams.MATCH_PARENT
        }
        else{
            first = LinearLayout.LayoutParams.WRAP_CONTENT
        }

        if(secondParam == "match"){
            second = LinearLayout.LayoutParams.MATCH_PARENT
        }else{
            second = LinearLayout.LayoutParams.WRAP_CONTENT
        }

        return LinearLayout.LayoutParams(first, second, gravity)
    }


    fun goToGameListActivity(){
        if(Database.isEmailVerified() == true){
            val myIntent: Intent = Intent(this, ListActivity::class.java)
            startActivity(myIntent)
        }else{
            displayEmailVerificationMenu()
        }
    }

    fun validatePassword(password: String): Boolean{

        var letterCheck = false
        var numberCheck = false
        var symbolCheck = false
        var hasSpace = false
        val numChars: Int = password.length
        val symbols: String = "!@#$%^&*()+-=_<>?{}[]';:,."

        for(i in 0 until password.length){
            val ch = password[i]
            if(Character.isLetter(ch)){
                letterCheck = true
            }
            else if(Character.isDigit(ch)){
                numberCheck = true
            }
            else if(ch == ' '){
                hasSpace = true
            }else if(symbols.contains(ch)){
                symbolCheck = true
            }
        }

        return (letterCheck && numberCheck && symbolCheck && numChars >= 8 && !hasSpace)
    }

    override fun onClick(clickedView: View?) {

        if(clickedView !is View) return

        //login
        if(clickedView.id == 1){
            if(emailTextBox.text.toString() == ""){
                val toasty = Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT)
                toasty.show()
                return
            }

            if(passwordTextBox.text.toString() == ""){
                val toasty = Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT)
                toasty.show()
                return
            }

            Database.login(emailTextBox.text.toString(), passwordTextBox.text.toString(), this)
        }

        //go to user reg page
        else if(clickedView.id == 2){
            displayRegisterMenu()
        }

        //reg new user
        else if (clickedView.id == 3){

            if(emailTextBox.text.toString() == ""){
                val toasty = Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT)
                toasty.show()
                return
            }

            if(passwordTextBox.text.toString() == ""){
                val toasty = Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT)
                toasty.show()
                return
            }

            if(validatePassword(passwordTextBox.text.toString()) == false){
                val toasty = Toast.makeText(this, "Password must be 8 characters in length\nand contain at least 1 letter, 1 number, and 1 symbol", Toast.LENGTH_SHORT)
                toasty.show()
                return
            }

            if(passwordTextBox.text.toString() == repeatedPasswordTextBox.text.toString()){

                Database.registerNewUser(emailTextBox.text.toString(), passwordTextBox.text.toString(), this)

            }else{
                val toasty = Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT)
                toasty.show()
            }
        }

        //continue to game List
        else if(clickedView.id == 4){
            goToGameListActivity()
        }

        //log out
        else if(clickedView.id == 5){

            Database.signout()
            displayLoginMenu()
        }

        //send email verification
        else if(clickedView.id == 6){
            Database.sendVerification(this)
        }
    }

}