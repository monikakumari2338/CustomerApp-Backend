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
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL,
  `average_rating_for_five_stars` double NOT NULL,
  `average_rating_for_four_stars` double NOT NULL,
  `average_rating_for_one_star` double NOT NULL,
  `average_rating_for_three_stars` double NOT NULL,
  `average_rating_for_two_stars` double NOT NULL,
  `brand` varchar(255) DEFAULT NULL,
  `color` varchar(255) DEFAULT NULL,
  `consume_within` varchar(255) DEFAULT NULL,
  `count_users_rated_product_five_stars` int NOT NULL,
  `count_users_rated_product_four_stars` int NOT NULL,
  `count_users_rated_product_one_star` int NOT NULL,
  `count_users_rated_product_three_stars` int NOT NULL,
  `count_users_rated_product_two_stars` int NOT NULL,
  `country` varchar(255) DEFAULT NULL,
  `country_of_origin` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `discount_persent` int DEFAULT NULL,
  `discounted_price` int DEFAULT NULL,
  `eligible_for_bogo` bit(1) NOT NULL,
  `eligible_for_promotion` bit(1) NOT NULL,
  `fabric` varchar(255) DEFAULT NULL,
  `fit` varchar(255) DEFAULT NULL,
  `generic_name` varchar(255) DEFAULT NULL,
  `ingredient` varchar(255) DEFAULT NULL,
  `material_care` varchar(255) DEFAULT NULL,
  `milk_type` varchar(255) DEFAULT NULL,
  `num_ratings` int DEFAULT NULL,
  `packaging` varchar(255) DEFAULT NULL,
  `preservatives` varchar(255) DEFAULT NULL,
  `price` int DEFAULT NULL,
  `product_code` varchar(255) DEFAULT NULL,
  `product_rating` double NOT NULL,
  `promotional_discounted_price` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `seller` varchar(255) DEFAULT NULL,
  `seller_info` varchar(255) DEFAULT NULL,
  `sleeves` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `wear_type` varchar(255) DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `pincode_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKowomku74u72o6h8q0khj7id8q` (`category_id`),
  KEY `FKtec3g9jbaix6b7753wbp049ry` (`pincode_id`),
  CONSTRAINT `FKowomku74u72o6h8q0khj7id8q` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  CONSTRAINT `FKtec3g9jbaix6b7753wbp049ry` FOREIGN KEY (`pincode_id`) REFERENCES `pincode` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (452,0,2,0,0,0,'Purvaja','Mauve','string',0,1,0,0,0,'India',NULL,'2024-12-10 17:54:01.900950','PURVAJA Women’s Bodycon Knee Length Dress(Siri-Turtle)',0,1749,_binary '\0',_binary '\0','Polyster','Oversized','string','string','Dry Clean','string',0,'string','string',1749,'Dress101',4,NULL,20,'Supercom Net',NULL,'Full','Women’s Bodycon Knee Length Dress','string',104,NULL),(453,0,0,0,0,0,'Istyle','Mauve','string',6,0,0,0,0,'India',NULL,'2024-12-10 17:56:34.210622','Istyle Can Solid Puff Sleeve Crepe Fit & Flare Midi Dress with Round Neck, Short Sleeves, Gathered Details, and Flared Hemline for Party Occasions',0,1749,_binary '\0',_binary '\0','Polyster','Oversized','string','string','Dry Clean','string',0,'string','string',1749,'Dress101',0,NULL,20,'Supercom Net',NULL,'Full','Puff Sleeve Crepe Fit & Flare Midi Dress','string',104,NULL),(454,0,0,0,0,0,'Globus','Mauve','string',4,0,0,0,0,'India',NULL,'2024-12-10 17:58:22.895280','Globus Women Strappy Shoulder Fit & Flare Sequinned Mini Party Dress',0,1500,_binary '\0',_binary '\0','Polyster','Oversized','string','string','Dry Clean','string',0,'string','string',1500,'Dress101',0,NULL,20,'Supercom Net',NULL,'Full','Globus Women Strappy Shoulder Fit & Flare Sequinned Mini','string',104,NULL),(455,0,0,0,0,0,'Globus','Mauve','string',7,0,0,0,0,'India',NULL,'2024-12-10 17:59:55.230909','Globus Shiny Cowl Neck Side Slit Knitted A-Line Midi Party Dress',0,1900,_binary '\0',_binary '\0','Polyster','Oversized','string','string','Dry Clean','string',0,'string','string',1900,'Dress101',0,NULL,20,'RetailNet',NULL,'Full','Slit Knitted A-Line Midi','string',104,NULL),(456,0,0,0,0,0,'Amayra','Mauve','string',1,0,0,0,0,'India',NULL,'2024-12-10 18:01:29.151029','this beautiful Printed Anarkali Kurta with Pant and Dupatta which is made from Cotton fabric with beautiful print all over.',0,1900,_binary '\0',_binary '\0','Polyster','Oversized','string','string','Dry Clean','string',0,'string','string',1900,'Dress101',0,NULL,20,'RetailNet',NULL,'Full','Cotton Printed Anarkali Kurta with Palazzo','string',4,NULL),(457,0,0,0,0,0,'idaLia','Mauve','string',0,4,0,0,0,'India',NULL,'2024-12-10 18:03:44.865270','stylish Prints Embrace a look that is both stylish and timeless with this sophisticated pink kurta set with dupatta.',0,1900,_binary '\0',_binary '\0','Polyster','Oversized','string','string','Dry Clean','string',0,'string','string',1900,'Dress101',0,NULL,20,'RetailNet',NULL,'Full','Printed Straight Cotton Kurta Set','string',4,NULL),(458,0,0,0,0,0,'Naixa','Mauve','string',0,6,0,0,0,'India',NULL,'2024-12-10 18:05:24.001803','stylish Prints Embrace a look that Naixa Women Women\'s Kurtas & Kurtis.',0,1900,_binary '\0',_binary '\0','Polyster','Oversized','string','string','Dry Clean','string',0,'string','string',1900,'Dress101',0,NULL,20,'Supercom Net',NULL,'Full','Naixa Women Women\'s Kurtas','string',4,NULL);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-15 15:20:30
