const express = require("express");
const authMiddleware = require("../middleware/auth.middleware");
const requireAdmin = require("../middleware/requireAdmin.middleware");
const { uploadImage } = require("../middleware/upload.middleware");
const { uploadImageFile } = require("../controllers/upload.controller");

const router = express.Router();

router.post(
  "/image",
  authMiddleware,
  requireAdmin,
  uploadImage.single("image"),
  uploadImageFile
);

module.exports = router;