# 🛒 Shop_With_Sahil - Full Stack E-Commerce Web Application

![Java](https://img.shields.io/badge/Java-23-orange)
![Servlet](https://img.shields.io/badge/Servlet-Jakarta-red)
![MySQL](https://img.shields.io/badge/MySQL-8-blue)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5-purple)
![License](https://img.shields.io/badge/License-MIT-green)

A complete Full Stack E-Commerce Web Application built using Java, Servlets, JDBC, MySQL, HTML, CSS, JavaScript and Bootstrap.

This project includes both **Customer** and **Admin** modules with secure authentication, shopping cart, wishlist, order management, email notifications and a modern responsive UI.

---

# ✨ Features

## 👤 Customer

- User Registration
- Secure Login & Logout
- BCrypt Password Encryption
- Session Authentication
- Product Listing
- Product Details
- Category Filtering
- Wishlist
- Shopping Cart
- Checkout
- Place Order
- Order History
- Order Details
- User Profile
- Order Confirmation Email

---

## 👨‍💼 Admin

- Admin Login
- Dashboard
- Product Management (CRUD)
- User Management
- Order Management
- Update Order Status
- Role Based Authorization

---

# 🔐 Security

- BCrypt Password Hashing
- Session Based Authentication
- Authentication Filter
- Authorization Filter
- CORS Filter
- Role Based Access Control

---

# 📧 Email Notifications

Automatic email notification is sent after:

- ✅ Order Placed Successfully

Powered by:

- Jakarta Mail API
- Gmail SMTP
- App Password Authentication

---

# 🛠️ Tech Stack

## Backend

- Java
- Servlets
- JDBC
- MySQL
- Maven
- Jackson
- BCrypt
- Jakarta Mail

## Frontend

- HTML5
- CSS3
- JavaScript (ES6)
- Bootstrap 5

## Database

- MySQL

## Server

- Apache Tomcat 10

---

# 📂 Project Structure

```
ShopVerse
│
├── Backend
│   └── online-shopping-cart
│       ├── Controller
│       ├── DAO
│       ├── DTO
│       ├── Filters
│       ├── Model
│       ├── Service
│       ├── Utility
│       └── Config
│
├── Frontend
│   ├── admin
│   ├── css
│   ├── js
│   ├── data
│   └── pages
│
├── README.md
└── LICENSE
```

---

# 🚀 Installation

## Clone Repository

```bash
git clone https://github.com/Sahil1916/ShopVerse.git
```

---

## Backend Setup

- Import as Maven Project
- Configure MySQL Database
- Update database credentials
- Configure Gmail App Password
- Run on Apache Tomcat 10

---

## Frontend Setup

Open the **Frontend** folder using VS Code.

Run using **Live Server**.

---

# 🗄 Database

Create database

```
shopverse
```

Import the provided SQL file.

Update your database credentials inside:

```
DBconnection.java
```

---

# 📧 Email Configuration

Create

```
EmailConfig.java
```

```java
public class EmailConfig {

    public static final String EMAIL = "your-email@gmail.com";

    public static final String APP_PASSWORD = "your-app-password";

}
```

---

# 📸 Screenshots

## Home Page

![Home](screenshots/Index1.png)
![Home](screenshots/Index2.png)
![Home](screenshots/Index3.png)

---
# Login Pages

![login](screenshots/login.png)
![register](screenshots/register.png)
![admin login](screenshots/admin login.png)


## Product Listing

![Product Listing](screenshots/shop.png)
---

## Cart

![cart](screenshots/Cart.png)


## Checkout

![Checkout](screenshots/Check Out.png)
---

## Orders

![Orders](screenshots/order done.png)
---

## Admin Dashboard

![Admin_Dashboard](screenshots/Admin dashboard.png)
![Admin_Dashboard](screenshots/Product curd.png)
![Admin_Dashboard](screenshots/all orders.png)

# user Mails
![mails](screenshots/mail.png)



---

# 🚀 Future Enhancements

- Razorpay Payment Gateway
- Forgot Password (OTP)
- WhatsApp Notifications
- SMS Notifications
- Product Reviews
- Product Ratings
- Invoice PDF
- Dashboard Analytics

---

# 👨‍💻 Developer

**Sahil Barge**

Java Full Stack Developer

GitHub

https://github.com/Sahil1916

---

# 📄 License

This project is licensed under the MIT License.

---

⭐ If you found this project useful, consider giving it a Star.