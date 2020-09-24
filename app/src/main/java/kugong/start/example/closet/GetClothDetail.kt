package kugong.start.example.closet

import kugong.start.example.DataClasses.ClothData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GetClothDetail {
    @GET("kkugong/v1/{category}/{id}")
    fun getTopDetail(
        @Header("Authorization") token : String,
        @Path("clothCategory") category: String,
        @Path("id") id : String
    ) : Call<ClothData>
}
