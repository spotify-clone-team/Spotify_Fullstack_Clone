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
      setMessage("Lỗi tải dữ liệu. Vui lòng thử lại!"); 
    }
  };

  useEffect(() => { fetchData(); }, []);

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

      setForm((prev) => ({ ...prev, coverUrl: response.data.data.coverUrl }));
      setMessage("Upload ảnh cover Playlist thành công!");
    } catch (error) { 
      setMessage("Upload ảnh thất bại!"); 
    } finally { 
      setUploadingImage(false); 
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      if (editingId) {
        await api.put(`/playlists/${editingId}`, form, { headers: getAuthHeaders() });
        setMessage("Cập nhật Playlist thành công!");
      } else {
        await api.post("/playlists", form, { headers: getAuthHeaders() });
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
      title: playlist.title,
      description: playlist.description,
      coverUrl: playlist.coverUrl,
      // Đảm bảo lấy ID bài hát ra cho chuẩn
      songs: (playlist.songs || []).map(s => typeof s === "object" ? s._id : s),
      isPublic: playlist.isPublic ?? true
    });
  };

  // --- HÀM XÓA (DELETE) ---
  const handleDelete = async (id) => {
    if (!window.confirm("Bồ có chắc muốn xóa Playlist này không? Bay màu luôn đó!")) return;
    try {
      await api.delete(`/playlists/${id}`, { headers: getAuthHeaders() });
      setMessage("Xóa Playlist thành công!");
      fetchData();
      if (editingId === id) resetForm();
    } catch (error) { 
      setMessage("Xóa thất bại!"); 
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Manage Playlists</h1>
        <p>Hệ thống quản lý Playlist - CRUD & Cloudinary tích hợp.</p>
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
              onChange={(e) => setForm({...form, title: e.target.value})} 
              required 
            />
            <input 
              name="description" 
              placeholder="Description" 
              value={form.description} 
              onChange={(e) => setForm({...form, description: e.target.value})} 
            />
            
            <div className="upload-group">
              <label className="upload-label">Upload Playlist Cover</label>
              <input type="file" accept="image/*" onChange={handleUploadImage} />
              {uploadingImage && <span className="upload-hint">Đang up lên mây...</span>}
            </div>
            
            <input name="coverUrl" placeholder="Cover URL" value={form.coverUrl} readOnly />
            {form.coverUrl && <img className="cover-preview" src={form.coverUrl} alt="Cover Preview" />}
            
            <button type="submit" className="primary-btn" disabled={loading || uploadingImage}>
               {loading ? "Đang lưu..." : editingId ? "Update Playlist" : "Create Playlist"}
            </button>
          </form>
        </div>
        
        <div className="card">
           <div className="songs-list-header">
             <h2 className="card-title">Playlist List</h2>
             <button className="secondary-btn" onClick={fetchData}>Refresh</button>
           </div>
           <div className="song-list">
              {playlists.map(p => (
                <div key={p._id} className="song-item">
                   <div className="song-item-content">
                     <div style={{display:'flex', gap:'15px', alignItems: 'center'}}>
                       {p.coverUrl && <img className="song-thumb" src={p.coverUrl} alt="" />}
                       <div>
                         <h3>{p.title}</h3>
                         <p>{p.description || "No description"}</p>
                         <span style={{ fontSize: '12px', color: '#888' }}>{p.songs?.length || 0} songs</span>
                       </div>
                     </div>
                     <div className="action-row" style={{marginTop:'10px'}}>
                        <button type="button" className="secondary-btn small-btn" onClick={() => handleEdit(p)}>Edit</button>
                        <button type="button" className="danger-btn small-btn" onClick={() => handleDelete(p._id)}>Delete</button>
                     </div>
                   </div>
                </div>
              ))}
              {playlists.length === 0 && <p style={{color: '#888'}}>Chưa có Playlist nào.</p>}
           </div>
        </div>
      </div>
    </div>
  );
}