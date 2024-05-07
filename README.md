
![title](https://github.com/vinsgiordi/LSO-Project/assets/75014356/0818ff81-a12e-4947-98b8-95ade6676e86)

# LSO-Project
Design a client-server application for adaptive content viewing.

# MovieHub
The project consists of a management system for a cinematic catalog platform, aimed at providing accessible navigation for everyone. Users can access the platform through a mobile application or a tablet, which offers an adaptive interface to ensure the best possible experience. We have implemented an advanced technological system that automatically adjusts the color range and object size to support those, for example, suffering from color blindness.
The catalog of movies and TV series is provided by The Movie DB, a reliable and comprehensive source of multimedia content. Users can search for their favorite content using a search bar or by utilizing predefined customized lists, such as "recommended," which offer a curated selection of content based on users' interests and preferences.

# Table of Contents
- Prerequisites
- Installation
- Run
- Project Structure
- Contributors
- Contributions and Feedback

# Prerequisites
Make sure you have the following installed on your system:
- Android Studio (recommended version: Iguana | 2023.2.1);
- Android SDK (version 34);
- JDK (Java Development Kit);
- WSL (recommended Ubuntu 22.04);
- Visual Studio Code;
- PostgreSQL (version 16);

# Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/vinsgiordi/LSO-Project.git
    ```
2. Open the project in Android Studio:
    - Launch Android Studio.
    - Select "Open an existing Android Studio project."
    - Navigate to the folder where you cloned the repository and select the `build.gradle` file in the `app` folder.
3. Set up the virtual device or connect a physical device.

# Run
1. Launch the application from Android Studio.
2. Select the virtual or physical device on which you want to run the application.
3. Click on "Run" in Android Studio.

# Project Structure
- `app`: Contains the Android app-specific code.
  - `src`: App source code.
    - `main`: Main app code.
      - `java`: Java source code.
        - `com.example.moviehub`: Main app package.
          - `View`: Main activities and UI components.
            - `MainActivity.java`: Main activity of the app.
          - `Adapters`: Adapters for RecyclerView or other UI elements.
          - `Model`: Project classes (User, Movie, ApiResponse).
      - `res`: App resources (layouts, drawables, values, etc.).
- `gradle`: Gradle configurations and scripts.
- `build`: Contains files and folders generated during compilation.
- `docs`: Project documentation.
- `config`: App-specific configurations.
- `README.md`: Main project documentation.

# Contributors
- **Antonio Navarra**: navarra.antonio99@gmail.com
- **Vincenzo Giordano**: vincenzogiordano99@libero.it

# Contributions and Feedback
If you want to contribute or provide feedback, feel free to open a new issue or send a pull request. We are open to improvements and collaborations!
