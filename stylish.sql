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
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Color`
--

LOCK TABLES `Color` WRITE;
/*!40000 ALTER TABLE `Color` DISABLE KEYS */;
INSERT INTO `Color` VALUES (1,'959493','灰色'),(2,'959493','灰色'),(3,'959493','灰色'),(4,'896a4c','棕色'),(5,'896a4c','棕色'),(6,'FFFFFF','白色'),(7,'FFFFFF','白色'),(8,'FFFFFF','白色'),(9,'334455','深藍'),(10,'334455','深藍'),(11,'334455','深藍'),(12,'064b0b','深綠'),(13,'ee2474','桃紅色'),(14,'ee2474','桃紅色'),(15,'ee2474','桃紅色'),(16,'fa66a1','粉紅色'),(17,'fa66a1','粉紅色'),(18,'41d0f6','天藍色'),(19,'41d0f6','天藍色'),(20,'41d0f6','天藍色'),(21,'41f66d','亮綠色'),(22,'41f66d','亮綠色'),(23,'b2c1b5','淺綠色'),(24,'b2c1b5','淺綠色'),(25,'b2c1b5','淺綠色'),(26,'808280','淡灰色'),(27,'808280','淡灰色'),(28,'215ccc','藍色'),(29,'215ccc','藍色'),(30,'898e99','淡灰色'),(31,'898e99','淡灰色'),(32,'215ccc','藍色'),(33,'215ccc','藍色'),(34,'898e99','淡灰色'),(35,'898e99','淡灰色'),(36,'898e99','淡灰色'),(37,'215ccc','藍色'),(38,'215ccc','藍色'),(39,'898e99','淡灰色'),(40,'898e99','淡灰色'),(41,'898e99','淡灰色'),(42,'30e6ec','水藍色'),(43,'30e6ec','水藍色'),(44,'30e6ec','水藍色'),(45,'30e6ec','水藍色'),(46,'30e6ec','水藍色'),(47,'1d9be8','藍色'),(48,'1d9be8','藍色'),(49,'d7f20f','土黃色'),(50,'d7f20f','土黃色'),(51,'d7f20f','土黃色'),(52,'f20ef6','桃紅色'),(53,'f20ef6','桃紅色'),(54,'c0bfc0','絕望灰'),(55,'c0bfc0','絕望灰'),(56,'c0bfc0','絕望灰'),(57,'22cdf0','活力藍'),(58,'22cdf0','活力藍'),(59,'22cdf0','活力藍'),(60,'f1e609','金黃色'),(61,'f1e609','金黃色'),(62,'f1e609','金黃色'),(63,'8e8810','抹茶色'),(64,'8e8810','抹茶色'),(65,'8e8810','抹茶色'),(66,'FFFFFF','白色'),(67,'FFFFFF','白色'),(68,'FFFFFF','白色'),(69,'8e4b10','棕色'),(70,'8e4b10','棕色'),(71,'8e4b10','棕色'),(72,'dc12f0','粉紅色'),(73,'dc12f0','粉紅色'),(74,'dc12f0','粉紅色'),(75,'000000','葬禮黑'),(76,'000000','葬禮黑'),(77,'000000','葬禮黑'),(78,'000000','黑色'),(79,'000000','黑色'),(80,'000000','黑色');
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
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Image`
--

