package np.com.bimalkafle.myapplication.pages


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import np.com.bimalkafle.myapplication.component.UserCard
import np.com.bimalkafle.myapplication.controllers.UserRepository
import np.com.bimalkafle.myapplication.model.User
import org.jetbrains.annotations.ApiStatus
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import np.com.bimalkafle.myapplication.component.ClassCard
import np.com.bimalkafle.myapplication.component.ModalForm
import np.com.bimalkafle.myapplication.controllers.CourseRepository


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherPage(modifier: Modifier = Modifier) {

    var searchQuery by remember {
        mutableStateOf("")
    }
    val showModalTeacherAdd = remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    var allTeacher by remember { mutableStateOf<List<User>>(emptyList()) }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isBlank()) {
            UserRepository.getAllTeacher { allTeacher = it }
        } else{
            UserRepository.getTeacherByName(searchQuery) {allTeacher = it}
        }
    }

    fun refreshData(){
        isRefreshing = true
        UserRepository.getAllTeacher { fetchedUser ->
            allTeacher = fetchedUser
            isRefreshing = false
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // TODO: handle add new user
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = "Add User")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
                .padding(innerPadding)
                .padding(bottom = 20.dp)
        ) {
            TopAppBar(
                title = {
                    Text(
                        "Teachers",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = {
                        showModalTeacherAdd.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add User",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF1F1F1F),
                    titleContentColor = Color.White
                )
            )

            if (showModalTeacherAdd.value) {
                ModalForm(
                    onDismiss = { showModalTeacherAdd.value = false },
                    onSave = { name, email, phone, role ->
                        println("User: $name, $email, $phone, $role")
                        showModalTeacherAdd.value = false
                        refreshData()
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search teacher...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))


            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { refreshData() }
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(allTeacher) { teacher ->
                        UserCard(teacher)
                    }
                }
            }
        }
    }
}
