<img width="199" height="199" alt="image" src="https://github.com/user-attachments/assets/03d28c9a-8c4e-4ae1-a4b5-db76794d3ae1" />

# MysLog — Your Personal Gym Log 

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white)

**MysLog** is a free, open-source Android application designed for tracking your gym workouts. Built with modern Android development tools, it focuses on simplicity, privacy, and offline functionality.

---

## ✨ Features

- **📱 Modern UI** – Built with Jetpack Compose & Material Design 3 for a clean, dynamic interface.
- **🏋️ Comprehensive Exercise Database** – Access over 870 exercises with filters by muscle, equipment, and custom routines. 
- **📊 Full Workout Tracking** – Log exercises, sets, weight, and repetitions with detailed session management.
- **📈 Progress Visualization** – View charts and statistics for each exercise to track your improvements.
- **🔒 100% Offline & Private** – All data is stored locally on your device. No accounts, no ads, no subscriptions.
- **⏱️ Built-in Rest Timer** – Customizable timer with notifications to manage your rest periods.
- **🌍 Bilingual Support** – Interface available in English and Spanish (exercise database currently in English).
>[!Note]
>The exercise database is based on the Free Exercise DB project by yuhonas: https://github.com/yuhonas/free-exercise-db/tree/main
---
<img width="318" height="709" alt="image" src="https://github.com/user-attachments/assets/27ec1462-82f3-47a0-ba11-a75c73d30839" />
<img width="318" height="709" alt="image" src="https://github.com/user-attachments/assets/782cdab8-81f8-4325-9b7f-2e95068ad162" />
<img width="318" height="709" alt="image" src="https://github.com/user-attachments/assets/63b4f99f-d761-4bd6-acac-76e636f4661a" />
<img width="318" height="709" alt="image" src="https://github.com/user-attachments/assets/44f87059-5e14-4173-92d4-1f9fef9bb4aa" />
<img width="289" height="650" alt="image" src="https://github.com/user-attachments/assets/0d160bbc-c902-4579-ab13-a94e1ed3c7e9" />
<img width="372" height="827" alt="image" src="https://github.com/user-attachments/assets/5f38831d-f80c-41d6-9383-fa44dafd6af4" />



## 🚀 Getting Started

### Download
The latest APK can be downloaded from the [Releases](https://github.com/musigatto/MysLog/releases) section.

### Build from Source
1. Clone the repository:
   ```bash
   git clone https://github.com/musigatto/MysLog.git
   ```
2. Open the project in Android Studio (Flamingo or newer recommended).
3. Build and run the app on an emulator or physical device.

---

## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI Toolkit:** Jetpack Compose
- **Architecture:** MVVM + Repository Pattern
- **Local Database:** Room
- **Dependency Injection:** Hilt
- **Asynchronous:** Kotlin Coroutines & Flow
- **Design System:** Material Design 3 (Dynamic Color)

---

## 📁 Project Structure
```text
MysLog/
├── app/
│   ├── src/main/java/com/myslog/
│   │   ├── core/          # Constants, navigation routes
│   │   ├── db/            # Database entities, DAO, Repository
│   │   ├── di/            # Dependency injection modules
│   │   ├── ui/            # Composable screens & ViewModels
│   │   └── utils/         # Extensions, converters, services
│   └── src/main/res/      # Resources (strings, themes, assets)
```
---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!
If you find a bug or have an idea for improvement, please:
1. Check the [Issues](https://github.com/musigatto/MysLog/issues) to see if it's already reported.
2. Open a new issue or submit a pull request.

### To contribute:
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

---

## 📄 License

This project is licensed under the **MIT License** – see the [LICENSE](LICENSE) file for details.

---

## 📬 Contact

Project Link: [https://github.com/musigatto/MysLog](https://github.com/musigatto/MysLog)

---

> Made with ❤️ for the fitness community. If you find this app useful, consider giving it a ⭐ on GitHub!
