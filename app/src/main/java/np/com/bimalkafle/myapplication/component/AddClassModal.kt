    package np.com.bimalkafle.myapplication.component

    import androidx.compose.foundation.layout.*
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.unit.dp
    import np.com.bimalkafle.myapplication.controllers.CourseRepository
    import np.com.bimalkafle.myapplication.controllers.UserRepository
    import np.com.bimalkafle.myapplication.model.Class
    import np.com.bimalkafle.myapplication.model.User


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddClassModal(
        onDismiss: () -> Unit,
        onSave: (name: String, description: String, durationMinutes: Int, maxCapacity: Int, teacher: String) -> Unit
    ) {
        var name by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var durationMinutes by remember { mutableStateOf("") }
        var maxCapacity by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }
        var selectedTeacher by remember { mutableStateOf<User?>(null) }
        val teachers = remember { mutableStateOf<List<User>>(emptyList()) }

        // Load teachers when this composable opens
        LaunchedEffect(Unit) {
            UserRepository.getAllTeacher{ fetchedTeachers ->
                teachers.value = fetchedTeachers
                if (selectedTeacher == null && fetchedTeachers.isNotEmpty()) {
                    selectedTeacher = fetchedTeachers[0]
                }
            }
        }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Add New Class") },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Class Name") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    OutlinedTextField(
                        value = durationMinutes,
                        onValueChange = { durationMinutes = it.filter { char -> char.isDigit() } },
                        label = { Text("Duration (minutes)") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    OutlinedTextField(
                        value = maxCapacity,
                        onValueChange = { maxCapacity = it.filter { char -> char.isDigit() } },
                        label = { Text("Max Capacity") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedTeacher?.name ?: "Select Teacher",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Teacher") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            teachers.value.forEach { teacher ->
                                DropdownMenuItem(
                                    text = { Text(teacher.name) },
                                    onClick = {
                                        selectedTeacher = teacher
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
                        onSave(
                            name,
                            description,
                            durationMinutes.toIntOrNull() ?: 0,
                            maxCapacity.toIntOrNull() ?: 0,
                            selectedTeacher?.userId ?: ""
                        )

                        CourseRepository.addCourse(Class("", name, description, durationMinutes, maxCapacity,
                            selectedTeacher.toString()
                        ))
                    },
                    enabled = name.isNotBlank() && durationMinutes.isNotBlank() && maxCapacity.isNotBlank() && selectedTeacher != null
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
