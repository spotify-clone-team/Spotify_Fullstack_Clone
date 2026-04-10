import { useEffect, useMemo, useState } from "react";
import api from "../services/api";
import { getAuthHeaders } from "../utils/auth";

const initialForm = {
  title: "",
  artist: "",
  album: "",
  genre: "",
  durationSeconds: "",
  coverUrl: "",
  audioUrl: ""
};

export default function SongsPage() {
  const [songs, setSongs] = useState([]);
  const [artists, setArtists] = useState([]);
  const [albums, setAlbums] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(false);
  const [fetching, setFetching] = useState(false);
  const [message, setMessage] = useState("");
  const [uploadingCover, setUploadingCover] = useState(false);
  const [uploadingAudio, setUploadingAudio] = useState(false);

  const filteredAlbums = useMemo(() => {
    if (!form.artist) return albums;

    return albums.filter((album) => {
      const artistId =
        typeof album.artist === "object" ? album.artist?._id : album.artist;
      return artistId === form.artist;
    });
  }, [albums, form.artist]);

  const fetchData = async () => {
    try {
      setFetching(true);
      setMessage("");

      const [songsRes, artistsRes, albumsRes] = await Promise.all([
        api.get("/songs"),
        api.get("/artists"),
        api.get("/albums")
      ]);

      setSongs(songsRes.data.data || []);
      setArtists(artistsRes.data.data || []);
      setAlbums(albumsRes.data.data || []);
    } catch (error) {
      setMessage("Không thể tải dữ liệu hệ thống!");
    } finally {
      setFetching(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === "artist") {
      setForm((prev) => ({ ...prev, artist: value, album: "" }));
      return;
    }

    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const resetForm = () => {
    setForm(initialForm);
    setEditingId(null);
  };

  const handleEditSong = (song) => {
    setEditingId(song._id);

    const artistId =
      typeof song.artist === "object" ? song.artist?._id || "" : song.artist || "";
    const albumId =
      typeof song.album === "object" ? song.album?._id || "" : song.album || "";

    setForm({
      title: song.title || "",
      artist: artistId,
      album: albumId,
      genre: song.genre || "",
      durationSeconds: song.durationSeconds || "",
      coverUrl: song.coverUrl || "",
      audioUrl: song.audioUrl || ""
    });
  };

  const handleDeleteSong = async (id) => {
    if (!window.confirm("Bồ có chắc chắn muốn xóa bài hát này?")) return;

    try {
      await api.delete(`/songs/${id}`, {
        headers: getAuthHeaders()
      });
      setMessage("Xóa bài hát thành công!");
      fetchData();

      if (editingId === id) {
        resetForm();
      }
    } catch (error) {
      setMessage("Xóa bài hát thất bại!");
    }
  };

  const handleUploadCover = async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    try {
      setUploadingCover(true);

      const formData = new FormData();
      formData.append("cover", file);

      const response = await api.post("/songs/upload-cover", formData, {
        headers: getAuthHeaders()
      });

      setForm((prev) => ({
        ...prev,
        coverUrl: response.data.data.coverUrl
      }));

      setMessage("Upload ảnh cover thành công!");
    } catch (error) {
      setMessage("Upload cover thất bại");
    } finally {
      setUploadingCover(false);
    }
  };

  const handleUploadAudio = async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    try {
      setUploadingAudio(true);

      const formData = new FormData();
      formData.append("audio", file);

      const response = await api.post("/songs/upload-audio", formData, {
        headers: getAuthHeaders()
      });

      setForm((prev) => ({
        ...prev,
        audioUrl: response.data.data.audioUrl
      }));

      setMessage("Upload file mp3 thành công!");
    } catch (error) {
      setMessage("Upload audio thất bại");
    } finally {
      setUploadingAudio(false);
    }
  };

  const handleSubmitSong = async (e) => {
    e.preventDefault();

    try {
      setLoading(true);

      const payload = {
        ...form,
        durationSeconds: Number(form.durationSeconds || 0),
        album: form.album || null
      };

      if (editingId) {
        await api.put(`/songs/${editingId}`, payload, {
          headers: getAuthHeaders()
        });
        setMessage("Cập nhật bài hát thành công!");
      } else {
        await api.post("/songs", payload, {
          headers: getAuthHeaders()
        });
        setMessage("Thêm bài hát mới thành công!");
      }

      resetForm();
      fetchData();
    } catch (error) {
      setMessage("Lưu bài hát thất bại!");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="songs-page">
      <div className="page-header">
        <h1>Manage Songs</h1>
        <p>Hệ thống quản lý bài hát tích hợp Cloudinary Storage.</p>
      </div>

      {message && <div className="alert-box">{message}</div>}

      <div className="songs-grid">
        <div className="card">
          <div className="songs-list-header">
            <h2 className="card-title">{editingId ? "Edit Song" : "Create Song"}</h2>

            {editingId && (
              <button type="button" className="secondary-btn" onClick={resetForm}>
                Cancel Edit
              </button>
            )}
          </div>

          <form className="song-form" onSubmit={handleSubmitSong}>
            <input
              name="title"
              placeholder="Title"
              value={form.title}
              onChange={handleChange}
              required
            />

            <select
              name="artist"
              value={form.artist}
              onChange={handleChange}
              required
            >
              <option value="">Select Artist</option>
              {artists.map((a) => (
                <option key={a._id} value={a._id}>
                  {a.name}
                </option>
              ))}
            </select>

            <select name="album" value={form.album} onChange={handleChange}>
              <option value="">No Album</option>
              {filteredAlbums.map((alb) => (
                <option key={alb._id} value={alb._id}>
                  {alb.title}
                </option>
              ))}
            </select>

            <input
              name="genre"
              placeholder="Genre"
              value={form.genre}
              onChange={handleChange}
            />

            <input
              name="durationSeconds"
              placeholder="Duration (s)"
              type="number"
              value={form.durationSeconds}
              onChange={handleChange}
            />

            <div className="upload-group">
              <label className="upload-label">Upload Cover</label>
              <input type="file" accept="image/*" onChange={handleUploadCover} />
              {uploadingCover && <span className="upload-hint">Đang up ảnh...</span>}
            </div>

            <input
              name="coverUrl"
              value={form.coverUrl}
              readOnly
              placeholder="Cover URL"
            />

            {form.coverUrl && (
              <div className="preview-container">
                <p className="preview-label">Cover Preview:</p>
                <div className="cover-preview-wrap">
                  <img
                    className="cover-preview fixed-cover-preview"
                    src={form.coverUrl}
                    alt="Cover Preview"
                  />
                </div>
              </div>
            )}

            <div className="upload-group">
              <label className="upload-label">Upload Audio (MP3)</label>
              <input type="file" accept="audio/*" onChange={handleUploadAudio} />
              {uploadingAudio && <span className="upload-hint">Đang up nhạc...</span>}
            </div>

            <input
              name="audioUrl"
              value={form.audioUrl}
              readOnly
              placeholder="Audio URL"
              required
            />

            {form.audioUrl && (
              <audio controls className="song-preview-audio">
                <source src={form.audioUrl} />
              </audio>
            )}

            <button
              type="submit"
              className="primary-btn"
              disabled={loading || uploadingCover || uploadingAudio}
            >
              {loading ? "Đang lưu..." : editingId ? "Update Song" : "Create Song"}
            </button>
          </form>
        </div>

        <div className="card">
          <div className="songs-list-header">
            <h2 className="card-title">Song List</h2>
            <button type="button" className="secondary-btn" onClick={fetchData}>
              Refresh
            </button>
          </div>

          <div className="song-list">
            {songs.map((song) => (
              <div className="song-item" key={song._id}>
                <div className="song-item-content">
                  <div className="item-main">
                    {song.coverUrl && (
                      <div className="thumb-wrap">
                        <img
                          className="song-thumb thumb-fixed"
                          src={song.coverUrl}
                          alt={song.title}
                        />
                      </div>
                    )}

                    <div className="item-info">
                      <h3>{song.title}</h3>
                      <p>{song.artist?.name || "Unknown Artist"}</p>
                      <span className="meta-tag">{song.genre || "No genre"}</span>
                    </div>
                  </div>

                  <div className="action-row">
                    <button
                      type="button"
                      className="secondary-btn small-btn"
                      onClick={() => handleEditSong(song)}
                    >
                      Edit
                    </button>

                    <button
                      type="button"
                      className="danger-btn small-btn"
                      onClick={() => handleDeleteSong(song._id)}
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            ))}

            {songs.length === 0 && !fetching && (
              <p className="empty-msg">Chưa có bài hát nào.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}