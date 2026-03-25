import { BrowserRouter, Route, Routes } from "react-router-dom";
import AdminLayout from "../layouts/AdminLayout";
import DashboardPage from "../pages/DashboardPage";
import SongsPage from "../pages/SongsPage";
import ArtistsPage from "../pages/ArtistsPage";
import AlbumsPage from "../pages/AlbumsPage";
import PlaylistsPage from "../pages/PlaylistsPage";
import LoginPage from "../pages/LoginPage";
import ProtectedRoute from "./ProtectedRoute";

export default function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />

        <Route
          path="/"
          element={
            <ProtectedRoute>
              <AdminLayout />
            </ProtectedRoute>
          }
        >
          <Route index element={<DashboardPage />} />
          <Route path="songs" element={<SongsPage />} />
          <Route path="artists" element={<ArtistsPage />} />
          <Route path="albums" element={<AlbumsPage />} />
          <Route path="playlists" element={<PlaylistsPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}