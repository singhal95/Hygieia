package app.project.com.hygieia

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var _emailText:EditText
    private lateinit var _passwordText:EditText
    private lateinit var _loginButton: Button
    private lateinit var _signupLink: TextView
    private lateinit var database: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         auth = FirebaseAuth.getInstance()
        _emailText = findViewById<EditText>(R.id.input_email)
        _passwordText = findViewById<EditText>(R.id.input_password)
         _loginButton=findViewById<Button>(R.id.btn_login)
         _signupLink=findViewById<TextView>(R.id.link_signup)
        database=getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = database.edit()
         _signupLink.setOnClickListener {
            // Start the Signup activity
            val intent = Intent(applicationContext, SignupActivity::class.java)
            startActivity(intent)
            finish()
         }
        _loginButton.setOnClickListener(){
            login()
        }


    }

    public override fun onStart() {
        super.onStart()
        // Check if User is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
    }


    fun login() {

        if (!validate()) {
            onLoginFailed()
            return
        }

        _loginButton.isEnabled = false

        val progressDialog = ProgressDialog(this)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Authenticating...")
        progressDialog.show()

        val email = _emailText.getText().toString()
        val password = _passwordText.getText().toString()


        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in User's information
                        val user = auth.getCurrentUser()
                        editor.putString("email",email)
                        editor.putString("userid",auth.uid)
                        editor.putString("password",password)
                        editor.commit()
                        onLoginSuccess()
                        progressDialog.dismiss()

                    } else {
                        // If sign in fails, display a message to the User.

                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        onLoginFailed()
                        progressDialog.dismiss()

                    }

                    // ...
                })
    }
    fun onLoginSuccess() {
        _loginButton.isEnabled = true
        val intent = Intent(this, FrontScreen::class.java)
        intent.putExtra("email", _emailText.toString())
        startActivity(intent)
    }

    fun onLoginFailed() {
        Toast.makeText(baseContext, "Login failed", Toast.LENGTH_LONG).show()

        _loginButton.isEnabled = true
    }

    fun validate(): Boolean {
        var valid = true

        val email = _emailText.getText().toString()
        val password = _passwordText.getText().toString()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address")
            valid = false
        } else {
            _emailText.setError(null)
        }

        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters")
            valid = false
        } else {
            _passwordText.setError(null)
        }

        return valid
    }
}
