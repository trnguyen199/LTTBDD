# Walkthrough - UTH Todo V2 (Navigation + OOP)

Đã hoàn thành bài tập **BÀI TẬP VỀ NHÀ 01: Thực Hành Navigation + OOP - UTH Todo V2**.

## Các thành phần đã triển khai:
1. **Kiến trúc OOP (Object-Oriented Programming)**:
   - [Models.kt](file:///E:/LTTBDD/Tu%E1%BA%A7n%204/app/src/main/java/com/example/myapplication/model/Models.kt): Định nghĩa các lớp dữ liệu (`Task`, `OnboardingPage`) và Enum phân loại (`TaskCategory`: Học tập, Cuộc sống).
   - [TaskManager.kt](file:///E:/LTTBDD/Tu%E1%BA%A7n%204/app/src/main/java/com/example/myapplication/model/TaskManager.kt): Lớp quản lý logic nghiệp vụ (thêm công việc, đánh dấu hoàn thành, xoá, lọc theo nhóm).

2. **Hệ thống Navigation (Jetpack Navigation Compose)**:
   - [NavGraph.kt](file:///E:/LTTBDD/Tu%E1%BA%A7n%204/app/src/main/java/com/example/myapplication/navigation/NavGraph.kt): Định nghĩa luồng điều hướng: `StartScreen` -> `OnboardingScreen` (3 bước) -> `MainScreen`.

3. **Giao diện người dùng (Jetpack Compose & Material 3)**:
   - [StartScreen.kt](file:///E:/LTTBDD/Tu%E1%BA%A7n%204/app/src/main/java/com/example/myapplication/ui/screens/StartScreen.kt): Màn hình khởi đầu với nền xanh dương, logo UTH Todo và thông tin phiên bản `2.1.0`.
   - [OnboardingScreen.kt](file:///E:/LTTBDD/Tu%E1%BA%A7n%204/app/src/main/java/com/example/myapplication/ui/screens/OnboardingScreen.kt): Gồm 3 bước onboarding (1a, 1b, 1c) với thanh top bar ("UTH Todo", "Bỏ qua"), hình minh họa, thanh chỉ báo trang (dots) và nút tiếp tục/bắt đầu ngay.
   - [MainScreen.kt](file:///E:/LTTBDD/Tu%E1%BA%A7n%204/app/src/main/java/com/example/myapplication/ui/screens/MainScreen.kt): Màn hình chính quản lý công việc theo nhóm Học tập / Cuộc sống, thêm mới công việc và checkbox hoàn thành.

## Kiểm tra build:
- Dự án đã biên dịch thành công (`app:assembleDebug` -> **BUILD SUCCESSFUL**).
