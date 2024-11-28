package com.example.learnconnect.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.learnconnect.data.model.Course
import com.example.learnconnect.data.model.User
import com.example.learnconnect.data.model.UserCourse
import com.example.learnconnect.data.model.VideoProgressEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, Course::class, UserCourse::class, VideoProgressEntity::class], // VideoProgressEntity eklendi
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun courseDao(): CourseDao
    abstract fun userCourseDao(): UserCourseDao
    abstract fun videoProgressDao(): VideoProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "learn_connect_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    println("AppDatabase Kurs ekleme başlatılıyor...")
                                    getInstance(context).courseDao().insertCourses(
                                        listOf(
                                            Course(
                                                name = "Mobil Uygulama Geliştirme",
                                                description = "Mobil cihazlar için uygulama geliştirme temellerini öğrenin ve pratik projelerle deneyim kazanın.",
                                                instructorName = "Ahmet Yılmaz"
                                            ),
                                            Course(
                                                name = "Web Geliştirme Temelleri",
                                                description = "Modern web teknolojileri ile etkileyici web siteleri oluşturmayı öğrenin.",
                                                instructorName = "Elif Kaya"
                                            ),
                                            Course(
                                                name = "Veri Bilimi ve Analitik",
                                                description = "Büyük veri analizi ve veri görselleştirme teknikleriyle karar verme süreçlerinizi güçlendirin.",
                                                instructorName = "Mehmet Çelik"
                                            ),
                                            Course(
                                                name = "Python ile Programlama",
                                                description = "Python ile temel programlama becerileri kazanın ve projelerinizi geliştirin.",
                                                instructorName = "Fatma Demir"
                                            ),
                                            Course(
                                                name = "Java ile Yazılım Geliştirme",
                                                description = "Java dilinin gücünü kullanarak yazılım geliştirme süreçlerine hakim olun.",
                                                instructorName = "Ali Veli"
                                            ),
                                            Course(
                                                name = "Veri Tabanı Yönetimi",
                                                description = "Veritabanı tasarımı ve yönetimi konularında uzmanlaşarak güçlü veri çözümleri üretin.",
                                                instructorName = "Hakan Şahin"
                                            )
                                        )
                                    )
                                    println("AppDatabase Kurslar başarıyla eklendi.")
                                } catch (e: Exception) {
                                    println("AppDatabase Kurs eklenirken hata oluştu: ${e.message}")
                                }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

