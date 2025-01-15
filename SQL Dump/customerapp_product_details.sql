-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: customerapp
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `product_details`
--

DROP TABLE IF EXISTS `product_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_details` (
  `product_id` bigint NOT NULL,
  `color` varchar(255) DEFAULT NULL,
  `image_data` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `quantity` int NOT NULL,
  `size` varchar(255) DEFAULT NULL,
  `sku` varchar(255) DEFAULT NULL,
  KEY `FKrhahp4f26x99lqf0kybcs79rb` (`product_id`),
  CONSTRAINT `FKrhahp4f26x99lqf0kybcs79rb` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_details`
--

LOCK TABLES `product_details` WRITE;
/*!40000 ALTER TABLE `product_details` DISABLE KEYS */;
INSERT INTO `product_details` VALUES (453,'Maroon','https://m.media-amazon.com/images/I/61475jtEDkL._SY879_.jpg','Puff Sleeve Crepe Fit & Flare Midi Dress',0,'S','sku004'),(453,'Teal','https://m.media-amazon.com/images/I/71IqP5HVahL._SY879_.jpg','Puff Sleeve Crepe Fit & Flare Midi Dress',20,'S','sku005'),(455,'Rust','https://m.media-amazon.com/images/I/71C3noU5GkL._SX679_.jpg','Slit Knitted A-Line Midi',30,'S','sku007'),(458,'Yellow','https://m.media-amazon.com/images/I/61K31ClSnDL._SY879_.jpg','Naixa Women Women\'s Kurtas',30,'L','sku012'),(458,'Yellow','https://m.media-amazon.com/images/I/61K31ClSnDL._SY879_.jpg','Naixa Women Women\'s Kurtas',30,'M','sku013'),(452,'Beige','https://m.media-amazon.com/images/I/71+PUBieL2L._SY879_.jpg','Women’s Bodycon Knee Length Dress',20,'L','sku001'),(457,'Dark Sea Green','https://m.media-amazon.com/images/I/71i5Hv0RYPL._SX679_.jpg','Printed Straight Cotton Kurta Set',30,'M','sku010'),(456,'Pink','https://m.media-amazon.com/images/I/61pP+trqOpL._SY879_.jpg','Women\'s Cotton Printed Anarkali Kurta with Palazzo',30,'M','sku008'),(454,'Lavender','https://m.media-amazon.com/images/I/61373gCmJxL._SY879_.jpg','Globus Women Strappy Shoulder Fit & Flare Sequinned Mini',30,'S','sku006');
/*!40000 ALTER TABLE `product_details` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-15 15:20:31
