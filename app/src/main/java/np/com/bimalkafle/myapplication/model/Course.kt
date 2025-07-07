package np.com.bimalkafle.myapplication.model

import com.google.firebase.Timestamp

data class Course(
    val classId: String = "",
    val name: String = "",
    val description: String = "",
    val durationMinutes: Int = 60,
    val maxCapacity: Int = 20,
    val teacherId: String = "",            // userId of employee
    val createdAt: Timestamp = Timestamp.now()
)
