import { useEffect, useState } from "react";
import api from "../services/api";
import { getAuthHeaders } from "../utils/auth";

const initialForm = {
  title: "",
  artist: "",
  coverUrl: "",
  releaseYear: ""
};

export default function AlbumsPage() {
  const [albums, setAlbums] = useState([]);
  const [artists, setArtists] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const [uploadingImage, setUploadingImage] = useState(false);

  const fetchData = async () => {
    try {
      const [albumsRes, artistsRes] = await Promise.all([
        api.get("/albums"),
        api.get("/artists")
      ]);
      setAlbums(albumsRes.data.data || []);
      setArtists(artistsRes.data.data || []);
    } catch (error) {
      setMessage("Không thể tải albums/artists");
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  // --- ĐÂY LÀ CHỖ QUAN TRỌNG ĐÃ FIX CHO CLOUDINARY ---
  const handleUploadImage = async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    try {
      setUploadingImage(true);
      setMessage("");

      const formData = new FormData();
      // FIX 1: Đổi tên key từ "image" thành "cover" để khớp với BE
      formData.append("cover", file); 

      // FIX 2: Đổi endpoint từ "/uploads/image" thành "/songs/upload-cover"
      const response = await api.post("/songs/upload-cover", formData, {
        headers: getAuthHeaders()
      });

      setForm((prev) => ({
        ...prev,
        // FIX 3: Lấy đúng key "coverUrl" từ data.data
        coverUrl: response.data.data.coverUrl 
      }));

      setMessage("Upload ảnh lên Cloudinary thành công!");
    } catch (error) {
      console.error(error);
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

      const payload = {
        ...form,
        releaseYear: form.releaseYear ? Number(form.releaseYear) : null
      };

      if (editingId) {
        await api.put(`/albums/${editingId}`, payload, {
          headers: getAuthHeaders()
        });
        setMessage("Cập nhật album thành công");
      } else {
        await api.post("/albums", payload, {
          headers: getAuthHeaders()
        });
        setMessage("Tạo album thành công");
      }

      resetForm();
      fetchData();
    } catch (error) {
      setMessage(error?.response?.data?.message || "Lưu album thất bại");
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (album) => {
    setEditingId(album._id);
    setForm({
      title: album.title || "",
      artist: album.artist?._id || "",
      coverUrl: album.coverUrl || "",
      releaseYear: album.releaseYear || ""
    });
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Xóa album này?")) return;

    try {
      await api.delete(`/albums/${id}`, {
        headers: getAuthHeaders()
      });
      setMessage("Xóa album thành công");
      fetchData();
      if (editingId === id) resetForm();
    } catch (error) {
      setMessage(error?.response?.data?.message || "Xóa album thất bại");
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Manage Albums</h1>
        <p>Tạo album từ artist đã có (Lưu trữ trên Cloudinary).</p>
      </div>

      {message && <div className="alert-box">{message}</div>}

      <div className="songs-grid">
        {/* FORM CARD */}
        <div className="card">
          <div className="songs-list-header">
            <h2 className="card-title">{editingId ? "Edit Album" : "Create Album"}</h2>
            {editingId && (
              <button type="button" className="secondary-btn" onClick={resetForm}>
                Cancel Edit
              </button>
            )}
          </div>

          <form className="song-form" onSubmit={handleSubmit}>
            <input
              name="title"
              placeholder="Album title"
              value={form.title}
              onChange={handleChange}
              required
            />

            <select name="artist" value={form.artist} onChange={handleChange} required>
              <option value="">Select artist</option>
              {artists.map((artist) => (
                <option key={artist._id} value={artist._id}>
                  {artist.name}
                </option>
              ))}
            </select>

            <div className="upload-group">
              <label className="upload-label">Upload Cover to Cloudinary</label>
              <input type="file" accept="image/*" onChange={handleUploadImage} />
              {uploadingImage && <span className="upload-hint">Uploading...</span>}
            </div>

            <input
              name="coverUrl"
              placeholder="Cover URL (Tự động điền sau khi upload)"
              value={form.coverUrl}
              onChange={handleChange}
              readOnly
              style={{ backgroundColor: '#1a1a1a', color: '#888' }}
            />

            <input
              name="releaseYear"
              placeholder="Release year"
              type="number"
              value={form.releaseYear}
              onChange={handleChange}
            />

            {form.coverUrl && (
              <div className="preview-container">
                 <p style={{fontSize: '12px', color: '#aaa'}}>Preview:</p>
                 <img className="cover-preview" src={form.coverUrl} alt="album preview" />
              </div>
            )}

            <button type="submit" className="primary-btn" disabled={loading || uploadingImage}>
              {loading ? "Saving..." : editingId ? "Update Album" : "Create Album"}
            </button>
          </form>
        </div>

        {/* LIST CARD */}
        <div className="card">
          <div className="songs-list-header">
            <h2 className="card-title">Album List</h2>
            <button type="button" className="secondary-btn" onClick={fetchData}>
              Refresh
            </button>
          </div>

          <div className="song-list">
            {albums.map((album) => (
              <div className="song-item" key={album._id}>
                <div className="song-item-content">
                  <div style={{ display: 'flex', gap: '15.dp', alignItems: 'center' }}>
                    {album.coverUrl && (
                      <img className="song-thumb" src={album.coverUrl} alt={album.title} />
                    )}
                    <div>
                      <h3>{album.title}</h3>
                      <p>{album.artist?.name || "Unknown artist"}</p>
                      <span className="year-tag">{album.releaseYear || "No year"}</span>
                    </div>
                  </div>

                  <div className="action-row" style={{ marginTop: '10.dp' }}>
                    <button
                      type="button"
                      className="secondary-btn small-btn"
                      onClick={() => handleEdit(album)}
                    >
                      Edit
                    </button>
                    <button
                      type="button"
                      className="danger-btn small-btn"
                      onClick={() => handleDelete(album._id)}
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            ))}

            {albums.length === 0 && <p className="empty-msg">Chưa có album nào trong bộ sưu tập.</p>}
          </div>
        </div>
      </div>
    </div>
  );
}