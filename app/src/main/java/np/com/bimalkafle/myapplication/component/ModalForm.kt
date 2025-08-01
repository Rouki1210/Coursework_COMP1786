package np.com.bimalkafle.myapplication.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import np.com.bimalkafle.myapplication.controllers.UserRepository
import np.com.bimalkafle.myapplication.model.User
import np.com.bimalkafle.myapplication.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalForm(
    onDismiss: () -> Unit,
    onSave: (name: String, email: String, phone: String?, role: UserRole) -> Unit,
    initialData: User? = null
) {
    var name by remember { mutableStateOf(initialData?.name ?: "") }
    var email by remember { mutableStateOf(initialData?.email ?: "") }
    var phone by remember { mutableStateOf(initialData?.phone ?: "") }
    val selectedRole = UserRole.TEACHER

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Teacher") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone (Optional)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                // Disabled role field
                OutlinedTextField(
                    value = selectedRole.name,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false, // disables the field entirely
                    label = { Text("Role") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(name, email, phone.takeIf { it.isNotBlank() }, selectedRole)
                    if (initialData == null) {
                        // Only add if creating new
                        UserRepository.addUser(
                            User(
                                userId = "",
                                name = name,
                                email = email,
                                phone = phone.takeIf { it.isNotBlank() },
                                role = selectedRole
                            )
                        )
                    } else {
                        UserRepository.updateUser(
                            initialData.copy(
                                name = name,
                                email = email,
                                phone = phone.takeIf { it.isNotBlank() },
                                role = selectedRole
                            )
                        )
                    }
                    onDismiss()
                },
                enabled = name.isNotBlank() && email.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
