-- MySQL dump 10.13  Distrib 8.4.1, for macos14 (arm64)
--
-- Host: localhost    Database: stylish_2
-- ------------------------------------------------------
-- Server version	8.4.1

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
-- Current Database: `stylish_2`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `stylish_2` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `stylish_2`;

--
-- Table structure for table `Campaign`
--

DROP TABLE IF EXISTS `Campaign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Campaign` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `story` text,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `campaign_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Campaign`
--

LOCK TABLES `Campaign` WRITE;
/*!40000 ALTER TABLE `Campaign` DISABLE KEYS */;
INSERT INTO `Campaign` VALUES (3,12,'e1f5f15d-e640-4a60-b6f0-29b5b275a7bc_201807202140_keyvisual.jpg','文案文案文案1'),(4,13,'971ae7f6-7e64-4248-9949-814c7f2f8235_201807242222_keyvisual.jpg','文案文案文案2'),(5,14,'93280817-3fb0-4dca-81a4-3376b59ba36e_201807242228_keyvisual.jpg','文案文案文案3'),(6,15,'e38b9d0f-7bb5-4f07-bd24-27618749b647_201807202140_keyvisual.jpg','文案文案文案4'),(7,18,'e1cb6c37-f2ad-4a72-87b9-6363a5026f4b_201807242222_keyvisual.jpg','文案文案文案5'),(8,19,'bf5caa72-c467-400d-b6aa-2aaa7b05932e_201807242228_keyvisual.jpg','文案文案文案6'),(9,20,'0ae6ec3d-8a78-48c7-8764-bd446c2d465b_201807202140_keyvisual.jpg','文案文案文案7'),(10,21,'d84098dc-2d95-4141-a4d7-a631957a9e89_201807242222_keyvisual.jpg','文案文案文案8'),(11,22,'850706fd-c6f6-4c1f-a07d-5c9c81a7c177_201807242228_keyvisual.jpg','文案文案文案9'),(12,23,'65b47ae4-adb5-4cb8-921f-6b3996c7f994_201807202140_keyvisual.jpg','文案文案文案10'),(13,24,'d4bce906-c408-497f-aff5-f1766f25e5b4_201807242222_keyvisual.jpg','文案文案文案11'),(14,25,'5ec8e2e0-fcaf-4949-ab8f-b36a4800f552_201807242228_keyvisual.jpg','文案文案文案12'),(15,26,'bf2ecd42-7d7f-4268-bd3e-43d79ecc9589_201807202140_keyvisual.jpg','文案文案文案13'),(16,27,'e152d4df-dfbe-47e3-a655-b14fcb153b7a_201807242222_keyvisual.jpg','文案文案文案14'),(17,28,'d82ae5ac-bd21-401c-a4ae-464059c09845_201807242228_keyvisual.jpg','文案文案文案15'),(18,32,'27498ece-e095-408a-88a3-70da08137aaf_201807202140_keyvisual.jpg','文案文案文案16');
/*!40000 ALTER TABLE `Campaign` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Color`
--

DROP TABLE IF EXISTS `Color`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Color` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Color`
--

LOCK TABLES `Color` WRITE;
/*!40000 ALTER TABLE `Color` DISABLE KEYS */;
INSERT INTO `Color` VALUES (5,'000000','黑色'),(6,'FFFFFF','白色'),(7,'DC12F0','桃紅色'),(8,'12F015','亮綠色'),(9,'808000','橄欖綠'),(10,'DA70D6','紫丁香'),(11,'7FFFD4','綠松石'),(12,'ADFF2F','春綠色'),(13,'008080','水鴨色'),(14,'FF69B4','粉紅'),(19,'D2691E','巧克力色'),(20,'808080','灰色'),(21,'C0C0C0','銀色'),(22,'00FF00','綠色'),(23,'FF0000','紅色'),(24,'800080','紫色'),(25,'FFA500','橘色'),(26,'800000','暗紅'),(27,'FF00FF','洋紅'),(28,'F0E68C','卡其'),(29,'FFD700','金色'),(30,'F5DEB3','小麥色'),(34,'000080','海軍'),(35,'E6E6FA','淡紫');
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
  `url` varchar(255) NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `image_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Image`
--

