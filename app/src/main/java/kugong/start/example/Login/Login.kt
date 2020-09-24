package kugong.start.example.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.login.*
import kugong.start.example.DataClasses.LoginData
import kugong.start.example.MainActivity
import kugong.start.example.R
import kugong.start.example.RetrofitBuilder
import kugong.start.example.SharedPreference.PreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val retrofit = RetrofitBuilder.getInstance()
        val loginService = retrofit.create(LoginService::class.java)

        loginButton.setOnClickListener{
            val userId = UserId.text.toString()
            Log.d("login","userid : " + "$userId")
            val userPW = UserPW.text.toString()
            Log.d("login","userpw : " + "$userPW")

            loginService.requestLogin(userId,userPW).enqueue(object : Callback<LoginData> {
                override fun onFailure(call: Call<LoginData>, t: Throwable) {
                    Log.d("login", "onFailure")
                    val dialog = AlertDialog.Builder(this@Login)
                    dialog.setTitle("실패")
                    dialog.setMessage("로그인 실패했습니다.")
                }
                override fun onResponse(call: Call<LoginData>, response: Response<LoginData>) {
                    if(response.isSuccessful){
                        Log.d("login", "onResponse")
                        PreferenceUtil(this@Login).saveToken(response.body()!!.token)
                        startActivity(Intent(this@Login, MainActivity::class.java))
                    }else{
                        Log.d("login", "fail")
                    }
                }
            })
        }

        registerButton.setOnClickListener {
            startActivity(Intent(applicationContext,Register::class.java))
        }
    }
}