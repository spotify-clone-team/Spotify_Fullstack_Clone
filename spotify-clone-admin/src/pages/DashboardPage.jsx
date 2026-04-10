import { useEffect, useMemo, useState } from "react";
import {
  Bar,
  BarChart,
  CartesianGrid,
  Cell,
  Legend,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis
} from "recharts";
import api from "../services/api";

const BAR_COLORS = ["#4ade80", "#60a5fa", "#f59e0b", "#a78bfa"];

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

  const stats = useMemo(() => {
    return [
      {
        name: "Songs",
        total: songs.length
      },
      {
        name: "Artists",
        total: artists.length
      },
      {
        name: "Albums",
        total: albums.length
      },
      {
        name: "Playlists",
        total: playlists.length
      }
    ];
  }, [songs.length, artists.length, albums.length, playlists.length]);

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
              So sánh nhanh số lượng Songs, Artists, Albums và Playlists
            </p>
          </div>
        </div>

        <div className="dashboard-chart-wrap">
          <ResponsiveContainer width="100%" height={380}>
            <BarChart
              data={stats}
              margin={{ top: 20, right: 20, left: 0, bottom: 10 }}
            >
              <CartesianGrid strokeDasharray="3 3" stroke="#2b3240" />
              <XAxis
                dataKey="name"
                stroke="#98a2b3"
                tick={{ fill: "#98a2b3", fontSize: 13 }}
              />
              <YAxis
                stroke="#98a2b3"
                tick={{ fill: "#98a2b3", fontSize: 13 }}
                allowDecimals={false}
              />
              <Tooltip
                cursor={{ fill: "rgba(255,255,255,0.04)" }}
                contentStyle={{
                  background: "#171a21",
                  border: "1px solid #2b3240",
                  borderRadius: "12px",
                  color: "#fff"
                }}
                labelStyle={{ color: "#fff", fontWeight: 700 }}
              />
              <Legend />
              <Bar
                dataKey="total"
                name="Số lượng"
                radius={[10, 10, 0, 0]}
                maxBarSize={70}
              >
                {stats.map((entry, index) => (
                  <Cell
                    key={`cell-${entry.name}`}
                    fill={BAR_COLORS[index % BAR_COLORS.length]}
                  />
                ))}
              </Bar>
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
}