LOCK TABLES `Image` WRITE;
/*!40000 ALTER TABLE `Image` DISABLE KEYS */;
INSERT INTO `Image` VALUES (23,'5080c30f-2277-4d3d-a6d4-bb6011c44b94_4e964d50.jpeg',12),(24,'d54efcf5-5c28-4567-a4d3-0723f55fdb52_7b21272c.jpeg',12),(25,'b7f3690a-aecb-4d09-83ef-e151098aae05_201807202140_1.jpg',13),(26,'3acee6f8-e08a-4d2c-b5c0-d7b80af685f9_201807202140_0.jpg',13),(27,'fd371b89-5504-4456-8b9d-3e8cc14784b8_201807202150_0.jpg',14),(28,'99c4a942-f1f9-4d35-87bb-78348c7f12a6_201807202150_1.jpg',14),(29,'9925ae6d-d0d3-4c25-a555-77df1ded07b2_201807202157_1.jpg',15),(30,'da89b6b9-089f-4973-9ea3-dd633efdea5f_201807202157_0.jpg',15),(35,'650057dd-3057-4a8a-bb28-4464030e13a7_201807202157_1.jpg',18),(36,'9c18eb1c-5e83-411f-9487-ce371e4b76be_201807202157_0.jpg',18),(37,'fa6aaaf9-b644-4734-b516-047fd0778fb7_201807242234_0.jpg',19),(38,'f86124b9-b457-4add-8a7c-481f0e4d3914_201807242234_1.jpg',19),(39,'b41ba40f-be17-4e90-9212-cea993091cc1_201807202140_1.jpg',20),(40,'795243af-7b46-4602-bca6-4921d87ad2c1_201807202140_0.jpg',20),(41,'cbe823ff-dcac-4d8a-902d-11f4d17f868a_201807202150_0.jpg',21),(42,'4cc1809e-9447-43da-9cf4-a778314b308a_201807202150_1.jpg',21),(43,'53d481e0-60e3-4a47-8ebc-eb42e387fdc0_201807242216_1.jpg',22),(44,'8e7696a2-150b-470a-bb93-171f9c691a52_201807242216_0.jpg',22),(45,'5aaf352c-b161-4ec9-acaa-42e2d2efee52_201807242211_1.jpg',23),(46,'488f8fa7-adca-478e-a2ae-55be4bcb40b4_201807242211_0.jpg',23),(47,'c8c87f89-a85b-4766-adcc-acc1d74621c1_201807242230_1.jpg',24),(48,'9d623142-ab5f-4b10-8112-1e4e6f36438f_201807242230_0.jpg',24),(49,'4a917419-6302-44df-966f-33a2843712a7_4e964d50.jpeg',25),(50,'3c49fa1a-635a-4cce-82eb-013156a4726d_7b21272c.jpeg',25),(51,'c22f88e1-a0bc-46f3-a25c-947b1c227aae_201807242234_1.jpg',26),(52,'0f9da41f-f2a7-4493-aaac-33800f57a586_201807242234_0.jpg',26),(53,'7f878ff6-f803-4617-92cf-805a8258b24a_201807202150_1.jpg',27),(54,'0452771c-0520-40c5-9ec6-03c5f75874f8_201807202150_0.jpg',27),(55,'7a86b31c-2c05-41a5-b0fc-0a929e19a93c_201807202140_1.jpg',28),(56,'86bba4d1-5f42-4e7d-a249-037dc82f52fd_201807202140_0.jpg',28),(63,'cc5c423b-437b-4b85-a81d-6ac8d65f8979_201807242234_1.jpg',32),(64,'b8505cec-04b8-4acf-bd9d-3dbbaa3d7c38_201807242234_0.jpg',32);
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
  `category` varchar(50) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `price` bigint NOT NULL,
  `texture` varchar(255) DEFAULT NULL,
  `wash` varchar(50) DEFAULT NULL,
  `place` varchar(50) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `story` varchar(255) DEFAULT NULL,
  `main_image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `product_chk_1` CHECK ((`price` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Product`
--

