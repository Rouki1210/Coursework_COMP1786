package np.com.bimalkafle.myapplication.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.oAuthProvider
import np.com.bimalkafle.myapplication.AuthState
import np.com.bimalkafle.myapplication.AuthViewModel
import np.com.bimalkafle.myapplication.component.AddClassModal
import np.com.bimalkafle.myapplication.component.AddUserModal
import np.com.bimalkafle.myapplication.pages.*
import np.com.bimalkafle.myapplication.component.ModalForm
import np.com.bimalkafle.myapplication.controllers.CourseRepository
import np.com.bimalkafle.myapplication.controllers.OrderRepository
import np.com.bimalkafle.myapplication.controllers.UserRepository
import np.com.bimalkafle.myapplication.model.User
import np.com.bimalkafle.myapplication.model.Class
import np.com.bimalkafle.myapplication.model.Order


data class DashboardItem(
    val count: Int,
    val label: String,
    val icon: ImageVector
)

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    var allUsers by remember { mutableStateOf<List<User>>(emptyList()) }
    var allTeacher by remember { mutableStateOf<List<User>>(emptyList()) }
    var allOrder by remember { mutableStateOf<List<Order>>(emptyList()) }
    var allClass by remember { mutableStateOf<List<Class>>(emptyList()) }
    val showAddTeacherDialog  = remember { mutableStateOf(false) }
    val showModalAddUser = remember { mutableStateOf(false) }
    val showModalAddClass = remember { mutableStateOf(false) }
    val userCount = allUsers.size
    val teacherCount = allTeacher.size
    val classCount = allClass.size
    val orderCount = allOrder.size


    LaunchedEffect(Unit) {
        UserRepository.getAllUser { users ->
            allUsers = users
        }
        UserRepository.getAllTeacher { teachers ->
            allTeacher = teachers
        }
        CourseRepository.getAllCourse { classes ->
            allClass = classes
        }
        OrderRepository.getAllOrder { orders ->
            allOrder = orders
        }
    }

    val dashboardItems = listOf(
        DashboardItem(userCount, "Users", Icons.Default.Person),
        DashboardItem(teacherCount, "Teacher", Icons.Default.School),
        DashboardItem(classCount, "Classes", Icons.Default.Museum),
        DashboardItem(orderCount, "Orders", Icons.Default.ShoppingCart)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFF121212))
    ) {
        Text(
            text = "Welcome Back",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp, top = 32.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(dashboardItems) { item ->
                CardAdmin(
                    count = item.count,
                    label = item.label,
                    icon = item.icon,
                    isSelected = item.label == "Users",
                    onClick = {

                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF333333), shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        )
        {
            Text(
                text = "Add Menu",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            AddMenuItem(
                title = "Add new user",
                icon = Icons.Default.Person
                ) {
                showModalAddUser.value = true
            }
            AddMenuItem(
                title = "Add teacher",
                icon = Icons.Default.School
            ) {
                showAddTeacherDialog.value = true
            }
            AddMenuItem(
                title = "Add Class",
                icon = Icons.Default.Museum
            ) {
                showModalAddClass.value = true
            }

            if(showAddTeacherDialog.value){
                ModalForm(
                    onDismiss = { showAddTeacherDialog.value = false },
                    onSave = { name, email, phone, role ->
                        println("User: $name, $email, $phone, $role")
                        showAddTeacherDialog.value = false
                    }
                )
            }

            if (showModalAddUser.value) {
                AddUserModal(
                    onDismiss = { showModalAddUser.value = false },
                    onSave = { name, email, phone, role ->
                        println("User: $name, $email, $phone, $role")
                        showModalAddUser.value = false
                    }
                )
            }

            if(showModalAddClass.value){
                AddClassModal(
                    onDismiss = {showModalAddClass.value = false},
                    onSave = { name, desc, dayofweek, timeofcourse, price, duration, capacity, teacherId ->
                        println("Class = $name, $desc, $duration, $capacity, $teacherId, $dayofweek, $timeofcourse, $price")
                        showModalAddClass.value = false
                    }
                )
            }

        }
    }
}

@Composable
fun AddMenuItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF444444),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White
            )
        }
    }

}


@Composable
fun CardAdmin(count: Int,
              label: String,
              isSelected: Boolean = false,
              icon: ImageVector,
              onClick: () -> Unit,
              modifier: Modifier = Modifier){
    val backgroundColor = Color(0xFF333333)
    val contentColor = Color(0xFFB0BEC5)

    Card(
        modifier = modifier.size(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = contentColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = count.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            Text(
                text = label,
                fontSize = 14.sp,
                color = contentColor
            )
        }
    }
}
