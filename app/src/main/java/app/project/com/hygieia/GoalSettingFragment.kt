package app.project.com.hygieia

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.FirebaseDatabase


class GoalSettingFragment : Fragment() {




    private lateinit var startingweight:EditText
    private lateinit var currentweight:EditText
    private lateinit var goalweight:EditText
    private lateinit var submit:Button
    private lateinit var firebasedatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_goal_setting, container, false)
        startingweight=view.findViewById<EditText>(R.id.startweight)
        currentweight=view.findViewById<EditText>(R.id.currentweight)
        goalweight=view.findViewById<EditText>(R.id.goalweight)
        submit=view.findViewById<Button>(R.id.submit)
        firebasedatabase= FirebaseDatabase.getInstance()
        submit.setOnClickListener {

        }


        return view
    }



}
