        package com.example.learnconnect.ui.navigation

        import android.annotation.SuppressLint
        import android.net.Uri
        import android.os.Build
        import androidx.annotation.RequiresApi
        import androidx.compose.runtime.*
        import androidx.compose.ui.platform.LocalContext
        import androidx.lifecycle.viewmodel.compose.viewModel
        import androidx.navigation.NavHostController
        import androidx.navigation.compose.NavHost
        import androidx.navigation.compose.composable
        import com.example.learnconnect.data.AppDatabase
        import com.example.learnconnect.data.CourseDao
        import com.example.learnconnect.data.model.Course
        import com.example.learnconnect.data.model.UserCourse
        import com.example.learnconnect.data.model.VideoProgressEntity
        import com.example.learnconnect.ui.theme.AppTheme
        import com.example.learnconnect.ui.theme.LocalThemeState
        import com.example.learnconnect.ui.theme.ThemeProvider
        import com.example.learnconnect.ui.view.*
        import com.example.learnconnect.ui.viewmodel.HomeViewModel
        import com.example.learnconnect.ui.viewmodel.HomeViewModelFactory
        import com.example.learnconnect.ui.viewmodel.PixabayViewModel
        import com.example.learnconnect.ui.viewmodel.RegisterViewModel
        import com.example.learnconnect.utils.PreferencesHelper
        import kotlinx.coroutines.CoroutineScope
        import kotlinx.coroutines.Dispatchers
        import kotlinx.coroutines.launch

        @SuppressLint("UnrememberedMutableState")
        @RequiresApi(Build.VERSION_CODES.O)
        @Composable
        fun NavGraph(
            navController: NavHostController,
            registerViewModel: RegisterViewModel,
            preferencesHelper: PreferencesHelper,
            pixabayViewModel: PixabayViewModel,
            homeViewModel: HomeViewModel
        ) {
            // Token ve kullanıcı bilgileri
            val userToken by remember { mutableStateOf(preferencesHelper.getToken()) }
            val isLoggedIn by derivedStateOf { userToken != null } // Token varsa oturum açık
            val loggedInEmail by derivedStateOf { preferencesHelper.getUser()?.first.orEmpty() }
            val context = LocalContext.current // Compose içinde context'e erişim
            val savedUser = preferencesHelper.getUser()
            println("Saved User: $savedUser") // Kayıtlı kullanıcıyı kontrol edin
            val isDarkModeState =
                remember { mutableStateOf(preferencesHelper.getDarkModePreference()) }
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(
                    courseDao = AppDatabase.getInstance(LocalContext.current).courseDao()
                )
            )

            AppTheme(isDarkMode = isDarkModeState.value) {
                ThemeProvider(isDarkMode = isDarkModeState) {

                    NavHost(
                        navController = navController,
                        startDestination = if (preferencesHelper.getToken() != null) "home" else "login"
                    ) {

                        // Login Screen
                        composable("login") {
                            LoginScreen(
                                onLoginClicked = { email, password ->
                                    registerViewModel.loginUser(email, password) { isSuccess ->
                                        if (isSuccess) {
                                            // Yeni bir token oluştur ve kaydet
                                            val token = java.util.UUID.randomUUID().toString()
                                            preferencesHelper.saveToken(token)

                                            // Kullanıcı bilgilerini kaydet
                                            preferencesHelper.saveUser(email, password)

                                            // Home ekranına yönlendir
                                            navController.navigate("home") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        } else {
                                            println("Invalid email or password")
                                        }
                                    }
                                },
                                onNavigateToRegister = { navController.navigate("register") }
                            )
                        }

                        // Register Screen
                        composable("register") {
                            RegisterScreen(
                                onRegisterClicked = { email, password ->
                                    registerViewModel.registerUser(email, password) { isSuccess ->
                                        if (isSuccess) {
                                            navController.navigate("login") {
                                                popUpTo("register") { inclusive = true }
                                            }
                                        } else {
                                            println("User already exists!")
                                        }
                                    }
                                },
                                onNavigateToLogin = { navController.navigate("login") }
                            )
                        }

                        // Home Screen
                        composable("home") {
                            val homeViewModel: HomeViewModel = viewModel(
                                factory = HomeViewModelFactory(
                                    courseDao = AppDatabase.getInstance(
                                        LocalContext.current
                                    ).courseDao()
                                )
                            )

                            HomeScreenWrapper(
                                currentScreen = "home",
                                email = loggedInEmail, // Dinamik olarak güncellenen email
                                onNavigate = { selectedScreen ->
                                    if (selectedScreen == "logout") {
                                        preferencesHelper.clearToken()
                                        preferencesHelper.clearUser()
                                        navController.navigate("login") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate(selectedScreen)
                                    }
                                },
                                preferencesHelper = preferencesHelper,
                                isDarkMode = LocalThemeState.current,
                                homeViewModel = homeViewModel
                            )
                        }

                        // Video List Screen
                        composable("video_list/{courseName}") { backStackEntry ->
                            val courseName = backStackEntry.arguments?.getString("courseName") ?: ""
                            val courseDao = AppDatabase.getInstance(context).courseDao()
                            val userCourseDao = AppDatabase.getInstance(context).userCourseDao()
                            val userDao = AppDatabase.getInstance(context).userDao()
                            val loggedInUserEmail =
                                preferencesHelper.getUser()?.first.orEmpty() // Kullanıcı email'ini al

                            val course = remember { mutableStateOf<Course?>(null) }
                            val isRegistered = remember { mutableStateOf(false) }
                            val coroutineScope = rememberCoroutineScope() // CoroutineScope oluştur

                            // Kurs bilgisi ve kayıt durumunu kontrol et
                            LaunchedEffect(courseName) {
                                val user = userDao.getUserByEmail(loggedInUserEmail)
                                println("Fetched User: $user") // Kullanıcı bilgilerini burada yazdır
                                val loggedInUserId = user?.id ?: 0 // Kullanıcı ID'si

                                // ID kontrolü
                                if (loggedInUserId == 0) {
                                    println("Error: Logged-in user not found in database.")
                                    return@LaunchedEffect
                                }

                                course.value = courseDao.getCourseByName(courseName)
                                course.value?.id?.let { courseId ->
                                    isRegistered.value = userCourseDao.isUserRegisteredToCourse(
                                        loggedInUserId,
                                        courseId
                                    )
                                }
                            }

                            course.value?.let { courseDetails ->
                                VideoListScreen(
                                    courseName = courseDetails.name,
                                    courseDescription = courseDetails.description,
                                    instructorName = courseDetails.instructorName,
                                    pixabayViewModel = pixabayViewModel,
                                    isRegistered = isRegistered.value,
                                    preferencesHelper = preferencesHelper,
                                    onRegisterCourse = {
                                        coroutineScope.launch {
                                            // Kullanıcı ve kurs ID'lerini alın
                                            val user = userDao.getUserByEmail(loggedInUserEmail)
                                            val userId = user?.id ?: 0 // Kullanıcı ID'si
                                            val courseId = courseDetails.id ?: -1 // Kurs ID'si

                                            // Loglama
                                            println("Registering userId: $userId to courseId: $courseId")

                                            // ID'leri kontrol et
                                            if (userId == 0 || courseId == -1) {
                                                println("Invalid userId or courseId. Please check the database.")
                                            } else {
                                                try {
                                                    // Veritabanına kayıt
                                                    userCourseDao.registerUserToCourse(
                                                        UserCourse(
                                                            userId = userId,
                                                            courseId = courseId
                                                        )
                                                    )
                                                    isRegistered.value = true
                                                    println("User successfully registered to the course.")
                                                } catch (e: Exception) {
                                                    println("Error registering user to course: ${e.message}")
                                                }
                                            }
                                        }
                                    },
                                    onNavigateToVideo = { videoUrl, progress ->
                                        val videos =
                                            pixabayViewModel.videos.value // Tüm videoları alın
                                        val limitedVideos = videos.take(10) // İlk 10 videoyu alın
                                        val courseId = courseDetails.id ?: -1 // Mevcut kurs ID'si
                                        val videoIndex =
                                            limitedVideos.indexOf(videoUrl) // Video'nun sırasını alın
                                        if (courseId != -1 && videoIndex != -1) {
                                            navController.navigate(
                                                "video_player/${
                                                    Uri.encode(
                                                        videoUrl
                                                    )
                                                }/$progress/$courseId/$videoIndex"
                                            )
                                        } else {
                                            println("Hata: Geçersiz courseId veya videoIndex")
                                        }
                                    },
                                    onBack = { navController.navigateUp() }, // Geri dönüş için ekleme
                                    currentScreen = "video_list", // Alt bar için geçerli ekran
                                    onNavigate = { selectedScreen ->
                                        navController.navigate(selectedScreen) // Alt bar için navigasyon
                                    }
                                )
                            }
                        }

                        // Profile Screen
                        composable("profile") {
                            val userDao = AppDatabase.getInstance(context).userDao()
                            val userCourseDao = AppDatabase.getInstance(context).userCourseDao()
                            val favoriteCourseDao = AppDatabase.getInstance(context).favoriteCourseDao() // Favori kurslar için DAO

                            val registeredCourses = remember { mutableStateOf<List<Course>>(emptyList()) }
                            val favoriteCourses = remember { mutableStateOf<List<Course>>(emptyList()) } // Favori kurslar için liste

                            // Kullanıcı e-postasını al
                            val loggedInUserEmail = preferencesHelper.getUser()?.first.orEmpty()

                            LaunchedEffect(loggedInUserEmail) {
                                val user = userDao.getUserByEmail(loggedInUserEmail)
                                val userId = user?.id ?: 0
                                if (userId != 0) {
                                    // Kayıtlı kursları al
                                    registeredCourses.value = userCourseDao.getCoursesForUser(userId)

                                    // Favori kursları al
                                    favoriteCourses.value = favoriteCourseDao.getFavoriteCoursesForUser(userId)
                                } else {
                                    println("Kullanıcı bulunamadı veya ID geçersiz")
                                }
                            }

                            ProfileScreen(
                                currentScreen = "profile",
                                email = loggedInUserEmail,
                                registeredCourses = registeredCourses.value,
                                favoriteCourses = favoriteCourses.value, // Favori kursları ekledik
                                preferencesHelper = preferencesHelper,
                                initialDarkMode = isDarkModeState.value, // Dark Mode
                                onToggleTheme = { newDarkMode ->
                                    isDarkModeState.value = newDarkMode
                                    preferencesHelper.saveDarkModePreference(newDarkMode)
                                },
                                onNavigate = { selectedScreen ->
                                    if (selectedScreen == "logout") {
                                        preferencesHelper.clearToken()
                                        preferencesHelper.clearUser()
                                        navController.navigate("login") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate(selectedScreen)
                                    }
                                }
                            )
                        }



                        // Search Screen
                        composable("search") {
                            val courses = homeViewModel.courses.collectAsState().value // Tüm kursları al
                            SearchScreen(
                                currentScreen = "search",
                                courses = courses, // Kursları parametre olarak ver
                                onNavigate = { selectedScreen ->
                                    navController.navigate(selectedScreen)
                                },
                                isDarkMode = isDarkModeState.value,
                                homeViewModel = homeViewModel
                            )
                        }

                    }
                }
            }
        }