LOCK TABLES `Image` WRITE;
/*!40000 ALTER TABLE `Image` DISABLE KEYS */;
INSERT INTO `Image` VALUES (1,'fbb835ba-3bdc-4a6e-ba6c-02d4a6fb82fd_201807242216_1.jpg',1),(2,'5b8c8371-c248-4639-8461-689438aa1870_201807242216_0.jpg',1),(3,'c88b04c4-5416-400e-8a1f-187b33b4d633_201807242211_1.jpg',2),(4,'5bc1aa65-4854-43a2-9da3-8fc1c0fe4466_201807242211_0.jpg',2),(5,'8b9b00db-7798-4f9a-b8a4-40d855682dc5_201807242222_0.jpg',3),(6,'c423deee-3d92-471e-9f63-b8459b3ca9e6_7b21272c.jpeg',4),(7,'caae4560-4795-42cc-b7ea-923f60af8e66_4e964d50.jpeg',4),(8,'50fa7f1d-2ac0-43f5-b786-cc6b295acd37_201807202140_1.jpg',5),(9,'10564b66-dcd8-4b6f-8227-0f16862decd4_201807202140_0.jpg',5),(10,'6c9384fe-e0c0-41e1-a2ee-03baa625b00e_201807202150_0.jpg',6),(11,'eed4f900-624b-408b-a61a-6f6f81c5246a_201807202150_1.jpg',6),(14,'86cee818-0239-498f-bffb-e90149df5600_201807202157_0.jpg',8),(15,'92f15208-d62e-48b6-b586-ca04df7bcff8_201807202157_1.jpg',8),(16,'1a55deba-fd98-48bd-8d69-cdfc311e2cbf_201807242234_0.jpg',9),(17,'6f15f495-5ba9-4189-9e46-5c2ae3fe41fb_201807242234_1.jpg',9),(20,'a768bec7-f7c7-435f-b39f-48b212900d0c_7b21272c.jpeg',11),(21,'a670a99e-bae9-4ac5-8663-07fb488fe433_4e964d50.jpeg',11),(22,'aba7cb15-e698-4829-b9a3-20ef92ea5219_201807202140_1.jpg',12),(23,'5fef6618-5c2d-40fa-8538-a90c5beaa57e_201807202140_0.jpg',12),(24,'f598b3d8-7a86-4a25-b259-bf0fdebc22d5_201807202150_1.jpg',13),(25,'3c43c9a1-6ec1-405e-90e5-a055df20eb64_201807202150_0.jpg',13),(26,'f5605a22-a98f-4ec3-bba2-36d053d2eedf_201807202157_1.jpg',14),(27,'4efb4765-ada2-418e-953c-bfc862110b58_201807202157_0.jpg',14),(28,'04698f91-0108-4947-914d-e91d2fa27941_201807242234_1.jpg',15),(29,'a29fa9a2-945d-45f2-904b-dc39c55b6555_201807242234_0.jpg',15),(30,'b679a0e8-2d7b-4d95-8460-558808992591_7b21272c.jpeg',16),(31,'8c97228a-2b6b-41ec-9040-da68ef8c9e56_4e964d50.jpeg',16),(32,'b647e183-1ea5-493e-ab41-e8669889b795_201807202150_1.jpg',17),(33,'d505306e-9eb0-46fe-9701-76ebb367cda6_201807202150_0.jpg',17),(34,'3cf91b3b-4ac5-43fe-b618-7be2647aac0d_201807242230_1.jpg',18),(35,'da71041e-0d49-46a4-89ec-13bf21fc8d33_201807242230_0.jpg',18),(36,'d7f83366-c4a0-45b3-b521-7543e54d60d3_201902191247_0.jpg',19),(37,'cd2c988f-5775-40e2-b513-9104236b26b4_201807242232_1.jpg',19),(38,'aa8f56e5-b65d-4b57-a2ea-c9e90af6854c_201902191247_0.jpg',20),(39,'b27bbb8b-27d1-482c-ac74-5f2a52343cef_201807242232_1.jpg',20),(40,'c0128da1-3878-4e4a-8c18-701593c8e19c_201807242234_1.jpg',21),(41,'f2b80f84-e79c-4c59-819d-5e17828e5e12_201807242234_0.jpg',21);
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
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Product`
--

LOCK TABLES `Product` WRITE;
/*!40000 ALTER TABLE `Product` DISABLE KEYS */;
INSERT INTO `Product` VALUES (1,'men','大野狼商務西裝','辦公室一匹狼',7600,'高級紐西蘭羊毛','請乾洗','美國','請參考實體','成熟穩重的外觀，讓客戶加倍信任您','bec73718-2937-473c-8e68-f5f3178271e2_201807242216_main.jpg'),(2,'men','Faker萬年白襯衫','中路大魔王 Faker 居家服裝',200,'韓國平價棉布','水洗','韓國','請參考實體','成功人士的標配，讓你花時間在打扮上，專注於本業','0b15d16a-27f4-4de2-9d18-c1e151995466_201807242211_main.jpg'),(3,'men','華爾街之狼西裝外套','李奧納多皮卡丘之戰袍',70000,'尊榮鱷魚皮羊毛','乾洗','美國(新墨西哥州)','請參考實體','詐騙專家都愛這套西裝','a7830bb1-c0a2-4a99-baeb-b545010d6726_201807242222_main.jpg'),(4,'women','碎花洋裝','少女心碎花洋裝',900,'平價棉布','水洗','中國','請參考實體','有少女心就可以穿，不限年齡','08727341-1f22-48a6-84fc-d7ddfdcb5d03_5a54ebc4.jpeg'),(5,'women','英式透氣洋裝','很透氣',900,'600','水洗','英國','請參考實體','讓你在英國的夏天更容易邂逅帥哥','dfcbe3df-c974-4895-b272-39e43430e19f_201807202140_main.jpg'),(6,'women','法式潮流披風','法國人的最愛',600,'羊毛','乾洗','法國','請參考實體','讓你在法國旅行時更像本地人','8c6d22c2-6651-4edd-8d85-e10345cd670d_201807202150_main.jpg'),(8,'women','新女性寬褲','解放女性勞動力量，下凡耕田，勞動階級的逆襲',9000,'尼龍','不用洗','越南','請參考實體','解放女性力量，提高生產力，此褲不用清洗，他會自己變乾淨','232cfd94-fecc-4828-9b31-757f4f09e96e_201807202157_main.jpg'),(9,'women','神奇秘魯毛衣','慵懶而不頹廢的style,讓你征服每個帥潮',2000,'頂級秘魯羊毛(純有機來自原始部落)','看你要不要洗，建議乾洗','秘魯','請參考實體','來自南美洲的神秘力量，讓你生活充滿驚喜','5d9654fe-bc9f-4a02-94a5-aad2d5fba99c_201807242234_main.jpg'),(11,'women','斑斕洋裝海底遨遊版','讓你不用水肺就可以在海底呼吸',2000,'斑斕海帶魚的鱗片(來自澳洲大堡礁)','這個算潛水裝，不用洗','澳洲','請參考實體','穿上後化身小美人魚','1dfb6c8a-f7dd-4fdc-8723-02883d716f10_5a54ebc4.jpeg'),(12,'women','星際效應女主太空裝內的衣服','讓你穿上就可以有遨遊太公的感覺',50000,'高品質石墨烯','洗了就沒用了(衣服內有高濃度迷幻藥)','台灣','請參考實體','穿上它，去電影院要小心被警犬聞到','08d7304d-4115-4130-9dbe-6a8b7b2560f4_201807202140_main.jpg'),(13,'women','高中國文老師專用教學服裝','讓你不用考取教師正也能體驗國小老師的一天',300,'連假尼龍','手洗(手洗限定，免費體驗傳統美德)','中國四川','請參考實體','不體驗也罷','6007ab66-d231-4ade-b063-964a55d505a0_201807202150_main.jpg'),(14,'women','潮流大學生寬褲','穿上他馬上變潮流女子',500,'連假羊毛','男朋友們幫忙洗','華語地區','請參考實體','只要男友們夠舔，你就不用洗衣服，造福女性友人','27e6cd07-674e-4322-bff9-c149f97169cf_201807202157_main.jpg'),(15,'women','萬磁王毛衣','穿上他馬上變萬磁王',99999999,'阿斯嘉德神秘礦石','放到洗衣機，洗衣機會壞掉','阿斯嘉德','請參考實體','非神族無法裝備，嚴重缺貨中','52f92fd1-7ea6-45bf-8597-e2c007af29cd_201807242234_main.jpg'),(16,'women','村婦的採茶裝(!!最後三組!!)','西元1670年彰化採茶婦女必備',20,'臣本布衣，躬耕於南陽....','不用洗','台灣彰化','請參考實體','西元1670年最時尚的女裝','f1f78f99-2065-4024-aa89-6cee69c37025_5a54ebc4.jpeg'),(17,'women','佛教慈濟醫療財團法人花蓮慈濟醫院合心十一樓整形外科指定護理師服裝+護士帽(統一白色)','辛苦了護理師，我愛慈濟，禁止離職',1,'塑膠','醫院不會幫忙洗','慈濟回收廠寶特瓶回收之塑膠纖維','請參考實體','不要再離職了，阿長頭很痛，護士帽強製裝備(女性限定)','5711901a-af4a-4b08-9357-300c1e803656_201807202150_main.jpg'),(18,'accessories','印度移工紳士帽(賣到缺貨)','提升印度移工社會地位',1000,'中國廉價塑膠棉花','不用洗','中國','請參考實體','帥就完事了','52a489af-9cfd-4433-840b-dec6b0442b7f_201807242230_main.jpg'),(19,'accessories','國小女童專用真珠美人魚粉紅手提包(因少子化導致工廠停產)','非國小女童無法裝備',555,'中國高級塑膠','請爸爸洗','中國','請參考實體','非國小女童裝備，會導致使用者變身為國小女童','d706898c-c614-48f8-99d2-3e6b6ed402ae_201807242232_main.jpg'),(20,'accessories','退流行之勞力士','該死的父權主義者標配(貨源充足)',99999999,'孟加拉報廢車回收鋼材','洗了馬上壞','孟加拉','請參考實體','父權主義者必須配戴，衰減陽壽二十年','eb3fa59b-50f5-4551-9587-1e9d77edefa3_201807242222_1.jpg'),(21,'women','幻術毛衣','宇智波幻術毛衣',4444,'宇智波體毛','手洗','木葉忍村','以實體完主','裝備者立即擁有宇智波瞳術','b032f099-46db-4376-9148-52b1112ab8ec_201807242234_main.jpg');
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
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Size`
--

