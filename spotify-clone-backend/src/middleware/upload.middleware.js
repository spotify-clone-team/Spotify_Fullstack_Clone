const multer = require("multer");
const path = require("path");
const fs = require("fs");

const ensureDir = (dirPath) => {
  if (!fs.existsSync(dirPath)) {
    fs.mkdirSync(dirPath, { recursive: true });
  }
};

ensureDir("uploads/covers");
ensureDir("uploads/audio");

const coverStorage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, "uploads/covers");
  },
  filename: (req, file, cb) => {
    const uniqueName = `cover-${Date.now()}${path.extname(file.originalname)}`;
    cb(null, uniqueName);
  }
});

const audioStorage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, "uploads/audio");
  },
  filename: (req, file, cb) => {
    const uniqueName = `audio-${Date.now()}${path.extname(file.originalname)}`;
    cb(null, uniqueName);
  }
});

const imageFileFilter = (req, file, cb) => {
  if (file.mimetype.startsWith("image/")) {
    cb(null, true);
  } else {
    cb(new Error("Image file only"), false);
  }
};

const audioFileFilter = (req, file, cb) => {
  if (file.mimetype.startsWith("audio/")) {
    cb(null, true);
  } else {
    cb(new Error("Audio file only"), false);
  }
};

const uploadImage = multer({
  storage: coverStorage,
  fileFilter: imageFileFilter,
  limits: { fileSize: 10 * 1024 * 1024 }
});

const uploadCover = multer({
  storage: coverStorage,
  fileFilter: imageFileFilter,
  limits: { fileSize: 10 * 1024 * 1024 }
});

const uploadAudio = multer({
  storage: audioStorage,
  fileFilter: audioFileFilter,
  limits: { fileSize: 100 * 1024 * 1024 }
});

module.exports = {
  uploadImage,
  uploadCover,
  uploadAudio
};