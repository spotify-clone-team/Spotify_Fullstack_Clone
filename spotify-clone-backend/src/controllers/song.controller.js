const Song = require("../models/Song");

const getAllSongs = async (req, res, next) => {
  try {
    const songs = await Song.find().sort({ createdAt: -1 });

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
      artistName,
      albumName,
      genre,
      durationSeconds,
      coverUrl,
      audioUrl
    } = req.body;

    if (!title || !artistName || !audioUrl) {
      return res.status(400).json({
        success: false,
        message: "title, artistName, audioUrl are required"
      });
    }

    const song = await Song.create({
      title,
      artistName,
      albumName,
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

module.exports = {
  getAllSongs,
  createSong
};