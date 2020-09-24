package kugong.start.example.Login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.register.*
import kugong.start.example.R
import kugong.start.example.RetrofitBuilder

class Register: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        var retrofit = RetrofitBuilder.getInstance()
        var requestService = retrofit.create(RegisterService::class.java)

        registerButton.setOnClickListener {

            val inputName = registerName.text.toString()
            val inputNickname = registerNickname.text.toString()
            val inputEmail = registerEmail.text.toString()
            val inputBirth = registerBirth.text.toString()
            val inputPassword = registerPassword.text.toString()
            val inputCheckPassword = registerCheckPassword.text.toString()


//            if(inputPassword != inputCheckPassword){
//                Log.d("register","wrongPW")
//                val dialog = AlertDialog.Builder(this)
//                dialog.setTitle("오류")
//                dialog.setMessage("비밀번호가 같지 않습니다.")
//            }
//            else{
//                requestService.requestRegister(inputBirth, inputEmail, inputName, inputNickname, inputPassword).enqueue(object : Callback<LoginData>{
//                    override fun onFailure(call: Call<LoginData>, t: Throwable) {
//                    }
//
//                    override fun onResponse(call: Call<LoginData>, response: Response<LoginData>) {
//                        val intent = Intent(this@Register, Login::class.java)
//                        startActivity(intent)
//                    }
//                })
//
//
//            }
        }
    }
}