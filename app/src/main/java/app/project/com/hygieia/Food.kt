package app.project.com.hygieia

/**
 * Created by nitinsinghal on 14/05/19.
 */
class Food{
    var Barcode: Long? = 0
    var Calories: Long? = 0
    var Carb: Long? = 0
    var Fat:Long?=0
    var Protien:Long?=0
    var name:String?=null

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    constructor(Barcode:Long,Calories:Long,Carb:Long,Fat:Long,Protien:Long){
        this.Barcode= Barcode
        this.Calories=Calories
        this.Carb=Carb
        this.Fat=Fat
        this.Protien=Protien
    }


}