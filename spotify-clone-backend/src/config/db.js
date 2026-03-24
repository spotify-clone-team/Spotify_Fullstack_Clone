const mongoose = require("mongoose");
const dns = require("dns");
const env = require("./env");

dns.setDefaultResultOrder("ipv4first");

const connectDatabase = async () => {
  try {
    await mongoose.connect(env.mongoUri);
    console.log("MongoDB connected");
  } catch (error) {
    console.error("MongoDB connection failed:", error.message);
    process.exit(1);
  }
};

module.exports = connectDatabase;