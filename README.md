# 🐛 BugTrack Pro - Advanced Bug Tracking System

A robust, role-based bug tracking system built with Java Swing, featuring comprehensive bug management, user authentication, and real-time tracking capabilities.
![image](https://github.com/user-attachments/assets/3c07171f-70fd-4b34-94ef-760338fa2f55)

## 🌟 Features

### 🔐 Multi-Role Authentication
- **Admin**: Full system control and user management
- **Project Manager**: Project oversight and bug statistics
- **Developer**: Bug assignment and resolution
- **Tester**: Bug reporting and verification
- **User**: Basic system access

### 🐞 Bug Management
- Create and track bugs with detailed information
- Assign bugs to developers
- Track bug status and progress
- Filter bugs by project and status
- Real-time bug statistics

### 📊 Dashboard Features
- Role-specific dashboards
- Real-time statistics
- Project-wise bug tracking
- Developer workload monitoring

## 🛠️ Technical Stack

- **Language**: Java
- **UI Framework**: Java Swing
- **Data Storage**: JSON
- **Build Tool**: Maven
- **Dependencies**:
  - JSON Simple
  - JUnit (for testing)

## 🚀 Getting Started

### Prerequisites
- Java JDK 11 or higher
- Maven

### Installation

1. Clone the repository
```bash
git clone https://github.com/ahmydyasser/bugtrack-pro.git
```

2. Navigate to project directory
```bash
cd bugtrack-pro
```

3. Build the project  because i am a **nVIMan** "Arch+Hyprland+nVim"
```bash
./build.sh
```

4. Run the application
```bash
java -jar target/bugtrack-pro.jar
```

## 📝 Usage

1. **Login/Register**
   - Use the login screen to access the system
   - New users can register with appropriate roles

2. **Bug Management**
   - Create new bugs with detailed information
   - Assign bugs to developers
   - Track and update bug status

3. **Dashboard**
   - Access role-specific features
   - View statistics and reports
   - Manage projects and users

## 🔒 Security Features

- Password encryption
- Role-based access control
- Input validation
- Session management

## 🧪 Testing

The system includes comprehensive validation for:
- User authentication
- Bug creation and assignment
- Data integrity
- Input validation
- Edge cases

## 📊 Project Structure

```
bugtrack-pro/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── gui/           # GUI components
│   │       ├── models/        # Data models
│   │       └── utils/         # Utility classes
├── lib/                       # Dependencies
├── data.json                  # Bug data storage
├── users.json                 # User data storage
└── pom.xml                    # Maven configuration
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- Ahmyd - Initial work

## 🙏 Acknowledgments

- Java Swing community
- JSON Simple library
- All contributors and users

## 📞 Support

For support, please open an issue in the GitHub repository or contact the maintainers.

---

⭐ Star this repository if you find it useful! 

---
You really think i used AI on ReadME ?
> I probably did  > _ <
