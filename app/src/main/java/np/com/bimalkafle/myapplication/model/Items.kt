package np.com.bimalkafle.myapplication.model

data class Items(
    val itemsId: String = "",
    val itemName: String = "",
    val itemPrice: String = "",
    val teacher: String = "",
    val items: Items,
)
