LearnConnect
LearnConnect modern bir video tabanlı eğitim platformudur. Kullanıcıların kurslara kaydolabildiği, ders videolarını izleyebildiği, ilerlemelerini takip edebildiği ve uygulamayı karanlık modda kullanabildiği bir mobil uygulama olarak geliştirilmiştir. Bu vaka çalışması kapsamında tüm temel gereksinimler yerine getirilmiştir.

Kullanılan Mimari ve Teknolojiler
1. Mimari
MVVM (Model-View-ViewModel): Tüm iş mantığı ve veri yönetimi için bu modern mimari kullanılmıştır.
Room: SQLite tabanlı veritabanı yönetimi için kullanıldı.
Hilt: Dependency Injection (Bağımlılık Enjeksiyonu) sağlamak için.
2. Kullanılan Teknolojiler
Dil: Kotlin
Veritabanı: Room (SQLite üzerinde çalışır)
HTTP İşlemleri: Retrofit
Video Oynatma: ExoPlayer
UI: Jetpack Compose
Bağımlılıklar: Gradle üzerinden yönetildi.
Navigasyon: Jetpack Compose Navigation
3. Kullanılan Kütüphaneler
Room (2.6.1): Veritabanı işlemleri.
Retrofit (2.9.0): API istekleri ve JSON dönüşümü.
ExoPlayer (2.19.0): Video oynatma işlemleri.
Hilt (2.48): Dependency Injection.
Jetpack Compose: Modern UI geliştirme.
4. Güvenlik
Şifre Hashleme:
Kullanıcı şifreleri, güvenli bir şekilde saklanması için BCrypt ile hashlenmektedir.
Şifreler düz metin olarak saklanmaz; doğrulama işlemi hashler üzerinden yapılır.
Token Tabanlı Oturum Yönetimi:
Kullanıcı giriş yaptığında bir token oluşturulur ve local olarak saklanır.
Oturum devam ederken, kullanıcı işlemleri bu token üzerinden doğrulanır.

Projenin Özellikleri
1. Temel Özellikler
Kullanıcı İşlemleri:

Kullanıcı kayıt (email ve şifre ile).
Kullanıcı giriş.
Kullanıcı profil görüntüleme.
Kurs Yönetimi:

Kurs listeleme.
Kullanıcının kurslara kaydolabilmesi.
Video Oynatıcı:

Kullanıcı kaydolduğu kursların ders videolarını izleyebilir.
Video ilerlemesi local olarak (Room) kaydedilir.
Dark Mode Desteği:

Kullanıcı, uygulamanın karanlık modunu aktif edebilir.
2. Eksik Kalan ve Geliştirilecek Özellikler
Offline Video İzleme:

Kullanıcıların videoları indirip offline izleyebilmesi için Room ve ExoPlayer entegre edilmeliydi. Ancak zaman kısıtlaması nedeniyle tamamlanamadı. Uygulama tasarımında indirme düğmesi ve local kayıt için temel altyapı hazır.
Yorum ve Puanlama Sistemi:

Kullanıcıların kurslara yorum bırakabilmesi ve puan verebilmesi için REST API ve Room tabanlı bir yapı planlandı.
AI Destekli Kurs Önerisi:

Kullanıcı davranışlarına göre yeni kurs önerileri sunulması için TensorFlow Lite veya Firebase Machine Learning kullanılarak bir yapı eklenebilirdi.
Push Bildirimleri:

Yeni kurslar veya etkinlikler hakkında kullanıcı bilgilendirilmesi için Firebase Cloud Messaging (FCM) entegrasyonu yapılabilirdi.





[Screen_recording_20241128_151422.webm](https://github.com/user-attachments/assets/c54a08b0-dc47-4c2a-b690-9e993a79755d)

