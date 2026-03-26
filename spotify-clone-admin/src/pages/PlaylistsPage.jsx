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
    } catch (error) { setMessage("Lỗi tải dữ liệu"); }
  };

  useEffect(() => { fetchData(); }, []);

  // --- FIX UPLOAD CLOUDINARY CHO PLAYLIST ---
  const handleUploadImage = async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    try {
      setUploadingImage(true);
      const formData = new FormData();
      formData.append("cover", file); // Dùng key 'cover'

      const response = await api.post("/songs/upload-cover", formData, {
        headers: getAuthHeaders()
      });

      setForm((prev) => ({
        ...prev,
        coverUrl: response.data.data.coverUrl // Nhận link Cloudinary
      }));
      setMessage("Upload ảnh playlist thành công");
    } catch (error) { setMessage("Upload thất bại"); }
    finally { setUploadingImage(false); }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      if (editingId) {
        await api.put(`/playlists/${editingId}`, form, { headers: getAuthHeaders() });
      } else {
        await api.post("/playlists", form, { headers: getAuthHeaders() });
      }
      setForm(initialForm); setEditingId(null);
      fetchData();
    } catch (error) { setMessage("Lưu thất bại"); }
    finally { setLoading(false); }
  };

  const handleEdit = (playlist) => {
    setEditingId(playlist._id);
    setForm({
      title: playlist.title,
      description: playlist.description,
      coverUrl: playlist.coverUrl,
      songs: (playlist.songs || []).map(s => s._id),
      isPublic: playlist.isPublic
    });
  };

  return (
    <div>
      <h1>Manage Playlists</h1>
      <div className="songs-grid">
        <div className="card">
          <form className="song-form" onSubmit={handleSubmit}>
            <input name="title" placeholder="Playlist title" value={form.title} onChange={(e)=>setForm({...form, title: e.target.value})} required />
            <div className="upload-group">
              <label className="upload-label">Upload Playlist Cover</label>
              <input type="file" accept="image/*" onChange={handleUploadImage} />
            </div>
            <input name="coverUrl" value={form.coverUrl} readOnly />
            {form.coverUrl && <img className="cover-preview" src={form.coverUrl} alt="" />}
            <button type="submit" className="primary-btn" disabled={loading || uploadingImage}>Save Playlist</button>
          </form>
        </div>
        <div className="card">
           <div className="song-list">
              {playlists.map(p => (
                <div key={p._id} className="song-item">
                   <img className="song-thumb" src={p.coverUrl} alt="" />
                   <div className="song-item-content">
                      <h3>{p.title}</h3>
                      <button className="secondary-btn small-btn" onClick={() => handleEdit(p)}>Edit</button>
                   </div>
                </div>
              ))}
           </div>
        </div>
      </div>
    </div>
  );
}