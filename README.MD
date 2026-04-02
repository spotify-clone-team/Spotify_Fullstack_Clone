# 🎧 Spotify Fullstack Clone

Dự án clone Spotify phục vụ mục đích học tập và thực hành fullstack với Node.js và MongoDB.

---

## 🚀 Tính năng

* Đăng ký / đăng nhập
* Xem danh sách bài hát
* Tìm kiếm nhạc
* Phát nhạc
* Quản lý playlist
* Favorite bài hát

---

## 🛠 Công nghệ sử dụng

### Backend

* Node.js
* Express.js
* MongoDB (Mongoose)
* dotenv

### Client

* Android (Java/Kotlin)

---

## 📦 Cài đặt project

### 1. Clone repo

```bash
git clone https://github.com/spotify-clone-team/Spotify_Fullstack_Clone.git
cd Spotify_Fullstack_Clone
```

---

## ⚙️ Backend setup

### 2. Cài thư viện

```bash
cd backend
npm install
```

---

### 3. Cài các package (nếu thiếu)

```bash
npm install express mongoose dotenv cors jsonwebtoken bcryptjs
```

---

### 4. Tạo file `.env`
tạo file .env sao chép file example.env
```

---

### 5. Chạy MongoDB


---

### 6. Chạy server

```bash
npm run dev
```

👉 Server chạy tại xong vào android run là oke

``

## ❗ Lỗi thường gặp

### Không connect MongoDB

* Kiểm tra MongoDB đã chạy chưa
* Kiểm tra MONGO_URI

### Android không gọi được API

* Emulator → dùng `10.0.2.2`
* Máy thật → dùng IP LAN

---

## 👥 Quy tắc làm việc

```bash
git checkout -b feature/login
git commit -m "feat: login"
git push origin feature/login
```

👉 Tạo Pull Request, không push trực tiếp main

---

## 📌 Ghi chú

* Dự án phục vụ học tập
* Có thể còn bug

---

## 👨‍💻 Tác giả

Nguyễn Khả Dương
