import { useEffect, useMemo, useState } from "react";
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

  const chartData = useMemo(() => {
    return [
      { label: "Songs", value: songs.length, colorClass: "songs" },
      { label: "Artists", value: artists.length, colorClass: "artists" },
      { label: "Albums", value: albums.length, colorClass: "albums" },
      { label: "Playlists", value: playlists.length, colorClass: "playlists" }
    ];
  }, [songs.length, artists.length, albums.length, playlists.length]);

  const maxValue = Math.max(...chartData.map((item) => item.value), 1);

  return (
    <div>
      <div className="page-header">
        <h1>Dashboard</h1>
        <p>Tổng quan dữ liệu hệ thống.</p>
      </div>

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

      <div className="card dashboard-chart-card">
        <div className="dashboard-chart-header">
          <div>
            <h2 className="chart-title">Biểu đồ thống kê dữ liệu</h2>
            <p className="chart-subtitle">
              So sánh số lượng Songs, Artists, Albums và Playlists
            </p>
          </div>
        </div>

        <div className="custom-chart">
          <div className="custom-chart-grid">
            <span>100%</span>
            <span>75%</span>
            <span>50%</span>
            <span>25%</span>
            <span>0%</span>
          </div>

          <div className="custom-chart-bars">
            {chartData.map((item) => {
              const heightPercent = (item.value / maxValue) * 100;

              return (
                <div className="custom-bar-col" key={item.label}>
                  <div className="custom-bar-value">{item.value}</div>

                  <div className="custom-bar-track">
                    <div
                      className={`custom-bar-fill ${item.colorClass}`}
                      style={{ height: `${heightPercent}%` }}
                    />
                  </div>

                  <div className="custom-bar-label">{item.label}</div>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
}