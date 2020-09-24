package kugong.start.example.closet

import kugong.start.example.DataClasses.SimilarClothData
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface UploadCloth {
    @Multipart
    @POST("kkugong/v1/{clothCategory}/recognition")
    fun uploadUserCloth (
        @Header("Authorization") token : String,
        @Path("clothCategory") clothCategory : String,
        @Part imageFile : MultipartBody.Part
    ) : Call<SimilarClothData>
}