package app.project.com.hygieia

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class MainScreen  : AppCompatActivity(),GoalFragment.OnTouchGoal,ActivityLevelFragment.OnTouchGoal1,BasicDetailFragment.OnTouchGoal2 ,PhysicalDetail.OnTouchGoal3{


    private lateinit var TEXTVIEW1: TextView
    private lateinit var TEXTVIEW2: TextView
    private lateinit var TEXTVIEW3: TextView
    private lateinit var TEXTVIEW4: TextView
    private lateinit var firebasedatabase:FirebaseDatabase
    private lateinit var fragmentManager:FragmentManager
    private lateinit var fragmentTransaction:FragmentTransaction
    private lateinit var database: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var  goal:GoalFragment
    private var calorie:Float = 0.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        TEXTVIEW1=findViewById<TextView>(R.id.text1)
        TEXTVIEW2=findViewById<TextView>(R.id.text2)
        TEXTVIEW3=findViewById<TextView>(R.id.text3)
        TEXTVIEW4=findViewById<TextView>(R.id.text4)
        firebasedatabase= FirebaseDatabase.getInstance()
        TEXTVIEW1.setBackgroundColor(Color.GREEN)
        database= getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = database.edit()
         goal=GoalFragment(this,applicationContext)
        openfragment(goal)

    }

    fun openfragment(fragment: Fragment){
        fragmentManager = getSupportFragmentManager()
        fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment,fragment)
        fragmentTransaction.commit()
    }
    override fun touchgoal() {
        TEXTVIEW1.setBackgroundColor(Color.BLACK)
        TEXTVIEW2.setBackgroundColor(Color.GREEN)
        val active=ActivityLevelFragment(this,applicationContext)
        openfragment(active)


    }
    override fun touchgoal1() {
        TEXTVIEW2.setBackgroundColor(Color.BLACK)
        TEXTVIEW3.setBackgroundColor(Color.GREEN)
        val active1=BasicDetailFragment(this,applicationContext)
        openfragment(active1)


    }

    override fun touchgoal2() {
        TEXTVIEW3.setBackgroundColor(Color.BLACK)
        TEXTVIEW4.setBackgroundColor(Color.GREEN)
        val active2=PhysicalDetail(this,applicationContext)
        openfragment(active2)


    }

    override fun touchgoal3() {
        val progressDialog = ProgressDialog(this@MainScreen)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Calculating ...")
        progressDialog.show()
        var myRef = firebasedatabase.getReference("Users")
        Log.i("TAG",myRef.toString())
        val email=database.getString("email","TEST")
        val userid=database.getString("userid" ,"TEST")
        val goal=database.getString("goal","TEST")
        val active=database.getString("activity","TEST")
        val gender=database.getString("gender","TEST")
        val name=database.getString("name","TEST")
        val location=database.getString("location","TEST")
        val birthday=database.getString("birthday","TEST")
        val height=database.getString("height","TEST")
        val weight=database.getString("weight","TEST")
        val weightinpounds=weight.toFloat()*2.20462
        val heightininches=height.toFloat()*0.393701
        val ageinyears=birthday.toInt()
        if(gender.equals("Male")){
            calorie= (66+(6.23*weightinpounds)+(12.7*heightininches)-(6.8*ageinyears)).toFloat()
            if(active.equals("Lightly Active")){
                calorie= (calorie*1.2).toFloat()

            }
            else if(active.equals("Active")){
                calorie= (calorie*1.375).toFloat()
            }
            else if(active.equals("Very Active")){
                calorie= (calorie*1.725).toFloat()
            }

            if(goal.equals("Lose weight")){
                calorie=calorie-500
            }
            else if(goal.equals("Gain Weight")){
                calorie=calorie+500
            }
        }
        else{
            calorie= (155+(4.35*weightinpounds)+(4.7*heightininches)-(14.7*ageinyears)).toFloat()
            if(active.equals("Lightly Active")){
                calorie= (calorie*1.2).toFloat()

            }
            else if(active.equals("Active")){
                calorie= (calorie*1.375).toFloat()
            }
            else if(active.equals("Very Active")){
                calorie= (calorie*1.725).toFloat()
            }

            if(goal.equals("Lose weight")){
                calorie=calorie-500
            }
            else if(goal.equals("Gain Weight")){
                calorie=calorie+500
            }
        }
        editor.putString("calorie",calorie.toString())
        editor.commit()
        val user=User(email,userid,goal,active,gender,name,location,birthday,height,weight,calorie.toString(),"TEST")
        myRef.child(userid).setValue(user).addOnSuccessListener {
            progressDialog.dismiss()

            val intent = Intent(this@MainScreen, FrontScreen::class.java)
            startActivity(intent)
            finish()
        } .addOnFailureListener {
            // Write failed

            Toast.makeText(applicationContext,"Please Chcek Your Internet Connection",Toast.LENGTH_SHORT).show()
            // ...
        }

    }


}
