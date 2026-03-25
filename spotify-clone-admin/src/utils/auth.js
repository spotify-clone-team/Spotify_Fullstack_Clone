export const getToken = () => localStorage.getItem("token");

export const getAuthHeaders = () => ({
  Authorization: `Bearer ${getToken()}`
});