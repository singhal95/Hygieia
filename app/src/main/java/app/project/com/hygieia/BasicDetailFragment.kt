package app.project.com.hygieia

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS


@SuppressLint("ValidFragment")
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BasicDetailFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BasicDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BasicDetailFragment(mainScreen: OnTouchGoal2,context: Context) : Fragment() {


    lateinit var goal: OnTouchGoal2
    lateinit var mcontext:Context
    private lateinit var database: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var radioGroup: RadioGroup
    private lateinit var radiobutton1: RadioButton
    private lateinit var radiobutton2: RadioButton
    private lateinit var birthday:EditText
    private lateinit var name:EditText
    private lateinit var location:EditText


    init {
        goal=mainScreen
        mcontext=context
    }

    private lateinit var NEXT:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view=inflater.inflate(R.layout.fragment_basic_detail, container, false)
        NEXT=view.findViewById<Button>(R.id.next)
        radioGroup=view.findViewById<RadioGroup>(R.id.radioGroup)
        radiobutton1=view.findViewById<RadioButton>(R.id.radioButton1)
        radiobutton2=view.findViewById<RadioButton>(R.id.radioButton2)
        birthday=view.findViewById<EditText>(R.id.birthday)
        location=view.findViewById<EditText>(R.id.location)
        name=view.findViewById<EditText>(R.id.name)
        database= mcontext.getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = database.edit()
        birthday.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                var date= dayOfMonth.toString() + "/"+monthOfYear.toString()+"/"+year.toString()
                birthday.setText(date)
            }, year, month, day)

            dpd.show()
        }

        NEXT.setOnClickListener(){
            var id=radioGroup.checkedRadioButtonId
            if(id==radiobutton1.id){
                editor.putString("gender",radiobutton1.text.toString())
            }
            else if (id==radiobutton2.id){
                editor.putString("gender",radiobutton2.text.toString())
            }
            var stringlocation=location.text.toString()
            var stringbirthday=birthday.text.toString()
            var stringname=name.text.toString()
            editor.putString("birthday",stringbirthday)
            editor.putString("location",stringlocation)
            editor.putString("name",stringname)
            editor.commit()
            goal.touchgoal2()
        }
        return view
    }

    interface OnTouchGoal2{
        fun touchgoal2()
    }





}// Required empty public constructor
