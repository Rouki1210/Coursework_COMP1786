package np.com.bimalkafle.myapplication.model

import com.google.firebase.Timestamp

data class User(
    val userId: String = "",               // Firestore doc ID
    val name: String = "",
    val email: String = "",
    val phone: String? = null,
    val role: UserRole = UserRole.CUSTOMER,
    val createdAt: Timestamp = Timestamp.now()
)

enum class UserRole {
    ADMIN, CUSTOMER, TEACHER
}
