package app.project.com.hygieia

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences



class SignupActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var _emailText: EditText
    private lateinit var _passwordText: EditText
    private lateinit var _reEnterPasswordText:EditText
    private  lateinit var _signupButton: Button
    private lateinit  var _loginLink: TextView
    private lateinit var database:SharedPreferences
    private lateinit var editor:SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()
        _emailText=findViewById<EditText>(R.id.input_email)
        _passwordText=findViewById<EditText>(R.id.input_password)
        _reEnterPasswordText=findViewById<EditText>(R.id.input_reEnterPassword)
        _signupButton=findViewById<Button>(R.id.btn_signup)
        _loginLink=findViewById<TextView>(R.id.link_login)
        database=getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = database.edit()

        _loginLink.setOnClickListener(){
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        _signupButton.setOnClickListener(){
            signup()
        }
    }

    fun signup() {
        if (!validate()) {
            onSignupFailed()
            return
        }

        _signupButton.isEnabled = false

        val progressDialog = ProgressDialog(this@SignupActivity)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Creating Account...")
        progressDialog.show()


        val email = _emailText.text.toString()
        val password = _passwordText.text.toString()
     //   val reEnterPassword = _reEnterPasswordText.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        progressDialog.dismiss()
                        editor.putString("email",email)
                        editor.putString("userid",auth.uid)
                        editor.putString("password",password)
                        editor.putInt("waterintake",0)
                        editor.commit()
                        onSignupSuccess()

                    } else {

                        // If sign in fails, display a message to the User.
                        Toast.makeText(this@SignupActivity, "Successfully not  Created ", Toast.LENGTH_SHORT).show()
                        onSignupFailed()
                    }

                    // ...
                })


    }


    fun onSignupSuccess() {
        _signupButton.isEnabled = true
        val intent = Intent(this@SignupActivity, MainScreen::class.java)
        intent.putExtra("email", _emailText.toString())
        startActivity(intent)

    }

    fun onSignupFailed() {
        Toast.makeText(baseContext, "Login failed", Toast.LENGTH_LONG).show()

        _signupButton.isEnabled = true
    }

    fun validate(): Boolean {
        var valid = true

        val email = _emailText.text.toString()
        val password = _passwordText.text.toString()
        val reEnterPassword = _reEnterPasswordText.text.toString()


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.error = "enter a valid email address"
            valid = false
        } else {
            _emailText.error = null
        }


        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            _passwordText.error = "between 4 and 10 alphanumeric characters"
            valid = false
        } else {
            _passwordText.error = null
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length < 4 || reEnterPassword.length > 10 || reEnterPassword != password) {
            _reEnterPasswordText.error = "Password Do not match"
            valid = false
        } else {
            _reEnterPasswordText.error = null
        }

        return valid
    }
}
