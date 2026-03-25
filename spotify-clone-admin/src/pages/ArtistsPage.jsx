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
      formData.append("image", file);

      const response = await api.post("/uploads/image", formData, {
        headers: getAuthHeaders()
      });

      setForm((prev) => ({
        ...prev,
        imageUrl: response.data.data.url
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
      setMessage(error?.response?.data?.message || "Lưu artist thất bại");
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
      if (editingId === id) resetForm();
    } catch (error) {
      setMessage(error?.response?.data?.message || "Xóa artist thất bại");
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Manage Artists</h1>
        <p>Tạo, sửa, xóa nghệ sĩ trực tiếp từ admin.</p>
      </div>

      {message && <div className="alert-box">{message}</div>}

      <div className="songs-grid">
        <div className="card">
          <div className="songs-list-header">
            <h2 className="card-title">{editingId ? "Edit Artist" : "Create Artist"}</h2>
            {editingId && (
              <button type="button" className="secondary-btn" onClick={resetForm}>
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
              <label className="upload-label">Upload Image</label>
              <input type="file" accept="image/*" onChange={handleUploadImage} />
              {uploadingImage && <span className="upload-hint">Uploading image...</span>}
            </div>

            <input
              name="imageUrl"
              placeholder="Image URL"
              value={form.imageUrl}
              onChange={handleChange}
            />

            <input
              name="bio"
              placeholder="Bio"
              value={form.bio}
              onChange={handleChange}
            />

            {form.imageUrl && (
              <img className="cover-preview" src={form.imageUrl} alt="artist preview" />
            )}

            <button type="submit" className="primary-btn" disabled={loading}>
              {loading ? "Saving..." : editingId ? "Update Artist" : "Create Artist"}
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
                  <h3>{artist.name}</h3>
                  <p>{artist.bio || "No bio"}</p>

                  {artist.imageUrl && (
                    <img className="song-thumb" src={artist.imageUrl} alt={artist.name} />
                  )}

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

            {artists.length === 0 && <p>Chưa có artist nào.</p>}
          </div>
        </div>
      </div>
    </div>
  );
}