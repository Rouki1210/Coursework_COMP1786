package np.com.bimalkafle.myapplication.controllers

import com.google.firebase.database.FirebaseDatabase
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
                    val user = child.getValue(User::class.java)
                    if (user != null && user.name.contains(userName, ignoreCase = true)) {
                        result.add(user.copy(userId = child.key ?: ""))
                    }
                }
                onResult(result)
            }
            .addOnFailureListener { exception ->
                // handle failure gracefully
                onResult(emptyList())
            }
    }


    fun getTeacherByName(teacherName: String, onResult: (List<User>) -> Unit) {
        db.get()
            .addOnSuccessListener { snapshot ->
                val result = mutableListOf<User>()
                snapshot.children.forEach { child ->
                    val user = child.getValue(User::class.java)
                    if (user != null &&
                        user.role == UserRole.TEACHER &&
                        user.name.contains(teacherName, ignoreCase = true)){
                        result.add(user.copy(userId = child.key ?: ""))
                    }
                }
                onResult(result)
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
}

