// import axios from "axios";

//deploy web server admin
// const api = axios.create({
//   baseURL: "https://spotify-clone-backend-new.onrender.com/api"
// });
// export default api;
import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
});

export default api;