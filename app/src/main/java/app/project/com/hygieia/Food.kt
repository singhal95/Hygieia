package app.project.com.hygieia

/**
 * Created by nitinsinghal on 14/05/19.
 */
class Food{
    var BarCode: Long? = null
    var Calories: Long? = null
    var Carb: Long? = null
    var Fat:Long?=null
    var Protein:Long?=null

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    constructor(Barcode:Long,Calories:Long,Carb:Long,Fat:Long,Protein:Long){
        this.BarCode= Barcode
        this.Calories=Calories
        this.Carb=Carb
        this.Fat=Fat
        this.Protein=Protein
    }


}