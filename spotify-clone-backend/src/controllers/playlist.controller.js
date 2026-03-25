const Playlist = require("../models/Playlist");

const getAllPlaylists = async (req, res, next) => {
  try {
    const playlists = await Playlist.find()
      .populate({
        path: "songs",
        populate: [
          { path: "artist" },
          { path: "album" }
        ]
      })
      .sort({ createdAt: -1 });

    return res.json({
      success: true,
      data: playlists
    });
  } catch (error) {
    next(error);
  }
};

const createPlaylist = async (req, res, next) => {
  try {
    const { title, description, coverUrl, songs, isPublic } = req.body;

    if (!title) {
      return res.status(400).json({
        success: false,
        message: "Playlist title is required"
      });
    }

    const playlist = await Playlist.create({
      title,
      description,
      coverUrl,
      songs: songs || [],
      isPublic,
      createdBy: req.user.userId
    });

    return res.status(201).json({
      success: true,
      message: "Playlist created successfully",
      data: playlist
    });
  } catch (error) {
    next(error);
  }
};

const updatePlaylist = async (req, res, next) => {
  try {
    const { id } = req.params;

    const playlist = await Playlist.findByIdAndUpdate(id, req.body, {
      new: true,
      runValidators: true
    }).populate({
      path: "songs",
      populate: [
        { path: "artist" },
        { path: "album" }
      ]
    });

    if (!playlist) {
      return res.status(404).json({
        success: false,
        message: "Playlist not found"
      });
    }

    return res.json({
      success: true,
      message: "Playlist updated successfully",
      data: playlist
    });
  } catch (error) {
    next(error);
  }
};

const deletePlaylist = async (req, res, next) => {
  try {
    const { id } = req.params;

    const playlist = await Playlist.findByIdAndDelete(id);

    if (!playlist) {
      return res.status(404).json({
        success: false,
        message: "Playlist not found"
      });
    }

    return res.json({
      success: true,
      message: "Playlist deleted successfully"
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  getAllPlaylists,
  createPlaylist,
  updatePlaylist,
  deletePlaylist
};