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
INSERT INTO `product_details` VALUES (152,'Mauve','https://www.myntra.com/blazers/all+about+you/all-about-you-women-mauve-solid-single-breasted-casual-blazer/14230964/buy','Single-Breasted Casual Blazer',20,'sku001'),(202,'Peach','https://www.myntra.com/dresses/salt+attire/salt-attire-floral-printed-v-neck-puff-sleeve-crepe-a-line-midi-dress/26074800/buy','Floral Printed A-Line Midi',20,'sku002'),(203,'Black','https://www.myntra.com/dresses/mast+%26+harbour/mast--harbour-women-solid-black-midi-jumper-dress/10322871/buy','Women Jumper Dress',20,'sku003'),(204,'White','https://www.myntra.com/dresses/stylecast/stylecast-women-round-neck-bodycon-maxi-dress/31386516/buy','Bodycon Maxi Dress',20,'sku004'),(205,'Black','https://www.myntra.com/dresses/dodo+%26+moa/dodo--moa-sheath-midi-dress/29436404/buy','Sheath Midi Dress',20,'sku005'),(206,'Pink','https://www.myntra.com/dresses/aayu/aayu-women-puff-sleeve-sheath-mini-dress/28906992/buy','Sheath Midi Dress',20,'sku006'),(207,'Black','https://www.myntra.com/dresses/tandul/tandul-print-maxi-dress/30235470/buy','Print Maxi Dress',20,'sku007'),(208,'Black','https://www.myntra.com/dresses/quiero/quiero-women-stylish-black-floral-sweetheart-neck-dress/16542888/buy','Sweetheart Neck Dress',20,'sku008'),(252,'Black','https://www.myntra.com/dresses/quiero/quiero-women-stylish-black-floral-sweetheart-neck-dress/16542888/buy','Sweetheart Neck Dress',20,'sku008');
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

-- Dump completed on 2024-12-10  8:57:34
