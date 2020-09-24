package kugong.start.example.SharedPreference

import android.content.Context
import android.content.SharedPreferences
import kugong.start.example.DataClasses.UserClothData

//shared preference에서 token 다루기
class PreferenceUtil(context: Context){
    private val tokenStore : SharedPreferences = context.getSharedPreferences("tokenStore", Context.MODE_PRIVATE)
    private val editor : SharedPreferences.Editor = tokenStore.edit()

    private val loginCount : SharedPreferences = context.getSharedPreferences("loginCount",Context.MODE_PRIVATE)
    private val loginCountEditor : SharedPreferences.Editor = loginCount.edit()

    private val topsStorage : SharedPreferences =  context.getSharedPreferences("userTops", Context.MODE_PRIVATE)
    private val topsEditor : SharedPreferences.Editor = topsStorage.edit()

    private val pantsStorage : SharedPreferences =  context.getSharedPreferences("userPants", Context.MODE_PRIVATE)
    private val pantsEditor : SharedPreferences.Editor = pantsStorage.edit()

    private val shoesStorage : SharedPreferences =  context.getSharedPreferences("userShoes", Context.MODE_PRIVATE)
    private val shoesEditor : SharedPreferences.Editor = shoesStorage.edit()

    //로그인하고 서버로 받아온 token을 sharedprefernece에 저장하는 fun
    public fun saveToken(token : String){
        editor.putString("loginToken",token)
        editor.apply() //처리 결과에 대한 true/false의 값이 필요한 경우는 commit를 쓰고 아닌 경우 apply를 쓰면 된다
    }
    //저장된 token을 사용할 때 sharedpreference에서 불러오는 fun
    public fun useToken() : String? {
        return if(tokenStore.contains("loginToken")){
            val token = tokenStore.getString("loginToken","")
            token
        } else{
            null
        }
    }
    //사용자가 자동 로그인 설정을 하기 않았을때 token을 삭제하는 fun
    public fun clearToken(){
        editor.clear()
        editor.apply()
    }

    //사용자 로그인 횟수 세는 fun
    public fun getCount(token : String) : Int{
        val count = loginCount.getInt(token, 0)
        return if(count == 0){
            loginCountEditor.putInt(token,1)
            1
        } else{
            loginCountEditor.putInt(token,count+1)
            //loginCount.getInt(token, 2)
            1
        }

    }

    //==================================사용자 옷 저장================================

    public fun saveClothID(clothType: String, i : UserClothData){
        when(clothType){
            "userTops" -> {topsEditor.putString(clothType,"${i.id}").apply()}
            "userPants" -> {pantsEditor.putString(clothType,"${i.id}").apply()}
            "userShoes" -> {shoesEditor.putString(clothType,"${i.id}").apply()}
        }
    }
    public fun saveCloth(clothType : String, i : UserClothData){
        when(clothType){
            "userTops" -> {
                topsEditor.putString("${i.id}", i.url).apply()
                topsEditor.putInt(i.url, i.id).apply()
            }
            "userPants" -> {
                pantsEditor.putString("${i.id}", i.url).apply()
                pantsEditor.putInt(i.url, i.id).apply()
            }
            "userShoes" -> {
                shoesEditor.putString("${i.id}", i.url).apply()
                shoesEditor.putInt(i.url, i.id).apply()
            }
        }
    }

    //옷 카테고리별 가지고 있는 옷 이미지 보여주기 위해서
    public fun getClothUrls(clothType : String) : List<String> {
        when(clothType){
            "userTops" -> {
                var topsUrlList = mutableListOf<String>()
                var topsIdSet : MutableSet<String>? = topsStorage.getStringSet(clothType, mutableSetOf())
                if(topsIdSet != null){
                    for(i in topsIdSet){
                        topsStorage.getString(i,"")?.let {topsUrlList.add(it)}
                    }
                }
                return topsUrlList
            }
            "userPants" -> {
                var pantsUrlList = mutableListOf<String>()
                var pantsIdSet : MutableSet<String>? = topsStorage.getStringSet(clothType, mutableSetOf())
                if(pantsIdSet != null){
                    for(i in pantsIdSet){
                        pantsStorage.getString(i,"")?.let { pantsUrlList.add(it) }
                    }
                }
                return pantsUrlList
            }
            "userShoes" -> {
                var shoesUrlList = mutableListOf<String>()
                var shoesIdSet : MutableSet<String>? = topsStorage.getStringSet(clothType, mutableSetOf())
                if(shoesIdSet != null){
                    for(i in shoesIdSet){
                        shoesStorage.getString(i,"")?.let { shoesUrlList.add(it) }
                    }
                }
                return shoesUrlList
            }
            else -> return listOf<String>()
        }
    }
    //옷 눌렀을 때(url알 때) 그 옷의 ID 알려주기 위해서
    public fun getSpecificClothID(clothType: String, url : String) : String? {
        var imageId : String? = null
        when(clothType){
            "userTops" -> {imageId = topsStorage.getString(url,"")}
            "userPants" -> {imageId = pantsStorage.getString(url,"")}
            "userShoes" -> {imageId = shoesStorage.getString(url,"")}
        }
        return imageId
    }
    //옷의 Id를 알때 URL 알려주기 위해서
    public fun getSpecificClothURL(clothType: String, id : String) : String? {
        var imageUrl : String? = null
        when(clothType){
            "userTops" -> {imageUrl = topsStorage.getString(id.toString(),"")}
            "userPants" -> {imageUrl = pantsStorage.getString(id.toString(),"")}
            "userShoes" -> {imageUrl = shoesStorage.getString(id.toString(),"")}
        }
        return imageUrl
    }
    public fun clearCloth(){
        topsEditor.clear().apply()
        pantsEditor.clear().apply()
        shoesEditor.clear().apply()

    }
}