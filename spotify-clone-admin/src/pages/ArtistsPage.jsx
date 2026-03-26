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

  useEffect(() => { fetchArtists(); }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  // --- FIX UPLOAD CLOUDINARY CHO ARTIST ---
  const handleUploadImage = async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    try {
      setUploadingImage(true);
      setMessage("");

      const formData = new FormData();
      formData.append("cover", file); // Đổi từ image -> cover

      const response = await api.post("/songs/upload-cover", formData, {
        headers: getAuthHeaders()
      });

      setForm((prev) => ({
        ...prev,
        imageUrl: response.data.data.coverUrl // Lấy coverUrl gán vào imageUrl
      }));

      setMessage("Upload ảnh nghệ sĩ lên Cloudinary thành công");
    } catch (error) {
      setMessage("Upload ảnh thất bại");
    } finally {
      setUploadingImage(false);
    }
  };

  const resetForm = () => { setForm(initialForm); setEditingId(null); };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      if (editingId) {
        await api.put(`/artists/${editingId}`, form, { headers: getAuthHeaders() });
        setMessage("Cập nhật artist thành công");
      } else {
        await api.post("/artists", form, { headers: getAuthHeaders() });
        setMessage("Tạo artist thành công");
      }
      resetForm();
      fetchArtists();
    } catch (error) {
      setMessage("Lưu artist thất bại");
    } finally { setLoading(false); }
  };

  const handleEdit = (artist) => {
    setEditingId(artist._id);
    setForm({ name: artist.name || "", imageUrl: artist.imageUrl || "", bio: artist.bio || "" });
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Xóa artist này?")) return;
    try {
      await api.delete(`/artists/${id}`, { headers: getAuthHeaders() });
      fetchArtists();
    } catch (error) { setMessage("Xóa thất bại"); }
  };

  return (
    <div>
      <h1>Manage Artists</h1>
      {message && <div className="alert-box">{message}</div>}
      <div className="songs-grid">
        <div className="card">
          <form className="song-form" onSubmit={handleSubmit}>
            <input name="name" placeholder="Artist name" value={form.name} onChange={handleChange} required />
            <div className="upload-group">
              <label className="upload-label">Upload Artist Photo</label>
              <input type="file" accept="image/*" onChange={handleUploadImage} />
            </div>
            <input name="imageUrl" placeholder="Image URL" value={form.imageUrl} readOnly />
            <input name="bio" placeholder="Bio" value={form.bio} onChange={handleChange} />
            {form.imageUrl && <img className="cover-preview" src={form.imageUrl} alt="preview" />}
            <button type="submit" className="primary-btn" disabled={loading || uploadingImage}>
              {editingId ? "Update Artist" : "Create Artist"}
            </button>
          </form>
        </div>
        <div className="card">
          <div className="song-list">
            {artists.map((artist) => (
              <div className="song-item" key={artist._id}>
                {artist.imageUrl && <img className="song-thumb" src={artist.imageUrl} alt="" />}
                <div className="song-item-content">
                  <h3>{artist.name}</h3>
                  <div className="action-row">
                    <button className="secondary-btn small-btn" onClick={() => handleEdit(artist)}>Edit</button>
                    <button className="danger-btn small-btn" onClick={() => handleDelete(artist._id)}>Delete</button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}