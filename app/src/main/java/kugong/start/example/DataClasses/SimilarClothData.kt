package kugong.start.example.DataClasses

data class SimilarClothData(
    val similar_things: List<SimilarThing>,
    val userTop_obj: UserTopObj
)

data class SimilarThing(
    val id: String,
    val img: String,
    val img_url: String,
    val top: String
)

data class UserTopObj(
    val id: Int,
    val img: String,
    val meta_top: Any,
    val user: Int
)