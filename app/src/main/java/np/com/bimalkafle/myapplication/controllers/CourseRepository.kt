package np.com.bimalkafle.myapplication.controllers

import com.google.firebase.database.FirebaseDatabase
import np.com.bimalkafle.myapplication.model.Class
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object CourseRepository {
    private val db = FirebaseDatabase.getInstance().getReference("classes")

    fun addCourse(aClass: Class){
        var classId = db.push().key ?: return
        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        db.child(classId).setValue(aClass.copy(classId = classId, createdAt = currentTime))
    }

    fun getNextDateForDay(dayAbbreviation: String): String {
        val dayMap = mapOf(
            "Sun" to Calendar.SUNDAY,
            "Mon" to Calendar.MONDAY,
            "Tue" to Calendar.TUESDAY,
            "Wed" to Calendar.WEDNESDAY,
            "Thu" to Calendar.THURSDAY,
            "Fri" to Calendar.FRIDAY,
            "Sat" to Calendar.SATURDAY
        )
        val targetDay = dayMap[dayAbbreviation] ?: return ""
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_WEEK)

        var daysUntil = (targetDay - today + 7) % 7
        if (daysUntil == 0) daysUntil = 7 // Avoid today, get next one

        calendar.add(Calendar.DAY_OF_YEAR, daysUntil)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(calendar.time)
    }

    fun getAllCourse(onResult: (List<Class>) -> Unit) {
        db.get()
            .addOnSuccessListener { snapshot ->
                val classList = mutableListOf<Class>()

                for(classSnapshot in snapshot.children){
                    val classId = classSnapshot.key ?: continue
                    val name = classSnapshot.child("name").getValue(String::class.java) ?: ""
                    val day_of_week = classSnapshot.child("day_of_week").getValue(String::class.java) ?: ""
                    val time_of_course = classSnapshot.child("time_of_course").getValue(String::class.java) ?: ""
                    val price = classSnapshot.child("price").getValue(String::class.java) ?: ""
                    val description = classSnapshot.child("description").getValue(String::class.java) ?: ""
                    val durationMinutes = classSnapshot.child("durationMinutes").getValue(String::class.java) ?: ""
                    val maxCapacity = classSnapshot.child("maxCapacity").getValue(String::class.java) ?: ""
                    val teacher = classSnapshot.child("teacher").getValue(String::class.java) ?: ""
                    val createAt = classSnapshot.child("createAt").getValue(String::class.java) ?: ""

                    val aclass = Class(
                        classId = classId,
                        name = name,
                        day_of_week = day_of_week,
                        time_of_course = time_of_course,
                        price = price,
                        description = description,
                        durationMinutes = durationMinutes,
                        maxCapacity = maxCapacity,
                        teacher = teacher,
                        createdAt = createAt
                    )

                    classList.add(aclass)
                }
                onResult(classList)
            }
    }

    fun getClassByName(className: String, onResult: (List<Class>) -> Unit){
        db.get()
            .addOnSuccessListener { snapshot ->
                val result = mutableListOf<Class>()
                snapshot.children.forEach { child ->
                    val aClass = child.getValue(Class::class.java)
                    if (aClass != null && aClass.name.contains(className, ignoreCase = true)) {
                        result.add(aClass.copy(classId = child.key ?: ""))
                    }
                }
                onResult(result)
            }
    }

    fun updateClass(aClass: Class){
        val classId =  aClass.classId
        if (classId.isBlank()) return

        val  classRef = db.child(classId)

        val updatedMap = mapOf(
            "name" to aClass.name,
            "day_of_week" to aClass.day_of_week,
            "time_of_course" to aClass.time_of_course,
            "price" to aClass.price,
            "description" to aClass.description,
            "durationMinutes" to aClass.durationMinutes,
            "maxCapacity" to aClass.maxCapacity,
            "teacher" to aClass.teacher,
            "createdAt" to aClass.createdAt // optional, or use ServerTimestamp
        )

        classRef.updateChildren(updatedMap)
    }

    fun deleteUser(classId: String) {
        CourseRepository.db.child(classId).removeValue()
    }
}