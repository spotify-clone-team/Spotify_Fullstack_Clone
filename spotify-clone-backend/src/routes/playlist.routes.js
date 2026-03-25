const express = require("express");
const authMiddleware = require("../middleware/auth.middleware");
const requireAdmin = require("../middleware/requireAdmin.middleware");
const {
  getAllPlaylists,
  createPlaylist,
  updatePlaylist,
  deletePlaylist
} = require("../controllers/playlist.controller");

const router = express.Router();

router.get("/", getAllPlaylists);
router.post("/", authMiddleware, requireAdmin, createPlaylist);
router.put("/:id", authMiddleware, requireAdmin, updatePlaylist);
router.delete("/:id", authMiddleware, requireAdmin, deletePlaylist);

module.exports = router;