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
import np.com.bimalkafle.myapplication.model.Class


@Composable
fun ClassCard(aClass: Class) {
    val descriptionText = if (aClass.description.length > 50) {
        aClass.description.take(30) + "..."
    } else {
        aClass.description
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
