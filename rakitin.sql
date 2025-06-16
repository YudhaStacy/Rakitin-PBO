-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 15, 2025 at 10:34 AM
-- Server version: 8.4.3
-- PHP Version: 8.3.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rakitin`
--

-- --------------------------------------------------------

--
-- Table structure for table `absensi`
--

CREATE TABLE `absensi` (
  `id_absensi` varchar(30) NOT NULL,
  `tanggal` date DEFAULT NULL,
  `waktu` time DEFAULT NULL,
  `status_absensi` enum('hadir','izin','sakit','alpha') DEFAULT NULL,
  `keterangan` varchar(225) DEFAULT NULL,
  `id_karyawan` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `barang`
--

CREATE TABLE `barang` (
  `id_barang` varchar(30) NOT NULL,
  `nama_barang` varchar(50) DEFAULT NULL,
  `stok` int DEFAULT NULL,
  `satuan` varchar(10) DEFAULT NULL,
  `harga_beli` decimal(10,2) DEFAULT NULL,
  `harga_jual` decimal(10,2) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `barang`
--

INSERT INTO `barang` (`id_barang`, `nama_barang`, `stok`, `satuan`, `harga_beli`, `harga_jual`, `status`) VALUES
('BRG001', 'Mouse Logitech', 40, 'pcs', 75000.00, 75000.00, NULL),
('BRG002', 'Keyboard Mechanical', 30, 'pcs', 250000.00, 250000.00, NULL),
('BRG003', 'Monitor 24 Inch', 0, 'unit', 1400000.00, 1400000.00, NULL),
('BRG004', 'Flashdisk 32GB', 1, 'pcs', 50000.00, 50000.00, NULL),
('BRG005', 'Kabel HDMI 2m', 80, 'pcs', 30000.00, 35000.00, NULL),
('BRG006', 'kayu', 10, 'pcs', 100000.00, 110000.00, 'Tersedia');

-- --------------------------------------------------------

--
-- Table structure for table `detail_pembelian`
--

CREATE TABLE `detail_pembelian` (
  `id_detail_pembelian` int NOT NULL,
  `jumlah` int DEFAULT NULL,
  `id_pembelian` varchar(30) DEFAULT NULL,
  `id_barang` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `detail_pembelian`
--

INSERT INTO `detail_pembelian` (`id_detail_pembelian`, `jumlah`, `id_pembelian`, `id_barang`) VALUES
(1, 1, 'PMB-20250615-001', 'BRG005');

-- --------------------------------------------------------

--
-- Table structure for table `detail_pesanan`
--

CREATE TABLE `detail_pesanan` (
  `id_detail_pesanan` int NOT NULL,
  `jumlah` int DEFAULT NULL,
  `harga_satuan` decimal(10,2) DEFAULT NULL,
  `id_pesanan` varchar(30) DEFAULT NULL,
  `id_barang` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `detail_pesanan`
--

INSERT INTO `detail_pesanan` (`id_detail_pesanan`, `jumlah`, `harga_satuan`, `id_pesanan`, `id_barang`) VALUES
(2, 1, 50000.00, 'PSN-20250607-001', 'BRG004'),
(3, 20, 1400000.00, 'PSN-20250608-001', 'BRG003'),
(4, 10, 75000.00, 'PSN-20250608-002', 'BRG001'),
(5, 1, 35000.00, 'PSN-20250615-001', 'BRG005');

-- --------------------------------------------------------

--
-- Table structure for table `karyawan`
--

CREATE TABLE `karyawan` (
  `email` varchar(100) DEFAULT NULL,
  `id_karyawan` varchar(30) NOT NULL,
  `nama_karyawan` varchar(50) DEFAULT NULL,
  `no_tlp` varchar(15) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('admin','user') DEFAULT 'admin',
  `status` enum('Aktif','Tidak Aktif') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'Aktif'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `karyawan`
--

INSERT INTO `karyawan` (`email`, `id_karyawan`, `nama_karyawan`, `no_tlp`, `password`, `role`, `status`) VALUES
('admin@admin.com', 'KRY001', 'Ahmad Subari', '081234567891', '202cb962ac59075b964b07152d234b70', 'admin', 'Aktif'),
('user1@user.com', 'KRY002', 'Siti Rahayu', '081234567892', '202cb962ac59075b964b07152d234b70', 'admin', 'Aktif'),
('user2@user.com', 'KRY003', 'Budi Santoso', '081234567893', 'd9b1d7db4cd6e70935368a1efb10e377', 'admin', 'Tidak Aktif');

-- --------------------------------------------------------

--
-- Table structure for table `pelanggan`
--

CREATE TABLE `pelanggan` (
  `id_pelanggan` varchar(30) NOT NULL,
  `nama_pelanggan` varchar(50) DEFAULT NULL,
  `alamat` varchar(100) DEFAULT NULL,
  `no_tlp` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `pelanggan`
--

INSERT INTO `pelanggan` (`id_pelanggan`, `nama_pelanggan`, `alamat`, `no_tlp`) VALUES
('PLG001', 'Rahmat Hidayat', 'Jl. Melati No. 10', '082112345678'),
('PLG002', 'Dewi Kartika', 'Jl. Anggrek No. 20', '082212345678'),
('PLG003', 'Andi Nugroho', 'Jl. Mawar No. 5', '082312345678');

-- --------------------------------------------------------

--
-- Table structure for table `pemasok`
--

CREATE TABLE `pemasok` (
  `id_pemasok` varchar(30) NOT NULL,
  `nama_pemasok` varchar(50) DEFAULT NULL,
  `alamat` varchar(100) DEFAULT NULL,
  `no_tlp` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `pemasok`
--

INSERT INTO `pemasok` (`id_pemasok`, `nama_pemasok`, `alamat`, `no_tlp`) VALUES
('PMS001', 'PT. Sumber Makmur', 'Jl. Industri No. 12', '081322223333'),
('PMS002', 'CV. Surya Abadi', 'Jl. Perdagangan No. 45', '081344445555'),
('PMS003', 'UD. Toko Jaya', 'Jl. Grosir No. 7', '081366667777');

-- --------------------------------------------------------

--
-- Table structure for table `pembelian`
--

CREATE TABLE `pembelian` (
  `id_pembelian` varchar(30) NOT NULL,
  `tanggal` datetime DEFAULT CURRENT_TIMESTAMP,
  `total_harga` decimal(10,2) DEFAULT NULL,
  `id_pemasok` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `pembelian`
--

INSERT INTO `pembelian` (`id_pembelian`, `tanggal`, `total_harga`, `id_pemasok`) VALUES
('PMB-20250615-001', '2025-06-15 18:33:31', 30000.00, 'PMS001');

-- --------------------------------------------------------

--
-- Table structure for table `pesanan`
--

CREATE TABLE `pesanan` (
  `id_pesanan` varchar(30) NOT NULL,
  `tanggal` datetime DEFAULT CURRENT_TIMESTAMP,
  `total_harga` decimal(10,2) DEFAULT '0.00',
  `id_pelanggan` varchar(30) DEFAULT NULL,
  `id_karyawan` varchar(30) DEFAULT NULL,
  `diskon_persen` double DEFAULT '0',
  `nilai_diskon` double DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `pesanan`
--

INSERT INTO `pesanan` (`id_pesanan`, `tanggal`, `total_harga`, `id_pelanggan`, `id_karyawan`, `diskon_persen`, `nilai_diskon`) VALUES
('PSN-20250607-001', '2025-06-07 00:00:00', 50000.00, NULL, NULL, 0, 0),
('PSN-20250608-001', '2025-06-08 00:00:00', 28000000.00, 'PLG001', NULL, 0, 0),
('PSN-20250608-002', '2025-06-08 00:00:00', 675000.00, 'PLG001', NULL, 10, 75000),
('PSN-20250608-003', '2025-06-08 00:00:00', 28000000.00, 'PLG001', NULL, 0, 0),
('PSN-20250608-004', '2025-06-08 00:00:00', 28000000.00, 'PLG001', NULL, 0, 0),
('PSN-20250608-005', '2025-06-08 00:00:00', 28000000.00, 'PLG001', NULL, 0, 0),
('PSN-20250608-006', '2025-06-08 00:00:00', 28000000.00, 'PLG001', NULL, 0, 0),
('PSN-20250608-007', '2025-06-08 00:00:00', 28000000.00, 'PLG001', NULL, 0, 0),
('PSN-20250608-008', '2025-06-08 00:00:00', 28000000.00, 'PLG001', NULL, 0, 0),
('PSN-20250608-009', '2025-06-08 00:00:00', 28000000.00, 'PLG001', NULL, 0, 0),
('PSN-20250608-010', '2025-06-08 00:00:00', 28000000.00, 'PLG001', NULL, 0, 0),
('PSN-20250615-001', '2025-06-15 00:00:00', 29750.00, 'PLG001', NULL, 15, 5250);

-- --------------------------------------------------------

--
-- Table structure for table `stok_opname`
--

CREATE TABLE `stok_opname` (
  `id_stok_opname` varchar(30) NOT NULL,
  `tanggal` date DEFAULT NULL,
  `stok_fisik` int DEFAULT NULL,
  `keterangan` varchar(255) DEFAULT NULL,
  `id_barang` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `absensi`
--
ALTER TABLE `absensi`
  ADD PRIMARY KEY (`id_absensi`),
  ADD KEY `id_karyawan` (`id_karyawan`);

--
-- Indexes for table `barang`
--
ALTER TABLE `barang`
  ADD PRIMARY KEY (`id_barang`);

--
-- Indexes for table `detail_pembelian`
--
ALTER TABLE `detail_pembelian`
  ADD PRIMARY KEY (`id_detail_pembelian`),
  ADD KEY `id_pembelian` (`id_pembelian`),
  ADD KEY `id_barang` (`id_barang`);

--
-- Indexes for table `detail_pesanan`
--
ALTER TABLE `detail_pesanan`
  ADD PRIMARY KEY (`id_detail_pesanan`),
  ADD KEY `id_pesanan` (`id_pesanan`),
  ADD KEY `id_barang` (`id_barang`);

--
-- Indexes for table `karyawan`
--
ALTER TABLE `karyawan`
  ADD PRIMARY KEY (`id_karyawan`);

--
-- Indexes for table `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`id_pelanggan`);

