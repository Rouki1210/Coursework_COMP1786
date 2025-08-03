package np.com.bimalkafle.myapplication.model

data class User(
    val userId: String = "",               // Firestore doc ID
    val name: String = "",
    val email: String = "",
    val phone: String? = null,
    val role: UserRole = UserRole.CUSTOMER,
    val createdAt: String = "",
    val cartItems: List<CartItem> = emptyList()
)

enum class UserRole {
    ADMIN, CUSTOMER, TEACHER
}

data class CartItem(
    val cartId: String = "",
    val cartName: String = "",
    val price: String = "",
    val teacher: String = "",
)
