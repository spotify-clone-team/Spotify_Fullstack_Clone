import axios from "axios";

const api = axios.create({
  baseURL: "https://spotify-clone-backend-0t69.onrender.com/api"
});

export default api;