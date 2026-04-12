const env = require("../config/env");

const buildFileUrl = (filePath) => {
  const cleanBaseUrl = env.baseUrl.replace(/\/+$/, "");
  const cleanPath = filePath.replace(/\\/g, "/").replace(/^\/+/, "");
  return `${cleanBaseUrl}/${cleanPath}`;
};

const uploadImageFile = async (req, res, next) => {
  try {
    if (!req.file) {
      return res.status(400).json({
        success: false,
        message: "No image file uploaded"
      });
    }

    return res.status(201).json({
      success: true,
      message: "Image uploaded successfully",
      data: {
        filename: req.file.filename,
        url: buildFileUrl(req.file.path)
      }
    });
  } catch (error) {
    next(error);
  }
};

const { cloudinary, cloudinaryConfig } = require("../config/cloudinary");

const getSignature = async (req, res, next) => {
  try {
    const timestamp = Math.round(new Date().getTime() / 1000);
    const signature = cloudinary.utils.api_sign_request(
      { timestamp: timestamp, folder: req.query.folder || "spotify-clone" },
      cloudinaryConfig.api_secret
    );

    res.json({
      success: true,
      data: {
        timestamp,
        signature,
        cloud_name: cloudinaryConfig.cloud_name,
        api_key: cloudinaryConfig.api_key
      }
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  uploadImageFile,
  getSignature
};