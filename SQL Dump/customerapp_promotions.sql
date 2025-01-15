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
-- Table structure for table `promotions`
--

DROP TABLE IF EXISTS `promotions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `discount_value` varchar(255) DEFAULT NULL,
  `exluded_product_id` varbinary(255) DEFAULT NULL,
  `max_discount` double DEFAULT NULL,
  `promotion_max_order_value` double DEFAULT NULL,
  `promotion_min_order_value` double DEFAULT NULL,
  `is_promotion_active` bit(1) DEFAULT NULL,
  `promotion_code` varchar(255) DEFAULT NULL,
  `promotion_description` varchar(255) DEFAULT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  `promotion_name` varchar(255) DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `promotion_type` varchar(255) DEFAULT NULL,
  `temporarely_inactive` bit(1) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `promotion_usage_count` int DEFAULT NULL,
  `promotion_usage_limit` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotions`
--

LOCK TABLES `promotions` WRITE;
/*!40000 ALTER TABLE `promotions` DISABLE KEYS */;
INSERT INTO `promotions` VALUES (1,'2024-12-09 16:55:07.630466','5.00',_binary '¨\Ì\0sr\0java.util.ArrayListxÅ\“ô\«aù\0I\0sizexp\0\0\0\0w\0\0\0\0x',100,99999,1500,_binary '\0','FIVEPERCENTOFF','Get 5% off on cart value ‚Çπ1500 equal or more.','2024-11-30 23:59:59.000000','5% Off on ‚Çπ1500+ Purchase','2024-12-09 16:55:07.630466','PERCENTAGE',_binary '\0','2024-12-09 16:55:07.630466',0,100),(2,'2024-12-09 17:05:01.248753','0.00',_binary '¨\Ì\0sr\0java.util.ArrayListxÅ\“ô\«aù\0I\0sizexp\0\0\0\0w\0\0\0\0x',0,0,0,_binary '','BOGO','Buy one product and get another one for free.','2024-12-31 23:59:59.000000','Buy One Get One Free','2024-12-09 17:05:01.248753','BOGO',_binary '\0','2024-12-09 17:05:22.633033',0,100),(3,'2025-01-13 11:57:51.260270','20.00',_binary '¨\Ì\0sr\0java.util.ArrayListxÅ\“ô\«aù\0I\0sizexp\0\0\0\0w\0\0\0\0x',100,99999,1500,_binary '\0','SAVE20PCT','Get 20% OFF On Product.','2024-11-30 23:59:59.000000','Item Wise Twenty Percent OFF On Product','2025-01-13 11:57:51.260270','PERCENTAGE',_binary '\0','2025-01-13 11:57:51.260270',0,100),(4,'2025-01-13 12:10:44.714298','200.00',_binary '¨\Ì\0sr\0java.util.ArrayListxÅ\“ô\«aù\0I\0sizexp\0\0\0\0w\0\0\0\0x',100,99999,1500,_binary '\0','ITEM200OFF','Get 200 OFF On Product.','2024-11-30 23:59:59.000000','200 off On Product','2025-01-13 12:10:44.714298','FLAT',_binary '\0','2025-01-13 12:10:44.714298',0,100),(5,'2025-01-13 12:12:33.479967','500.00',_binary '¨\Ì\0sr\0java.util.ArrayListxÅ\“ô\«aù\0I\0sizexp\0\0\0\0w\0\0\0\0x',500,99999,1500,_binary '\0','FIVEHUNDREDOFF','Get 500 OFF On Product.','2024-11-30 23:59:59.000000','500 off On Product','2025-01-13 12:12:33.479967','FLAT',_binary '\0','2025-01-13 12:12:33.479967',0,100);
/*!40000 ALTER TABLE `promotions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-15 15:20:27
