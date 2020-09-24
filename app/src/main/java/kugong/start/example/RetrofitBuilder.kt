package kugong.start.example

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private var instance : Retrofit? = null
    //private val gson = GsonBuilder().setLenient().create()

    fun getInstance() : Retrofit {
        if(instance == null){
            instance = Retrofit.Builder()
                .baseUrl("http://ec2-52-204-66-85.compute-1.amazonaws.com:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance!!
    }
}