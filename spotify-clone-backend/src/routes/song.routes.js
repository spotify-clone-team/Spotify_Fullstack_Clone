const express = require("express");
const authMiddleware = require("../middleware/auth.middleware");
const requireAdmin = require("../middleware/requireAdmin.middleware");
const {
  getAllSongs,
  createSong,
  updateSong,
  deleteSong,
  uploadCoverFile,
  uploadAudioFile
} = require("../controllers/song.controller");
const {
  uploadCover,
  uploadAudio
} = require("../middleware/upload.middleware");

const router = express.Router();

router.get("/", getAllSongs);

router.post("/", authMiddleware, requireAdmin, createSong);
router.put("/:id", authMiddleware, requireAdmin, updateSong);
router.delete("/:id", authMiddleware, requireAdmin, deleteSong);

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

module.exports = router;