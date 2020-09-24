package kugong.start.example.closet

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.nav1_my_closet.*
import kugong.start.example.MainActivity
import kugong.start.example.R
import kugong.start.example.RetrofitBuilder
import kugong.start.example.SharedPreference.PreferenceUtil
import retrofit2.Retrofit
import java.io.File

class Nav1FragmentMyCloset: Fragment() {

    private var recyclerView :RecyclerView? = null
    var addRequestComment : TextView? =null
    private var imageAdapter : MyClothesImageAdapter? = null
    private val list_outer = ArrayList<String>()
    private val list_top = ArrayList<String>()
    private val list_pants = ArrayList<String>()
    private val list_shoes = ArrayList<String>()

    private var permissionButton : FloatingActionButton? = null
    private var uploadChooser : UploadChooser? = null

    private val CAMERA_PERMISSION_CODE = 1000
    private val GALLERY_PERMISSION_CODE = 1001
    private val FILE_NAME = "picture.jpg"

    private var mContext : Context? = null
    private var activity1 : Activity? = null

    private lateinit var retrofit: Retrofit
    private val retrofitBuilder = RetrofitBuilder

    //fragment에서 getContext, getActivity를 문제 없이 사용하기 위해서 onAttach에서 사용
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

        if(context is Activity){
            activity1 = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView : ViewGroup = inflater.inflate(R.layout.nav1_my_closet,container,false) as ViewGroup
        recyclerView = rootView.findViewById(R.id.recycler1) as RecyclerView
        addRequestComment = rootView.findViewById(R.id.add_cloth_comment)
        //화면 위에 각 버튼 별 리사이클러뷰 바꿔 쓰기
        val btn_outer : Button = rootView.findViewById(R.id.outer_btn)
        val btn_top : Button = rootView.findViewById(R.id.top_btn)
        val btn_pants : Button = rootView.findViewById(R.id.pants_btn)
        val btn_shoes : Button = rootView.findViewById(R.id.shoes_btn)

        //리사이클러뷰로 사용자가 가진 옷 보여줄 list 채우기
        for(i in PreferenceUtil(mContext!!).getClothUrls("userTops")){
            list_top.add(i)
        }
        for(i in PreferenceUtil(mContext!!).getClothUrls("userPants")){
            list_pants.add(i)
        }
        for(i in PreferenceUtil(mContext!!).getClothUrls("userShoes")){
            list_shoes.add(i)
        }

        val onClickListener = BtnClickListener()

        btn_outer.setOnClickListener(onClickListener)
        btn_top.setOnClickListener(onClickListener)
        btn_pants.setOnClickListener(onClickListener)
        btn_shoes.setOnClickListener(onClickListener)

        //다이얼로그 불러오는 함수
        permissionButton = rootView.findViewById(R.id.fab) as FloatingActionButton
        callDialogByBtn(permissionButton)

        return rootView
    }

    var clothType : String? = null
    //각 버튼 별로 다른 정보 사용하기
    inner class BtnClickListener() : View.OnClickListener{
        private var addClothCommentVisibility = addRequestComment?.visibility
        override fun onClick(v: View) {
            when(v){
                outer_btn -> {
                    clothType = "userOuters"
                    addClothCommentVisibility = if(list_outer.size != 0) View.GONE else View.VISIBLE
                    useRecyclerView(recyclerView!!, list_outer)
                }
                top_btn ->{
                    clothType = "userTops"
                    addClothCommentVisibility = if(list_top.size != 0) View.GONE else View.VISIBLE
                    useRecyclerView(recyclerView!!, list_top)
                }
                pants_btn -> {
                    clothType = "userPants"
                    addClothCommentVisibility = if(list_pants.size != 0) View.GONE else View.VISIBLE
                    useRecyclerView(recyclerView!!, list_pants)
                }
                shoes_btn -> {
                    clothType = "userShoes"
                    addClothCommentVisibility = if(list_shoes.size != 0) View.GONE else View.VISIBLE
                    useRecyclerView(recyclerView!!, list_shoes)
                }
            }
        }
    }

    private fun useRecyclerView(recyclerView: RecyclerView, list : ArrayList<String>){
        recyclerView.setHasFixedSize(true)
        imageAdapter = MyClothesImageAdapter(mContext!!, list)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = imageAdapter
    }

