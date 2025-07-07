package np.com.bimalkafle.myapplication.controllers

import com.google.firebase.database.FirebaseDatabase
import np.com.bimalkafle.myapplication.model.User

object UserRepository {
    private val db = FirebaseDatabase.getInstance().getReference("users")

    fun addUser(user: User){
        val userId = db.push().key ?: run{
            return
        }
        db.child(userId)
            .setValue(user.copy(userId = userId, createdAt = System.currentTimeMillis()))
            .addOnSuccessListener {
                println("User saved successfully")
            }
            .addOnFailureListener { e ->
                println("Failed to save user: ${e.message}")
            }
    }
}

