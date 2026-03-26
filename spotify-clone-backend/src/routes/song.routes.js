const express = require("express");
const authMiddleware = require("../middleware/auth.middleware");
const requireAdmin = require("../middleware/requireAdmin.middleware");

const {
  getAllSongs,
  getAllArtists,   // Mới thêm nè
  getAllAlbums,    // Mới thêm nè
  getAllPlaylists, // Mới thêm nè
  createSong,
  updateSong,
  deleteSong,
  uploadCoverFile,
  uploadAudioFile
} = require("../controllers/song.controller");

// Cấu hình Cloudinary bồ đã làm ở Bước 1
const { uploadCover, uploadAudio } = require("../config/cloudinary");

const router = express.Router();

// --- 1. ROUTES LẤY DỮ LIỆU (CÔNG KHAI) ---
router.get("/", getAllSongs);
router.get("/artists", getAllArtists);     // API: /api/songs/artists
router.get("/albums", getAllAlbums);       // API: /api/songs/albums
router.get("/playlists", getAllPlaylists); // API: /api/songs/playlists

// --- 2. ROUTES QUẢN LÝ BÀI HÁT (YÊU CẦU ADMIN) ---
router.post("/", authMiddleware, requireAdmin, createSong);
router.put("/:id", authMiddleware, requireAdmin, updateSong);
router.delete("/:id", authMiddleware, requireAdmin, deleteSong);

// --- 3. ROUTES UPLOAD LÊN CLOUDINARY ---
router.post(
  "/upload-cover",
  authMiddleware,
  requireAdmin,
  uploadCover.single("cover"),
  uploadCoverFile
);

router.post(
  "/upload-audio",
  authMiddleware,
  requireAdmin,
  uploadAudio.single("audio"),
  uploadAudioFile
);

// CỰC KỲ QUAN TRỌNG: Giữ lại để app không sập
module.exports = router;