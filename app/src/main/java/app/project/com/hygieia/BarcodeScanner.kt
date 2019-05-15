package app.project.com.hygieia

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.gms.vision.barcode.Barcode
import info.androidhive.barcode.BarcodeReader
import android.widget.Toast


@SuppressLint("ValidFragment")
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BarcodeScanner.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BarcodeScanner.newInstance] factory method to
 * create an instance of this fragment.
 */
class BarcodeScanner(context: Context,pass1:OnScanBarcode) : Fragment(), BarcodeReader.BarcodeReaderListener {



    private lateinit var barcodeReader:BarcodeReader
    private lateinit var context1:Context
    private lateinit var database: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var scanned:OnScanBarcode
    private lateinit var button:Button
    private lateinit var finalCodes:String

    init {
        context1=context
        scanned=pass1
    }
    override fun onBitmapScanned(sparseArray: SparseArray<Barcode>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onScannedMultiple(barcodes: MutableList<Barcode>?) {

        var codes = ""
        if (barcodes != null) {
            for (barcode in barcodes) {
                codes += barcode.displayValue + ", "
            }
        }

     finalCodes = codes

        activity!!.runOnUiThread {
            Toast.makeText(activity, "Barcodes: " + finalCodes, Toast.LENGTH_SHORT).show()




        }
    }

    override fun onCameraPermissionDenied() {
        Toast.makeText(getActivity(), "Camera permission denied!", Toast.LENGTH_LONG).show();
    }

    override fun onScanned(barcode: Barcode?) {
       // barcodeReader.playBeep()

        activity!!.runOnUiThread {
            if (barcode != null) {
                Toast.makeText(context1, "Barcode: " + barcode.displayValue, Toast.LENGTH_SHORT).show()
                Toast.makeText(context1,"Tap the next Button", Toast.LENGTH_SHORT).show()
                button.setOnClickListener(){
                    Toast.makeText(context1,barcode.displayValue+"done",Toast.LENGTH_SHORT).show()
                    editor.putString("barcode",barcode.displayValue)
                    editor.commit()
                    scanned.scan()
                }
            }
        }
    }

    override fun onScanError(errorMessage: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
     var view= inflater.inflate(R.layout.fragment_barcode_scanner, container, false)
        database= context1.getSharedPreferences("Database", Context.MODE_PRIVATE)
        button=view.findViewById<Button>(R.id.next)
        editor = database.edit()
        barcodeReader = childFragmentManager.findFragmentById(R.id.barcode_fragment) as BarcodeReader
        barcodeReader.setListener(this)

        return view
    }

    interface OnScanBarcode{
      fun scan()
    }





}
