import { Outlet } from "react-router-dom";
import Sidebar from "../components/Sidebar";

export default function AdminLayout() {
  return (
    <div className="admin-layout">
      <div style={{ height: "100vh", position: "fixed", top: 0, left: 0 }}>
        <Sidebar />
      </div>
      <main className="admin-content">
        <Outlet />
      </main>
    </div>
  );
}