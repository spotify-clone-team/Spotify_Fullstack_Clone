// import { NavLink, useNavigate } from "react-router-dom";

// const menuItems = [
//   { path: "/", label: "Dashboard" },
//   { path: "/songs", label: "Songs" },
//   { path: "/artists", label: "Artists" },
//   { path: "/albums", label: "Albums" },
//   { path: "/playlists", label: "Playlists" }
// ];

// export default function Sidebar() {
//   const navigate = useNavigate();

//   const handleLogout = () => {
//     localStorage.removeItem("token");
//     localStorage.removeItem("user");
//     navigate("/login");
//   };

//   return (
//     <aside className="sidebar">
//       <h2 className="sidebar-title">Spotify Admin</h2>

//       <nav className="sidebar-menu">
//         {menuItems.map((item) => (
//           <NavLink
//             key={item.path}
//             to={item.path}
//             end={item.path === "/"}
//             className={({ isActive }) =>
//               isActive ? "menu-item active" : "menu-item"
//             }
//           >
//             {item.label}
//           </NavLink>
//         ))}
//       </nav>

//       <button className="logout-btn" onClick={handleLogout}>
//         Logout
//       </button>
//     </aside>
//   );
// }
import { NavLink, useNavigate } from "react-router-dom";

const menuItems = [
  { path: "/", label: "Dashboard" },
  { path: "/songs", label: "Songs" },
  { path: "/artists", label: "Artists" },
  { path: "/albums", label: "Albums" },
  { path: "/playlists", label: "Playlists" }
];

export default function Sidebar() {
  const navigate = useNavigate();
  const rawUser = localStorage.getItem("user");
  const user = rawUser ? JSON.parse(rawUser) : null;

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    navigate("/login");
  };

  return (
    <aside className="sidebar">
      <div>
        <h2 className="sidebar-title">Spotify Admin</h2>
        {user && (
          <div className="sidebar-user">
            <strong>{user.name}</strong>
            <span>{user.email}</span>
          </div>
        )}

        <nav className="sidebar-menu">
          {menuItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              end={item.path === "/"}
              className={({ isActive }) =>
                isActive ? "menu-item active" : "menu-item"
              }
            >
              {item.label}
            </NavLink>
          ))}
        </nav>
      </div>

      <button className="logout-btn" onClick={handleLogout}>
        Logout
      </button>
    </aside>
  );
}