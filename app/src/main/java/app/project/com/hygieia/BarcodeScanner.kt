package app.project.com.hygieia

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.vision.barcode.Barcode
import info.androidhive.barcode.BarcodeReader
import android.widget.Toast


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BarcodeScanner.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BarcodeScanner.newInstance] factory method to
 * create an instance of this fragment.
 */
class BarcodeScanner : Fragment(), BarcodeReader.BarcodeReaderListener {



    private lateinit var barcodeReader:BarcodeReader
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

        val finalCodes = codes
        activity!!.runOnUiThread { Toast.makeText(activity, "Barcodes: " + finalCodes, Toast.LENGTH_SHORT).show() }
    }

    override fun onCameraPermissionDenied() {
        Toast.makeText(getActivity(), "Camera permission denied!", Toast.LENGTH_LONG).show();
    }

    override fun onScanned(barcode: Barcode?) {
       // barcodeReader.playBeep()

        activity!!.runOnUiThread {
            if (barcode != null) {
                Toast.makeText(activity, "Barcode: " + barcode.displayValue, Toast.LENGTH_SHORT).show()
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
        barcodeReader = childFragmentManager.findFragmentById(R.id.barcode_fragment) as BarcodeReader
        barcodeReader.setListener(this)
        return view
    }





}
