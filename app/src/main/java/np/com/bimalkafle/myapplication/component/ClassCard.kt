package np.com.bimalkafle.myapplication.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import np.com.bimalkafle.myapplication.controllers.CourseRepository
import np.com.bimalkafle.myapplication.model.Class


@Composable
fun ClassCard(aClass: Class) {
    val descriptionText = if (aClass.description.length > 50) {
        aClass.description.take(30) + "..."
    } else {
        aClass.description
    }



    val showEditModal = remember { mutableStateOf(false) }
    var classToEdit by remember { mutableStateOf<Class?>(null) }

    if(showEditModal.value && classToEdit != null){
        AddClassModal(
            onDismiss = {
                showEditModal.value = false
            },
            onSave = { name, desc, dayofweek, timeofcourse, price, duration, capacity, scheduledDate, teacher ->
                val updatedClass = classToEdit!!.copy(
                    name = name,
                    description = desc,
                    day_of_week = dayofweek,
                    time_of_course = timeofcourse,
                    price = price,
                    durationMinutes = duration,
                    maxCapacity = capacity,
                    scheduleDate = scheduledDate,
                    teacher = teacher
                )
                CourseRepository.updateClass(updatedClass)
                showEditModal.value = false
            },
            initialData = classToEdit
        )
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "${aClass.name} - ${aClass.price}$",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                descriptionText,
                color = Color.LightGray,
                fontSize = 14.sp
            )
            Text(
                "Teacher: ${aClass.teacher}",
                color = Color.LightGray,
                fontSize = 14.sp
            )
            Text(
                "Duration: ${aClass.durationMinutes} mins | Slot: ${aClass.maxCapacity}",
                color = Color.LightGray,
                fontSize = 14.sp
            )
            Text(
                "Day: ${aClass.day_of_week} || Time: ${aClass.time_of_course}",
                color = Color.LightGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = {
                    classToEdit = aClass
                    showEditModal.value = true
                }) {
                    Text("Edit", color = Color.White)
                }
                TextButton(onClick = {
                   CourseRepository.deleteUser(classId = aClass.classId)
                }) {
                    Text("Delete", color = Color.Red)
                }
            }
        }
    }
}