LOCK TABLES `Size` WRITE;
/*!40000 ALTER TABLE `Size` DISABLE KEYS */;
INSERT INTO `Size` VALUES (1,'L'),(2,'M'),(3,'S'),(4,'L'),(5,'S'),(6,'L'),(7,'M'),(8,'S'),(9,'L'),(10,'M'),(11,'S'),(12,'L'),(13,'L'),(14,'M'),(15,'S'),(16,'M'),(17,'S'),(18,'L'),(19,'M'),(20,'S'),(21,'M'),(22,'S'),(23,'L'),(24,'M'),(25,'S'),(26,'M'),(27,'S'),(28,'L'),(29,'M'),(30,'S'),(31,'M'),(32,'L'),(33,'M'),(34,'S'),(35,'M'),(36,'L'),(37,'M'),(38,'S'),(39,'S'),(40,'M'),(41,'L'),(42,'M'),(43,'S'),(44,'L'),(45,'M'),(46,'S'),(47,'L'),(48,'S'),(49,'L'),(50,'M'),(51,'S'),(52,'Ｍ'),(53,'S'),(54,'L'),(55,'M'),(56,'S'),(57,'L'),(58,'M'),(59,'S'),(60,'L'),(61,'M'),(62,'S'),(63,'L'),(64,'M'),(65,'S'),(66,'L'),(67,'M'),(68,'S'),(69,'L'),(70,'M'),(71,'S'),(72,'L'),(73,'M'),(74,'S'),(75,'L'),(76,'M'),(77,'S'),(78,'L'),(79,'M'),(80,'S');
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
INSERT INTO `Variant` VALUES (1,1,1,33),(2,2,1,123),(3,3,1,0),(4,4,1,12),(5,5,1,44),(6,6,2,99992),(7,7,2,0),(8,8,2,89999),(9,9,3,432),(10,10,3,12),(11,11,3,414),(12,12,3,33),(13,13,4,341),(14,14,4,0),(15,15,4,221),(16,16,4,32),(17,17,4,12),(18,18,5,23),(19,19,5,12),(20,20,5,34),(21,21,5,77),(22,22,5,98),(23,23,6,57),(24,24,6,43),(25,25,6,44),(26,26,6,145),(27,27,6,45),(32,32,8,999),(33,33,8,999),(34,34,8,999),(35,35,8,999),(36,36,8,999),(37,37,9,123),(38,38,9,0),(39,39,9,31),(40,40,9,0),(41,41,9,12),(44,44,11,254),(45,45,11,252),(46,46,11,421),(47,47,11,411),(48,48,11,0),(49,49,12,43),(50,50,12,23),(51,51,12,47),(52,52,12,373),(53,53,12,555),(54,54,13,33),(55,55,13,0),(56,56,13,44),(57,57,14,999),(58,58,14,999),(59,59,14,999),(60,60,15,0),(61,61,15,0),(62,62,15,0),(63,63,16,1),(64,64,16,1),(65,65,16,1),(66,66,17,999999),(67,67,17,999999),(68,68,17,999999),(69,69,18,0),(70,70,18,5),(71,71,18,0),(72,72,19,2),(73,73,19,2),(74,74,19,2),(75,75,20,100000),(76,76,20,100000),(77,77,20,100000),(78,78,21,43),(79,79,21,23),(80,80,21,0);
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

-- Dump completed on 2025-04-06  7:46:48
