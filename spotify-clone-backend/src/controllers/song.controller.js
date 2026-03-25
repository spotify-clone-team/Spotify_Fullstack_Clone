const Song = require("../models/Song");
const Artist = require("../models/Artist");
const Album = require("../models/Album");
const Playlist = require("../models/Playlist");
const env = require("../config/env");

const buildFileUrl = (filePath) => {
  const cleanBaseUrl = env.baseUrl.replace(/\/+$/, "");
  const cleanPath = filePath.replace(/\\/g, "/").replace(/^\/+/, "");
  return `${cleanBaseUrl}/${cleanPath}`;
};
const getAllSongs = async (req, res, next) => {
  try {
    const songs = await Song.find()
      .populate("artist")
      .populate("album")
      .sort({ createdAt: -1 });

    return res.json({
      success: true,
      data: songs
    });
  } catch (error) {
    next(error);
  }
};

const createSong = async (req, res, next) => {
  try {
    const {
      title,
      artist,
      album,
      genre,
      durationSeconds,
      coverUrl,
      audioUrl
    } = req.body;

    if (!title || !audioUrl || !artist) {
      return res.status(400).json({
        success: false,
        message: "title, artist, audioUrl are required"
      });
    }

    const artistDoc = await Artist.findById(artist);
    if (!artistDoc) {
      return res.status(400).json({
        success: false,
        message: "Artist không tồn tại"
      });
    }

    let albumDoc = null;
    if (album) {
      albumDoc = await Album.findById(album);
      if (!albumDoc) {
        return res.status(400).json({
          success: false,
          message: "Album không tồn tại"
        });
      }

      if (String(albumDoc.artist) !== String(artistDoc._id)) {
        return res.status(400).json({
          success: false,
          message: "Album không thuộc artist đã chọn"
        });
      }
    }

    const song = await Song.create({
      title,
      artist: artistDoc._id,
      artistName: artistDoc.name,
      album: albumDoc?._id || null,
      albumName: albumDoc?.title || "",
      genre,
      durationSeconds,
      coverUrl,
      audioUrl,
      createdBy: req.user.userId
    });

    return res.status(201).json({
      success: true,
      message: "Song created successfully",
      data: song
    });
  } catch (error) {
    next(error);
  }
};

const updateSong = async (req, res, next) => {
  try {
    const { id } = req.params;
    const {
      title,
      artist,
      album,
      genre,
      durationSeconds,
      coverUrl,
      audioUrl
    } = req.body;

    const song = await Song.findById(id);
    if (!song) {
      return res.status(404).json({
        success: false,
        message: "Song not found"
      });
    }

    let artistDoc = null;
    let albumDoc = null;

    if (artist) {
      artistDoc = await Artist.findById(artist);
      if (!artistDoc) {
        return res.status(400).json({
          success: false,
          message: "Artist không tồn tại"
        });
      }
    }

    if (album) {
      albumDoc = await Album.findById(album);
      if (!albumDoc) {
        return res.status(400).json({
          success: false,
          message: "Album không tồn tại"
        });
      }
    }

    const finalArtist = artistDoc || (song.artist ? await Artist.findById(song.artist) : null);

    if (albumDoc && finalArtist && String(albumDoc.artist) !== String(finalArtist._id)) {
      return res.status(400).json({
        success: false,
        message: "Album không thuộc artist đã chọn"
      });
    }

    song.title = title ?? song.title;
    song.artist = finalArtist?._id || null;
    song.artistName = finalArtist?.name || song.artistName;
    song.album = albumDoc?._id || null;
    song.albumName = albumDoc?.title || "";
    song.genre = genre ?? song.genre;
    song.durationSeconds = durationSeconds ?? song.durationSeconds;
    song.coverUrl = coverUrl ?? song.coverUrl;
    song.audioUrl = audioUrl ?? song.audioUrl;

    await song.save();

    return res.json({
      success: true,
      message: "Song updated successfully",
      data: song
    });
  } catch (error) {
    next(error);
  }
};

const deleteSong = async (req, res, next) => {
  try {
    const { id } = req.params;

    const playlistExists = await Playlist.exists({ songs: id });
    if (playlistExists) {
      return res.status(409).json({
        success: false,
        message: "Không thể xóa bài hát vì đang nằm trong playlist"
      });
    }

    const deletedSong = await Song.findByIdAndDelete(id);

    if (!deletedSong) {
      return res.status(404).json({
        success: false,
        message: "Song not found"
      });
    }

    return res.json({
      success: true,
      message: "Song deleted successfully"
    });
  } catch (error) {
    next(error);
  }
};

const uploadCoverFile = async (req, res, next) => {
  try {
    if (!req.file) {
      return res.status(400).json({
        success: false,
        message: "No cover file uploaded"
      });
    }

    const coverUrl = buildFileUrl(req.file.path);

    return res.status(201).json({
      success: true,
      message: "Cover uploaded successfully",
      data: {
        filename: req.file.filename,
        coverUrl
      }
    });
  } catch (error) {
    next(error);
  }
};

const uploadAudioFile = async (req, res, next) => {
  try {
    if (!req.file) {
      return res.status(400).json({
        success: false,
        message: "No audio file uploaded"
      });
    }

    const audioUrl = buildFileUrl(req.file.path);

    return res.status(201).json({
      success: true,
      message: "Audio uploaded successfully",
      data: {
        filename: req.file.filename,
        audioUrl
      }
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  getAllSongs,
  createSong,
  updateSong,
  deleteSong,
  uploadCoverFile,
  uploadAudioFile
};