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
  const [allSongs, setAllSongs] = useState([]);
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
      setAllSongs(songsRes.data.data || []);
    } catch (error) {
      setMessage("Lỗi tải dữ liệu. Vui lòng thử lại!");
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const resetForm = () => {
    setForm(initialForm);
    setEditingId(null);
  };

  const handleUploadImage = async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    try {
      setUploadingImage(true);

      const formData = new FormData();
      formData.append("cover", file);

      const response = await api.post("/songs/upload-cover", formData, {
        headers: getAuthHeaders()
      });

      setForm((prev) => ({
        ...prev,
        coverUrl: response.data.data.coverUrl
      }));

      setMessage("Upload ảnh cover Playlist thành công!");
    } catch (error) {
      setMessage("Upload ảnh thất bại!");
    } finally {
      setUploadingImage(false);
    }
  };

  const handleSongsChange = (e) => {
    const selectedOptions = Array.from(e.target.selectedOptions).map(
      (option) => option.value
    );

    setForm((prev) => ({
      ...prev,
      songs: selectedOptions
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      setLoading(true);

      if (editingId) {
        await api.put(`/playlists/${editingId}`, form, {
          headers: getAuthHeaders()
        });
        setMessage("Cập nhật Playlist thành công!");
      } else {
        await api.post("/playlists", form, {
          headers: getAuthHeaders()
        });
        setMessage("Tạo mới Playlist thành công!");
      }

      resetForm();
      fetchData();
    } catch (error) {
      setMessage("Lưu dữ liệu thất bại. Kiểm tra lại kết nối.");
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
      songs: (playlist.songs || []).map((s) =>
        typeof s === "object" ? s._id : s
      ),
      isPublic: playlist.isPublic ?? true
    });
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Bồ có chắc muốn xóa Playlist này không?")) return;

    try {
      await api.delete(`/playlists/${id}`, {
        headers: getAuthHeaders()
      });
      setMessage("Xóa Playlist thành công!");
      fetchData();

      if (editingId === id) {
        resetForm();
      }
    } catch (error) {
      setMessage("Xóa thất bại!");
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Manage Playlists</h1>
        <p>Hệ thống quản lý Playlist - Thêm bài hát trực tiếp vào Playlist.</p>
      </div>

      {message && <div className="alert-box">{message}</div>}

      <div className="songs-grid">
        <div className="card">
          <div className="songs-list-header">
            <h2 className="card-title">
              {editingId ? "Edit Playlist" : "Create Playlist"}
            </h2>

            {editingId && (
              <button
                type="button"
                className="secondary-btn"
                onClick={resetForm}
              >
                Cancel Edit
              </button>
            )}
          </div>

          <form className="song-form" onSubmit={handleSubmit}>
            <input
              name="title"
              placeholder="Playlist title"
              value={form.title}
              onChange={(e) => setForm({ ...form, title: e.target.value })}
              required
            />

            <input
              name="description"
              placeholder="Description"
              value={form.description}
              onChange={(e) =>
                setForm({ ...form, description: e.target.value })
              }
            />

            <div className="upload-group">
              <label className="upload-label">Upload Playlist Cover</label>
              <input type="file" accept="image/*" onChange={handleUploadImage} />
              {uploadingImage && (
                <span className="upload-hint">Đang up lên mây...</span>
              )}
            </div>

            <input
              name="coverUrl"
              placeholder="Cover URL"
              value={form.coverUrl}
              readOnly
            />

            {form.coverUrl && (
              <div className="preview-container">
                <p className="preview-label">Preview:</p>
                <div className="cover-preview-wrap">
                  <img
                    className="cover-preview fixed-cover-preview"
                    src={form.coverUrl}
                    alt="Cover Preview"
                  />
                </div>
              </div>
            )}

            <div className="multi-select-section">
              <label className="upload-label multi-select-label">
                Chọn bài hát cho Playlist này (Giữ Ctrl hoặc Cmd để chọn nhiều bài)
              </label>

              <select
                multiple
                name="songs"
                value={form.songs}
                onChange={handleSongsChange}
                className="multi-select"
              >
                {allSongs.map((song) => (
                  <option key={song._id} value={song._id}>
                    {song.title} - {song.artistName || "Nghệ sĩ ẩn danh"}
                  </option>
                ))}
              </select>

              <p className="multi-select-hint">
                Đã chọn: {form.songs.length} bài hát
              </p>
            </div>

            <button
              type="submit"
              className="primary-btn"
              disabled={loading || uploadingImage}
            >
              {loading ? "Đang lưu..." : editingId ? "Update Playlist" : "Create Playlist"}
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
            {playlists.map((p) => (
              <div key={p._id} className="song-item">
                <div className="song-item-content">
                  <div className="item-main">
                    {p.coverUrl && (
                      <div className="thumb-wrap">
                        <img
                          className="song-thumb thumb-fixed"
                          src={p.coverUrl}
                          alt={p.title}
                        />
                      </div>
                    )}

                    <div className="item-info">
                      <h3>{p.title}</h3>
                      <p>{p.description || "No description"}</p>
                      <span className="meta-tag">
                        🎵 Có {p.songs?.length || 0} bài hát
                      </span>
                    </div>
                  </div>

                  <div className="action-row">
                    <button
                      type="button"
                      className="secondary-btn small-btn"
                      onClick={() => handleEdit(p)}
                    >
                      Edit
                    </button>

                    <button
                      type="button"
                      className="danger-btn small-btn"
                      onClick={() => handleDelete(p._id)}
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            ))}

            {playlists.length === 0 && (
              <p className="empty-msg">Chưa có Playlist nào.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}