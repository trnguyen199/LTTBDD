package com.example.myapplication.model

// OOP Base class for navigation screens
sealed class NavigationScreen(val route: String, val stepName: String) {
    object Start : NavigationScreen("start", "Start")
    object Step1 : NavigationScreen("step1", "Section\n1a. Onboarding - bước 1")
    object Step2 : NavigationScreen("step2", "Section\n1b. Onboarding - bước 2")
    object Step3 : NavigationScreen("step3", "Section\n1c. Onboarding - bước 3")
}

// OOP Data class for onboarding content
data class OnboardingContent(
    val screen: NavigationScreen,
    val title: String,
    val description: String,
    val buttonText: String
) {
    companion object {
        val pages = listOf(
            OnboardingContent(
                screen = NavigationScreen.Step1,
                title = "Mọi việc, gọn trong một chỗ",
                description = "Ghi lại bài tập, việc nhà, lịch hẹn. Không còn quên việc nào.",
                buttonText = "Tiếp tục"
            ),
            OnboardingContent(
                screen = NavigationScreen.Step2,
                title = "Chia theo Học tập và Cuộc sống",
                description = "Mỗi việc có nhóm riêng, nhìn là biết hôm nay cần lo gì.",
                buttonText = "Tiếp tục"
            ),
            OnboardingContent(
                screen = NavigationScreen.Step3,
                title = "Nhắc đúng lúc",
                description = "Bật thông báo để UTH Todo nhắc bạn trước hạn chót.",
                buttonText = "Bắt đầu ngay"
            )
        )
    }
}