    //+버튼 누르면 카메라/갤러리 선택할 수 있게 하는 함수
    private fun callDialogByBtn(fab : FloatingActionButton?){
        fab?.setOnClickListener(View.OnClickListener {
            uploadChooser = UploadChooser().apply {
                addNotifier(object : UploadChooser.UploadChooseNotifierInterface{
                    override fun cameraOnClick() {
                        checkCameraPermission()
                    }
                    override fun galleryOnClick() {
                        checkGalleryPermission()
                    }
                })
            }
            activity?.supportFragmentManager?.let { it1 -> uploadChooser!!.show(it1,"") }
        })
    }

    //카메라의 권한을 확인하고 있다면 camera를 open
    private fun checkCameraPermission(){
        if(PermissionUtil().requestPermission( mContext!!,CAMERA_PERMISSION_CODE,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE)) openCamera()
    }
    //갤러리의 권한을 확인하고 있다면 open
    private fun checkGalleryPermission(){
        if(PermissionUtil().requestPermission( mContext!!,GALLERY_PERMISSION_CODE,Manifest.permission.READ_EXTERNAL_STORAGE)) openGallery()
    }
    //갤러리로 넘어가는 상황 설정
    private fun openGallery(){
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        requireActivity().startActivityForResult(Intent.createChooser(intent,"Select a photo"), GALLERY_PERMISSION_CODE)
    }
    //사진이 저장될 위치 설정 + 넘어가도록 intent
    private fun openCamera() {
        val photoURI = FileProvider.getUriForFile(mContext!!,mContext!!.applicationContext.packageName + ".provider", createCameraFile())
        requireActivity().startActivityForResult(
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }, CAMERA_PERMISSION_CODE
        )
    }
    //찍은 사진을 원하는 View에 올리기 위함
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CAMERA_PERMISSION_CODE -> {
                //작업이 잘 되었는지 아닌지(작업 결과물과는 상관없이 잘 이루어졌는지)를 알려주는 코드 : RESULT_OK
                if(resultCode != Activity.RESULT_OK) return
                //사진을 저장한 경로와 동일한 경로를 사용해야만 찍은 사진을 가져올 수 있기 때문임
                val photoURI = FileProvider.getUriForFile(mContext!!,mContext!!.applicationContext.packageName + ".provider", createCameraFile())
                uploadImage(photoURI)
            }
            GALLERY_PERMISSION_CODE -> {
                //사용자가 고른 이미지 input이 data에 들어있음
                data?.let{it.data?.let { it1 -> uploadImage(it1)}}
            }
        }
    }
    //여기에서 intent로 다른 activity로 이동 해야될듯
    private fun uploadImage(imageURI: Uri){
        val intent = Intent(activity1, SelectCloth::class.java)
        intent.putExtra("takenPhoto","$imageURI")
        startActivity(intent)
        uploadChooser?.dismiss()
    }
    //카메라로 찍을 사진을 저장할 경로(directory)를 생성하는 함수
    private fun createCameraFile(): File {
        val dir = mContext!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(dir, FILE_NAME)
    }
    //permission 요청에 대한 사용자의 액션을 관리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            GALLERY_PERMISSION_CODE ->{
                if(PermissionUtil().permissionGranted(requestCode, GALLERY_PERMISSION_CODE, grantResults)) openGallery()
            }
            CAMERA_PERMISSION_CODE ->{
                if(PermissionUtil().permissionGranted(requestCode, CAMERA_PERMISSION_CODE, grantResults)) openCamera()
            }
        }
    }

    //recyclerview adapter
    inner class MyClothesImageAdapter(val content : Context, list: ArrayList<String>) :
        RecyclerView.Adapter<MyClothesImageAdapter.ViewHolder>() {
        private val mData : ArrayList<String> = list

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val imageView1 : ImageView = itemView.findViewById(R.id.closet_item_image)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val context = parent.context
            val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = inflater.inflate(R.layout.cloest_recyclerview_item,parent,false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = mData.size

        //여기서 해당 옷의 ID를 보내야함
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val imageUrl : String = mData[position]
            Glide.with(holder.itemView.context).load(imageUrl).into(holder.imageView1)
            holder.imageView1.setOnClickListener(View.OnClickListener {
                val imageId : String? = PreferenceUtil(mContext!!).getSpecificClothID(clothType!!, imageUrl)
                val intent = Intent(context, Clothes_detail::class.java)
                intent.putExtra("chosenClothId", imageId)
                intent.putExtra("chosenClothUrl", imageUrl)
                startActivity(intent)
            })
        }
    }

}