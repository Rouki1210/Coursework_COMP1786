package np.com.bimalkafle.myapplication.pages


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import np.com.bimalkafle.myapplication.AuthState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.*
import androidx.navigation.compose.*


import np.com.bimalkafle.myapplication.AuthViewModel
import np.com.bimalkafle.myapplication.NavItem
import org.w3c.dom.Text

@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel ) {

    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.UnAuthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home,0),
        NavItem("Teacher", Icons.Default.School,0),
        NavItem("Class", Icons.Default.Museum,0),
        NavItem("User", Icons.Default.Person,0)
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF121212),
        bottomBar = {
            NavigationBar (
                containerColor = Color(0xFF1E1E1E), // Dark nav bar background
                contentColor = Color.White
            ){
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected =  selectedIndex == index ,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                            BadgedBox(badge = {
                                if(navItem.badgeCount>0)
                                    Badge(){
                                        Text(text = navItem.badgeCount.toString())
                                    }
                            }) {
                                Icon(imageVector = navItem.icon, contentDescription = "Icon")
                            }

                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding),
            selectedIndex = selectedIndex,
            navController = navController,
            authViewModel = authViewModel)
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex : Int,
                  navController: NavController,
                  authViewModel: AuthViewModel) {
    when(selectedIndex){
        0-> HomeScreen(navController = navController, authViewModel = authViewModel)
        1-> TeacherPage()
        2-> ClassPage()
        3-> UserPage()
    }
}

