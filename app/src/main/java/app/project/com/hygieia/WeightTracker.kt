package app.project.com.hygieia

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_weight_tracker.*
import lecho.lib.hellocharts.model.PointValue
import java.util.*


@SuppressLint("ValidFragment")
class WeightTracker (context: Context): Fragment() {




    private lateinit var myRef: DatabaseReference
    private lateinit var helo:String
    private lateinit var database: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var weight:EditText
    private lateinit var date1:TextView
    private lateinit var Submit:Button
    lateinit var mcontext:Context
    private lateinit var firebasedatabase: FirebaseDatabase
    private lateinit var datalist:ArrayList<String>
    private lateinit var countlist:ArrayList<Int>
    private var monthofyear:Int = 0
    private lateinit var button:Button
    private lateinit var graph: GraphView
    private lateinit var graph1: GraphView
    private lateinit var graph2: GraphView
    private lateinit var graph3: GraphView


    init {

        mcontext=context
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       var view=inflater.inflate(R.layout.fragment_weight_tracker, container, false)
        firebasedatabase= FirebaseDatabase.getInstance()
        weight=view.findViewById<EditText>(R.id.weight)
        date1=view.findViewById<TextView>(R.id.date)
        Submit=view.findViewById<Button>(R.id.submit)
        button=view.findViewById<Button>(R.id.showchart)
        graph=view.findViewById<GraphView>(R.id.graph1)
        graph1=view.findViewById<GraphView>(R.id.graph2)
        graph2=view.findViewById<GraphView>(R.id.graph3)
        graph3=view.findViewById<GraphView>(R.id.graph4)
        database= mcontext.getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = database.edit()

        date1.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                date.setText(dayOfMonth.toString()+"/"+monthOfYear.toString()+"/"+year)

                monthofyear=monthOfYear+1
            }, year, month, day)

            dpd.show()
        }
        Submit.setOnClickListener(){
            val progressDialog = ProgressDialog(mcontext)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Adding To Database ...")
            progressDialog.show()

            var weightstring=weight.text.toString()
            val userid=database.getString("userid" ,"TEST")
            var myRef = firebasedatabase.getReference("Users")
            var weighttrack=Weight(weightstring)
            var datestring=date.text.toString()
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val date=day.toString()+"-"+(month+1).toString()+"-"+year
            myRef.child(userid).child("weighttracker").child(date).setValue(weightstring).addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(mcontext,"Weight is Added",Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(mcontext,"Please Chcek Your Internet Connection", Toast.LENGTH_SHORT).show()
            }

        }
        button.setOnClickListener(){

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



        }

        return view
    }





}
