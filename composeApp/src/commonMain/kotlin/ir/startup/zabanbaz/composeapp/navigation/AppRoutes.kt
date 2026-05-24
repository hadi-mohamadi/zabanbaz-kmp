package ir.startup.zabanbaz.composeapp.navigation

import ir.startup.zabanbaz.common.profile.ProfileRoutes

object AppRoutes {
    const val Splash = "splash"
    const val Login = "login"
    const val Onboarding = "onboarding"
    const val Placement = "placement"
    const val Home = "home"
    const val DiscussionQueue = "discussion_queue"
    const val DiscussionCall = "discussion_call/{sessionId}"

    fun discussionCallRoute(sessionId: Int): String = "discussion_call/$sessionId"
    const val Profile = ProfileRoutes.Profile
}
