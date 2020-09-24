package kugong.start.example.closet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.upload_chooser.*
import kugong.start.example.R

class UploadChooser : BottomSheetDialogFragment() {
    //클릭시 행동을 구현하는 interface
    interface UploadChooseNotifierInterface{
        fun cameraOnClick()
        fun galleryOnClick()
    }

    var uploadChooseNotifierInterface : UploadChooseNotifierInterface? =  null

    fun addNotifier(listener : UploadChooseNotifierInterface?){
        uploadChooseNotifierInterface = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.upload_chooser,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListener()
    }

    private fun setupListener(){
        camera.setOnClickListener{
            uploadChooseNotifierInterface?.cameraOnClick()
        }
        gallery.setOnClickListener{
            uploadChooseNotifierInterface?.galleryOnClick()
        }
    }
}