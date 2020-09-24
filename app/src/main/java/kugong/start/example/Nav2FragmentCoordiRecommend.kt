package kugong.start.example

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

//사용자가 가지고 있는 옷 기반 코디 추천
class Nav2FragmentCoordiRecommend: Fragment() {

    private var recyclerView : RecyclerView? = null
    private val list = ArrayList<String>()

    private var mContext : Context? = null
    private var activity1 : Activity? = null

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
        val rootView : ViewGroup = inflater.inflate(R.layout.nav2_recommended_coordi,container,false) as ViewGroup
        recyclerView = rootView.findViewById(R.id.recommended_coordi) as RecyclerView


        list.add("https://image.msscdn.net/mfile_s01/_shopstaff/list.staff_5ee6c8276e6ec.png")
        list.add("https://image.msscdn.net/mfile_s01/_shopstaff/list.staff_5ee713da8d2fc.png")
        list.add("https://image.msscdn.net/mfile_s01/_shopstaff/list.staff_5ee70851beca3.png")
        list.add("https://image.msscdn.net/mfile_s01/_shopstaff/list.staff_5ee70851beca3.png")

        recyclerView?.setHasFixedSize(true)
        val imageAdapter = CoordiRecommendAdapter(mContext!!, list)
        recyclerView?.layoutManager = GridLayoutManager(mContext!!, 4)
        recyclerView?.adapter = imageAdapter

        return rootView
    }

    inner class CoordiRecommendAdapter(val context: Context, list: ArrayList<String>) :
        RecyclerView.Adapter<CoordiRecommendAdapter.ViewHolder>() {
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

        //나중엔 json 파일을 보내는걸로..!
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