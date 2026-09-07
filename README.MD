# 🎧 Spotify Fullstack Clone

Dự án mô phỏng Spotify được xây dựng nhằm thực hành phát triển ứng dụng **Full-stack**, kết hợp **Android Client**, **REST API Backend**, **MongoDB** và **Admin Dashboard**.

> Dự án phục vụ mục đích học tập và thực hành phát triển phần mềm.

---

## 🚀 Tính năng chính

### 👤 Người dùng
- Đăng ký tài khoản
- Đăng nhập
- Xem danh sách bài hát
- Tìm kiếm bài hát
- Phát nhạc
- Thêm bài hát yêu thích
- Quản lý playlist

### 🛠️ Admin
- Quản lý bài hát
- Upload nhạc
- Upload ảnh bìa
- Quản lý dữ liệu hệ thống
- Theo dõi thông tin qua Admin Dashboard

---

## 🧩 Kiến trúc hệ thống

```text
Android Client
      │
      │ REST API
      ▼
Node.js + Express.js
      │
      ▼
   MongoDB
      ▲
      │
Admin Dashboard

Dự án được chia thành ba thành phần chính:

Spotify_Fullstack_Clone/
│
├── SpotifyClone/              # Android Client
├── spotify-clone-admin/       # Admin Dashboard
├── spotify-clone-backend/     # Backend REST API
├── .gitignore
├── README.MD
└── package.json
🛠️ Công nghệ sử dụng
📱 Mobile
Android Studio
Java
Kotlin
⚙️ Backend
Node.js
Express.js
REST API
JSON Web Token (JWT)
bcryptjs
CORS
dotenv
🗄️ Database
MongoDB
Mongoose
🖥️ Admin
JavaScript
Node.js ecosystem
Dashboard quản trị
🔧 Công cụ
Git
GitHub
Postman
Visual Studio Code
Android Studio
🔐 Authentication

Hệ thống sử dụng JWT Authentication để xác thực người dùng.

Mật khẩu được mã hóa bằng:

bcryptjs

Thông tin nhạy cảm được lưu bằng biến môi trường:

MONGO_URI=your_mongodb_connection_string
JWT_SECRET=your_jwt_secret_here

File .env không được commit lên repository.

📦 Cài đặt dự án
1. Clone repository
git clone https://github.com/spotify-clone-team/Spotify_Fullstack_Clone.git
cd Spotify_Fullstack_Clone
⚙️ Backend
2. Di chuyển vào backend
cd spotify-clone-backend
3. Cài dependencies
npm install
4. Tạo file .env

Sao chép cấu hình từ:

example.env

Sau đó cập nhật thông tin MongoDB và JWT phù hợp với môi trường của bạn.

5. Chạy server
npm run dev
📱 Android Client

Mở thư mục:

SpotifyClone

bằng Android Studio.

Nếu sử dụng Android Emulator và backend chạy trên máy tính:

10.0.2.2

có thể được sử dụng để truy cập localhost của máy host.

Nếu chạy trên thiết bị Android thật, sử dụng IP LAN của máy tính.

🖥️ Admin Dashboard

Di chuyển vào:

cd spotify-clone-admin

Cài dependencies:

npm install

Sau đó chạy project theo script đã cấu hình trong package.json.

🌿 Git Workflow

Trong quá trình phát triển, có thể sử dụng workflow:

git checkout -b feature/login
git add .
git commit -m "feat: implement login"
git push origin feature/login

Sau đó tạo Pull Request để merge code vào nhánh chính.

📌 Trạng thái dự án
✅ Android Client
✅ Backend API
✅ MongoDB Database
✅ Authentication
✅ Music Management
✅ Admin Dashboard
🔧 Tiếp tục cải thiện UI/UX và một số chức năng
🎯 Mục tiêu của dự án

Thông qua project này, mình thực hành:

Phát triển ứng dụng Android
Thiết kế REST API
Kết nối Mobile App với Backend
Authentication với JWT
Làm việc với MongoDB
Quản lý file và media
Xây dựng Admin Dashboard
Git và GitHub workflow
Tổ chức một project Full-stack nhiều thành phần
👨‍💻 Tác giả

Nguyễn Khả Dương

GitHub: @Kad1711
Spotify Fullstack Clone
