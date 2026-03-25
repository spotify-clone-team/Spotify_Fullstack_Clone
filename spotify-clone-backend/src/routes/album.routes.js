const express = require("express");
const authMiddleware = require("../middleware/auth.middleware");
const requireAdmin = require("../middleware/requireAdmin.middleware");
const {
  getAllAlbums,
  createAlbum,
  updateAlbum,
  deleteAlbum
} = require("../controllers/album.controller");

const router = express.Router();

router.get("/", getAllAlbums);
router.post("/", authMiddleware, requireAdmin, createAlbum);
router.put("/:id", authMiddleware, requireAdmin, updateAlbum);
router.delete("/:id", authMiddleware, requireAdmin, deleteAlbum);

module.exports = router;