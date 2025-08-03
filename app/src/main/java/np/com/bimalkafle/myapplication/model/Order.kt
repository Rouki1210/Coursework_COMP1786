package np.com.bimalkafle.myapplication.model

data class Order(
    val  orderId : String = "",
    val userId : String = "",
    val totalPrice : String = "",
    val item : List<items> = emptyList()
)

data class items(
    val itemsId: String = "",
    val itemName: String = "",
    val itemPrice: String = "",
    val teacher: String = "",
)
