package app.project.com.hygieia

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView


@SuppressLint("ValidFragment")
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Hydraaation.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Hydraaation.newInstance] factory method to
 * create an instance of this fragment.
 */
class Hydraaation (context:Context): Fragment() {


    lateinit var context1:Context
    lateinit var BUTTON:ImageButton

init {
    context1=context
}

    private lateinit var mWaterCountDisplay:TextView
    var waterCount:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment



    val view=inflater.inflate(R.layout.fragment_hydraaation, container, false)
        mWaterCountDisplay=view.findViewById<TextView>(R.id.tv_water_count)
        mWaterCountDisplay.setText((waterCount).toString())
        BUTTON=view.findViewById<ImageButton>(R.id.ib_water_increment)
        BUTTON.setOnClickListener(){
            updateWaterCount()
        }

        return view
    }

    private fun updateWaterCount() {
        waterCount++;
        mWaterCountDisplay.setText((waterCount).toString())
    }





    }
    /**
     * Adds one to the water count and shows a toast
     */



// Required empty public constructor
