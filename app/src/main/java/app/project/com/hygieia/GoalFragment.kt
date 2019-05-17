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
import kotlinx.android.synthetic.main.fragment_goal.*
import kotlinx.android.synthetic.main.fragment_goal.view.*


@SuppressLint("ValidFragment")
class GoalFragment(mainScreen:OnTouchGoal ,context: Context) : Fragment() {


    lateinit var goal:OnTouchGoal
    lateinit var mcontext:Context
    private lateinit var database: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
   private lateinit var radioGroup: RadioGroup
    private lateinit var radiobutton1:RadioButton
    private lateinit var radiobutton2:RadioButton
    private lateinit var radiobutton3:RadioButton


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
        val view=inflater.inflate(R.layout.fragment_goal, container, false)
        NEXT=view.findViewById<Button>(R.id.next)
        radioGroup=view.findViewById<RadioGroup>(R.id.radioGroup)
        radiobutton1=view.findViewById<RadioButton>(R.id.radioButton1)
        radiobutton2=view.findViewById<RadioButton>(R.id.radioButton2)
        radiobutton3=view.findViewById<RadioButton>(R.id.radioButton3)
        database= mcontext.getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = database.edit()
        if(! radiobutton1.isChecked || !radiobutton2.isChecked || !radiobutton3.isChecked ){
            NEXT.isEnabled=false
        }
        radiobutton1.setOnClickListener{
            NEXT.isEnabled=true
        }
        radiobutton2.setOnClickListener{
            NEXT.isEnabled=true
        }
        radiobutton3.setOnClickListener{
            NEXT.isEnabled=true
        }


        NEXT.setOnClickListener(){
            var id=radioGroup.checkedRadioButtonId
           if(id==radiobutton1.id){
               editor.putString("goal",radiobutton1.text.toString())
           }
            else if (id==radiobutton2.id){
               editor.putString("goal",radiobutton2.text.toString())
           }
            else if(id==radiobutton3.id){
               editor.putString("goal",radiobutton3.text.toString())
           }
            editor.commit()
            goal.touchgoal()
        }

        return view
    }


    interface OnTouchGoal{
        fun touchgoal()
    }





}// Required empty public constructor
