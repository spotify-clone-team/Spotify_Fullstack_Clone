const fs = require("fs");
const path = require("path");
const cloudinary = require("../config/cloudinary");
const User = require("../models/User");

const uploadAvatar = async (req, res, next) => {
  try {
    const userId = req.user.userId; // ✅ Sửa từ id thành userId

    if (!userId) {
      return res.status(401).json({
        success: false,
        message: "Unauthorized: Missing User ID in token"
      });
    }

    if (!req.file) {
      return res.status(400).json({
        success: false,
        message: "Vui lòng chọn ảnh avatar"
      });
    }

    const result = await cloudinary.uploader.upload(req.file.path, {
      folder: "spotify-clone/avatars"
    });

    const updatedUser = await User.findByIdAndUpdate(
      userId,
      { avatarUrl: result.secure_url },
      { new: true }
    ).select("-passwordHash");

    try {
      fs.unlinkSync(path.resolve(req.file.path));
    } catch (error) {
      console.warn("Không xóa được file tạm:", error.message);
    }

    return res.status(200).json({
      success: true,
      message: "Upload avatar thành công",
      data: updatedUser
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  uploadAvatar
};