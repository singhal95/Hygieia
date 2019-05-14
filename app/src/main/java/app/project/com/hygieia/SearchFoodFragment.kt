package app.project.com.hygieia

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList


@SuppressLint("ValidFragment")
class SearchFoodFragment(context: Context, mainScreen: OnBarcodeLogoTouch): Fragment() {


    private lateinit var mcontext:Context
    private lateinit var search:OnBarcodeLogoTouch
    private lateinit var firebasedatabase: FirebaseDatabase
    private lateinit var foodsnapshot:Iterable<DataSnapshot>
    private lateinit var FoodList:List<Food>


    init {
        search=mainScreen
        mcontext=context
    }


    private lateinit var BREAKFASTADD: TextView
    private lateinit var BARCODESCAN:ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

     var view= inflater.inflate(R.layout.fragment_search_food, container, false)
        BARCODESCAN=view.findViewById<ImageButton>(R.id.barcodescan)
        firebasedatabase= FirebaseDatabase.getInstance()
        val myRef = firebasedatabase.getReference("Food")
        FoodList= ArrayList()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                foodsnapshot= dataSnapshot.children
                for (data in foodsnapshot){
                    Log.i("nitin123",data.toString())
                    var foodd=data.getValue(Food::class.java)
                    Log.i("nitin123", foodd!!.Carb.toString())
                    (FoodList as ArrayList<Food>).add(foodd!!)

                }
                for(data in FoodList){
                    Log.i("nitin123",data.BarCode.toString())
                }


            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        BARCODESCAN.setOnClickListener(){
            search.ontouch()
        }
        return view
    }


    interface OnBarcodeLogoTouch{
        fun ontouch()
    }


}
