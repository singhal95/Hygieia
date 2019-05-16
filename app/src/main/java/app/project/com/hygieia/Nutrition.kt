package app.project.com.hygieia

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_nutrition.*
import java.util.*


@SuppressLint("ValidFragment")
class Nutrition (context: Context): Fragment() {



    private lateinit var choosedate: Button
    private lateinit var datevalue: TextView
    private lateinit var firebasedatabase: FirebaseDatabase
    private lateinit var context1 : Context
    private lateinit var database: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var mcontext:Context
    private lateinit var breakfastvalue:TextView
    private lateinit var lunchvalue:TextView
    private lateinit var dinnervsalue:TextView

    init {
        mcontext=context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view=inflater.inflate(R.layout.fragment_nutrition, container, false)
        firebasedatabase= FirebaseDatabase.getInstance()
        database= mcontext.getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = database.edit()
        choosedate=view.findViewById<Button>(R.id.choosedate)
        datevalue=view.findViewById<TextView>(R.id.datevalue)
        breakfastvalue=view.findViewById<TextView>(R.id.breakfastvalue)
        lunchvalue=view.findViewById<TextView>(R.id.lunchvalue)
       dinnervsalue=view.findViewById<TextView>(R.id.dinnervalue)

        choosedate.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                datevalue.setText(dayOfMonth.toString()+"-"+(monthOfYear+1).toString()+"-"+year)
                var myRef = firebasedatabase.getReference("Users")
                var breakfastref=myRef.child(database.getString("userid","TEST")).child("Meal").child(datevalue.text.toString()).child("BreakFast")
                var lunchref= myRef.child(database.getString("userid","TEST")).child("Meal").child(datevalue.text.toString()).child("Lunch")
                var dinnerref=myRef.child(database.getString("userid","TEST")).child("Meal").child(datevalue.text.toString()).child("Dinner")
                breakfastref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        val value =p0.getValue(String::class.java)
                        editor.putString("bv",value.toString())
                        editor.commit()
                        breakfastvalue.setText(database.getString("bv","0").toString())
                    }

                    override fun onCancelled(p0: DatabaseError) {
                    }

                })
                lunchref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        val value =p0.getValue(String::class.java)
                        editor.putString("lv",value.toString())
                        editor.commit()
                        lunchvalue.setText(database.getString("lv","0").toString())


                    }

                    override fun onCancelled(p0: DatabaseError) {
                    }

                })
                dinnerref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        val value =p0.getValue(String::class.java)
                        editor.putString("dv",value.toString())
                        editor.commit()
                     dinnervsalue.setText(database.getString("dv","0").toString())
                    }

                    override fun onCancelled(p0: DatabaseError) {
                    }

                })



            }, year, month, day)

            dpd.show()
        }


        return view
    }



}
