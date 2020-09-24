package kugong.start.example.Login

import kugong.start.example.DataClasses.LoginData
import retrofit2.Call
import retrofit2.http.*

interface LoginService {
    @POST("kkugong/v1/users/login/")
    @FormUrlEncoded
        fun requestLogin(
            @Field("username") userID : String,
            @Field("password") userPW : String
        ) : Call<LoginData>
}