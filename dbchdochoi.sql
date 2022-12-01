-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1:3306
-- Thời gian đã tạo: Th12 01, 2022 lúc 04:05 PM
-- Phiên bản máy phục vụ: 5.7.36
-- Phiên bản PHP: 7.4.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `dbchdochoi`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `dochoi`
--

DROP TABLE IF EXISTS `dochoi`;
CREATE TABLE IF NOT EXISTS `dochoi` (
  `masp` varchar(20) NOT NULL,
  `tensp` varchar(100) NOT NULL,
  `maloaisp` varchar(20) NOT NULL,
  `thongtin` varchar(500) NOT NULL,
  `gia` int(11) NOT NULL,
  `hinhanh` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`masp`),
  KEY `maloaisp` (`maloaisp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `dochoi`
--

INSERT INTO `dochoi` (`masp`, `tensp`, `maloaisp`, `thongtin`, `gia`, `hinhanh`) VALUES
('nar01', 'Combo Chibi Naruto', 'NARUTO', 'Combo 6 mô hình 3D chibi các nhân vật trong Naruto\r\nĐộ cao 12 cm', 200000, 'https://cipershop.com/public/userfiles/images/do-choi-mo-hinh/naruto/naruto-2-01316/bo-6-nhan-vat-naruto-bien-hoa.jpg'),
('nar02', 'Uchiha Itachi ', 'NARUTO', 'Mô hình Uchiha Itachi\nMặt trăng led sắc nét màu đậm\nĐộ cao 32cm', 300000, 'https://cf.shopee.vn/file/96f0d2b09e00a1a9477493e530441325'),
('op01', 'Mô hình Luffy Gear4', 'ONEPIECE', 'Luffy Gear4\nMô hình 3D\nĐộ cao 35cm', 228000, 'https://cdn.pancake.vn/1/s1400x1400/b0/bb/7d/e9/ef3830943588b981e6df84f7adbebdc61e27fbafeca8da24272238e7.jpg'),
('op02', 'Zoro Kid', 'ONEPIECE', 'Mô hình Zoro Kid\nMô hình 3D\nĐộ cao 20cm', 100000, 'https://cipershop.com/public/userfiles/products/mo-hinh-zoro-kid-mo-hinh-one-piece-5.jpg'),
('op03', 'Combo Team Luffy', 'ONEPIECE', 'Set mô hình 9 nhân vật băng mũ rơm\nMô hình 3D\nĐộ cao 18 cm', 500000, 'https://cf.shopee.vn/file/ed90c8ffe6fd15523127cf008f309588'),
('op04', 'Sanji Wano', 'ONEPIECE', 'Mô hình Sanji Wano\nĐộ cao 15cm', 100000, 'https://cf.shopee.vn/file/4a0d224f1443b59040ba116b2d2e19d4');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loaisanpham`
--

DROP TABLE IF EXISTS `loaisanpham`;
CREATE TABLE IF NOT EXISTS `loaisanpham` (
  `maloaisp` varchar(20) NOT NULL,
  `tenloaisp` varchar(100) NOT NULL,
  PRIMARY KEY (`maloaisp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `loaisanpham`
--

INSERT INTO `loaisanpham` (`maloaisp`, `tenloaisp`) VALUES
('NARUTO', 'Nhẫn Giả'),
('ONEPIECE', 'Vua Hải Tặc');

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `dochoi`
--
ALTER TABLE `dochoi`
  ADD CONSTRAINT `dochoi_ibfk_1` FOREIGN KEY (`maloaisp`) REFERENCES `loaisanpham` (`maloaisp`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
