-- MySQL dump 10.13  Distrib 8.0.41, for Linux (x86_64)
--
-- Host: localhost    Database: stylish
-- ------------------------------------------------------
-- Server version	8.0.41-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `stylish`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `stylish` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `stylish`;

--
-- Table structure for table `Color`
--

DROP TABLE IF EXISTS `Color`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Color` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Color`
--

LOCK TABLES `Color` WRITE;
/*!40000 ALTER TABLE `Color` DISABLE KEYS */;
INSERT INTO `Color` VALUES (1,'334455','深藍'),(2,'334455','深藍'),(3,'FFFFFF','白色'),(4,'FFFFFF','白色'),(5,'808080','灰色'),(6,'808080','灰色'),(7,'808080','灰色'),(8,'FFFFFF','白色'),(9,'FFFFFF','白色'),(10,'FFFFFF','白色'),(11,'F5F5DC','米色'),(12,'F5F5DC','米色'),(13,'FFFFFF','白色'),(14,'FFFFFF','白色'),(15,'000000','黑色'),(16,'000000','黑色'),(17,'000000','黑色'),(18,'964B00','棕色'),(19,'964B00','棕色'),(20,'FFFFFF','白色'),(21,'FFC0CB','粉紅色'),(22,'FFC0CB','粉紅色'),(23,'FFC0CB','粉紅色'),(24,'b0e0e6','粉藍色'),(25,'b0e0e6','粉藍色'),(26,'FFFFFF','白色');
/*!40000 ALTER TABLE `Color` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Image`
--

DROP TABLE IF EXISTS `Image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Image` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `Image_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Image`
--

LOCK TABLES `Image` WRITE;
/*!40000 ALTER TABLE `Image` DISABLE KEYS */;
INSERT INTO `Image` VALUES (1,'http://35.74.181.119/images/e18a8bc1-e59c-4a17-80d4-bbad94077713_201807242222_main.jpg',1),(2,'http://35.74.181.119/images/159f1c34-5676-4944-b168-06152795074a_201807242222_1.jpg',1),(3,'http://35.74.181.119/images/e31d1dae-7e61-4f20-b268-e4373fbc7128_201807242216_0.jpg',2),(4,'http://35.74.181.119/images/6c93e0d4-fbf3-4b77-b7ea-53be160ee61e_201807242216_main.jpg',2),(5,'http://35.74.181.119/images/21a174d4-fb5d-4f9b-a8c1-8099fc3879f5_9b598d44.jpeg',3),(6,'http://35.74.181.119/images/b3c477d0-31ba-43a2-8a66-986efe542137_9a4a9a26.jpeg',3),(7,'http://35.74.181.119/images/a4b84077-9000-4bb5-99f3-9926f85a454c_201807202150_1.jpg',4),(8,'http://35.74.181.119/images/4abb206e-c62d-45de-9430-20fcb18a8636_201807202150_0.jpg',4),(9,'http://35.74.181.119/images/bb3f4738-73f7-40b2-add1-ae1bdc8d9aec_201807242230_1.jpg',5),(10,'http://35.74.181.119/images/fb75f7e4-6ec7-4ce5-8e7a-51fa1e22eba3_201807242230_0.jpg',5),(11,'http://35.74.181.119/images/5eb7f17b-74f7-4cf0-b74b-eee370910af1_201902191247_1.jpg',6),(12,'http://35.74.181.119/images/a3d2457b-ea4b-4620-85ba-55eddcf0054a_201902191247_0.jpg',6);
/*!40000 ALTER TABLE `Image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Product`
--

DROP TABLE IF EXISTS `Product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` varchar(50) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `price` bigint DEFAULT NULL,
  `texture` varchar(255) DEFAULT NULL,
  `wash` varchar(50) DEFAULT NULL,
  `place` varchar(50) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `story` varchar(255) DEFAULT NULL,
  `main_image_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Product`
--

LOCK TABLES `Product` WRITE;
/*!40000 ALTER TABLE `Product` DISABLE KEYS */;
INSERT INTO `Product` VALUES (1,'men','厚實毛呢格子外套','高抗寒素材選用，保暖也時尚有型',2200,'棉、聚脂纖維','手洗（水溫40度','韓國','實品顏色以單品照為主','你絕對不能錯過的超值商品','http://35.74.181.119/images/dae93cc6-58c6-4340-8f20-47544db3ed7d_201807242222_main.jpg'),(2,'men','厚實灰色外套','高抗寒素材選用，帥氣也時尚有型',2500,'棉、聚脂纖維','手洗（水溫40度','法國','實品顏色以單品照為主','你絕對不能錯過的超值商品','http://35.74.181.119/images/39427d35-bd03-4bf5-824f-295232c3eb9b_201807242216_main.jpg'),(3,'women','前開衩扭結洋裝','厚薄：薄\\r\\n彈性：無',799,'棉 100%','手洗，溫水','中國','實品顏色以單品照為主','O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.','http://35.74.181.119/images/3b05d00d-76e4-4c69-851f-57a18fe250cc_7ed29648.jpeg'),(4,'women','精美洋裝','厚薄：薄\\r\\n彈性：無',3400,'棉 100%','手洗，溫水','英國','實品顏色以單品照為主','O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.','http://35.74.181.119/images/2b504b68-0b7c-45e8-bb72-f255ea9da9d6_201807202150_main.jpg'),(5,'category','西部風格牛仔帽','厚薄：薄\\r\\n彈性：無',5000,'牛皮 100%','手洗，溫水','澳洲','實品顏色以單品照為主','西部迷人風味','http://35.74.181.119/images/e1d28dd2-cd49-4290-bf11-036d721d8394_201807242230_main.jpg'),(6,'women','精美螺紋披風','厚薄：薄\\r\\n彈性：無',2600,'蕾絲 100%','手洗，溫水','美國','實品顏色以單品照為主','精緻女孩愛物','http://35.74.181.119/images/23723ea5-427f-47bb-a478-7b44ce3d22e1_201902191247_main.jpg');
/*!40000 ALTER TABLE `Product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Size`
--

DROP TABLE IF EXISTS `Size`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Size` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `size` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Size`
--

LOCK TABLES `Size` WRITE;
/*!40000 ALTER TABLE `Size` DISABLE KEYS */;
INSERT INTO `Size` VALUES (1,'S'),(2,'M'),(3,'S'),(4,'M'),(5,'Ｌ'),(6,'M'),(7,'S'),(8,'Ｌ'),(9,'M'),(10,'S'),(11,'M'),(12,'L'),(13,'M'),(14,'S'),(15,'S'),(16,'L'),(17,'M'),(18,'S'),(19,'M'),(20,'M'),(21,'S'),(22,'L'),(23,'M'),(24,'S'),(25,'M'),(26,'S');
/*!40000 ALTER TABLE `Size` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Variant`
--

DROP TABLE IF EXISTS `Variant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Variant` (
  `size_id` bigint NOT NULL,
  `color_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `stock` int NOT NULL,
  PRIMARY KEY (`product_id`,`size_id`,`color_id`),
  KEY `size_id` (`size_id`),
  KEY `color_id` (`color_id`),
  CONSTRAINT `Variant_ibfk_1` FOREIGN KEY (`size_id`) REFERENCES `Size` (`id`),
  CONSTRAINT `Variant_ibfk_2` FOREIGN KEY (`color_id`) REFERENCES `Color` (`id`),
  CONSTRAINT `Variant_ibfk_3` FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Variant`
--

LOCK TABLES `Variant` WRITE;
/*!40000 ALTER TABLE `Variant` DISABLE KEYS */;
INSERT INTO `Variant` VALUES (1,1,1,5),(2,2,1,10),(3,3,1,0),(4,4,1,2),(5,5,2,12),(6,6,2,0),(7,7,2,16),(8,8,3,2),(9,9,3,1),(10,10,3,2),(11,11,4,2),(12,12,4,11),(13,13,4,34),(14,14,4,0),(15,15,5,21),(16,16,5,0),(17,17,5,0),(18,18,5,13),(19,19,5,27),(20,20,5,27),(21,21,6,20),(22,22,6,20),(23,23,6,20),(24,24,6,0),(25,25,6,27),(26,26,6,12);
/*!40000 ALTER TABLE `Variant` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-03  2:13:20
