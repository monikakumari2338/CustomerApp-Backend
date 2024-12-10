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
INSERT INTO `product` VALUES (1,15,23,0,0,0,'Aarvia','#025043',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:37:39.829685','A traditional garment embodying elegance and grace. Crafted from fine fabrics, it features intricate embroidery and a relaxed fit, providing comfort and style.',65,699,_binary '',_binary '\0','Cotton','Bodycon Fit',NULL,NULL,'Dry Clean',NULL,0,NULL,NULL,1999,'KURTA12345601',0,NULL,100,'AarviaStyles',NULL,NULL,'Women Bodycon Kurta','Anarkali',3,NULL),(2,11,21,0,0,0,'Ethnic Elegance','#c30010',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:37:39.862793','Step out in style with this Anarkali style embroidered kurta. The red color and intricate embroidery add an ethnic touch to your look.',64,899,_binary '\0',_binary '\0','Silk','Flared Fit',NULL,NULL,'Dry Clean',NULL,0,NULL,NULL,2499,'KURTA12345602',0,'699.0',80,'EthnicEleganceStyles',NULL,NULL,'Anarkali Embroidered Kurta','Anarkali',3,NULL),(3,33,33,0,0,0,'Traditional Charm','#6082B6',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:37:39.963423','Stay comfortable and chic with this printed straight fit kurta. The blue color and traditional print make it a versatile choice.',64,749,_binary '\0',_binary '\0','Cotton','Straight Fit',NULL,NULL,'Machine Wash',NULL,0,NULL,NULL,2099,'KURTA12345603',0,'649.0',60,'TraditionalCharmStyles',NULL,NULL,'Printed StraightFit Kurta','Straight',3,NULL),(4,2,0,0,0,0,'Festive Fusion','#fab546',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:37:39.976571','Celebrate in style with this embroidered peplum kurta. The yellow color and peplum silhouette add a festive touch to your wardrobe.',64,999,_binary '\0',_binary '\0','Georgette','Regular Fit',NULL,NULL,'Dry Clean',NULL,0,NULL,NULL,2799,'KURTA12345604',0,'799.0',90,'FestiveFusionStyles',NULL,NULL,'Peplum Kurta','Peplum',3,NULL),(5,63,64,0,0,0,'Contemporary Classic','#FFFFFF',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:37:39.997444','Elevate your look with this A-line block print kurta. The white color and block print design make it a contemporary classic.',65,799,_binary '\0',_binary '\0','Cotton','A-Line Fit',NULL,NULL,'Machine Wash',NULL,0,NULL,NULL,2299,'KURTA12345605',0,NULL,80,'ContemporaryClassicStyles',NULL,NULL,'A-Line Print Kurta','A-Line',3,NULL),(6,55,0,0,0,0,'Boho Vibes','#fcaed5',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:37:40.034062','Channel boho vibes with this floral embroidered kurti. The pink color and floral embroidery add a touch of freshness to your style.',64,899,_binary '\0',_binary '\0','Rayon','Regular Fit',NULL,NULL,'Dry Clean',NULL,0,NULL,NULL,2499,'KURTA12345606',0,NULL,60,'BohoVibesStyles',NULL,NULL,'Floral Embroidered Kurti','Kurti',3,NULL),(7,33,43,0,0,0,'Chic Comfort','#f2d3bc',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:37:40.064342','Experience chic comfort with this cotton asymmetric hem kurta. The beige color and asymmetric hem make it a stylish yet comfortable choice.',65,699,_binary '\0',_binary '\0','Cotton','Regular Fit',NULL,NULL,'Machine Wash',NULL,0,NULL,NULL,1999,'KURTA12345607',0,NULL,80,'ChicComfortStyles',NULL,NULL,'Cotton Asymmetric Kurta','Asymmetric',3,NULL),(8,4,0,0,0,0,'Sophisticated Style','#152238',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:37:40.086999','Add sophistication to your wardrobe with this straight cut mirror work kurta. The navy blue color and mirror work detailing create a stylish look.',65,799,_binary '\0',_binary '\0','Silk','Straight Fit',NULL,NULL,'Dry Clean',NULL,0,NULL,NULL,2299,'KURTA12345608',0,NULL,60,'SophisticatedStyleStyles',NULL,NULL,'StraightMirror Kurta','Straight',3,NULL),(9,55,66,0,0,0,'Trendy Fusion','#DAA520',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:37:40.105678','Get the trendy fusion look with this layered angrakha style kurta. The mustard color and layered design make it a unique addition to your collection.',65,899,_binary '\0',_binary '\0','Rayon','Regular Fit',NULL,NULL,'Dry Clean',NULL,0,NULL,NULL,2599,'KURTA12345609',0,NULL,80,'TrendyFusionStyles',NULL,NULL,'Layer Angrakha Kurta','Layered',3,NULL),(10,6,0,0,0,0,'Boho Chic','#4b5320',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:37:40.128957','Embrace boho chic with this embroidered bohemian kurta. The multi-color embroidery adds a playful and bohemian touch to your style.',65,999,_binary '\0',_binary '\0','Cotton','Regular Fit',NULL,NULL,'Dry Clean',NULL,0,NULL,NULL,2899,'KURTA12345610',0,NULL,60,'BohoChicStyles',NULL,NULL,'Bohemian Kurta','Bohemian',3,NULL),(11,77,3,0,0,0,'Aarvia','green',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:39:02.137561','A traditional garment embodying elegance and grace. Crafted from fine fabrics, it features intricate embroidery and a relaxed fit, providing comfort and style.',65,699,_binary '\0',_binary '\0','Silk','Slim Fit',NULL,NULL,'Dry Clean Only',NULL,0,NULL,NULL,1999,'DRESS74561201',0,'499.0',100,'AarviaStyles',NULL,NULL,'Women Bodycon Dress','Bodycon',4,NULL),(12,4,55,0,0,0,'Graceful Attire','green',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:39:02.151077','Step out in style with this floral print maxi dress. Perfect for a day out or a special occasion, this dress combines fashion and comfort effortlessly.',64,899,_binary '\0',_binary '\0','Cotton','Regular Fit',NULL,NULL,'Machine Wash',NULL,0,NULL,NULL,2499,'DRESS74561202',0,'699.0',80,'AttireStyles',NULL,NULL,'Floral Maxi Dress','Maxi',4,NULL),(13,77,33,0,0,0,'Elegance Couture','black',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:39:02.165024','Make a grand entrance with this chiffon evening gown. The flowing silhouette and rich burgundy color add an air of sophistication to your ensemble.',55,1499,_binary '\0',_binary '\0','Chiffon','A-Line Fit',NULL,NULL,'Dry Clean Only',NULL,0,NULL,NULL,3299,'DRESS74561203',0,'1399.0',60,'CoutureStyles',NULL,NULL,'Chiffon Evening Gown','Evening Gown',4,NULL),(14,9,0,0,0,0,'Trendy Chic','navyblue',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:39:02.174771','Stay trendy and comfortable with this casual striped shirt dress. Ideal for a day out or a casual gathering, this dress offers a relaxed and stylish look.',60,799,_binary '\0',_binary '\0','Polyester','Relaxed Fit',NULL,NULL,'Machine Wash',NULL,0,NULL,NULL,1999,'DRESS74561204',0,NULL,90,'ChicStyles',NULL,NULL,'Casual Striped Dress','Shirt Dress',4,NULL),(15,86,0,0,0,0,'Modern Flair','pink',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:39:02.184117','Add a touch of elegance with this ruffled sleeve A-line dress. The pink hue and A-line silhouette make it a charming choice for various occasions.',60,999,_binary '\0',_binary '\0','Cotton Blend','Regular Fit',NULL,NULL,'Machine Wash',NULL,0,NULL,NULL,2499,'DRESS74561205',0,NULL,80,'FlairStyles',NULL,NULL,'Ruffled A-line Dress','A-line',4,NULL),(16,0,0,0,0,0,'Feminine Flare','white',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:39:02.194825','Make a statement with this floral embroidered midi dress. The delicate embroidery and midi length add a touch of femininity to your look.',57,1299,_binary '\0',_binary '\0','Linen','Fit and Flare',NULL,NULL,'Dry Clean Only',NULL,0,NULL,NULL,2999,'DRESS74561206',0,NULL,60,'FeminineStyles',NULL,NULL,'Floral Midi Dress','Midi',4,NULL),(17,0,0,0,0,0,'Chic Elegance','Black',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:39:02.213665','Dress to impress with this lace overlay cocktail dress. The black color and lace detailing make it an elegant choice for special occasions.',55,1499,_binary '\0',_binary '\0','Lace','Bodycon Fit',NULL,NULL,'Dry Clean Only',NULL,0,NULL,NULL,3299,'DRESS74561207',0,NULL,80,'EleganceStyles',NULL,NULL,'Lace Cocktail Dress','Cocktail',4,NULL),(18,0,0,0,0,0,'Sophisticated Charm','red',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:39:02.226527','Elevate your style with this satin wrap dress. The vibrant red color and wrap silhouette add a touch of sophistication to your wardrobe.',57,999,_binary '\0',_binary '\0','Satin','Wrap Fit',NULL,NULL,'Machine Wash',NULL,0,NULL,NULL,2299,'DRESS74561208',0,NULL,60,'CharmStyles',NULL,NULL,'Satin Wrap Dress','Wrap',4,NULL),(19,0,0,0,0,0,'Trendy Allure','green',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:39:02.238601','Show off your shoulders with this off-shoulder bodycon dress. The black color and figure-hugging silhouette make it a trendy choice for a night out.',60,799,_binary '\0',_binary '\0','Polyester','Off-Shoulder Fit',NULL,NULL,'Dry Clean Only',NULL,0,NULL,NULL,1999,'DRESS74561209',0,NULL,80,'AllureStyles',NULL,NULL,'Off-Bodycon Dress','Bodycon',4,NULL),(20,0,0,0,0,0,'Boho Chic','cream',NULL,0,0,0,0,0,'India',NULL,'2024-11-05 10:39:02.252522','Channel bohemian vibes with this bohemian print maxi dress. The vibrant colors and flowing silhouette make it a perfect choice for a boho-chic look.',57,1299,_binary '\0',_binary '\0','Rayon','Bohemian Fit',NULL,NULL,'Machine Wash',NULL,0,NULL,NULL,2999,'DRESS74561210',0,NULL,60,'ChicBohoStyles',NULL,NULL,'Bohemian Maxi Dress','Maxi',4,NULL),(152,0,0,0,0,0,'all about you','Mauve','string',10,0,0,0,0,'India',NULL,'2024-12-06 22:52:49.308272','Mauve Solid Single-Breasted blazer, has a notched lapel collar, long sleeves, button closure, two pockets and one in- built pocket with polyester lining',0,1749,_binary '\0',_binary '\0','Polyster','Oversized','string','string','Dry Clean','string',0,'string','string',1749,'blazer1262024',0,NULL,20,'string',NULL,'Full','Women Mauve Solid Single-Breasted Casual Blazer','string',104,NULL),(202,0,0,0,0,0,'all about you','Mauve','string',9,0,0,0,0,'India',NULL,'2024-12-09 22:39:52.092406','Floral Printed V-Neck Puff Sleeve Crepe A-Line Midi',0,1749,_binary '\0',_binary '\0','Polyster','Oversized','string','string','Dry Clean','string',0,'string','string',1749,'dress1262024',0,NULL,20,'string',NULL,'Full','Floral Printed A-Line Midi','string',104,NULL),(203,0,0,0,0,0,'all about you','Mauve','string',12,0,0,0,0,'India',NULL,'2024-12-09 22:41:48.069500','Women Solid Black Midi Jumper Dress',0,1749,_binary '\0',_binary '\0','Polyster','Oversized','string','string','Dry Clean','string',0,'string','string',1749,'dress1362024',0,NULL,20,'string',NULL,'Full','Women Jumper Dress','string',104,NULL),(204,0,0,0,0,0,'all about you','Mauve','string',8,0,0,0,0,'India',NULL,'2024-12-09 22:43:27.498877','Women Round Neck Bodycon Maxi Dress',0,1749,_binary '\0',_binary '\0','Polyster','Oversized','string','string','Dry Clean','string',0,'string','string',1749,'dress1462024',0,NULL,20,'string',NULL,'Full','Bodycon Maxi Dress','string',104,NULL),(205,0,0,0,0,0,'all about you','Mauve','string',5,0,0,0,0,'India',NULL,'2024-12-09 22:45:12.682316','Women Sheath Midi Dress',0,1749,_binary '\0',_binary '\0','Polyster','Oversized','string','string','Dry Clean','string',0,'string','string',1749,'dress1562024',0,NULL,20,'string',NULL,'Full','Sheath Midi Dress','string',104,NULL),(206,0,0,0,0,0,'all about you','Mauve','string',0,4,0,0,0,'India',NULL,'2024-12-09 22:46:44.525464','Women Puff Sleeve Sheath Mini Dress',0,1749,_binary '\0',_binary '\0','Polyster','Oversized','string','string','Dry Clean','string',0,'string','string',1749,'dress1562024',0,NULL,20,'string',NULL,'Full','Sheath Mini Dresss','string',104,NULL),(207,0,0,0,0,0,'all about you','Mauve','string',0,44,0,0,0,'India',NULL,'2024-12-09 22:47:47.669827','Print Maxi Dress',0,1749,_binary '\0',_binary '\0','Polyster','Oversized','string','string','Dry Clean','string',0,'string','string',1749,'dress1562024',0,NULL,20,'string',NULL,'Full','Print Maxi Dress','string',104,NULL),(208,0,0,0,0,0,'all about you','Mauve','string',0,6,0,0,0,'India',NULL,'2024-12-09 22:49:05.706218','Women Stylish Black Floral Sweetheart Neck Dress',0,1749,_binary '\0',_binary '\0','Polyster','Oversized','string','string','Dry Clean','string',0,'string','string',1749,'dress1562024',0,NULL,20,'string',NULL,'Full','Sweetheart Neck Dress','string',104,NULL),(252,0,0,0,0,0,'all about you','Mauve','string',0,0,0,0,0,'India',NULL,'2024-12-09 22:55:11.294735','Women Stylish Black Floral Sweetheart Neck Dress',0,1749,_binary '\0',_binary '\0','Polyster','Oversized','string','string','Dry Clean','string',0,'string','string',1749,'dress1562024',0,NULL,20,'string',NULL,'Full','Sweetheart Neck Dress','string',104,NULL);
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

-- Dump completed on 2024-12-10  8:57:27
