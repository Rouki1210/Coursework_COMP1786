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
fun AddUserModal(
    onDismiss: () -> Unit,
    onSave: (name: String, email: String, phone: String?, role: UserRole) -> Unit,
    initialData: User? = null
) {
    var name by remember { mutableStateOf(initialData?.name ?: "") }
    var email by remember { mutableStateOf(initialData?.email ?: "") }
    var phone by remember { mutableStateOf(initialData?.phone ?: "") }
    var expanded by remember { mutableStateOf(false) }
    var selectedRole by remember {
        mutableStateOf(initialData?.role ?: UserRole.CUSTOMER)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New User") },
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

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                ) {
                    OutlinedTextField(
                        value = selectedRole.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Role") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        UserRole.values().forEach { role ->
                            DropdownMenuItem(
                                text = { Text(role.name) },
                                onClick = {
                                    selectedRole = role
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newUser = User(
                        userId = initialData?.userId ?: "",
                        name = name,
                        email = email,
                        phone = phone.takeIf { it.isNotBlank() },
                        role = selectedRole,
                        createdAt = initialData?.createdAt ?: ""
                     )
                    if (initialData == null){
                        UserRepository.addUser(newUser)
                    }else{
                        UserRepository.updateUser(newUser)
                    }
                    onSave(name, email, phone.takeIf { it.isNotBlank() }, selectedRole)

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
