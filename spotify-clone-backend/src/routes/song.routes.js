const express = require("express");
const authMiddleware = require("../middleware/auth.middleware");
const {
  getAllSongs,
  createSong
} = require("../controllers/song.controller");

const router = express.Router();

router.get("/", getAllSongs);
router.post("/", authMiddleware, createSong);

module.exports = router;