package app.project.com.hygieia

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import lecho.lib.hellocharts.model.PointValue
import java.util.*


@SuppressLint("ValidFragment")

class Hydraaation (context:Context): Fragment() {


    lateinit var context1:Context
    lateinit var BUTTON:ImageButton
    private lateinit var database: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var firebasedatabase: FirebaseDatabase
    private lateinit var button: Button
    private lateinit var graph: GraphView
    private lateinit var graph1: GraphView
    private lateinit var graph2: GraphView
    private lateinit var graph3: GraphView
    private lateinit var datalist:ArrayList<String>
    private lateinit var countlist:ArrayList<Int>


    init {
    context1=context
}

    private lateinit var mWaterCountDisplay:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment



    val view=inflater.inflate(R.layout.fragment_hydraaation, container, false)
        mWaterCountDisplay=view.findViewById<TextView>(R.id.tv_water_count)

        BUTTON=view.findViewById<ImageButton>(R.id.ib_water_increment)
        firebasedatabase= FirebaseDatabase.getInstance()
        database= context1.getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = database.edit()
        var count=database.getInt("waterintake",0)
        mWaterCountDisplay.setText((count).toString())
        button=view.findViewById<Button>(R.id.showchart)
        graph=view.findViewById<GraphView>(R.id.graph1)
        graph1=view.findViewById<GraphView>(R.id.graph2)
        graph2=view.findViewById<GraphView>(R.id.graph3)
        graph3=view.findViewById<GraphView>(R.id.graph4)


         BUTTON.setOnClickListener(){
            updateWaterCount()
        }

        datalist= ArrayList()
        countlist= ArrayList()
        var myRef=firebasedatabase.getReference("Users")
        var myref2= myRef.child(database.getString("userid","TEST")).child("StepCounter")
        myref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var datasnapshot1= dataSnapshot.children
                for (data in datasnapshot1){
                    datalist.add(data.key.toString())
                    if(countlist.size==0) {
                        var value=data.getValue(Int::class.java)
                        var exactvount = value
                        countlist.add(exactvount!!)



                    }
                    else{
                        var value=data.getValue(Int::class.java)
                        var exactvount = value!! - countlist.get(countlist.size-1)

                        countlist.add(exactvount)
                    }
                }
                var entries=ArrayList<PointValue>()
                var i=0;
                for(data in countlist) {

                    entries.add(PointValue(i.toFloat(), data.toFloat()))

                    i++
                }


                var series = LineGraphSeries(arrayOf
                (DataPoint(0.0, 1.0),
                        DataPoint(1.0, 5.0),
                        DataPoint(2.0, 3.0),
                        DataPoint(3.0, 2.0),
                        DataPoint(4.0, 5.0),
                        DataPoint(5.0, 3.0),
                        DataPoint(6.0, 2.0),
                        DataPoint(7.0, 5.0),
                        DataPoint(8.0, 3.0),
                        DataPoint(9.0, 2.0),
                        DataPoint(10.0, 6.0)))
                graph.addSeries(series)

                var series4 = LineGraphSeries(arrayOf
                (DataPoint(0.0, 1.0),
                        DataPoint(2.0, 2.0),
                        DataPoint(4.0, 3.0),
                        DataPoint(6.0, 2.0)
                ))
                graph1.addSeries(series4)
                var k=countlist.get(countlist.size-1).toDouble()
                val series2=LineGraphSeries(arrayOf(
                        DataPoint(0.0,0.0),
                        DataPoint(6.0, k),
                        DataPoint(12.0,0.0)

                ))
                graph2.addSeries(series2)

                var k1=countlist.get(countlist.size-1).toDouble()
                val series3=LineGraphSeries(arrayOf(
                        DataPoint(0.0,0.0),
                        DataPoint(1.0, k),
                        DataPoint(2.0,0.0)

                ))
                graph3.addSeries(series2)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })





        return view
    }

    private fun updateWaterCount() {
        val progressDialog = ProgressDialog(context1)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Adding To Database ...")
        progressDialog.show()
        var count=database.getInt("waterintake",0)
        count=count+1
        mWaterCountDisplay.setText((count).toString())
        editor.putInt("waterintake",count)
        editor.commit()
        var myref=firebasedatabase.getReference("Users")
        val userid=database.getString("userid" ,"TEST")
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val date=day.toString()+"-"+(month+1).toString()+"-"+year
        var watercount=Water(count.toString())
        myref.child(userid).child("waterintaker").child(date).setValue(count).addOnSuccessListener {
            progressDialog.dismiss()
            Toast.makeText(context1,"One Water Glass is Added", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            progressDialog.dismiss()
            Toast.makeText(context1,"Please Chcek Your Internet Connection", Toast.LENGTH_SHORT).show()
        }

    }





    }
    /**
     * Adds one to the water count and shows a toast
     */



// Required empty public constructor
