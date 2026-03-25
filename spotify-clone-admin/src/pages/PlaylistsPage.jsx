import { useEffect, useState } from "react";
import api from "../services/api";
import { getAuthHeaders } from "../utils/auth";

const initialForm = {
  title: "",
  description: "",
  coverUrl: "",
  songs: [],
  isPublic: true
};

export default function PlaylistsPage() {
  const [playlists, setPlaylists] = useState([]);
  const [songs, setSongs] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const [uploadingImage, setUploadingImage] = useState(false);

  const fetchData = async () => {
    try {
      const [playlistsRes, songsRes] = await Promise.all([
        api.get("/playlists"),
        api.get("/songs")
      ]);
      setPlaylists(playlistsRes.data.data || []);
      setSongs(songsRes.data.data || []);
    } catch (error) {
      setMessage("Không thể tải playlists/songs");
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value
    }));
  };

  const handleSongsChange = (e) => {
    const selected = Array.from(e.target.selectedOptions).map((option) => option.value);
    setForm((prev) => ({
      ...prev,
      songs: selected
    }));
  };

  const handleUploadImage = async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    try {
      setUploadingImage(true);
      setMessage("");

      const formData = new FormData();
      formData.append("image", file);

      const response = await api.post("/uploads/image", formData, {
        headers: getAuthHeaders()
      });

      setForm((prev) => ({
        ...prev,
        coverUrl: response.data.data.url
      }));

      setMessage("Upload ảnh thành công");
    } catch (error) {
      setMessage(error?.response?.data?.message || "Upload ảnh thất bại");
    } finally {
      setUploadingImage(false);
    }
  };

  const resetForm = () => {
    setForm(initialForm);
    setEditingId(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      setLoading(true);
      setMessage("");

      if (editingId) {
        await api.put(`/playlists/${editingId}`, form, {
          headers: getAuthHeaders()
        });
        setMessage("Cập nhật playlist thành công");
      } else {
        await api.post("/playlists", form, {
          headers: getAuthHeaders()
        });
        setMessage("Tạo playlist thành công");
      }

      resetForm();
      fetchData();
    } catch (error) {
      setMessage(error?.response?.data?.message || "Lưu playlist thất bại");
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (playlist) => {
    setEditingId(playlist._id);
    setForm({
      title: playlist.title || "",
      description: playlist.description || "",
      coverUrl: playlist.coverUrl || "",
      songs: (playlist.songs || []).map((song) => song._id),
      isPublic: playlist.isPublic ?? true
    });
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Xóa playlist này?")) return;

    try {
      await api.delete(`/playlists/${id}`, {
        headers: getAuthHeaders()
      });
      setMessage("Xóa playlist thành công");
      fetchData();
      if (editingId === id) resetForm();
    } catch (error) {
      setMessage(error?.response?.data?.message || "Xóa playlist thất bại");
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Manage Playlists</h1>
        <p>Tạo playlist từ các bài hát đã có.</p>
      </div>

      {message && <div className="alert-box">{message}</div>}

      <div className="songs-grid">
        <div className="card">
          <div className="songs-list-header">
            <h2 className="card-title">{editingId ? "Edit Playlist" : "Create Playlist"}</h2>
            {editingId && (
              <button type="button" className="secondary-btn" onClick={resetForm}>
                Cancel Edit
              </button>
            )}
          </div>

          <form className="song-form" onSubmit={handleSubmit}>
            <input
              name="title"
              placeholder="Playlist title"
              value={form.title}
              onChange={handleChange}
              required
            />

            <input
              name="description"
              placeholder="Description"
              value={form.description}
              onChange={handleChange}
            />

            <div className="upload-group">
              <label className="upload-label">Upload Cover</label>
              <input type="file" accept="image/*" onChange={handleUploadImage} />
              {uploadingImage && <span className="upload-hint">Uploading image...</span>}
            </div>

            <input
              name="coverUrl"
              placeholder="Cover URL"
              value={form.coverUrl}
              onChange={handleChange}
            />

            <label className="upload-label">Select songs</label>
            <select
              multiple
              value={form.songs}
              onChange={handleSongsChange}
              className="multi-select"
            >
              {songs.map((song) => (
                <option key={song._id} value={song._id}>
                  {song.title} - {song.artist?.name || song.artistName}
                </option>
              ))}
            </select>

            <label className="checkbox-row">
              <input
                type="checkbox"
                name="isPublic"
                checked={form.isPublic}
                onChange={handleChange}
              />
              Public playlist
            </label>

            {form.coverUrl && (
              <img className="cover-preview" src={form.coverUrl} alt="playlist preview" />
            )}

            <button type="submit" className="primary-btn" disabled={loading}>
              {loading ? "Saving..." : editingId ? "Update Playlist" : "Create Playlist"}
            </button>
          </form>
        </div>

        <div className="card">
          <div className="songs-list-header">
            <h2 className="card-title">Playlist List</h2>
            <button type="button" className="secondary-btn" onClick={fetchData}>
              Refresh
            </button>
          </div>

          <div className="song-list">
            {playlists.map((playlist) => (
              <div className="song-item" key={playlist._id}>
                <div className="song-item-content">
                  <h3>{playlist.title}</h3>
                  <p>{playlist.description || "No description"}</p>
                  <span>
                    {playlist.songs?.length || 0} songs • {playlist.isPublic ? "Public" : "Private"}
                  </span>

                  {playlist.coverUrl && (
                    <img className="song-thumb" src={playlist.coverUrl} alt={playlist.title} />
                  )}

                  <div className="action-row">
                    <button
                      type="button"
                      className="secondary-btn small-btn"
                      onClick={() => handleEdit(playlist)}
                    >
                      Edit
                    </button>
                    <button
                      type="button"
                      className="danger-btn small-btn"
                      onClick={() => handleDelete(playlist._id)}
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            ))}

            {playlists.length === 0 && <p>Chưa có playlist nào.</p>}
          </div>
        </div>
      </div>
    </div>
  );
}