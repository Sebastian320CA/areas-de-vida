-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 26, 2024 at 05:49 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ecogrow`
--

-- --------------------------------------------------------

--
-- Table structure for table `documentation`
--

CREATE TABLE `documentation` (
  `id` int(11) NOT NULL,
  `document_type` varchar(100) NOT NULL,
  `document_number` varchar(50) NOT NULL,
  `issue_date` date DEFAULT NULL,
  `issuing_authority` varchar(100) DEFAULT NULL,
  `living_area_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `documentation`
--

INSERT INTO `documentation` (`id`, `document_type`, `document_number`, `issue_date`, `issuing_authority`, `living_area_id`) VALUES
(7, 'venezolanos ', '404045', '2024-11-28', 'registraduria general de la nacion', NULL),
(9, 'pasaporte', '111', '2024-11-27', 'contraloria', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `forest_censuses`
--

CREATE TABLE `forest_censuses` (
  `id` int(11) NOT NULL,
  `census_date` date NOT NULL,
  `vegetation_coverage` double NOT NULL,
  `ecosystem_state` varchar(255) NOT NULL,
  `observations` text DEFAULT NULL,
  `living_area_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `forest_censuses`
--

INSERT INTO `forest_censuses` (`id`, `census_date`, `vegetation_coverage`, `ecosystem_state`, `observations`, `living_area_id`) VALUES
(1, '2024-11-20', 45.5, '2342', 'sdfgsdf', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `living_areas`
--

CREATE TABLE `living_areas` (
  `id` int(11) NOT NULL,
  `location` varchar(255) NOT NULL,
  `size` decimal(10,2) DEFAULT NULL,
  `conservation_status` enum('Critical','Endangered','Vulnerable','Stable','Improving','At Risk') DEFAULT NULL,
  `creation_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `living_areas`
--

INSERT INTO `living_areas` (`id`, `location`, `size`, `conservation_status`, `creation_date`) VALUES
(105, 'Pacific Region', 4500.80, 'Critical', '2024-02-08'),
(106, 'viracacha', 54.90, 'Improving', '2024-10-28'),
(107, 'tunja', 4677.80, 'Endangered', '2024-11-28');

-- --------------------------------------------------------

--
-- Table structure for table `planting_activities`
--

CREATE TABLE `planting_activities` (
  `id` int(11) NOT NULL,
  `activity_date` date NOT NULL,
  `activity_type` varchar(50) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `living_area_id` int(11) DEFAULT NULL,
  `tree_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `planting_activities`
--

INSERT INTO `planting_activities` (`id`, `activity_date`, `activity_type`, `description`, `living_area_id`, `tree_id`) VALUES
(1, '2024-11-20', 'gsdfgsd', 'gsdfgsdf', 107, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tree_taxonomy`
--

CREATE TABLE `tree_taxonomy` (
  `id` int(11) NOT NULL,
  `species` varchar(100) NOT NULL,
  `genus` varchar(100) DEFAULT NULL,
  `family` varchar(100) DEFAULT NULL,
  `common_name` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tree_taxonomy`
--

INSERT INTO `tree_taxonomy` (`id`, `species`, `genus`, `family`, `common_name`) VALUES
(1, '543535', '6546546', '643634', '65464365');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `Rol` varchar(50) DEFAULT NULL,
  `phone` varchar(15) NOT NULL,
  `document` varchar(50) NOT NULL,
  `userType` varchar(255) DEFAULT 'user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `password`, `Rol`, `phone`, `document`, `userType`) VALUES
(1, 'sebas', 'sebas@gmail.com', '1', NULL, '4343', '4333', 'environmental authority');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `documentation`
--
ALTER TABLE `documentation`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `document_number` (`document_number`),
  ADD KEY `fk_documentation_living_area` (`living_area_id`);

--
-- Indexes for table `forest_censuses`
--
ALTER TABLE `forest_censuses`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_forest_census_living_area` (`living_area_id`);

--
-- Indexes for table `living_areas`
--
ALTER TABLE `living_areas`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `planting_activities`
--
ALTER TABLE `planting_activities`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_planting_activities_living_area` (`living_area_id`),
  ADD KEY `fk_planting_activities_tree` (`tree_id`);

--
-- Indexes for table `tree_taxonomy`
--
ALTER TABLE `tree_taxonomy`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `documentation`
--
ALTER TABLE `documentation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `forest_censuses`
--
ALTER TABLE `forest_censuses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `living_areas`
--
ALTER TABLE `living_areas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=108;

--
-- AUTO_INCREMENT for table `planting_activities`
--
ALTER TABLE `planting_activities`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tree_taxonomy`
--
ALTER TABLE `tree_taxonomy`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `documentation`
--
ALTER TABLE `documentation`
  ADD CONSTRAINT `documentation_ibfk_1` FOREIGN KEY (`living_area_id`) REFERENCES `living_areas` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_documentation_living_area` FOREIGN KEY (`living_area_id`) REFERENCES `living_areas` (`id`);

--
-- Constraints for table `forest_censuses`
--
ALTER TABLE `forest_censuses`
  ADD CONSTRAINT `fk_forest_census_living_area` FOREIGN KEY (`living_area_id`) REFERENCES `living_areas` (`id`);

--
-- Constraints for table `planting_activities`
--
ALTER TABLE `planting_activities`
  ADD CONSTRAINT `fk_planting_activities_living_area` FOREIGN KEY (`living_area_id`) REFERENCES `living_areas` (`id`),
  ADD CONSTRAINT `fk_planting_activities_tree` FOREIGN KEY (`tree_id`) REFERENCES `tree_taxonomy` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
