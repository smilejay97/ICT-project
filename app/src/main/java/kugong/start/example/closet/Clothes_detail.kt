package kugong.start.example.closet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.closet_detail.*
import kugong.start.example.DataClasses.ClothData
import kugong.start.example.MainActivity
import kugong.start.example.R
import kugong.start.example.RetrofitBuilder
import kugong.start.example.SharedPreference.PreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//옷장 화면에서 특정 옷을 눌렀을 때 호출되는 화면
class Clothes_detail : AppCompatActivity() {

    private var list = ArrayList<String>()
    private var recyclerView : RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.closet_detail)

        val intent = intent
        //나중엔 해당 옷의 ID 받아서 여기서 서버에서 고화질로 받아오고, 그 옷이랑 관련된 코디까지 한번에 하면 좋을거 같음
        //이부분에서 받아오는 데이터가 id 형태면 밑에 코디 사진이랑 합쳐서 같이 받아오는 걸로 하고, image 바로 불러올 수 있으면 따로 하는 것도 괜찮음
        val imageId = intent.extras?.getString("chosenClothId")
        val imageURI = intent.extras?.getString("chosenClothUrl")
        Glide.with(this).load(imageURI).into(chosen_closet)

        val clothId : String

        val clothExplain : TextView = findViewById(R.id.explain_cloth)
        recyclerView = findViewById(R.id.recommended_closet)

        //누른 옷에 관련된 코디 받아야되니까 이 옷 id 전송해서 관련된 코디 받아오자
        val userToken = PreferenceUtil(this).useToken()
        val retrofit = RetrofitBuilder.getInstance()
        val getClothDetailService = retrofit.create(GetClothDetail::class.java)

        getClothDetailService.getTopDetail(userToken!!,"top",imageId!!).enqueue(object : Callback<ClothData>{
            override fun onFailure(call: Call<ClothData>, t: Throwable) {
                Log.e("login", t.message)
                val dialog = AlertDialog.Builder(this@Clothes_detail)
                dialog.setTitle("실패")
                dialog.setMessage("로딩에 실패했습니다.")
            }

            override fun onResponse(call: Call<ClothData>, response: Response<ClothData>) {
                clothExplain.text = "옷 설명ㅎㅎ"
                TODO("list에 코디 사진 넣는 작업 수행")
            }
        })

        list.add("https://image.msscdn.net/mfile_s01/_shopstaff/list.staff_5ee6c8276e6ec.png")
        list.add("https://image.msscdn.net/mfile_s01/_shopstaff/list.staff_5ee713da8d2fc.png")
        list.add("https://image.msscdn.net/mfile_s01/_shopstaff/list.staff_5ee70851beca3.png")
        list.add("https://image.msscdn.net/mfile_s01/_shopstaff/list.staff_5ee70851beca3.png")

        recyclerView?.setHasFixedSize(true)
        val imageAdapter = RecommendClothAdapter(this, list)
        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView?.adapter = imageAdapter
    }

    inner class RecommendClothAdapter(val context: Context, list: ArrayList<String>) :
        RecyclerView.Adapter<RecommendClothAdapter.ViewHolder>() {
        private var mData: ArrayList<String> = list


        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var imageView1: ImageView = itemView.findViewById(R.id.closet_recommended_item_image)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val context = parent.context
            val inflater: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = inflater.inflate(R.layout.closet_recommend_recyclerview_item, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = mData.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val image : String = mData[position]
            Glide.with(holder.itemView.context).load(image).into(holder.imageView1)
            holder.imageView1.setOnClickListener(View.OnClickListener {
                TODO("이거 누르면 이 옷이랑 관련된 옷 뭐뭐인지 자세히 보여주기 해야됨")
                TODO("코디 id 전송하면 구성하고 있는 옷 보내주는 프로세스 만들기")
            })
        }
    }
}
