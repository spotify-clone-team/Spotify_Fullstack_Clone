const express = require("express");
const cors = require("cors");
const path = require("path");

const authRoutes = require("./routes/auth.routes");
const songRoutes = require("./routes/song.routes");
const artistRoutes = require("./routes/artist.routes");
const albumRoutes = require("./routes/album.routes");
const playlistRoutes = require("./routes/playlist.routes");
const uploadRoutes = require("./routes/upload.routes");
const errorMiddleware = require("./middleware/error.middleware");

const app = express();

app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

app.use("/uploads", express.static(path.join(process.cwd(), "uploads")));

app.get("/", (req, res) => {
  res.json({
    message: "Spotify Clone Backend is running"
  });
});

app.use("/api/auth", authRoutes);
app.use("/api/songs", songRoutes);
app.use("/api/artists", artistRoutes);
app.use("/api/albums", albumRoutes);
app.use("/api/playlists", playlistRoutes);
app.use("/api/uploads", uploadRoutes);

app.use(errorMiddleware);

module.exports = app;