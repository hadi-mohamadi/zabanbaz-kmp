package ir.startup.zabanbaz.composeapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.startup.zabanbaz.composeapp.navigation.AppRoutes
import ir.startup.zabanbaz.composeapp.navigation.AppScreenPadding
import ir.startup.zabanbaz.composeapp.ui.auth.LoginScreen
import ir.startup.zabanbaz.composeapp.ui.home.HomeScreen
import ir.startup.zabanbaz.composeapp.ui.onboarding.OnboardingScreen
import ir.startup.zabanbaz.composeapp.ui.placement.PlacementScreen
import ir.startup.zabanbaz.composeapp.ui.profile.ProfileScreen
import ir.startup.zabanbaz.composeapp.ui.splash.SplashScreen

@Composable
fun ZabanbazApp() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = AppRoutes.Splash,
            modifier = Modifier.fillMaxSize(),
        ) {
            composable(AppRoutes.Splash) {
                SplashScreen(
                    onNavigateToLogin = {
                        navController.navigate(AppRoutes.Login) {
                            popUpTo(AppRoutes.Splash) { inclusive = true }
                        }
                    },
                    onNavigateToOnboarding = {
                        navController.navigate(AppRoutes.Onboarding) {
                            popUpTo(AppRoutes.Splash) { inclusive = true }
                        }
                    },
                    onNavigateToPlacement = {
                        navController.navigate(AppRoutes.Placement) {
                            popUpTo(AppRoutes.Splash) { inclusive = true }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(AppRoutes.Home) {
                            popUpTo(AppRoutes.Splash) { inclusive = true }
                        }
                    },
                    snackbarHostState = snackbarHostState,
                )
            }
            composable(AppRoutes.Login) {
                AppScreenPadding(padding) {
                    LoginScreen(
                        onAuthenticated = {
                            navController.navigate(AppRoutes.Splash) {
                                popUpTo(AppRoutes.Login) { inclusive = true }
                            }
                        },
                        snackbarHostState = snackbarHostState,
                    )
                }
            }
            composable(AppRoutes.Onboarding) {
                AppScreenPadding(padding) {
                    OnboardingScreen(
                        onNavigateToPlacement = {
                            navController.navigate(AppRoutes.Placement) {
                                popUpTo(AppRoutes.Onboarding) { inclusive = true }
                            }
                        },
                        onNavigateToHome = {
                            navController.navigate(AppRoutes.Home) {
                                popUpTo(AppRoutes.Onboarding) { inclusive = true }
                            }
                        },
                        snackbarHostState = snackbarHostState,
                    )
                }
            }
            composable(AppRoutes.Placement) {
                AppScreenPadding(padding) {
                    PlacementScreen(
                        onComplete = {
                            navController.navigate(AppRoutes.Home) {
                                popUpTo(AppRoutes.Placement) { inclusive = true }
                            }
                        },
                        snackbarHostState = snackbarHostState,
                    )
                }
            }
            composable(AppRoutes.Home) {
                AppScreenPadding(padding) {
                    HomeScreen(
                        onNavigateToProfile = { navController.navigate(AppRoutes.Profile) },
                        onLoggedOut = {
                            navController.navigate(AppRoutes.Login) {
                                popUpTo(AppRoutes.Home) { inclusive = true }
                            }
                        },
                        snackbarHostState = snackbarHostState,
                    )
                }
            }
            composable(AppRoutes.Profile) {
                AppScreenPadding(padding) {
                    ProfileScreen(
                        onBack = { navController.popBackStack() },
                        snackbarHostState = snackbarHostState,
                    )
                }
            }
        }
    }
}
