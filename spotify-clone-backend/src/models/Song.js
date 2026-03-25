const mongoose = require("mongoose");

const songSchema = new mongoose.Schema(
  {
    title: {
      type: String,
      required: true,
      trim: true
    },
    artistName: {
      type: String,
      required: true,
      trim: true
    },
    artist: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "Artist",
      default: null
    },
    albumName: {
      type: String,
      default: "",
      trim: true
    },
    album: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "Album",
      default: null
    },
    genre: {
      type: String,
      default: "",
      trim: true
    },
    durationSeconds: {
      type: Number,
      default: 0
    },
    coverUrl: {
      type: String,
      default: ""
    },
    audioUrl: {
      type: String,
      required: true
    },
    createdBy: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "User",
      default: null
    }
  },
  {
    timestamps: true
  }
);

module.exports = mongoose.model("Song", songSchema);