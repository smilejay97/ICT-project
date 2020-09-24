package kugong.start.example.Login

import kugong.start.example.DataClasses.LoginData
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegisterService {
    @POST("kkugong/v1/users/")
    @FormUrlEncoded
    fun requestRegister(
        @Field("id") identification : Int,
        @Field("username") username : String,
        @Field("email") email : String,
        @Field("avatar") avatar : Any,
        @Field("gender") gender : String,
        @Field("birthday") birthday : String,
        @Field("nickname") nickname : String,
        @Field("age") age: Int
    ) : Call<LoginData>
}