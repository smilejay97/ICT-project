package kugong.start.example.closet

import kugong.start.example.DataClasses.ClothData
import kugong.start.example.DataClasses.UserClothData
import kugong.start.example.DataClasses.UserClothDataArray
import kugong.start.example.DataClasses.UserData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GetUserData {

    @GET("kkugong/v1/users/my")
    fun getUserInfo(
        @Header("Authorization") token : String
    ) : Call<UserData>

    @GET("kkugong/v1/users/my/{clothCategory}")
    fun getUserClothes(
        @Header("Authorization") token : String,
        @Path("clothCategory") cloth : String
    ) : Call<UserClothDataArray>

}