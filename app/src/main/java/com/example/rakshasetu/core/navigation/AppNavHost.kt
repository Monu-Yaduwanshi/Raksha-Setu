package com.example.rakshasetu.core.navigation
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rakshasetu.core.components.SplashScreen
import com.example.rakshasetu.core.components.LogoutScreen
import com.example.rakshasetu.ui.Authentication.SignInScreen
import com.example.rakshasetu.ui.Authentication.SignUpScreen
import com.example.rakshasetu.ui.Community.SearchCommunityScreen
import com.example.rakshasetu.ui.Community.JoinCommunityScreen
import com.example.rakshasetu.ui.Community.JoinRequestsScreen
import com.example.rakshasetu.ui.announcement.AnnouncementsScreen
import com.example.rakshasetu.ui.directory.DirectoryScreen
import com.example.rakshasetu.ui.drawer.AboutAppScreen
import com.example.rakshasetu.ui.drawer.FeedbackScreen
import com.example.rakshasetu.ui.drawer.HelpSupportScreen
import com.example.rakshasetu.ui.drawer.SettingsScreen
import com.example.rakshasetu.ui.emergency.EmergencyScreen
import com.example.rakshasetu.ui.profile.ProfileScreen
import com.example.rakshasetu.ui.resident.*
import com.example.rakshasetu.ui.secretary.*
import com.example.rakshasetu.ui.watchMan.*
import com.example.rakshasetu.ui.notification.NotificationScreen
import com.example.rakshasetu.viewModel.viewModel.auth.User
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    isUserLoggedIn: Boolean,
    userRole: String?,
    userData: User?,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // ==================== SPLASH & AUTH SCREENS ====================

        composable(Screen.Splash.route) {
            SplashScreen(
                navController = navController,
                isUserLoggedIn = isUserLoggedIn,
                userRole = userRole
            )
        }

        composable(Screen.SignIn.route) {
            SignInScreen(navController = navController)
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }

        composable(Screen.Logout.route) {
            LogoutScreen(
                navController = navController,
                userName = userData?.fullName ?: "User",
                userRole = userRole
            )
        }

        // ==================== SECRETARY SCREENS ====================

        composable(Screen.SecretaryHome.route) {
            SecretaryHomeScreen(navController = navController)
        }

        composable(Screen.CreateSociety.route) {
            CreateSocietyScreen(
                navController = navController,
                userRole = userRole ?: "secretary"
            )
        }

        composable(Screen.ManageSociety.route) {
            ManageSocietyScreen(
                navController = navController,
                userRole = userRole ?: "secretary",
                societyId = userData?.societyId ?: ""
            )
        }

        composable(Screen.AddFlats.route) {
            AddFlatsScreen(
                navController = navController,
                userRole = userRole ?: "secretary",
                societyId = userData?.societyId ?: "",
                blocks = emptyList()
            )
        }

        composable(Screen.CreateResident.route) {
            CreateResidentScreen(
                navController = navController,
                userRole = userRole ?: "secretary",
                societyId = userData?.societyId ?: "",
                societyName = userData?.societyName ?: ""
            )
        }

        composable(Screen.CreateWatchman.route) {
            CreateWatchmanScreen(
                navController = navController,
                userRole = userRole ?: "secretary",
                societyId = userData?.societyId ?: "",
                societyName = userData?.societyName ?: ""
            )
        }

        composable(Screen.JoinRequests.route) {
            JoinRequestsScreen(
                navController = navController,
                userRole = userRole ?: "secretary",
                societyId = userData?.societyId ?: ""
            )
        }

        composable(Screen.SendAnnouncement.route) {
            SendAnnouncementScreen(
                navController = navController,
                userRole = userRole ?: "secretary",
                societyId = userData?.societyId ?: "",
                createdByName = userData?.fullName ?: "Secretary"
            )
        }

        composable(Screen.ManageAnnouncements.route) {
            ManageAnnouncementsScreen(
                navController = navController,
                userRole = userRole ?: "secretary",
                societyId = userData?.societyId ?: ""
            )
        }

        composable(Screen.AllVisitors.route) {
            AllVisitorsScreen(
                navController = navController,
                userRole = userRole ?: "secretary",
                societyId = userData?.societyId ?: ""
            )
        }

        // ==================== WATCHMAN SCREENS ====================

        composable(Screen.WatchmanHome.route) {
            WatchmanHomeScreen(navController = navController)
        }

        composable(Screen.SelectFlat.route) {
            SelectFlatScreen(
                navController = navController,
                userRole = userRole ?: "watchman"
            )
        }

        // ✅ ONLY ONE add_visitor route - KEPT THIS ONE
        composable("add_visitor/{flatNumber}") { backStackEntry ->
            val flatNumber = backStackEntry.arguments?.getString("flatNumber") ?: ""
            AddVisitorScreen(
                navController = navController,
                flatNumber = flatNumber,
                userRole = userRole
            )
        }

        // ✅ ONLY ONE capture_photo route - KEPT THIS ONE
        composable("capture_photo") {
            CapturePhotoScreen(
                navController = navController,
                userRole = userRole
            )
        }

        composable(Screen.VisitorHistory.route) {
            VisitorHistoryScreen(
                navController = navController,
                userRole = userRole ?: "watchman"
            )
        }

        composable(Screen.PreApprovedList.route) {
            PreApprovedListScreen(
                navController = navController,
                userRole = userRole ?: "watchman"
            )
        }

        composable(Screen.SendEmergencyMessage.route) {
            SendEmergencyMessageScreen(
                navController = navController,
                userRole = userRole ?: "watchman"
            )
        }

        // ==================== RESIDENT SCREENS ====================

        composable(Screen.ResidentHome.route) {
            ResidentHomeScreen(navController = navController)
        }

        composable(Screen.VisitorRequests.route) {
            VisitorRequestsScreen(
                navController = navController,
                userRole = userRole ?: "resident"
            )
        }

        composable(Screen.VisitorApproval.route) { backStackEntry ->
            VisitorApprovalScreen(
                navController = navController,
                userRole = userRole ?: "resident"
            )
        }

        composable(Screen.VisitorHistoryResident.route) {
            VisitorHistoryResidentScreen(
                navController = navController,
                userRole = userRole ?: "resident"
            )
        }

        composable(Screen.PreApproveGuest.route) {
            PreApproveGuestScreen(
                navController = navController,
                userRole = userRole ?: "resident"
            )
        }

        composable(Screen.HouseholdMembers.route) {
            HouseholdMembersScreen(
                navController = navController,
                userRole = userRole ?: "resident"
            )
        }

        composable(Screen.FilterVisitorsResident.route) {
            FilterVisitorsResidentScreen(
                navController = navController,
                userRole = userRole ?: "resident"
            )
        }

        // ==================== COMMUNITY SCREENS ====================

        composable(Screen.SearchCommunity.route) {
            SearchCommunityScreen(
                navController = navController,
                userRole = userRole
            )
        }

        composable(Screen.JoinCommunity.route) { backStackEntry ->
            JoinCommunityScreen(
                navController = navController,
                userRole = userRole
            )
        }

        // ==================== COMMON SCREENS ====================

        composable(Screen.Announcements.route) {
            AnnouncementsScreen(
                navController = navController,
                userRole = userRole
            )
        }

        composable(Screen.Emergency.route) {
            EmergencyScreen(
                navController = navController,
                userRole = userRole
            )
        }

        composable(Screen.Directory.route) {
            DirectoryScreen(
                navController = navController,
                userRole = userRole
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                userRole = userRole
            )
        }

        composable(Screen.Notifications.route) {
            NotificationScreen(
                navController = navController,
                userRole = userRole
            )
        }

        // ==================== DRAWER SCREENS ====================

        composable(Screen.Settings.route) {
            SettingsScreen(
                navController = navController,
                userRole = userRole
            )
        }

        composable(Screen.HelpSupport.route) {
            HelpSupportScreen(
                navController = navController,
                userRole = userRole
            )
        }

        composable(Screen.AboutApp.route) {
            AboutAppScreen(
                navController = navController,
                userRole = userRole
            )
        }

        composable(Screen.Feedback.route) {
            FeedbackScreen(
                navController = navController,
                userRole = userRole
            )
        }
    }

    // ==================== POST-SPLASH NAVIGATION ====================

    LaunchedEffect(isUserLoggedIn, userRole) {
        val currentRoute = navController.currentDestination?.route
        if (currentRoute == Screen.Splash.route) {
            delay(100)

            if (isUserLoggedIn) {
                when (userRole) {
                    "secretary" -> {
                        navController.navigate(Screen.SecretaryHome.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                    "watchman" -> {
                        navController.navigate(Screen.WatchmanHome.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                    "resident" -> {
                        navController.navigate(Screen.ResidentHome.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                    else -> {
                        navController.navigate(Screen.SignIn.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            } else {
                navController.navigate(Screen.SignIn.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }
}