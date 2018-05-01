-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 01, 2018 at 08:54 PM
-- Server version: 5.7.22-0ubuntu0.16.04.1
-- PHP Version: 7.0.28-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `Assistant`
--

-- --------------------------------------------------------

--
-- Table structure for table `ingredient`
--

CREATE TABLE `ingredient` (
  `_ID` int(11) NOT NULL,
  `name` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ingredient`
--

INSERT INTO `ingredient` (`_ID`, `name`) VALUES
(1, 'New Eggs'),
(2, 'New Chicken '),
(3, 'New Minced '),
(4, 'New Garlic'),
(5, 'New Onions'),
(6, 'New Corn'),
(7, 'New Asparagus');

-- --------------------------------------------------------

--
-- Table structure for table `recipe`
--

CREATE TABLE `recipe` (
  `_ID` int(11) NOT NULL,
  `title` varchar(250) NOT NULL,
  `cookTime` varchar(15) NOT NULL,
  `description` varchar(2000) NOT NULL,
  `instructions` varchar(5000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `recipe`
--

INSERT INTO `recipe` (`_ID`, `title`, `cookTime`, `description`, `instructions`) VALUES
(1, 'BestFood', '1:15', 'The best food in the world.', 'Do the tings.');

-- --------------------------------------------------------

--
-- Table structure for table `recipeContent`
--

CREATE TABLE `recipeContent` (
  `recipeId` int(11) NOT NULL,
  `ingredientId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `recipeContent`
--

INSERT INTO `recipeContent` (`recipeId`, `ingredientId`) VALUES
(1, 1),
(1, 2),
(1, 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ingredient`
--
ALTER TABLE `ingredient`
  ADD PRIMARY KEY (`_ID`);

--
-- Indexes for table `recipe`
--
ALTER TABLE `recipe`
  ADD PRIMARY KEY (`_ID`);

--
-- Indexes for table `recipeContent`
--
ALTER TABLE `recipeContent`
  ADD PRIMARY KEY (`recipeId`,`ingredientId`),
  ADD KEY `ingredientId` (`ingredientId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ingredient`
--
ALTER TABLE `ingredient`
  MODIFY `_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `recipe`
--
ALTER TABLE `recipe`
  MODIFY `_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `recipeContent`
--
ALTER TABLE `recipeContent`
  ADD CONSTRAINT `recipeContent_ibfk_1` FOREIGN KEY (`recipeId`) REFERENCES `recipe` (`_ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `recipeContent_ibfk_2` FOREIGN KEY (`ingredientId`) REFERENCES `ingredient` (`_ID`) ON DELETE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
