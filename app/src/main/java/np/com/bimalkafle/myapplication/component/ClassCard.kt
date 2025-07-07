package np.com.bimalkafle.myapplication.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import np.com.bimalkafle.myapplication.model.Course


@Composable
fun ClassCard(course: Course) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                course.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                course.teacherId,
                color = Color.LightGray,
                fontSize = 14.sp
            )
            Text(
                course.description,
                color = Color.LightGray,
                fontSize = 14.sp
            )
            Text(
                "Teacher Id: ${course.teacherId}",
                color = Color.LightGray,
                fontSize = 14.sp
            )
            Text(
                "Duration: ${course.durationMinutes} mins | Slot: ${course.maxCapacity}",
                color = Color.LightGray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = {
                    // TODO: handle edit
                }) {
                    Text("Edit", color = Color.White)
                }
                TextButton(onClick = {
                    // TODO: handle delete
                }) {
                    Text("Delete", color = Color.Red)
                }
            }
        }
    }
}


