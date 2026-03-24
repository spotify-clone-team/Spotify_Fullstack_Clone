const express = require("express");
const authMiddleware = require("../middleware/auth.middleware");
const {
  register,
  login,
  me
} = require("../controllers/auth.controller");

const router = express.Router();

router.post("/register", register);
router.post("/login", login);
router.get("/me", authMiddleware, me);

module.exports = router;