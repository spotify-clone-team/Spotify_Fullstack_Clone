import { useEffect, useState } from "react";
import api from "../services/api";

export default function DashboardPage() {
  const [songs, setSongs] = useState([]);
  const [artists, setArtists] = useState([]);
  const [albums, setAlbums] = useState([]);
  const [playlists, setPlaylists] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
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
        console.error(error);
      }
    };

    fetchData();
  }, []);

  return (
    <div>
      <h1>Dashboard</h1>
      <p>Tổng quan dữ liệu hệ thống.</p>

      <div className="stats-grid">
        <div className="stat-card">
          <h3>Total Songs</h3>
          <p>{songs.length}</p>
        </div>

        <div className="stat-card">
          <h3>Total Artists</h3>
          <p>{artists.length}</p>
        </div>

        <div className="stat-card">
          <h3>Total Albums</h3>
          <p>{albums.length}</p>
        </div>

        <div className="stat-card">
          <h3>Total Playlists</h3>
          <p>{playlists.length}</p>
        </div>
      </div>
    </div>
  );
}