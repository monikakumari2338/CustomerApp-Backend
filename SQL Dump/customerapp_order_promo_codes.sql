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
-- Table structure for table `order_promo_codes`
--

DROP TABLE IF EXISTS `order_promo_codes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_promo_codes` (
  `order_id` bigint NOT NULL,
  `discount_value` double DEFAULT NULL,
  `promo_code` varchar(255) NOT NULL,
  PRIMARY KEY (`order_id`,`promo_code`),
  CONSTRAINT `FKfikjqwoemw5f4dsoivvq3ldrg` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_promo_codes`
--

LOCK TABLES `order_promo_codes` WRITE;
/*!40000 ALTER TABLE `order_promo_codes` DISABLE KEYS */;
INSERT INTO `order_promo_codes` VALUES (1,599,'ANNIVERSARY299'),(1,500,'FIVEHUNDREDOFF'),(1,600,'ITEM200OFF'),(2,599,'ANNIVERSARY299'),(2,400,'ITEM200OFF'),(3,599,'ANNIVERSARY299'),(3,699,'BOGO'),(3,400,'ITEM200OFF'),(5,699,'BOGO'),(5,500,'FIVEHUNDREDOFF'),(5,400,'ITEM200OFF'),(6,699,'BOGO'),(6,500,'FIVEHUNDREDOFF'),(6,400,'ITEM200OFF');
/*!40000 ALTER TABLE `order_promo_codes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-10  8:57:22
