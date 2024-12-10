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
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `attachment` varchar(255) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `delivery_date` datetime(6) DEFAULT NULL,
  `discounte` int DEFAULT NULL,
  `filtered_sum` varchar(255) DEFAULT NULL,
  `order_date` datetime(6) DEFAULT NULL,
  `order_id` varchar(255) DEFAULT NULL,
  `order_status` smallint DEFAULT NULL,
  `payment_id` varchar(255) DEFAULT NULL,
  `payment_method` smallint DEFAULT NULL,
  `razorpay_payment_id` varchar(255) DEFAULT NULL,
  `razorpay_payment_link_id` varchar(255) DEFAULT NULL,
  `razorpay_payment_link_reference_id` varchar(255) DEFAULT NULL,
  `razorpay_payment_link_status` varchar(255) DEFAULT NULL,
  `status` smallint DEFAULT NULL,
  `promotion_discount` decimal(38,2) DEFAULT NULL,
  `redeemed_points` int DEFAULT NULL,
  `total_discounted_price` int DEFAULT NULL,
  `total_item` int NOT NULL,
  `total_price` double NOT NULL,
  `used_points` int NOT NULL,
  `cancellation_reason_id` bigint DEFAULT NULL,
  `original_order_id` bigint DEFAULT NULL,
  `return_reason_id` bigint DEFAULT NULL,
  `shipping_address_id` bigint DEFAULT NULL,
  `store_pickup_id` bigint DEFAULT NULL,
  `subscription_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `razorpay_payment_id​` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5b07kc3upc1xkx5hv8ofc5ajy` (`cancellation_reason_id`),
  KEY `FKdl5mgond9cv9e5w0j93o00g6h` (`original_order_id`),
  KEY `FKnqau9l8jyf8fpq6rmptkg17st` (`return_reason_id`),
  KEY `FKh0uue95ltjysfmkqb5abgk7tj` (`shipping_address_id`),
  KEY `FKm7odob88ek2ejlemfgbmteyx5` (`store_pickup_id`),
  KEY `FKe21e5mx56k96q7ob301a7m98u` (`subscription_id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK5b07kc3upc1xkx5hv8ofc5ajy` FOREIGN KEY (`cancellation_reason_id`) REFERENCES `cancellation_reasons` (`id`),
  CONSTRAINT `FKdl5mgond9cv9e5w0j93o00g6h` FOREIGN KEY (`original_order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKe21e5mx56k96q7ob301a7m98u` FOREIGN KEY (`subscription_id`) REFERENCES `subscription_type` (`id`),
  CONSTRAINT `FKh0uue95ltjysfmkqb5abgk7tj` FOREIGN KEY (`shipping_address_id`) REFERENCES `address` (`id`),
  CONSTRAINT `FKm7odob88ek2ejlemfgbmteyx5` FOREIGN KEY (`store_pickup_id`) REFERENCES `store_pickup` (`id`),
  CONSTRAINT `FKnqau9l8jyf8fpq6rmptkg17st` FOREIGN KEY (`return_reason_id`) REFERENCES `return_reasons` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,NULL,NULL,'2024-11-05 16:54:34.092774',NULL,7700,'2498.0','2024-11-05 16:54:34.092747',NULL,8,NULL,NULL,NULL,NULL,NULL,NULL,0,1699.00,0,3396,4,11096,0,NULL,NULL,NULL,1,NULL,NULL,2,NULL),(2,NULL,NULL,'2024-11-07 14:43:41.510851',NULL,5900,'2398.0','2024-11-07 14:43:41.510824',NULL,8,NULL,NULL,NULL,NULL,NULL,NULL,0,999.00,0,2697,3,8597,0,NULL,NULL,NULL,1,NULL,NULL,2,NULL),(3,NULL,NULL,'2024-11-07 16:45:12.348395',NULL,7200,'299.0','2024-11-07 16:45:12.348372',NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,0,2397.00,0,3396,5,10596,0,2,NULL,NULL,1,NULL,NULL,2,NULL),(4,NULL,NULL,'2024-11-08 07:37:43.850838',NULL,0,NULL,'2024-11-08 07:37:43.850812',NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,0,0.00,0,0,0,0,0,NULL,NULL,NULL,2,NULL,NULL,2,NULL),(5,NULL,NULL,'2024-11-08 07:40:06.114092',NULL,0,'699.0','2024-11-08 07:40:06.114079',NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,0,2298.00,0,0,0,0,0,2,NULL,NULL,1,NULL,NULL,2,NULL),(6,NULL,NULL,'2024-11-08 07:59:37.071412',NULL,0,'1031.0','2024-11-08 07:59:37.071391',NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,0,1599.00,0,0,0,0,0,2,NULL,NULL,2,NULL,NULL,2,NULL);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-10 18:07:44
