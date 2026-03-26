const Song = require("../models/Song");
const Artist = require("../models/Artist");
const Album = require("../models/Album");
const Playlist = require("../models/Playlist");

// --- 1. LẤY DANH SÁCH CHO LIBRARY (NEW) ---

// Lấy toàn bộ Artist từ Database
const getAllArtists = async (req, res, next) => {
  try {
    const artists = await Artist.find().sort({ name: 1 });
    return res.json({ success: true, data: artists });
  } catch (error) {
    next(error);
  }
};

// Lấy toàn bộ Album từ Database
const getAllAlbums = async (req, res, next) => {
  try {
    const albums = await Album.find().populate("artist").sort({ createdAt: -1 });
    return res.json({ success: true, data: albums });
  } catch (error) {
    next(error);
  }
};

// Lấy toàn bộ Playlist từ Database
const getAllPlaylists = async (req, res, next) => {
  try {
    const playlists = await Playlist.find().sort({ createdAt: -1 });
    return res.json({ success: true, data: playlists });
  } catch (error) {
    next(error);
  }
};

// --- 2. QUẢN LÝ BÀI HÁT ---

const getAllSongs = async (req, res, next) => {
  try {
    const songs = await Song.find()
      .populate("artist")
      .populate("album")
      .sort({ createdAt: -1 });

    return res.json({ success: true, data: songs });
  } catch (error) {
    next(error);
  }
};

const createSong = async (req, res, next) => {
  try {
    const { title, artist, album, genre, durationSeconds, coverUrl, audioUrl } = req.body;

    if (!title || !audioUrl || !artist) {
      return res.status(400).json({ success: false, message: "title, artist, audioUrl are required" });
    }

    const artistDoc = await Artist.findById(artist);
    if (!artistDoc) return res.status(400).json({ success: false, message: "Artist không tồn tại" });

    let albumDoc = null;
    if (album) {
      albumDoc = await Album.findById(album);
      if (!albumDoc) return res.status(400).json({ success: false, message: "Album không tồn tại" });
    }

    const song = await Song.create({
      title,
      artist: artistDoc._id,
      artistName: artistDoc.name,
      album: albumDoc?._id || null,
      albumName: albumDoc?.title || "",
      genre,
      durationSeconds,
      coverUrl, // Link Cloudinary từ Web Admin gửi xuống
      audioUrl,  // Link Cloudinary từ Web Admin gửi xuống
      createdBy: req.user.userId
    });

    return res.status(201).json({ success: true, data: song });
  } catch (error) {
    next(error);
  }
};

const updateSong = async (req, res, next) => {
  try {
    const { id } = req.params;
    const song = await Song.findById(id);
    if (!song) return res.status(404).json({ success: false, message: "Song not found" });

    // Cập nhật các trường thông tin (giữ logic cũ của bồ)
    Object.assign(song, req.body);
    await song.save();

    return res.json({ success: true, data: song });
  } catch (error) {
    next(error);
  }
};

const deleteSong = async (req, res, next) => {
  try {
    const { id } = req.params;
    const playlistExists = await Playlist.exists({ songs: id });
    if (playlistExists) return res.status(409).json({ success: false, message: "Đang nằm trong playlist, không xóa được!" });

    await Song.findByIdAndDelete(id);
    return res.json({ success: true, message: "Deleted" });
  } catch (error) {
    next(error);
  }
};

// --- 3. UPLOAD FILE LÊN CLOUDINARY (UPDATED) ---

const uploadCoverFile = async (req, res, next) => {
  try {
    if (!req.file) return res.status(400).json({ success: false, message: "No file uploaded" });

    // FIX: Với Cloudinary, req.file.path là URL hoàn chỉnh luôn
    return res.status(201).json({
      success: true,
      message: "Cover uploaded to Cloudinary",
      data: {
        coverUrl: req.file.path 
      }
    });
  } catch (error) {
    next(error);
  }
};

const uploadAudioFile = async (req, res, next) => {
  try {
    if (!req.file) return res.status(400).json({ success: false, message: "No file uploaded" });

    return res.status(201).json({
      success: true,
      message: "Audio uploaded to Cloudinary",
      data: {
        audioUrl: req.file.path 
      }
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  getAllSongs,
  getAllArtists,
  getAllAlbums,
  getAllPlaylists,
  createSong,
  updateSong,
  deleteSong,
  uploadCoverFile,
  uploadAudioFile
};