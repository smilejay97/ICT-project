package kugong.start.example.DataClasses

class UserClothDataArray : ArrayList<UserClothData>()

data class UserClothData(
    val id: Int,
    val user: Int,
    val url : String,
    val meta : MetaTop
)

data class MetaTop(
    val brand: String,
    val id: String,
    val images: List<Image>,
    val item_url: String,
    val product: String
)

data class Image(
    val id: String,
    val img: String,
    val img_url: String,
    val top: String
)