--
-- Indexes for table `pemasok`
--
ALTER TABLE `pemasok`
  ADD PRIMARY KEY (`id_pemasok`);

--
-- Indexes for table `pembelian`
--
ALTER TABLE `pembelian`
  ADD PRIMARY KEY (`id_pembelian`),
  ADD KEY `id_pemasok` (`id_pemasok`);

--
-- Indexes for table `pesanan`
--
ALTER TABLE `pesanan`
  ADD PRIMARY KEY (`id_pesanan`),
  ADD KEY `id_pelanggan` (`id_pelanggan`),
  ADD KEY `id_karyawan` (`id_karyawan`);

--
-- Indexes for table `stok_opname`
--
ALTER TABLE `stok_opname`
  ADD PRIMARY KEY (`id_stok_opname`),
  ADD KEY `id_barang` (`id_barang`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `detail_pembelian`
--
ALTER TABLE `detail_pembelian`
  MODIFY `id_detail_pembelian` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `detail_pesanan`
--
ALTER TABLE `detail_pesanan`
  MODIFY `id_detail_pesanan` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `absensi`
--
ALTER TABLE `absensi`
  ADD CONSTRAINT `absensi_ibfk_1` FOREIGN KEY (`id_karyawan`) REFERENCES `karyawan` (`id_karyawan`);

--
-- Constraints for table `detail_pembelian`
--
ALTER TABLE `detail_pembelian`
  ADD CONSTRAINT `detail_pembelian_ibfk_1` FOREIGN KEY (`id_pembelian`) REFERENCES `pembelian` (`id_pembelian`),
  ADD CONSTRAINT `detail_pembelian_ibfk_2` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`id_barang`);

--
-- Constraints for table `detail_pesanan`
--
ALTER TABLE `detail_pesanan`
  ADD CONSTRAINT `detail_pesanan_ibfk_1` FOREIGN KEY (`id_pesanan`) REFERENCES `pesanan` (`id_pesanan`),
  ADD CONSTRAINT `detail_pesanan_ibfk_2` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`id_barang`);

--
-- Constraints for table `pembelian`
--
ALTER TABLE `pembelian`
  ADD CONSTRAINT `pembelian_ibfk_1` FOREIGN KEY (`id_pemasok`) REFERENCES `pemasok` (`id_pemasok`);

--
-- Constraints for table `pesanan`
--
ALTER TABLE `pesanan`
  ADD CONSTRAINT `pesanan_ibfk_1` FOREIGN KEY (`id_pelanggan`) REFERENCES `pelanggan` (`id_pelanggan`),
  ADD CONSTRAINT `pesanan_ibfk_2` FOREIGN KEY (`id_karyawan`) REFERENCES `karyawan` (`id_karyawan`);

--
-- Constraints for table `stok_opname`
--
ALTER TABLE `stok_opname`
  ADD CONSTRAINT `stok_opname_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`id_barang`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
