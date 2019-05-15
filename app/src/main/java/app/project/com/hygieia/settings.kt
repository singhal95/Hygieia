package app.project.com.hygieia

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import java.nio.file.Files.delete
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser






@SuppressLint("ValidFragment")
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [settings.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [settings.newInstance] factory method to
 * create an instance of this fragment.
 */
class settings (context: Context,mainScreen:OnTouchclick): Fragment() {

    lateinit var context1:Context
    lateinit var click:OnTouchclick

    init {
        context1=context
        click=mainScreen
    }


    private lateinit var mprofileedit:TextView
    private lateinit var mmygoalt:TextView
    private lateinit var mresetpassword:TextView
    private lateinit var mlogout:TextView
    private lateinit var mpdeleteaccount:TextView
    private lateinit var database: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view=inflater.inflate(R.layout.settings, container, false)
        mprofileedit=view.findViewById<TextView>(R.id.profileedit)
        mmygoalt=view.findViewById<TextView>(R.id.mygoal)
        database=context1.getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = database.edit()
        mresetpassword=view.findViewById<TextView>(R.id.resetpassword)
        mlogout=view.findViewById<TextView>(R.id.logout);
        mpdeleteaccount=view.findViewById<TextView>(R.id.deleteaccount);
        mprofileedit.setOnClickListener {

        }

        mresetpassword.setOnClickListener {
            FirebaseAuth.getInstance().sendPasswordResetEmail(database.getString("email","qwerty@gmail.com"))
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context1,"Password Reset Link Is Sent To Your Email Id",Toast.LENGTH_SHORT).show()
                            click.ontouchclick()
                        }
                    }
        }
        mlogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            click.ontouchclick()

        }
        mpdeleteaccount.setOnClickListener {

            val user = FirebaseAuth.getInstance().currentUser
            val credential = EmailAuthProvider
                    .getCredential(database.getString("email","qwerty@gmail.com"), database.getString("password","123456"))

            user!!.reauthenticate(credential)
                    .addOnCompleteListener {
                        user.delete()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        click.ontouchclick()
                                    }
                                }
                    }
        }

        return view
    }



    interface OnTouchclick{
        fun ontouchclick()
    }




    }

