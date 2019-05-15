package app.project.com.hygieia

/**
 * Created by nitinsinghal on 15/05/19.
 */
 class IngridentDetail {

    var protein:Float = 0.0f
    var fat:Float = 0.0f
    var carb:Float = 0.0f


    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }



   fun setkprotein(protein : Float){
      this.protein=protein
   }

}
