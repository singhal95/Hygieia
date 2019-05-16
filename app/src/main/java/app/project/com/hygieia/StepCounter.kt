package app.project.com.hygieia

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.database.*
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_step_counter.*
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.view.LineChartView
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("ValidFragment")
class StepCounter (context:Context): Fragment(), SensorEventListener {



    private lateinit var firebasedatabase: FirebaseDatabase
    private lateinit var database: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var myRef:DatabaseReference
    private lateinit var datalist:ArrayList<String>
    private lateinit var countlist:ArrayList<Int>
    lateinit var context1:Context
    var running = false
    var sensorManager: SensorManager? = null
    private lateinit var stepCounter:TextView
    private  var k:Float = 0F
    private lateinit var button:Button
    private lateinit var graph: GraphView
    private lateinit var graph1: GraphView
    private lateinit var graph2: GraphView
    private lateinit var graph3: GraphView


    init {
        context1=context
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_step_counter, container, false)
        stepCounter=view.findViewById<TextView>(R.id.stepsValue)
        button=view.findViewById<Button>(R.id.showchart)
        sensorManager = context1.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        firebasedatabase= FirebaseDatabase.getInstance()
        graph=view.findViewById<GraphView>(R.id.graph1)
        graph1=view.findViewById<GraphView>(R.id.graph2)
        graph2=view.findViewById<GraphView>(R.id.graph3)
        graph3=view.findViewById<GraphView>(R.id.graph4)
        database= context1.getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = database.edit()
     myRef = firebasedatabase.getReference("Users")
        button.setOnClickListener(){

            datalist= ArrayList()
            countlist= ArrayList()
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



                            var k=countlist.get(countlist.size-1).toDouble()
                            val series2=LineGraphSeries(arrayOf(
                                    DataPoint(0.0,0.0),
                                    DataPoint(6.0, k),
                                    DataPoint(12.0,0.0)

                            ))
                            graph2.addSeries(series2)


                        }
                        else{
                            var value=data.getValue(Int::class.java)
                            var exactvount = value!! - countlist.get(countlist.size-1)

                            countlist.add(exactvount)
                        } }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })



        }
        return view
    }
    override fun onResume() {
        super.onResume()
        running = true
        var stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepsSensor == null) {
            Toast.makeText(context1, "No Step Counter Sensor !", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        running = false
        sensorManager?.unregisterListener(this)


        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val date=day.toString()+"-"+(month+1).toString()+"-"+year.toString()
        myRef.child(database.getString("userid","TEST")).child("StepCounter").child(date).setValue(k).addOnSuccessListener {

            Toast.makeText(context1,"Weight is Added",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {

            Toast.makeText(context1,"Please Chcek Your Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (running) {
             k=event.values[0]
            stepCounter.setText(k.toString())
        }
    }
}







// Required empty public constructor
