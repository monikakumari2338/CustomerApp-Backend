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
-- Table structure for table `product_images`
--

DROP TABLE IF EXISTS `product_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_images` (
  `product_id` bigint NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  KEY `FKi8jnqq05sk5nkma3pfp3ylqrt` (`product_id`),
  CONSTRAINT `FKi8jnqq05sk5nkma3pfp3ylqrt` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_images`
--

LOCK TABLES `product_images` WRITE;
/*!40000 ALTER TABLE `product_images` DISABLE KEYS */;
INSERT INTO `product_images` VALUES (1,'https://adn-static1.nykaa.com/nykdesignstudio-images/pub/media/catalog/product/1/8/18a747d1509_1.jpg?rnd=20200526195200'),(2,'https://adn-static1.nykaa.com/nykdesignstudio-images/pub/media/catalog/product/4/b/4bf6ea8MLABAH00000367_3.jpg?rnd=20200526195200'),(3,'https://adn-static1.nykaa.com/nykdesignstudio-images/pub/media/catalog/product/0/e/tr:w-960,/0e59f55JK4983_1.jpg?rnd=20200526195200'),(4,'https://adn-static1.nykaa.com/nykdesignstudio-images/pub/media/catalog/product/a/3/a3ff815LIKAA00004009_03.jpg?rnd=20200526195200'),(5,'https://adn-static1.nykaa.com/nykdesignstudio-images/pub/media/catalog/product/2/c/2c7bb6dK_150_HW_1.jpg?rnd=20200526195200'),(6,'https://adn-static1.nykaa.com/nykdesignstudio-images/pub/media/catalog/product/b/4/b4b3931NYKFF_LAIS080_1.jpg?rnd=20200526195200'),(7,'https://adn-static1.nykaa.com/nykdesignstudio-images/pub/media/catalog/product/0/1/tr:w-960,/015dd7bFashor_KA017494F3FS_1.jpg?rnd=20200526195200'),(8,'https://adn-static1.nykaa.com/nykdesignstudio-images/pub/media/catalog/product/e/6/tr:w-960,/e634931NYKFF_NUHHX00000167_1.jpg?rnd=20200526195200'),(9,'https://adn-static1.nykaa.com/nykdesignstudio-images/pub/media/catalog/product/b/2/tr:w-960,/b2a9108IBK9360MT_1.jpg?rnd=20200526195200'),(10,'https://adn-static1.nykaa.com/nykdesignstudio-images/pub/media/catalog/product/3/d/tr:w-960,/3dbd728IF1002AKS_1.jpg?rnd=20200526195200'),(11,'https://www.montecarlo.in/cdn/shop/products/223059078-2-36_5-dzG-051408-2BN.jpg?v=1696658329&width=1000'),(12,'https://www.montecarlo.in/cdn/shop/products/222055858-1-36_1-APF-032915-zrl.jpg?v=1696614053&width=1000'),(13,'https://www.montecarlo.in/cdn/shop/products/1239814DS-1-36_5.jpg?v=1697893979&width=1000'),(14,'https://www.montecarlo.in/cdn/shop/products/1239804DS-1-36_5.jpg?v=1702019311&width=1100'),(15,'https://www.montecarlo.in/cdn/shop/products/223059083-1-36_5-M0T-030900-wNb.jpg?v=1696658352&width=1000'),(16,'https://www.montecarlo.in/cdn/shop/products/222055839-1-36_1-smb-042303-1s3.jpg?v=1696614010&width=1000'),(17,'https://www.montecarlo.in/cdn/shop/products/1239804DS-2-36_5.jpg?v=1702019323&width=1100'),(18,'https://www.montecarlo.in/cdn/shop/products/223059260-1-36_5-5i0-040647-oMM.jpg?v=1696659032&width=1000'),(19,'https://www.montecarlo.in/cdn/shop/products/223059083-2-36_3-db3-044232-dL6.jpg?v=1694354657&width=1000'),(20,'https://www.montecarlo.in/cdn/shop/products/222055827-1-36_1-mNi-120940-cMa.jpg?v=1696613952&width=1000'),(152,'string'),(202,'string'),(203,'string'),(204,'string'),(205,'string'),(206,'string'),(207,'string'),(208,'string'),(252,'string');
/*!40000 ALTER TABLE `product_images` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-10  8:57:35
