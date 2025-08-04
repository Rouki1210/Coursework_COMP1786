package np.com.bimalkafle.myapplication.controllers

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import np.com.bimalkafle.myapplication.model.CartItem
import np.com.bimalkafle.myapplication.model.Class
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

    fun getUserByName(userName: String, onResult: (List<User>) -> Unit) {
        db.get()
            .addOnSuccessListener { snapshot ->
                val result = mutableListOf<User>()
                snapshot.children.forEach { child ->
                    val name = child.child("name").getValue(String::class.java) ?: ""

                    if (name.contains(userName, ignoreCase = true)) {
                        result.add(
                            User(
                                userId = child.key ?: "",
                                name = name
                            )
                        )
                    }
                }
                onResult(result)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }



    fun getTeacherByName(teacherName: String, onResult: (List<User>) -> Unit) {
        db.get()
            .addOnSuccessListener { snapshot ->
                val result = mutableListOf<User>()
                snapshot.children.forEach { child ->
                    val name = child.child("name").getValue(String::class.java) ?: ""
                    val roleStr = child.child("role").getValue(String::class.java)
                    val role = try {
                        UserRole.valueOf(roleStr ?: "")
                    } catch (e: IllegalArgumentException) {
                        UserRole.CUSTOMER
                    }

                    if (role == UserRole.TEACHER &&
                        name.contains(teacherName, ignoreCase = true)
                    ) {
                        result.add(
                            User(
                                userId = child.key ?: "",
                                name = name,
                                role = role
                                // omit fields that may cause type issues
                            )
                        )
                    }
                }
                onResult(result)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }



    fun getAllTeacher(onResult: (List<User>) -> Unit) {
        db.get()
            .addOnSuccessListener { snapshot ->
                val teacherList = mutableListOf<User>()

                for (userSnapshot in snapshot.children) {
                    val userId = userSnapshot.key ?: continue
                    val name = userSnapshot.child("name").getValue(String::class.java) ?: ""
                    val email = userSnapshot.child("email").getValue(String::class.java) ?: ""
                    val phone = userSnapshot.child("phone").getValue(String::class.java) ?: ""
                    val roleStr = userSnapshot.child("role").getValue(String::class.java) ?: ""
                    val createdAt = userSnapshot.child("createAt").getValue(String::class.java) ?: ""

                    val roleEnum = try {
                        UserRole.valueOf(roleStr)
                    } catch (e: Exception) {
                        UserRole.CUSTOMER
                    }

                    if (roleEnum == UserRole.TEACHER) {
                        val teacher = User(
                            userId = userId,
                            name = name,
                            email = email,
                            phone = phone,
                            role = roleEnum,
                            createdAt = createdAt
                        )
                        teacherList.add(teacher)
                    }
                }
                onResult(teacherList)
            }
    }

    fun updateUser(user: User){
        val userId = user.userId
        if (userId.isBlank()){
            return
        }

        val userRef = db.child(userId)

        val userUpdate = mapOf(
            "name" to user.name,
            "email" to user.email,
            "phone" to user.phone,
            "role" to user.role.name,
            "createdAt" to user.createdAt
        )

        userRef.updateChildren(userUpdate)

    }

    fun deleteUser(userId: String) {
        db.child(userId).removeValue()
    }

    fun addCart(userId: String, cartItem: CartItem){
        val db = FirebaseDatabase.getInstance()
        val cartItemRef = db.getReference("cartItems").child(cartItem.cartId)
        val userCartRef = db.getReference("users").child(userId).child("cart")

        // Step 1: Get CartItem data
        cartItemRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val cartItem = snapshot.getValue(CartItem::class.java)

                // Step 2: Add to user's cart node
                if (cartItem != null) {
                    userCartRef.child(cartItem.cartId).setValue(cartItem)
                        .addOnSuccessListener {
                            Log.d("Cart", "Added to user cart successfully.")
                        }
                        .addOnFailureListener {
                            Log.e("Cart", "Failed to add to user cart: ${it.message}")
                        }
                }
            } else {
                Log.e("Cart", "CartItem not found.")
            }
        }.addOnFailureListener {
            Log.e("Cart", "Failed to fetch cartItem: ${it.message}")
        }
    }
}

