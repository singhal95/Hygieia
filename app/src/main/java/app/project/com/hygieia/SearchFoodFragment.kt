package app.project.com.hygieia

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_search_food.*
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.content.Intent
import android.content.SharedPreferences
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import java.util.*


@SuppressLint("ValidFragment")
class SearchFoodFragment(context: Context, mainScreen: OnBarcodeLogoTouch,meal :Int,barcode:Int): Fragment() {


    private lateinit var mcontext:Context
    private lateinit var search:OnBarcodeLogoTouch
    private lateinit var firebasedatabase: FirebaseDatabase
    private lateinit var foodsnapshot:Iterable<DataSnapshot>
    private lateinit var FoodList:List<Food>
    private lateinit var lidtview:ListView
    private lateinit var searchtext:EditText
    private lateinit var SearchList:ArrayList<Food>
    private lateinit var adapter:ArrayAdapter<String>
    private lateinit var foodnamelist:ArrayList<String>
    private lateinit var SearchButton:Button
    private lateinit var Detailshow:LinearLayout
    private lateinit var FOOD_NAME:TextView
    private lateinit var FOOD_BARCODE:TextView
    private lateinit var FOOD_CARB:TextView
    private lateinit var FOOD_PROTEIN:TextView
    private lateinit var FOOD_FAT:TextView
    private lateinit var Food_CALORIES:TextView
    private lateinit var Detailsadd:LinearLayout
    private lateinit var Edit_FOOD_NAME:TextView
    private lateinit var Edit_FOOD_BARCODE:TextView
    private lateinit var Edit_FOOD_CARB:TextView
    private lateinit var Edit_FOOD_PROTEIN:TextView
    private lateinit var Edit_FOOD_FAT:TextView
    private lateinit var Edit_Food_CALORIES:TextView
    private lateinit var ADD_FOOD:Button
    private lateinit var ADD_DATABASE:Button
    private lateinit var ADDMEAL:Button
    private lateinit var CALORIEREMAINNING:String
    private lateinit var totalbreakfastcalorie:String
    private lateinit var totallunchcalorie:String
    private lateinit var totaldinnercalorie:String
    private var barcode:Int = 0
    private var meal:Int
    private  var total:Float = 0.0f
    private lateinit var flag:TextView

