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
    import androidx.compose.foundation.lazy.LazyRow
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material3.FilterChip



    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddClassModal(
        onDismiss: () -> Unit,
        onSave:
            (name: String,
             description: String,
             day_of_week: String,
             time_of_course: String,
             price: String ,
             durationMinutes: String,
             maxCapacity: String,
             teacher: String) -> Unit,
        initialData: Class? = null
    ) {
        var name by remember { mutableStateOf(initialData?.name ?: "") }
        var day_of_week by remember { mutableStateOf(initialData?.day_of_week ?: "") }
        var time_of_course by remember { mutableStateOf(initialData?.time_of_course ?: "") }
        var price by remember { mutableStateOf(initialData?.price ?: "") }
        var description by remember { mutableStateOf(initialData?.description ?: "") }
        var durationMinutes by remember { mutableStateOf(initialData?.durationMinutes ?: "") }
        var maxCapacity by remember { mutableStateOf(initialData?.maxCapacity ?: "") }
        var selectedDays by remember {
            mutableStateOf(
                initialData?.day_of_week?.split(",")?.toSet() ?: emptySet()
            )
        }
        var selectedTeacher by remember { mutableStateOf<User?>(null) }

        var expanded by remember { mutableStateOf(false) }
        val teachers = remember { mutableStateOf<List<User>>(emptyList()) }
        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

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

                    Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(daysOfWeek.size) { index ->
                                val day = daysOfWeek[index]
                                FilterChip(
                                    selected = selectedDays.contains(day),
                                    onClick = {
                                        selectedDays = if (selectedDays.contains(day)) {
                                            selectedDays - day
                                        } else {
                                            selectedDays + day
                                        }
                                    },
                                    label = { Text(day) }
                                )
                            }
                        }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = time_of_course,
                            onValueChange = { time_of_course = it },
                            label = { Text("Time") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = price,
                            onValueChange = { price = it.filter { c -> c.isDigit() || c == '.' } },
                            label = { Text("Price ($)") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                        )
                        OutlinedTextField(
                            value = maxCapacity,
                            onValueChange = { maxCapacity = it.filter { char -> char.isDigit() } },
                            label = { Text("Capacity") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }

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
                        val newClass = Class(
                            classId = initialData?.classId ?: "",
                            name = name,
                            day_of_week = selectedDays.joinToString(","),
                            time_of_course = time_of_course,
                            price = price,
                            description = description,
                            durationMinutes = durationMinutes,
                            maxCapacity = maxCapacity,
                            teacher = selectedTeacher?.name ?: "",
                            createdAt = initialData?.createdAt ?: ""
                        )
                        if(initialData == null){
                            CourseRepository.addCourse(newClass)
                        }else{
                            CourseRepository.updateClass(newClass)
                        }
                        onSave(
                            name,
                            description,
                            selectedDays.joinToString(","),
                            time_of_course,
                            price,
                            durationMinutes,
                            maxCapacity,
                            selectedTeacher?.name ?: ""
                        )
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
