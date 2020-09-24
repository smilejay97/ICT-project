package kugong.start.example.closet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.upload_image.*
import kugong.start.example.DataClasses.SimilarClothData
import kugong.start.example.DataClasses.SimilarThing
import kugong.start.example.MainActivity
import kugong.start.example.R
import kugong.start.example.RetrofitBuilder
import kugong.start.example.SharedPreference.PreferenceUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//카메라로 사진 찍고 내 옷 고를 때 호출되는 화면, 여기서 누르는 사진이 데이터에 저장되어야함
class SelectCloth : AppCompatActivity() {

    private var list = ArrayList<String>()
    private var numOfSimilarCloth : List<SimilarThing>? = null
    private var recyclerView : RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload_image)

        recyclerView = findViewById(R.id.similar_clothes)

        val intent = intent
        val imageURI = intent.extras?.getString("takenPhoto")
        Glide.with(this).load(imageURI).into(taken_photo)
        Log.d("takenPhoto","Glide")



        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),imageURI!!)
        Log.d("takenPhoto","requestFile")
        val body = MultipartBody.Part.createFormData("image",imageURI,requestFile)
        Log.d("takenPhoto","body")

        val userToken = PreferenceUtil(this).useToken()
        val retrofit = RetrofitBuilder.getInstance()
        val getSimilarCloth = retrofit.create(UploadCloth::class.java)

        getSimilarCloth.uploadUserCloth(userToken!!,"tops",body).enqueue(object : Callback<SimilarClothData>{
            override fun onFailure(call: Call<SimilarClothData>, t: Throwable) {
                Log.e("login", t.message)
                val dialog = AlertDialog.Builder(this@SelectCloth)
                dialog.setTitle("실패")
                dialog.setMessage("로딩에 실패했습니다.")
            }

            override fun onResponse(call: Call<SimilarClothData>, response: Response<SimilarClothData>) {
                numOfSimilarCloth = response.body()!!.similar_things
            }
        })

        for(i in numOfSimilarCloth!!){
            list.add(i.img_url)
        }

        recyclerView?.setHasFixedSize(true)
        val imageAdapter = SimilarClothAdapter(this, list)
        recyclerView?.layoutManager = GridLayoutManager(this, 4)
        recyclerView?.adapter = imageAdapter
    }

    inner class SimilarClothAdapter(val context: Context, list: ArrayList<String>) :
        RecyclerView.Adapter<SimilarClothAdapter.ViewHolder>() {
        private var mData: ArrayList<String> = list

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var imageView1: ImageView = itemView.findViewById(R.id.match_clothes_image)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val context = parent.context
            val inflater: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = inflater.inflate(R.layout.match_recyclerview_item, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = mData.size

        //나중엔 json 파일을 보내는걸로..! 누른 옷의 id user DB에서 저장할 수 있도록 하기
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val image: String = mData[position]
            Glide.with(holder.itemView.context).load(image).into(holder.imageView1)
            holder.imageView1.setOnClickListener(View.OnClickListener {



                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("addCloth",image)
                startActivity(intent)
            })
        }
    }
}