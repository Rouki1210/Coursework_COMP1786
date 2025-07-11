package np.com.bimalkafle.myapplication.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import np.com.bimalkafle.myapplication.controllers.UserRepository
import np.com.bimalkafle.myapplication.model.Class
import np.com.bimalkafle.myapplication.model.User
import np.com.bimalkafle.myapplication.model.UserRole


@Composable
fun UserCard(user: User) {

    val showEditModal = remember { mutableStateOf(false) }
    var userToEdit by remember { mutableStateOf<User?>(null) }
    val showEditTeacherModal = remember { mutableStateOf(false) }

    if(showEditModal.value && userToEdit != null && user.role == UserRole.CUSTOMER){
        AddUserModal(
            onDismiss = {
                showEditModal.value = false
                userToEdit = null
            },
            onSave = { name, email, phone, role ->
                // Build updated User object
                val updatedUser = userToEdit!!.copy(
                    name = name,
                    email = email,
                    phone = phone,
                    role = role
                )

                // Call repository to update
                UserRepository.updateUser(updatedUser)

                showEditModal.value = false
                userToEdit = null
            },
            initialData = userToEdit
        )
    }

    if (showEditTeacherModal.value && userToEdit != null && userToEdit!!.role == UserRole.TEACHER) {
        ModalForm(
            onDismiss = {
                showEditTeacherModal.value = false
                userToEdit = null
            },
            onSave = { name, email, phone, role ->
                val updatedUser = userToEdit!!.copy(
                    name = name,
                    email = email,
                    phone = phone,
                    role = role
                )
                UserRepository.updateUser(updatedUser)
                showEditTeacherModal.value = false
                userToEdit = null
            },
            initialData = userToEdit
        )
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                user.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                user.email,
                color = Color.LightGray,
                fontSize = 14.sp
            )
            Text(
                user.phone.toString(),
                color = Color.LightGray,
                fontSize = 14.sp
            )
            Text(
                "Role: ${user.role}",
                color = Color.LightGray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = {
                    userToEdit = user
                    if (user.role == UserRole.TEACHER) {
                        showEditTeacherModal.value = true
                    } else {
                        showEditModal.value = true
                    }
                }) {
                    Text("Edit", color = Color.White)
                }
                TextButton(onClick = {
                    UserRepository.deleteUser(user.userId)
                }) {
                    Text("Delete", color = Color.Red)
                }
            }
        }
    }
}


