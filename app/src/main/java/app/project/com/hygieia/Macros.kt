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
import kotlinx.android.synthetic.main.fragment_weight_tracker.*
import java.util.*


@SuppressLint("ValidFragment")
class Macros (context :Context) : Fragment() {



    private lateinit var choosedate:Button
    private lateinit var datevalue:TextView
    private lateinit var proteinvalue:TextView
    private lateinit var carbvalue:TextView
    private lateinit var fatvalue:TextView
    private lateinit var firebasedatabase: FirebaseDatabase
    private lateinit var context1 : Context
    private lateinit var database: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    init {
        context1=context
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_macros, container, false)
        firebasedatabase= FirebaseDatabase.getInstance()
        database= context1.getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = database.edit()
        choosedate=view.findViewById<Button>(R.id.choosedate)
        datevalue=view.findViewById<TextView>(R.id.datevalue)
        proteinvalue=view.findViewById<TextView>(R.id.proteinvalue)
        carbvalue=view.findViewById<TextView>(R.id.Carbsvalue)
        fatvalue=view.findViewById<TextView>(R.id.Fatvalue)
        choosedate.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                datevalue.setText(dayOfMonth.toString()+"-"+(monthOfYear+1).toString()+"-"+year)
                var myRef = firebasedatabase.getReference("Users")
                var proteinref=myRef.child(database.getString("userid","TEST")).child("Ingredeints").child(datevalue.text.toString()).child("protein")
                var fatref= myRef.child(database.getString("userid","TEST")).child("Ingredeints").child(datevalue.text.toString()).child("fat")
                var carbref=myRef.child(database.getString("userid","TEST")).child("Ingredeints").child(datevalue.text.toString()).child("carb")
                proteinref.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        val value =p0.getValue(Float::class.java)
                        editor.putFloat("pv", value!!.toFloat())
                        editor.commit()
                        proteinvalue.setText(database.getFloat("pv",0.0F).toString())
                    }

                    override fun onCancelled(p0: DatabaseError) {
                    }

                })
                fatref.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        val value =p0.getValue(Float::class.java)
                        editor.putFloat("fv", value!!.toFloat())
                        editor.commit()
                        fatvalue.setText(database.getFloat("fv",0.0F).toString())


                    }

                    override fun onCancelled(p0: DatabaseError) {
                    }

                })
                carbref.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        val value =p0.getValue(Float::class.java)
                        editor.putFloat("cv", value!!.toFloat())
                        editor.commit()
                        carbvalue.setText(database.getFloat("cv",0.0F).toString())

                    }

                    override fun onCancelled(p0: DatabaseError) {
                    }

                })



            }, year, month, day)

            dpd.show()

        }
        return view
    }


}// Required empty public constructor
