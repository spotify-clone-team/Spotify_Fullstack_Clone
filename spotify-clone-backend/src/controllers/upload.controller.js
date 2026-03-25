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

module.exports = {
  uploadImageFile
};