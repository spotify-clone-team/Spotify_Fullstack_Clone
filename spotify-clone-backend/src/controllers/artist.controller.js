const Artist = require("../models/Artist");
const Song = require("../models/Song");
const Album = require("../models/Album");

const getAllArtists = async (req, res, next) => {
  try {
    const artists = await Artist.find().sort({ createdAt: -1 });

    return res.json({
      success: true,
      data: artists
    });
  } catch (error) {
    next(error);
  }
};

const createArtist = async (req, res, next) => {
  try {
    const { name, imageUrl, bio } = req.body;

    if (!name) {
      return res.status(400).json({
        success: false,
        message: "Artist name is required"
      });
    }

    const artist = await Artist.create({
      name,
      imageUrl,
      bio,
      createdBy: req.user.userId
    });

    return res.status(201).json({
      success: true,
      message: "Artist created successfully",
      data: artist
    });
  } catch (error) {
    next(error);
  }
};

const updateArtist = async (req, res, next) => {
  try {
    const { id } = req.params;

    const artist = await Artist.findByIdAndUpdate(id, req.body, {
      new: true,
      runValidators: true
    });

    if (!artist) {
      return res.status(404).json({
        success: false,
        message: "Artist not found"
      });
    }

    return res.json({
      success: true,
      message: "Artist updated successfully",
      data: artist
    });
  } catch (error) {
    next(error);
  }
};

const deleteArtist = async (req, res, next) => {
  try {
    const { id } = req.params;

    const songExists = await Song.exists({ artist: id });
    if (songExists) {
      return res.status(409).json({
        success: false,
        message: "Không thể xóa artist vì đang có bài hát thuộc artist này"
      });
    }

    const albumExists = await Album.exists({ artist: id });
    if (albumExists) {
      return res.status(409).json({
        success: false,
        message: "Không thể xóa artist vì đang có album thuộc artist này"
      });
    }

    const artist = await Artist.findByIdAndDelete(id);

    if (!artist) {
      return res.status(404).json({
        success: false,
        message: "Artist not found"
      });
    }

    return res.json({
      success: true,
      message: "Artist deleted successfully"
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  getAllArtists,
  createArtist,
  updateArtist,
  deleteArtist
};