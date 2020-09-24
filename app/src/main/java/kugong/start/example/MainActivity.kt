package kugong.start.example

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.login.*
import kugong.start.example.DataClasses.UserClothData
import kugong.start.example.DataClasses.UserClothDataArray
import kugong.start.example.DataClasses.UserData
import kugong.start.example.Login.Login
import kugong.start.example.SharedPreference.PreferenceUtil
import kugong.start.example.closet.Nav1FragmentMyCloset
import kugong.start.example.closet.GetUserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private var nav1Fragment = Nav1FragmentMyCloset()
    private var nav2Fragment = Nav2FragmentCoordiRecommend()
    private var nav3Fragment = nav3_Fragment()
    private var nav4Fragment = nav4_Fragment()

    public var userData : UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        처음 시작화면에서 앱 로고를 보여주는 역할
//        val loadingIntent = Intent(this, StartActivity::class.java)
//        startActivity(loadingIntent)

        //app bar
        val toolbar : Toolbar = findViewById(R.id.app_toolbar)
        Log.d("whats wrong","toolbar")
        setSupportActionBar(toolbar)
        Log.d("whats wrong","setSupportActionBar")

        //사용자의 토큰 여부에 따라 로그인 or token 이용한 데이터 불러오기
        val startLoadingToken : String? = PreferenceUtil(this).useToken()
        val loginCount = startLoadingToken?.let { PreferenceUtil(this).getCount(it) }

        if(startLoadingToken != null && loginCount == 1 ){
            val retrofit = RetrofitBuilder.getInstance()
            val getUserInfo = retrofit.create(GetUserData::class.java)

            var responseUserTops : ArrayList<UserClothData>? = null
            var responseUserPants : ArrayList<UserClothData>? = null
            var responseUserShoes : ArrayList<UserClothData>? = null
            Log.d("whats wrong","beforeMainRetrofit")

            //User tops, pants, shoes 받아오기
            getUserInfo.getUserClothes(startLoadingToken,"tops").enqueue(object : Callback<UserClothDataArray>{
                override fun onFailure(call: Call<UserClothDataArray>, t: Throwable) {
                    Log.e("login", t.message!!)
                    val dialog = AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("실패")
                    dialog.setMessage("로딩에 실패했습니다.")
                }

                override fun onResponse( call: Call<UserClothDataArray>, response: Response<UserClothDataArray>) {
                    Log.d("saveUserData", "getToken:${startLoadingToken}")
                    if(response.isSuccessful){
                    responseUserTops = response.body()
                    responseUserTops?.let{for(top in it) {
                        PreferenceUtil(this@MainActivity).saveClothID("userTops", top)
                        PreferenceUtil(this@MainActivity).saveCloth("userTops", top)
                        Log.d("saveUserData", "savetop")
                    }}}
                    else{
                        Log.d("saveUserData","fail, ${response.code()}")
                    }
                }
            })
            getUserInfo.getUserClothes(startLoadingToken, "pants").enqueue(object : Callback<UserClothDataArray>{
                override fun onFailure(call: Call<UserClothDataArray>, t: Throwable) {
                    Log.e("login", t.message!!)
                    val dialog = AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("실패")
                    dialog.setMessage("로딩에 실패했습니다.")
                }

                override fun onResponse(call: Call<UserClothDataArray>, response: Response<UserClothDataArray>) {
                    responseUserPants = response.body()
                    responseUserPants?.let{for(pant in it) {
                        PreferenceUtil(this@MainActivity).saveClothID("userPants", pant)
                        PreferenceUtil(this@MainActivity).saveCloth("userPants", pant)
                        Log.d("saveUserData", "savepants")
                    }}
                }
            })
            getUserInfo.getUserClothes(startLoadingToken,"shoes").enqueue(object : Callback<UserClothDataArray> {
                override fun onFailure(call: Call<UserClothDataArray>, t: Throwable) {
                    Log.e("login", t.message!!)
                    val dialog = AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("실패")
                    dialog.setMessage("로딩에 실패했습니다.")
                }

                override fun onResponse(call: Call<UserClothDataArray>, response: Response<UserClothDataArray>) {
                    responseUserShoes = response.body()
                    responseUserShoes?.let{for(shoes in it){
                        PreferenceUtil(this@MainActivity).saveClothID("userShoes",shoes)
                        PreferenceUtil(this@MainActivity).saveCloth("userPants",shoes)
                        Log.d("saveUserData","saveshoes")
                    }}
                }
            })

            //bottom navigation bar 설정
            val transaction : FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.container, nav1Fragment).commitAllowingStateLoss()

            val bottomNavigationView : BottomNavigationView = findViewById(R.id.bottom_navigation)
            bottomNavigationView.setOnNavigationItemSelectedListener(ItemSelectedListener())
        }
        else if(startLoadingToken != null && userData != null){
            //bottom navigation bar 설정
            val transaction : FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.container, nav1Fragment).commitAllowingStateLoss()

            val bottomNavigationView : BottomNavigationView = findViewById(R.id.bottom_navigation)
            bottomNavigationView.setOnNavigationItemSelectedListener(ItemSelectedListener())
        }
        else{
            //처음 앱 로고 볼 시간 주고 실행해야됨(StartActivity 참고)
            val loginIntent = Intent(this, Login::class.java)
            startActivity(loginIntent)
        }
    }

    //navigation fragment에서 화면 표시하기 위함
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val request : Int = requestCode

        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        fragment?.onActivityResult(request,resultCode,data)
    }

    //자동 로그인 체크 안되어 있으면 어플 끝날 때 Token 삭제
    //여기서 login 창에 있는 자동로그인 버튼까지 컨트롤할 수 있는 확인해야함
    override fun onDestroy() {
        super.onDestroy()
        autoLogin.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(!isChecked){
                PreferenceUtil(this).clearToken()
            }
        })
    }

    //app bar 클릭 구현
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_logout -> {
                PreferenceUtil(this).clearToken()
                PreferenceUtil(this).clearCloth()
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
            R.id.action_settings -> {
                TODO("세팅 기능 구현")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //bottom navigation 구현
    inner class ItemSelectedListener : BottomNavigationView.OnNavigationItemSelectedListener{
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            val transaction : FragmentTransaction = fragmentManager.beginTransaction()
            when(item.itemId){
                R.id.nav_item1 -> {
                    transaction.replace(R.id.container, nav1Fragment).commitAllowingStateLoss()
                }
                R.id.nav_item2 -> {
                    transaction.replace(R.id.container, nav2Fragment).commitAllowingStateLoss()
                }
                R.id.nav_item3 -> {
                    transaction.replace(R.id.container, nav3Fragment).commitAllowingStateLoss()
                }
                R.id.nav_item4 -> {
                    transaction.replace(R.id.container, nav4Fragment).commitAllowingStateLoss()
                }
            }
            return true
        }
    }
}