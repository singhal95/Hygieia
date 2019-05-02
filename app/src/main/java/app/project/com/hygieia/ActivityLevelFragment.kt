package app.project.com.hygieia

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup


@SuppressLint("ValidFragment")
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ActivityLevelFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ActivityLevelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ActivityLevelFragment(mainScreen: OnTouchGoal1,context: Context) : Fragment() {

    lateinit var goal: OnTouchGoal1

    private lateinit var database: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var radioGroup: RadioGroup
    private lateinit var radiobutton1: RadioButton
    private lateinit var radiobutton2: RadioButton
    private lateinit var radiobutton3: RadioButton
    private lateinit var radiobutton4: RadioButton
    lateinit var mcontext:Context
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
        // Inflate the layout for this fragment

        val view=inflater.inflate(R.layout.fragment_activity_level, container, false)
        NEXT=view.findViewById<Button>(R.id.next)
        radioGroup=view.findViewById<RadioGroup>(R.id.radioGroup)
        radiobutton1=view.findViewById<RadioButton>(R.id.radioButton1)
        radiobutton2=view.findViewById<RadioButton>(R.id.radioButton2)
        radiobutton3=view.findViewById<RadioButton>(R.id.radioButton3)
        radiobutton4=view.findViewById<RadioButton>(R.id.radioButton3)
        database= mcontext.getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = database.edit()


        NEXT.setOnClickListener(){
            var id=radioGroup.checkedRadioButtonId
            if(id==radiobutton1.id){
                editor.putString("activity",radiobutton1.text.toString())
            }
            else if (id==radiobutton2.id){
                editor.putString("activity",radiobutton2.text.toString())
            }
            else if(id==radiobutton3.id){
                editor.putString("activity",radiobutton3.text.toString())
            }
            else if(id==radiobutton4.id){
                editor.putString("activity",radiobutton4.text.toString())
            }
            editor.commit()
            goal.touchgoal1()
        }
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event

    interface OnTouchGoal1{
        fun touchgoal1()
    }



}// Required empty public constructor
