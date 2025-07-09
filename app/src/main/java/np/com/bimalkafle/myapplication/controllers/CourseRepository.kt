package np.com.bimalkafle.myapplication.controllers

import com.google.firebase.database.FirebaseDatabase
import np.com.bimalkafle.myapplication.model.Class
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CourseRepository {
    private val db = FirebaseDatabase.getInstance().getReference("classes")

    fun addCourse(aClass: Class){
        var classId = db.push().key ?: return
        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        db.child(classId).setValue(aClass.copy(classId = classId, createdAt = currentTime))
    }
}