const Album = require("../models/Album");
const Song = require("../models/Song");

const getAllAlbums = async (req, res, next) => {
  try {
    const albums = await Album.find()
      .populate("artist")
      .sort({ createdAt: -1 });

    return res.json({
      success: true,
      data: albums
    });
  } catch (error) {
    next(error);
  }
};

const createAlbum = async (req, res, next) => {
  try {
    const { title, artist, coverUrl, releaseYear } = req.body;

    if (!title || !artist) {
      return res.status(400).json({
        success: false,
        message: "title and artist are required"
      });
    }

    const album = await Album.create({
      title,
      artist,
      coverUrl,
      releaseYear,
      createdBy: req.user.userId
    });

    return res.status(201).json({
      success: true,
      message: "Album created successfully",
      data: album
    });
  } catch (error) {
    next(error);
  }
};

const updateAlbum = async (req, res, next) => {
  try {
    const { id } = req.params;

    const album = await Album.findByIdAndUpdate(id, req.body, {
      new: true,
      runValidators: true
    }).populate("artist");

    if (!album) {
      return res.status(404).json({
        success: false,
        message: "Album not found"
      });
    }

    return res.json({
      success: true,
      message: "Album updated successfully",
      data: album
    });
  } catch (error) {
    next(error);
  }
};

const deleteAlbum = async (req, res, next) => {
  try {
    const { id } = req.params;

    const songExists = await Song.exists({ album: id });
    if (songExists) {
      return res.status(409).json({
        success: false,
        message: "Không thể xóa album vì đang có bài hát thuộc album này"
      });
    }

    const album = await Album.findByIdAndDelete(id);

    if (!album) {
      return res.status(404).json({
        success: false,
        message: "Album not found"
      });
    }

    return res.json({
      success: true,
      message: "Album deleted successfully"
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  getAllAlbums,
  createAlbum,
  updateAlbum,
  deleteAlbum
};