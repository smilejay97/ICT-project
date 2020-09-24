package kugong.start.example.DataClasses

data class LoginData (
    //서버로 호출했을때 클라이언트(안드로이드)로 내려주는 내용 정의
    //나중에 토큰을 어떻게 할지 정하면 고칠 필요 있음
    val token : String
)