    private lateinit var database: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    init {
        search=mainScreen
        mcontext=context
        this.meal=meal
        this.barcode=barcode
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
        lidtview=view.findViewById<ListView>(R.id.foodlist)
        searchtext=view.findViewById<EditText>(R.id.searchEditText)
        SearchButton=view.findViewById<Button>(R.id.searchbutton)
        Detailshow=view.findViewById<LinearLayout>(R.id.detailsshow)
        FOOD_NAME=view.findViewById<TextView>(R.id.namevalue)
        FOOD_BARCODE=view.findViewById<TextView>(R.id.Barcodevalue)
        FOOD_CARB=view.findViewById<TextView>(R.id.Carbsvalue)
        FOOD_PROTEIN=view.findViewById<TextView>(R.id.proteinvalue)
        FOOD_FAT=view.findViewById<TextView>(R.id.Fatvalue)
        Food_CALORIES=view.findViewById<TextView>(R.id.Caloriesvalue)
        Detailsadd=view.findViewById<LinearLayout>(R.id.detailsadd)
        Edit_FOOD_NAME=view.findViewById<TextView>(R.id.editnamevalue)
        Edit_FOOD_BARCODE=view.findViewById<TextView>(R.id.editBarcodevalue)
        Edit_FOOD_CARB=view.findViewById<TextView>(R.id.editCarbsvalue)
        Edit_FOOD_PROTEIN=view.findViewById<TextView>(R.id.editproteinvalue)
        Edit_FOOD_FAT=view.findViewById<TextView>(R.id.editFatvalue)
        Edit_Food_CALORIES=view.findViewById<TextView>(R.id.editCaloriesvalue)
        ADD_FOOD=view.findViewById<Button>(R.id.addfood)
        ADDMEAL=view.findViewById<Button>(R.id.add)
        flag=view.findViewById<TextView>(R.id.test)
        ADD_DATABASE=view.findViewById<Button>(R.id.addtodatabse)
        database= mcontext.getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = database.edit()
        totalbreakfastcalorie="0"
        totallunchcalorie="0"
        totaldinnercalorie="0"
        firebasedatabase= FirebaseDatabase.getInstance()
        val myRef = firebasedatabase.getReference("Food")
        var myRefcalorie = firebasedatabase.getReference("Users")
        var myref2=myRefcalorie.child(database.getString("userid","TEST")).child("calorie")
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val date=day.toString()+"-"+(month+1).toString()+"-"+year
        var breakfastvalue=myRefcalorie.child(database.getString("userid","TEST")).child("Meal").child(date).child("BreakFast")
        var LunchVal=myRefcalorie.child(database.getString("userid","TEST")).child("Meal").child(date).child("Lunch")
        var DinnerVal=myRefcalorie.child(database.getString("userid","TEST")).child("Meal").child(date).child("Dinner")
        var caloriesremaing=myRefcalorie.child(database.getString("userid","TEST")).child("caloriesremaining")
        var caloriesintake=myRefcalorie.child(database.getString("userid","TEST")).child("caloriesintake")
        myref2.addValueEventListener(object : ValueEventListener {


            override fun onDataChange(dataSnapshot: DataSnapshot) {


               val value = dataSnapshot.getValue(String::class.java)
                CALORIEREMAINNING=value.toString()
                editor.putString("calorie",CALORIEREMAINNING)
                editor.commit()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        FoodList= ArrayList()
        SearchList= ArrayList()
        foodnamelist= ArrayList()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                foodsnapshot= dataSnapshot.children
                for (data in foodsnapshot){
                    Log.i("nitin123",data.toString())
                    var foodd=data.getValue(Food::class.java)
                    foodd!!.name=data.key
                    (FoodList as ArrayList<Food>).add(foodd!!)

                }
                for(data in FoodList){
                    Log.i("nitin123",data.name.toString())
                }

                if(barcode==1){
                    SearchList.clear()
                    foodnamelist.clear()
                    Detailshow.visibility=View.GONE
                    var searchString=searchtext.text.toString()
                    for(data in FoodList) {
                        if (database.getString("barcode","TEST").equals(data.Barcode.toString())) {
                            SearchList.add(data)
                            foodnamelist.add(data.name!!)
                            adapter = ArrayAdapter(mcontext, android.R.layout.simple_list_item_1, foodnamelist)
                            lidtview.adapter = adapter
                        }
                    }
                    if(SearchList.size==0 && foodnamelist.size==0){
                        ADD_FOOD.visibility=View.VISIBLE
                        ADD_FOOD.setOnClickListener(){
                            Detailsadd.visibility=View.VISIBLE
                        }
                    }
                }


            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        SearchButton.setOnClickListener(){
            SearchList.clear()
            foodnamelist.clear()
            Detailshow.visibility=View.GONE
            var searchString=searchtext.text.toString()
            for(data in FoodList) {
                if (searchString.equals(data.name)) {
                    SearchList.add(data)
                    foodnamelist.add(data.name!!)
                    adapter = ArrayAdapter(mcontext, android.R.layout.simple_list_item_1, foodnamelist)
                    lidtview.adapter = adapter
                }
            }
        if(SearchList.size==0 && foodnamelist.size==0){
            ADD_FOOD.visibility=View.VISIBLE
            ADD_FOOD.setOnClickListener(){
                Detailsadd.visibility=View.VISIBLE
            }
        }

        }
        lidtview.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            Detailshow.visibility=View.VISIBLE
            FOOD_NAME.setText(SearchList.get(position).name.toString())
            FOOD_BARCODE.setText(SearchList.get(position).Barcode.toString())
            FOOD_CARB.setText(SearchList.get(position).Carb.toString())
            FOOD_PROTEIN.setText(SearchList.get(position).Protien.toString())
            FOOD_FAT.setText(SearchList.get(position).Fat.toString())
            Food_CALORIES.setText(SearchList.get(position).Calories.toString())

            ADDMEAL.setOnClickListener(){
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                var month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                var ref1=firebasedatabase.getReference("Users")
                val ref2=ref1.child(database.getString("userid","TEST")).child("Ingredeints").child(date)
                val protein=ref2.child("protein")
                val carb=ref2.child("carb")
                val fat=ref2.child("fat")
                var ingredient=IngridentDetail()
                protein.addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {


                    }



                    override fun onDataChange(p0: DataSnapshot) {
                        var value = p0.getValue(Float::class.java)
                        editor.putFloat("protein", value!!.toFloat())
                        editor.commit()

                    }

                })

                carb.addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        var value = p0.getValue(Float::class.java)
                        editor.putFloat("carb", value!!.toFloat())
                        editor.commit()

                    }

                })
          //      ingredient.carb=flag.text.toString().toFloat()
                fat.addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        var value = p0.getValue(Float::class.java)
                        editor.putFloat("fat", value!!.toFloat())
                        editor.commit()

                    }

                })
                if(!date.equals(database.getString("datep","0"))){
                    ingredient.protein= 0.0F
                    ingredient.carb=0.0F
                    ingredient.fat=0.0F
                    editor.putString("datep",date)
                    editor.commit()
                }
                else {
                    ingredient.protein=database.getFloat("protein",0.0F)
                    ingredient.carb=database.getFloat("carb",0.0F)
                    ingredient.fat=database.getFloat("fat",0.0F)
                }


                ingredient.protein=ingredient.protein+ SearchList.get(position).Protien!!.toFloat()
                ingredient.carb=ingredient.carb+ SearchList.get(position).Carb!!.toFloat()
                ingredient.fat=ingredient.fat+ SearchList.get(position).Fat!!.toFloat()
                ref2.setValue(ingredient).addOnSuccessListener {

                }.addOnFailureListener {
                    Toast.makeText(context,"Cantt be added", Toast.LENGTH_SHORT).show()
                }


                if(meal==1){
                    breakfastvalue.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {


                            val value = dataSnapshot.getValue(String::class.java)
                            totalbreakfastcalorie=value.toString()
                            Log.i("nitinzx",totalbreakfastcalorie)
                            editor.putString("breakfast",totalbreakfastcalorie)
                            editor.commit()

                        }
                    })
                    if(!date.equals(database.getString("dateb","0"))){
                        totalbreakfastcalorie="0"
                        editor.putString("dateb",date)
                        editor.commit()
                    }
                    else {
                        totalbreakfastcalorie = database.getString("breakfast", "0")
                    }

                    total=totalbreakfastcalorie.toFloat()+ SearchList.get(position).Calories!!.toFloat()
                    Log.i("nitinzx",totalbreakfastcalorie)
                    Log.i("nitinzx",SearchList.get(position).Calories.toString())
                    myRefcalorie.child(database.getString("userid","TEST")).child("Meal").child(date).child("BreakFast").setValue(total.toString()).addOnSuccessListener {
                        Toast.makeText(mcontext,"BreakFast Calorie is Added",Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener {
                        Toast.makeText(mcontext,"Please Chcek Your Internet Connection", Toast.LENGTH_SHORT).show()
                    }
                    var calorieintakevalue=0.0F
                    var caloriesremaingvalue=0.0F
                    caloriesintake.child(date).addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            val value =p0.getValue(Float::class.java)
                            editor.putFloat("intake", value!!.toFloat())
                            editor.commit()

                        }


                    })
                    if(!date.equals(database.getString("datei","0"))){
                        calorieintakevalue=0.0F
                        editor.putString("datei",date)
                        editor.commit()
                    }
                    else {
                        calorieintakevalue=database.getFloat("intake",0.0F)
                    }
                    var calorievalue=CALORIEREMAINNING.toFloat()
                    calorieintakevalue=calorieintakevalue+SearchList.get(position).Calories!!.toFloat()
                    caloriesremaingvalue=calorievalue-calorieintakevalue
                    caloriesintake.child(date).setValue(calorieintakevalue).addOnSuccessListener {
                        Log.i("nitin789","done")
                        Toast.makeText(mcontext,"Calories Intake Is Updated",Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Log.i("nitin789","notdone")
                        Toast.makeText(mcontext,"Please Chcek Your Internet Connection", Toast.LENGTH_SHORT).show()
                    }
                   caloriesremaing.child(date).setValue(caloriesremaingvalue).addOnSuccessListener {
                        Toast.makeText(mcontext,"Calories Remaining Is Updated",Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(mcontext,"Please Chcek Your Internet Connection", Toast.LENGTH_SHORT).show()
                    }

                }
                if(meal==2){
                    LunchVal.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {


                            val value = dataSnapshot.getValue(String::class.java)
                            totallunchcalorie=value.toString()
                            editor.putString("lunch",totallunchcalorie)
                            editor.commit()

                        }
                    })
                    if(!date.equals(database.getString("datel","0"))){
                        totallunchcalorie="0"
                        editor.putString("datel",date)
                        editor.commit()
                    }
                    else {
                        totallunchcalorie = database.getString("breakfast", "0")
                    }
                    var total=totallunchcalorie.toFloat()+ SearchList.get(position).Calories!!.toFloat()
                    myRefcalorie.child(database.getString("userid","TEST")).child("Meal").child(date).child("Lunch").setValue(total.toString()).addOnSuccessListener {
                        Toast.makeText(mcontext,"Lunch Calorie is Added",Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener {
                        Toast.makeText(mcontext,"Please Chcek Your Internet Connection", Toast.LENGTH_SHORT).show()
                    }
                    var calorieintakevalue=0.0F
                    var caloriesremaingvalue=0.0F
                    caloriesintake.child(date).addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            val value =p0.getValue(Float::class.java)
                            editor.putFloat("intake", value!!.toFloat())
                            editor.commit()

                        }


                    })
                    if(!date.equals(database.getString("datei","0"))){
                        calorieintakevalue=0.0F
                        editor.putString("datei",date)
                        editor.commit()
                    }
                    else {
                        calorieintakevalue=database.getFloat("intake",0.0F)
                    }
                    var calorievalue=CALORIEREMAINNING.toFloat()
                    calorieintakevalue=calorieintakevalue+SearchList.get(position).Calories!!.toFloat()
                    caloriesremaingvalue=calorievalue-calorieintakevalue
                    caloriesintake.child(date).setValue(calorieintakevalue).addOnSuccessListener {
                        Toast.makeText(mcontext,"Calories Intake Is Updated",Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(mcontext,"Please Chcek Your Internet Connection", Toast.LENGTH_SHORT).show()
                    }
                    caloriesremaing.child(date).setValue(caloriesremaingvalue).addOnSuccessListener {
                        Toast.makeText(mcontext,"Calories Remaining Is Updated",Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(mcontext,"Please Chcek Your Internet Connection", Toast.LENGTH_SHORT).show()
                    }
                }
                if(meal==3){
                    DinnerVal.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {


                            val value = dataSnapshot.getValue(String::class.java)
                          totaldinnercalorie=value.toString()
                            editor.putString("dinner",totaldinnercalorie)
                            editor.commit()

                        }
                    })
                    if(!date.equals(database.getString("dated","0"))){
                        totaldinnercalorie="0"
                        editor.putString("dated",date)
                        editor.commit()
                    }
                    else {
                        totaldinnercalorie = database.getString("dinner", "0")
                    }
                    var total=totaldinnercalorie.toFloat()+ SearchList.get(position).Calories!!.toFloat()
                    myRefcalorie.child(database.getString("userid","TEST")).child("Meal").child(date).child("Dinner").setValue(total.toString()).addOnSuccessListener {
                        Toast.makeText(mcontext,"Dinner Calorie is Added",Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener {
                        Toast.makeText(mcontext,"Please Chcek Your Internet Connection", Toast.LENGTH_SHORT).show()
                    }
                    var calorieintakevalue=0.0F
                    var caloriesremaingvalue=0.0F
                    caloriesintake.child(date).addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            val value =p0.getValue(Float::class.java)
                            editor.putFloat("intake", value!!.toFloat())
                            editor.commit()

                        }


                    })
                    if(!date.equals(database.getString("datei","0"))){
                        calorieintakevalue=0.0F
                        editor.putString("datei",date)
                        editor.commit()
                    }
                    else {
                        calorieintakevalue=database.getFloat("intake",0.0F)
                    }
                    var calorievalue=CALORIEREMAINNING.toFloat()
                    calorieintakevalue=calorieintakevalue+SearchList.get(position).Calories!!.toFloat()
                    caloriesremaingvalue=calorievalue-calorieintakevalue
                    caloriesintake.child(date).setValue(calorieintakevalue).addOnSuccessListener {
                        Toast.makeText(mcontext,"Calories Intake Is Updated",Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(mcontext,"Please Chcek Your Internet Connection", Toast.LENGTH_SHORT).show()
                    }
                    caloriesremaing.child(date).setValue(caloriesremaingvalue).addOnSuccessListener {
                        Toast.makeText(mcontext,"Calories Remaining Is Updated",Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(mcontext,"Please Chcek Your Internet Connection", Toast.LENGTH_SHORT).show()
                    }

                }
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
