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
import android.widget.EditText
import android.widget.Toast


@SuppressLint("ValidFragment")
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PhysicalDetail.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PhysicalDetail.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhysicalDetail(mainScreen: OnTouchGoal3,context:Context) : Fragment() {


    lateinit var goal: OnTouchGoal3
    lateinit var mcontext: Context
    private lateinit var database: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var Height:EditText
    private lateinit var Weight:EditText

    init {
        goal=mainScreen
        mcontext=context
    }

    private lateinit var NEXT: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }






    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_physical_detail, container, false)
        NEXT=view.findViewById<Button>(R.id.next)
        Height=view.findViewById<EditText>(R.id.height)
        Weight=view.findViewById<EditText>(R.id.weight)
        database= mcontext.getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = database.edit()
        NEXT.setOnClickListener(){

            var stringheight=Height.text.toString()
            var stringweight=Weight.text.toString()
            editor.putString("height",stringheight)
            editor.putString("weight",stringweight)
            if(stringheight.equals("") || stringweight.equals("")){
                Toast.makeText(mcontext,"PLease Fill all the feilds and click on next button", Toast.LENGTH_SHORT).show()
            }
            else {
                editor.commit()
                goal.touchgoal3()
            }
        }
        return view
    }


    interface OnTouchGoal3{
        fun touchgoal3()
    }
}// Required empty public constructor
