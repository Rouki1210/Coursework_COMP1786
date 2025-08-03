package np.com.bimalkafle.myapplication.model

data class Order(
    val  orderId : String = "",
    val userId : String = "",
    val totalPrice : String = "",
    val createAt : String = "",
    val item : List<Items> = emptyList()
)
