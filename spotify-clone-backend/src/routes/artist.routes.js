const express = require("express");
const authMiddleware = require("../middleware/auth.middleware");
const requireAdmin = require("../middleware/requireAdmin.middleware");
const {
  getAllArtists,
  createArtist,
  updateArtist,
  deleteArtist
} = require("../controllers/artist.controller");

const router = express.Router();

router.get("/", getAllArtists);
router.post("/", authMiddleware, requireAdmin, createArtist);
router.put("/:id", authMiddleware, requireAdmin, updateArtist);
router.delete("/:id", authMiddleware, requireAdmin, deleteArtist);

module.exports = router;