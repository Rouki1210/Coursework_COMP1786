package np.com.bimalkafle.myapplication

import androidx.compose.runtime.Composable
import np.com.bimalkafle.myapplication.pages.LoginPage
import  androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import np.com.bimalkafle.myapplication.pages.HomePage
import np.com.bimalkafle.myapplication.pages.SignupPage
import np.com.bimalkafle.myapplication.pages.UserPage


@Composable
fun MyAppNavigation(modifier: Modifier = Modifier , authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", builder = {
        composable( "login"){
            LoginPage(modifier, navController, authViewModel)
        }
        composable( "register"){
            SignupPage(modifier, navController, authViewModel)
        }
        composable( "home"){
            HomePage(modifier, navController, authViewModel)
        }
        composable("user") {
            UserPage()
        }
    } )
}