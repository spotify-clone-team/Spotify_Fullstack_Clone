require("dotenv").config();
module.exports = {
  port: process.env.PORT || 5000,
  mongoUri: process.env.MONGODB_URI,
  jwtSecret: process.env.JWT_SECRET,
  baseUrl: process.env.BASE_URL || "http://localhost:5000"
};