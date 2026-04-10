import { useEffect, useState } from "react";
import api from "../services/api";
import { getAuthHeaders } from "../utils/auth";

const initialForm = {
  name: "",
  imageUrl: "",
  bio: ""
};

export default function ArtistsPage() {
  const [artists, setArtists] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const [uploadingImage, setUploadingImage] = useState(false);

  const fetchArtists = async () => {
    try {
      const res = await api.get("/artists");
      setArtists(res.data.data || []);
    } catch (error) {
      setMessage("Không thể tải artists");
    }
  };

  useEffect(() => {
    fetchArtists();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleUploadImage = async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    try {
      setUploadingImage(true);
      setMessage("");

      const formData = new FormData();
      formData.append("cover", file);

      const response = await api.post("/songs/upload-cover", formData, {
        headers: getAuthHeaders()
      });

      setForm((prev) => ({
        ...prev,
        imageUrl: response.data.data.coverUrl
      }));

      setMessage("Upload ảnh nghệ sĩ lên Cloudinary thành công");
    } catch (error) {
      setMessage("Upload ảnh thất bại");
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

      if (editingId) {
        await api.put(`/artists/${editingId}`, form, {
          headers: getAuthHeaders()
        });
        setMessage("Cập nhật artist thành công");
      } else {
        await api.post("/artists", form, {
          headers: getAuthHeaders()
        });
        setMessage("Tạo artist thành công");
      }

      resetForm();
      fetchArtists();
    } catch (error) {
      setMessage("Lưu artist thất bại");
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (artist) => {
    setEditingId(artist._id);
    setForm({
      name: artist.name || "",
      imageUrl: artist.imageUrl || "",
      bio: artist.bio || ""
    });
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Xóa artist này?")) return;

    try {
      await api.delete(`/artists/${id}`, {
        headers: getAuthHeaders()
      });
      setMessage("Xóa artist thành công");
      fetchArtists();

      if (editingId === id) {
        resetForm();
      }
    } catch (error) {
      setMessage("Xóa thất bại");
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Manage Artists</h1>
        <p>Quản lý nghệ sĩ và ảnh đại diện lưu trữ trên Cloudinary.</p>
      </div>

      {message && <div className="alert-box">{message}</div>}

      <div className="songs-grid">
        <div className="card">
          <div className="songs-list-header">
            <h2 className="card-title">
              {editingId ? "Edit Artist" : "Create Artist"}
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
              name="name"
              placeholder="Artist name"
              value={form.name}
              onChange={handleChange}
              required
            />

            <div className="upload-group">
              <label className="upload-label">Upload Artist Photo</label>
              <input type="file" accept="image/*" onChange={handleUploadImage} />
              {uploadingImage && (
                <span className="upload-hint">Đang upload ảnh...</span>
              )}
            </div>

            <input
              name="imageUrl"
              placeholder="Image URL"
              value={form.imageUrl}
              readOnly
            />

            <input
              name="bio"
              placeholder="Bio"
              value={form.bio}
              onChange={handleChange}
            />

            {form.imageUrl && (
              <div className="preview-container">
                <p className="preview-label">Preview:</p>
                <div className="cover-preview-wrap">
                  <img
                    className="cover-preview fixed-cover-preview"
                    src={form.imageUrl}
                    alt="artist preview"
                  />
                </div>
              </div>
            )}

            <button
              type="submit"
              className="primary-btn"
              disabled={loading || uploadingImage}
            >
              {loading
                ? "Saving..."
                : editingId
                ? "Update Artist"
                : "Create Artist"}
            </button>
          </form>
        </div>

        <div className="card">
          <div className="songs-list-header">
            <h2 className="card-title">Artist List</h2>
            <button type="button" className="secondary-btn" onClick={fetchArtists}>
              Refresh
            </button>
          </div>

          <div className="song-list">
            {artists.map((artist) => (
              <div className="song-item" key={artist._id}>
                <div className="song-item-content">
                  <div className="item-main">
                    {artist.imageUrl && (
                      <div className="thumb-wrap">
                        <img
                          className="song-thumb thumb-fixed"
                          src={artist.imageUrl}
                          alt={artist.name}
                        />
                      </div>
                    )}

                    <div className="item-info">
                      <h3>{artist.name}</h3>
                      <p>{artist.bio || "No bio"}</p>
                    </div>
                  </div>

                  <div className="action-row">
                    <button
                      type="button"
                      className="secondary-btn small-btn"
                      onClick={() => handleEdit(artist)}
                    >
                      Edit
                    </button>

                    <button
                      type="button"
                      className="danger-btn small-btn"
                      onClick={() => handleDelete(artist._id)}
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            ))}

            {artists.length === 0 && (
              <p className="empty-msg">Chưa có artist nào.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}