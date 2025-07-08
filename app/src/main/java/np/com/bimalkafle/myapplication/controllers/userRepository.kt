package np.com.bimalkafle.myapplication.controllers

import com.google.firebase.database.FirebaseDatabase
import np.com.bimalkafle.myapplication.model.User
import np.com.bimalkafle.myapplication.model.UserRole
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object UserRepository {
    private val db = FirebaseDatabase.getInstance().getReference("users")

    fun addUser(user: User){
        val userId = db.push().key ?: run{
            return
        }
        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        db.child(userId).setValue(user.copy(userId = userId, createdAt = currentTime))
    }

    fun getAllUser(onResult: (List<User>) -> Unit){
        db.get()
            .addOnSuccessListener { snapshot ->
                val userList = mutableListOf<User>()

                for(userSnapshot in snapshot.children){
                    val userId = userSnapshot.key ?: continue
                    val name = userSnapshot.child("name").getValue(String::class.java) ?: ""
                    val email = userSnapshot.child("email").getValue(String::class.java) ?: ""
                    val phone = userSnapshot.child("phone").getValue(String::class.java) ?: ""
                    val roleStr = userSnapshot.child("role").getValue(String::class.java) ?: ""
                    val createAt = userSnapshot.child("createAt").getValue(String::class.java) ?: ""

                    val roleEnum = try {
                        UserRole.valueOf(roleStr)
                    } catch (e: Exception) {
                        UserRole.CUSTOMER
                    }

                    val user = User(
                        userId = userId,
                        name = name,
                        email = email,
                        phone = phone,
                        role = roleEnum,
                        createdAt = createAt,
                    )

                    userList.add(user)
                }
                onResult(userList)
            }
    }

    fun deleteUser(userId: String) {
        db.child(userId).removeValue()
    }
}

