import { useEffect, useMemo, useState } from "react";
import api from "../services/api";
import { getAuthHeaders } from "../utils/auth";

const initialForm = {
  title: "",
  artist: "",
  album: "",
  playlist: "", 
  genre: "",
  durationSeconds: "",
  coverUrl: "",
  audioUrl: ""
};

export default function SongsPage() {
  const [songs, setSongs] = useState([]);
  const [artists, setArtists] = useState([]);
  const [albums, setAlbums] = useState([]);
  const [playlists, setPlaylists] = useState([]); 
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
      const artistId = typeof album.artist === "object" ? album.artist?._id : album.artist;
      return artistId === form.artist;
    });
  }, [albums, form.artist]);

  const fetchData = async () => {
    try {
      setFetching(true);
      setMessage("");
      const [songsRes, artistsRes, albumsRes, playlistsRes] = await Promise.all([
        api.get("/songs"),
        api.get("/artists"),
        api.get("/albums"),
        api.get("/playlists") 
      ]);
      setSongs(songsRes.data.data || []);
      setArtists(artistsRes.data.data || []);
      setAlbums(albumsRes.data.data || []);
      setPlaylists(playlistsRes.data.data || []);
    } catch (error) {
      setMessage("Không thể tải dữ liệu hệ thống");
    } finally {
      setFetching(false);
    }
  };

  useEffect(() => { fetchData(); }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "artist") {
      setForm((prev) => ({ ...prev, artist: value, album: "" }));
      return;
    }
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const resetForm = () => { setForm(initialForm); setEditingId(null); };

  const handleEditSong = (song) => {
    setEditingId(song._id);
    const artistId = typeof song.artist === "object" ? song.artist?._id || "" : song.artist || "";
    const albumId = typeof song.album === "object" ? song.album?._id || "" : song.album || "";
    
    setForm({
      title: song.title || "",
      artist: artistId,
      album: albumId,
      playlist: "", 
      genre: song.genre || "",
      durationSeconds: song.durationSeconds || "",
      coverUrl: song.coverUrl || "",
      audioUrl: song.audioUrl || ""
    });
  };

  // --- THÊM HÀM XÓA CHUẨN CRUD ---
  const handleDeleteSong = async (id) => {
    if (!window.confirm("Bạn có chắc chắn muốn xóa bài hát này? Hành động này không thể hoàn tác.")) return;
    try {
      await api.delete(`/songs/${id}`, { headers: getAuthHeaders() });
      setMessage("Xóa bài hát thành công");
      fetchData();
      if (editingId === id) resetForm();
    } catch (error) { 
      setMessage("Xóa bài hát thất bại"); 
    }
  };

  const handleUploadCover = async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;
    try {
      setUploadingCover(true);
      const formData = new FormData();
      formData.append("cover", file);
      const response = await api.post("/songs/upload-cover", formData, { headers: getAuthHeaders() });
      setForm((prev) => ({ ...prev, coverUrl: response.data.data.coverUrl }));
      setMessage("Upload cover lên mây thành công!");
    } catch (error) { setMessage("Upload cover thất bại"); }
    finally { setUploadingCover(false); }
  };

  const handleUploadAudio = async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;
    try {
      setUploadingAudio(true);
      const formData = new FormData();
      formData.append("audio", file);
      const response = await api.post("/songs/upload-audio", formData, { headers: getAuthHeaders() });
      setForm((prev) => ({ ...prev, audioUrl: response.data.data.audioUrl }));
      setMessage("Upload audio lên mây thành công!");
    } catch (error) { setMessage("Upload audio thất bại"); }
    finally { setUploadingAudio(false); }
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

      let songId;
      if (editingId) {
        const res = await api.put(`/songs/${editingId}`, payload, { headers: getAuthHeaders() });
        songId = res.data.data._id;
        setMessage("Cập nhật bài hát thành công");
      } else {
        const res = await api.post("/songs", payload, { headers: getAuthHeaders() });
        songId = res.data.data._id;
        setMessage("Thêm bài hát mới thành công");
      }

      if (form.playlist) {
        await api.put(`/playlists/${form.playlist}`, { 
            $push: { songs: songId } 
        }, { headers: getAuthHeaders() });
      }

      resetForm();
      fetchData();
    } catch (error) { setMessage("Lưu bài hát thất bại"); }
    finally { setLoading(false); }
  };

  return (
    <div className="songs-page">
      <div className="page-header">
        <h1>Manage Songs</h1>
        <p>Hệ thống quản lý bài hát (CRUD) tích hợp Cloudinary Storage.</p>
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
            <input name="title" placeholder="Title" value={form.title} onChange={handleChange} required />

            <select name="artist" value={form.artist} onChange={handleChange} required>
              <option value="">Select Artist</option>
              {artists.map((a) => <option key={a._id} value={a._id}>{a.name}</option>)}
            </select>

            <select name="album" value={form.album} onChange={handleChange}>
              <option value="">No Album</option>
              {filteredAlbums.map((alb) => <option key={alb._id} value={alb._id}>{alb.title}</option>)}
            </select>

            <select name="playlist" value={form.playlist} onChange={handleChange}>
              <option value="">Add to Playlist (Optional)</option>
              {playlists.map((pl) => (
                <option key={pl._id} value={pl._id}>
                  {pl.title}
                </option>
              ))}
            </select>

            <input name="genre" placeholder="Genre" value={form.genre} onChange={handleChange} />
            <input name="durationSeconds" placeholder="Duration (s)" type="number" value={form.durationSeconds} onChange={handleChange} />

            <div className="upload-group">
              <label className="upload-label">Upload Cover</label>
              <input type="file" accept="image/*" onChange={handleUploadCover} />
              {uploadingCover && <span className="upload-hint">Uploading cover...</span>}
            </div>
            <input name="coverUrl" value={form.coverUrl} readOnly placeholder="Cover URL" />

            <div className="upload-group">
              <label className="upload-label">Upload Audio</label>
              <input type="file" accept="audio/*" onChange={handleUploadAudio} />
              {uploadingAudio && <span className="upload-hint">Uploading audio...</span>}
            </div>
            <input name="audioUrl" value={form.audioUrl} readOnly placeholder="Audio URL" required />

            <button type="submit" className="primary-btn" disabled={loading || uploadingCover || uploadingAudio}>
              {editingId ? "Update Song" : "Create Song"}
            </button>
          </form>
        </div>

        <div className="card">
          <div className="songs-list-header">
            <h2 className="card-title">Song List</h2>
            <button className="secondary-btn" onClick={fetchData}>Refresh</button>
          </div>
          <div className="song-list">
            {songs.map((song) => (
              <div className="song-item" key={song._id}>
                <div className="song-item-content">
                  <div style={{display:'flex', gap:'10px'}}>
                    <img className="song-thumb" src={song.coverUrl} alt="" />
                    <div>
                      <h3>{song.title}</h3>
                      <p>{song.artist?.name || "Unknown Artist"}</p>
                    </div>
                  </div>
                  <div className="action-row" style={{marginTop:'10px'}}>
                    <button className="secondary-btn small-btn" onClick={() => handleEditSong(song)}>Edit</button>
                    <button className="danger-btn small-btn" onClick={() => handleDeleteSong(song._id)}>Delete</button>
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