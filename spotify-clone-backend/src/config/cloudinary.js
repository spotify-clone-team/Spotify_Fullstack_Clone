const cloudinary = require('cloudinary').v2;
const { CloudinaryStorage } = require('multer-storage-cloudinary');
const multer = require('multer');

// ĐIỀN THÔNG SỐ CỦA BỒ VÀO ĐÂY
const cloudinaryConfig = {
  cloud_name: 'dt6dbeo5u',
  api_key: '551813779419298',
  api_secret: '4nJop1FuCEF3uBxpSkUOdkfMexs'
};

cloudinary.config(cloudinaryConfig);

// Cấu hình kho chứa cho Ảnh
const storageCover = new CloudinaryStorage({
  cloudinary: cloudinary,
  params: {
    folder: 'spotify_covers', // Tên thư mục trên Cloudinary
    allowed_formats: ['jpg', 'png', 'jpeg', 'jfif'],
  },
});

// Cấu hình kho chứa cho Nhạc (Resource_type là auto/video để hỗ trợ mp3)
const storageAudio = new CloudinaryStorage({
  cloudinary: cloudinary,
  params: {
    folder: 'spotify_audio',
    resource_type: 'auto', 
  },
});

const uploadCover = multer({ storage: storageCover });
const uploadAudio = multer({ storage: storageAudio });

module.exports = { cloudinary, uploadCover, uploadAudio, cloudinaryConfig };