LOCK TABLES `Product` WRITE;
/*!40000 ALTER TABLE `Product` DISABLE KEYS */;
INSERT INTO `Product` VALUES (12,'women','女用毛衣1','好穿',4000,'尼龍','水洗','泰國','好很穿','有故事的毛衣','61617bb8-0760-439f-80f4-f0f095b56deb_5a54ebc4.jpeg'),(13,'women','女用毛衣2','好穿',5000,'麻布','水洗','泰國','好很穿','有故事的毛衣','e5793a62-66e3-46f3-8605-0362d5e2e1fe_201807202140_main.jpg'),(14,'women','女用毛衣3','好穿',7000,'麻布','水洗','泰國','好很穿','有故事的毛衣','5792f929-cae6-47d4-a9d1-4c0c12e0c5c0_201807202150_main.jpg'),(15,'women','女用毛衣4','好穿',3000,'麻布','水洗','泰國','好很穿','有故事的毛衣','0c544af1-e22b-49fb-b14a-f09794671a30_201807202157_main.jpg'),(18,'women','女用毛衣5','好穿',3000,'麻布','水洗','泰國','好很穿','有故事的毛衣','145f2d36-3404-4e51-af60-f1e63d29be5e_201807202157_main.jpg'),(19,'women','女用毛衣6','好穿',6000,'麻布','水洗','泰國','好很穿','有故事的毛衣','e61c0c98-d634-4d92-86b4-d96ba074bca4_201807242234_main.jpg'),(20,'women','女用毛衣7','好穿',6000,'麻布','水洗','泰國','好很穿','有故事的毛衣','500278c2-1a70-41fc-bb91-32c8bb5df90e_201807202140_main.jpg'),(21,'women','女用毛衣8','好穿',1410,'麻布','水洗','泰國','好很穿','有故事的毛衣','7070bdd7-14ba-4c47-8804-7666c78341e2_201807202150_main.jpg'),(22,'men','男用西裝1','好穿',3423,'麻布','水洗','泰國','好很穿','有故事的毛衣','0f611823-a971-4e70-b115-01e52e2fa1d6_201807242216_main.jpg'),(23,'men','男用西裝2','好穿',4242,'麻布','水洗','泰國','好很穿','有故事的毛衣','2489fb58-ddf7-4cb5-a776-c0bd0a819181_201807242211_main.jpg'),(24,'accessories','男用帽子','好穿',4832,'麻布','水洗','泰國','好很穿','有故事的毛衣','305efbec-6911-4b23-9a16-ce958e4a866c_201807242230_main.jpg'),(25,'women','女用衣服','好穿',5222,'麻布','水洗','泰國','好很穿','有故事的毛衣','00de0481-2f08-4d3c-af1c-8d9b08e8f8a3_5a54ebc4.jpeg'),(26,'women','女用衣服10','好穿',5222,'麻布','水洗','泰國','好很穿','有故事的毛衣','4ce7e9d7-563a-4ccd-b3d3-ed9b061192b1_201807242234_main.jpg'),(27,'women','女用衣服11','好穿',6555,'麻布','水洗','泰國','好很穿','有故事的毛衣','e6b5faf4-f3ec-4182-aef9-45014a96c058_201807202150_main.jpg'),(28,'women','女用衣服12','好穿',1444,'麻布','水洗','泰國','好很穿','有故事的毛衣','d31fb3bc-623d-424b-8efd-7efdc2678886_201807202140_main.jpg'),(32,'women','女用衣服13','好穿',9999,'麻布','水洗','泰國','好很穿','有故事的毛衣','8dd17183-0638-4b32-b577-bebc36e4972d_201807242234_main.jpg');
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
  `size` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `size` (`size`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Size`
--

LOCK TABLES `Size` WRITE;
/*!40000 ALTER TABLE `Size` DISABLE KEYS */;
INSERT INTO `Size` VALUES (4,'L'),(5,'M'),(6,'S');
/*!40000 ALTER TABLE `Size` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UserInfo`
--

DROP TABLE IF EXISTS `UserInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UserInfo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `provider` varchar(50) NOT NULL,
  `picture` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserInfo`
--

LOCK TABLES `UserInfo` WRITE;
/*!40000 ALTER TABLE `UserInfo` DISABLE KEYS */;
INSERT INTO `UserInfo` VALUES (3,'邱大恕','maxchauo0628@gmail.com',NULL,'facebook','https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=4095871230640126&height=200&width=200&ext=1750251191&hash=AT-83RECpuhDSuRZo0y8vvU7');
/*!40000 ALTER TABLE `UserInfo` ENABLE KEYS */;
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
  CONSTRAINT `variant_ibfk_1` FOREIGN KEY (`size_id`) REFERENCES `Size` (`id`),
  CONSTRAINT `variant_ibfk_2` FOREIGN KEY (`color_id`) REFERENCES `Color` (`id`),
  CONSTRAINT `variant_ibfk_3` FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`),
  CONSTRAINT `variant_chk_1` CHECK ((`stock` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Variant`
--

LOCK TABLES `Variant` WRITE;
/*!40000 ALTER TABLE `Variant` DISABLE KEYS */;
INSERT INTO `Variant` VALUES (4,5,12,1231),(5,5,12,2132),(5,7,12,122),(6,6,12,0),(4,8,13,131),(5,6,13,12),(5,9,13,346),(6,5,13,123),(4,12,14,54),(5,6,14,12),(5,11,14,85),(5,12,14,346),(6,10,14,75),(4,13,15,86),(4,14,15,56),(5,13,15,0),(5,14,15,678),(6,13,15,0),(4,6,18,52),(4,14,18,5),(5,19,18,4),(4,6,19,52),(4,14,19,5),(5,19,19,4),(5,20,20,4),(6,20,20,2),(6,21,20,0),(4,22,21,42),(5,22,21,21),(6,23,21,32),(5,24,22,24),(5,25,22,11),(6,24,22,7),(4,24,23,0),(4,25,23,87),(5,24,23,0),(5,5,24,3),(5,26,24,2),(6,6,24,1),(5,5,25,3),(5,26,25,2),(6,6,25,1),(4,27,26,6),(5,22,26,6),(5,27,26,5),(4,6,27,2),(5,6,27,3),(6,5,27,0),(4,28,28,3),(4,29,28,2),(4,30,28,0),(4,30,32,31),(4,35,32,41),(5,30,32,0),(5,34,32,32),(5,35,32,23),(6,30,32,22),(6,35,32,21);
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

-- Dump completed on 2025-05-19 21:11:31
