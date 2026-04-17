package com.example.rakshasetu.core.navigation

sealed class Screen(val route: String) {
    // Auth screens
    object Splash : Screen("splash")
    object SignIn : Screen("signin")
    object SignUp : Screen("signup")
    object Logout : Screen("logout")

    // Secretary screens
    object SecretaryHome : Screen("secretary_home")
    object CreateSociety : Screen("create_society")
    object ManageSociety : Screen("manage_society")
    object AddFlats : Screen("add_flats")
    object CreateResident : Screen("create_resident")
    object CreateWatchman : Screen("create_watchman")
    object JoinRequests : Screen("join_requests")
    object SendAnnouncement : Screen("send_announcement")
    object ManageAnnouncements : Screen("manage_announcements")
    object AllVisitors : Screen("all_visitors")

    // Watchman screens
    object WatchmanHome : Screen("watchman_home")
    object AddVisitor : Screen("add_visitor/{flatNumber}") {
        fun createRoute(flatNumber: String) = "add_visitor/$flatNumber"
    }
    object SelectFlat : Screen("select_flat")
    object CapturePhoto : Screen("capture_photo")
    object VisitorHistory : Screen("visitor_history")
    object PreApprovedList : Screen("pre_approved_list")
    object FilterVisitors : Screen("filter_visitors")
    object SendEmergencyMessage : Screen("send_emergency_message")

    // Resident screens
    object ResidentHome : Screen("resident_home")
    object VisitorApproval : Screen("visitor_approval/{requestId}") {
        fun createRoute(requestId: String) = "visitor_approval/$requestId"
    }
    object VisitorRequests : Screen("visitor_requests")
    object VisitorHistoryResident : Screen("visitor_history_resident")
    object PreApproveGuest : Screen("pre_approve_guest")
    object FilterVisitorsResident : Screen("filter_visitors_resident")
    object HouseholdMembers : Screen("household_members")

    // Community screens
    object SearchCommunity : Screen("search_community")
    object JoinCommunity : Screen("join_community/{societyId}") {  // ADDED societyId parameter
        fun createRoute(societyId: String) = "join_community/$societyId"
    }

    // Common screens (Bottom Navigation - visible to all users)
    object Announcements : Screen("announcements")
    object Emergency : Screen("emergency")
    object Directory : Screen("directory")
    object Profile : Screen("profile")
    object Notifications : Screen("notifications")

    // Drawer screens (with #007F50 top bar)
    object Settings : Screen("settings")
    object HelpSupport : Screen("help_support")
    object AboutApp : Screen("about_app")
    object Feedback : Screen("feedback")

    companion object {
        fun withArgs(route: String, vararg args: String): String {
            return buildString {
                append(route)
                args.forEach { arg ->
                    append("/$arg")
                }
            }
        }
    }
}