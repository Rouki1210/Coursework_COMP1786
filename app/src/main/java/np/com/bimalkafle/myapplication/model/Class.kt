package np.com.bimalkafle.myapplication.model

data class Class(
    val classId: String = "",
    val name: String = "",
    val day_of_week: String = "",
    val time_of_course: String = "",
    val price: String = "",
    val description: String = "",
    val durationMinutes: String = "",
    val maxCapacity: String = "",
    val teacher: String = "",            // userId of employee
    val createdAt: String = "",
)
