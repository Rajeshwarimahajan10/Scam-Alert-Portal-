# Scam-Alert-Portal-

A full-stack web application designed to help users report, track, and verify online and offline scam activities. The platform enables citizens to submit scam complaints, browse scam history, search for scam-related data, and stay aware of ongoing fraud cases.

## Features

- 🔐 **User Authentication**: Secure login and registration with role-based access (USER/ADMIN)
- 📝 **Scam Reporting**: Submit detailed scam reports with supporting information
- 🔍 **Search Functionality**: Search scams by phone number, email, website, or keyword
- 📋 **Report Management**: View all reports, filter by status, and track your submissions
- ✅ **Status Tracking**: Reports can be PENDING, VERIFIED, REJECTED, or RESOLVED
- 🎨 **Modern UI**: Responsive design with Bootstrap and React

## Technologies Used

### Backend
- **Core Java** & **Advanced Java** (JDBC/Servlet concepts)
- **Spring Boot** (REST API development)
- **MySQL** Database
- **JWT** for authentication
- **BCrypt** for password hashing

### Frontend
- **React.js** (v18.2.0)
- **React Router** for navigation
- **Bootstrap 5** & **React Bootstrap** for styling
- **Axios** for API calls

## Project Structure

```
Scam Alert Portal/
├── backend/                    # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/scamalert/
│   │   │   │   ├── config/     # Security configuration
│   │   │   │   ├── controller/ # REST controllers
│   │   │   │   ├── dao/        # Data Access Objects (JDBC)
│   │   │   │   ├── dto/        # Data Transfer Objects
│   │   │   │   ├── model/      # Entity models
│   │   │   │   ├── service/    # Business logic
│   │   │   │   └── util/       # Utility classes (JWT)
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
├── frontend/                   # React frontend
│   ├── public/
│   ├── src/
│   │   ├── components/         # React components
│   │   ├── context/            # React context (Auth)
│   │   ├── App.js
│   │   └── index.js
│   └── package.json
└── database/
    └── schema.sql              # Database schema
```

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java JDK 11** or higher
- **Maven 3.6+**
- **Node.js 16+** and **npm**
- **MySQL 8.0+**

## Setup Instructions

### 1. Database Setup

1. Start your MySQL server
2. Open MySQL command line or MySQL Workbench
3. Run the SQL script to create the database and tables:

```bash
mysql -u root -p < database/schema.sql
```

Or manually execute the contents of `database/schema.sql` in your MySQL client.

**Default Admin Credentials:**
- Username: `admin`
- Password: `admin123`

**Note:** In production, change the default admin password immediately!

### 2. Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Update database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

3. Build and run the Spring Boot application:
```bash
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 3. Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the React development server:
```bash
npm start
```

The frontend will start on `http://localhost:3000`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login user

### Reports
- `GET /api/reports` - Get all reports
- `GET /api/reports/{reportId}` - Get report by ID
- `GET /api/reports/user/{userId}` - Get user's reports
- `POST /api/reports` - Create a new report (requires authentication)
- `GET /api/reports/search/phone?phone={number}` - Search by phone
- `GET /api/reports/search/email?email={email}` - Search by email
- `GET /api/reports/search/website?website={url}` - Search by website
- `GET /api/reports/search/keyword?keyword={keyword}` - Search by keyword
- `GET /api/reports/status/{status}` - Get reports by status
- `PUT /api/reports/{reportId}/status` - Update report status (Admin only)

## Usage

1. **Register/Login**: Create an account or login with existing credentials
2. **Submit Report**: Fill out the scam report form with details
3. **Search**: Use the search feature to check if a contact has been reported
4. **Browse Reports**: View all reported scams and filter by status
5. **Track Reports**: View your submitted reports and their status

## Default Users

The database schema includes two default users:

1. **Admin User**
   - Username: `admin`
   - Password: `admin123`
   - Role: ADMIN

2. **Test User**
   - Username: `testuser`
   - Password: `user123`
   - Role: USER

**Important:** Change these passwords in production!

## Development Notes

- The backend uses JDBC for database operations (no JPA/Hibernate)
- JWT tokens are used for authentication
- CORS is configured to allow requests from `http://localhost:3000`
- File upload functionality is prepared but not fully implemented in the frontend

## Troubleshooting

### Backend Issues
- Ensure MySQL is running and accessible
- Check database credentials in `application.properties`
- Verify port 8080 is not in use

### Frontend Issues
- Clear browser cache if you see stale data
- Check that the backend is running on port 8080
- Verify CORS settings if API calls fail

### Database Issues
- Ensure MySQL server is running
- Verify database `scam_alert_db` exists
- Check user permissions for the database

ortfolio project demonstrating full-stack development skills.

