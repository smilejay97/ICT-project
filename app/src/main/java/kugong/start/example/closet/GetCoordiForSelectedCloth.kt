package kugong.start.example.closet

import kugong.start.example.DataClasses.CoordiData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GetCoordiForSelectedCloth {
    @GET()
    fun getCoordi(
        @Header("Authorization") token : String,
        @Query("id") id : String
    ) : Call<CoordiData>
}