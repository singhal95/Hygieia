package app.project.com.hygieia

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.flags.impl.SharedPreferencesFactory.getSharedPreferences
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


@SuppressLint("ValidFragment")
class HomeFragment(context: Context,mainScreen: OnTouchAdd) : Fragment() {



 lateinit var search:OnTouchAdd


    private lateinit var database: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var firebasedatabase: FirebaseDatabase
    private lateinit var BREAKFASTADD: TextView
    private lateinit var LUNCHADD: TextView
    private lateinit var DINNERADD: TextView
    private lateinit var caloriesremaining: TextView
    private lateinit var breakfastvalueshow:TextView
    private lateinit var lunchvalueshow:TextView
    private lateinit var dinnervalueshow:TextView
    lateinit var mcontext:Context


    init {
      search=mainScreen
        mcontext=context
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         var view=inflater.inflate(R.layout.fragment_home, container, false)
        firebasedatabase= FirebaseDatabase.getInstance()
        database= mcontext.getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = database.edit()
        caloriesremaining=view.findViewById<TextView>(R.id.caloriesremaing)
        BREAKFASTADD=view.findViewById<TextView>(R.id.breakfastadd)
        LUNCHADD=view.findViewById<TextView>(R.id.lunchadd)
        DINNERADD=view.findViewById<TextView>(R.id.dinneradd)
        breakfastvalueshow=view.findViewById<TextView>(R.id.breakfastCalorie)
        lunchvalueshow=view.findViewById<TextView>(R.id.lunchCalorie)
        dinnervalueshow=view.findViewById<TextView>(R.id.dinnercalorie)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val date=day.toString()+"-"+(month+1).toString()+"-"+year
        var myRef = firebasedatabase.getReference("Users")
        var myref2=myRef.child(database.getString("userid","TEST")).child("calorie")
        var myref3=myRef.child(database.getString("userid","TEST")).child("caloriesremaining").child(date)
        var breakfastvalue=myRef.child(database.getString("userid","TEST")).child("Meal").child(date).child("BreakFast")
        var LunchVal=myRef.child(database.getString("userid","TEST")).child("Meal").child(date).child("Lunch")
        var DinnerVal=myRef.child(database.getString("userid","TEST")).child("Meal").child(date).child("Dinner")

    breakfastvalue.addValueEventListener(object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {


            val value = dataSnapshot.getValue(String::class.java)
            Log.i("nitin123", value.toString())
            breakfastvalueshow.setText(value)
        }
    })
        LunchVal.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {


                val value = dataSnapshot.getValue(String::class.java)
                Log.i("nitin123", value.toString())
                lunchvalueshow.setText(value)


            }
        })
               DinnerVal.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {


                        val value = dataSnapshot.getValue(String::class.java)
                        Log.i("nitin123", value.toString())
                        dinnervalueshow.setText(value)


                    }
                })
        myref3.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                val value = dataSnapshot.getValue(Float::class.java)
                caloriesremaining.setText(value.toString())


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



        myref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                val value = dataSnapshot.getValue(String::class.java)
                Log.i("nitin123",value.toString())
                editor.putString("calorie",value)
                editor.commit()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        BREAKFASTADD.setOnClickListener {
          search.ontouchadd(1)
        }
        LUNCHADD.setOnClickListener {
          search.ontouchadd(2)

        }
        DINNERADD.setOnClickListener {
            search.ontouchadd(3)
        }

        return view
    }


    interface OnTouchAdd{
        fun ontouchadd(meal:Int)
    }



}// Required empty public constructor
