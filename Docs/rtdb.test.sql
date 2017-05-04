-- MySQL dump 10.13  Distrib 5.7.17, for Linux (x86_64)
--
-- Host: localhost    Database: rtdb
-- ------------------------------------------------------
-- Server version	5.7.17-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tbl_deliverables`
--

DROP TABLE IF EXISTS `tbl_deliverables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_deliverables` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_deliverables`
--

LOCK TABLES `tbl_deliverables` WRITE;
/*!40000 ALTER TABLE `tbl_deliverables` DISABLE KEYS */;
INSERT INTO `tbl_deliverables` VALUES (1,'大门钥匙'),(3,'信报箱钥匙'),(4,'门禁卡'),(5,'钥匙A'),(6,'测试的交付物');
/*!40000 ALTER TABLE `tbl_deliverables` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_facility_type`
--

DROP TABLE IF EXISTS `tbl_facility_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_facility_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `type_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_facility_type`
--

LOCK TABLES `tbl_facility_type` WRITE;
/*!40000 ALTER TABLE `tbl_facility_type` DISABLE KEYS */;
INSERT INTO `tbl_facility_type` VALUES (4,'5rWL6K-V6K6-5pa9\n'),(3,'6K6-5pa9\n'),(1,'家具'),(2,'家电'),(6,'测试类别'),(5,'测试设施');
/*!40000 ALTER TABLE `tbl_facility_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_facilitys`
--

DROP TABLE IF EXISTS `tbl_facilitys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_facilitys` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` bigint(20) NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `type` (`type`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_facilitys`
--

LOCK TABLES `tbl_facilitys` WRITE;
/*!40000 ALTER TABLE `tbl_facilitys` DISABLE KEYS */;
INSERT INTO `tbl_facilitys` VALUES (6,1,'沙发茶几'),(8,1,'测试'),(3,2,'电冰箱'),(4,2,'电视机'),(7,2,'立式空调');
/*!40000 ALTER TABLE `tbl_facilitys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_house`
--

DROP TABLE IF EXISTS `tbl_house`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_house` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `building_no` int(11) NOT NULL DEFAULT '0',
  `floor_total` int(11) NOT NULL DEFAULT '0',
  `floor_this` int(11) NOT NULL DEFAULT '0',
  `house_no` varchar(20) NOT NULL DEFAULT '',
  `bedrooms` int(11) NOT NULL DEFAULT '0',
  `livingrooms` int(11) NOT NULL DEFAULT '0',
  `bathrooms` int(11) NOT NULL DEFAULT '0',
  `acreage` int(11) NOT NULL DEFAULT '0',
  `cover_img` bigint(20) NOT NULL DEFAULT '0',
  `property_id` bigint(20) NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  `agency_id` bigint(20) NOT NULL,
  `submit_time` datetime NOT NULL,
  `publish_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `rent_stat` int(11) NOT NULL DEFAULT '0',
  `for_sale` tinyint(1) NOT NULL DEFAULT '0',
  `for_rent` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `property_id` (`property_id`,`building_no`,`house_no`),
  KEY `tbl_house_property_id` (`property_id`),
  KEY `tbl_house_agency_id` (`agency_id`),
  KEY `tbl_house_for_sale` (`for_sale`),
  KEY `tbl_house_for_rent` (`for_rent`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_house`
--

LOCK TABLES `tbl_house` WRITE;
/*!40000 ALTER TABLE `tbl_house` DISABLE KEYS */;
INSERT INTO `tbl_house` VALUES (2,199,35,15,'1505',2,1,1,10000,31,2,9,6,'2017-01-26 07:39:04','2017-05-03 21:14:42','2017-05-03 21:14:42',1,0,0),(3,177,35,17,'1505',3,2,2,13148,0,1,10,4,'2017-01-26 12:31:55','2017-04-13 00:00:00',NULL,2,0,1),(4,56,32,16,'1605',3,2,2,13148,0,2,4,6,'2017-01-31 17:41:20',NULL,NULL,0,0,1),(6,56,35,16,'1606',4,2,3,17788,30,2,2,1,'2017-03-04 20:10:03','2017-04-19 00:00:00','2017-03-04 23:30:49',0,0,1);
/*!40000 ALTER TABLE `tbl_house` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_house_cert`
--

DROP TABLE IF EXISTS `tbl_house_cert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_house_cert` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `house` bigint(20) NOT NULL DEFAULT '0',
  `who` bigint(20) NOT NULL DEFAULT '0',
  `when` datetime NOT NULL,
  `comment` varchar(20) NOT NULL DEFAULT '',
  `pass` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `tbl_house_cert_house` (`house`),
  KEY `tbl_house_cert_when` (`when`)
) ENGINE=InnoDB AUTO_INCREMENT=632 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_house_cert`
--

LOCK TABLES `tbl_house_cert` WRITE;
/*!40000 ALTER TABLE `tbl_house_cert` DISABLE KEYS */;
INSERT INTO `tbl_house_cert` VALUES (1,6,4,'2017-03-11 20:15:01','5pKk6ZSA5Y-R5biD\n',0),(2,6,4,'2017-03-11 20:21:13','已经和业主核实，同意发布',1),(3,6,4,'2017-03-11 20:22:05','撤销发布',0),(4,6,4,'2017-03-11 20:22:23','已经和业主核实，同意发布',1),(5,2,6,'2017-04-23 21:24:45','test pass',0),(6,2,6,'2017-04-23 21:25:41','test pass',0),(7,2,6,'2017-04-23 21:29:29','test pass',0),(8,2,6,'2017-04-23 21:30:03','test pass',0),(9,2,6,'2017-04-23 21:32:35','test pass',0),(10,2,6,'2017-04-23 22:49:06','test pass',0),(11,2,6,'2017-04-23 22:53:17','test pass',0),(12,2,4,'2017-04-23 22:53:17','test pass',0),(13,2,6,'2017-04-23 22:55:03','test pass',0),(14,2,4,'2017-04-23 22:55:03','test pass',0),(15,2,6,'2017-04-23 22:55:44','test pass',0),(16,2,4,'2017-04-23 22:55:44','test pass',0),(17,2,4,'2017-04-23 22:55:44','certification passed',1),(18,2,6,'2017-04-23 22:59:06','test pass',0),(19,2,4,'2017-04-23 22:59:06','test pass',0),(20,2,4,'2017-04-23 22:59:06','certification passed',1),(21,2,6,'2017-04-23 22:59:34','test pass',0),(22,2,4,'2017-04-23 22:59:34','test pass',0),(23,2,4,'2017-04-23 22:59:34','certification passed',1),(24,2,6,'2017-04-23 23:03:35','test pass',0),(25,2,6,'2017-04-23 23:03:35','certification passed',1),(26,2,4,'2017-04-23 23:03:35','test pass',0),(27,2,4,'2017-04-23 23:03:35','certification passed',1),(28,2,6,'2017-04-24 21:08:41','test pass',0),(29,2,6,'2017-04-24 21:08:41','certification passed',1),(30,2,4,'2017-04-24 21:08:41','test pass',0),(31,2,4,'2017-04-24 21:08:41','certification passed',1),(32,2,6,'2017-04-24 21:09:37','test pass',0),(33,2,6,'2017-04-24 21:09:37','certification passed',1),(34,2,4,'2017-04-24 21:09:37','test pass',0),(35,2,4,'2017-04-24 21:09:37','certification passed',1),(36,2,6,'2017-04-24 21:10:16','test pass',0),(37,2,6,'2017-04-24 21:10:16','certification passed',1),(38,2,4,'2017-04-24 21:10:16','test pass',0),(39,2,4,'2017-04-24 21:10:16','certification passed',1),(40,2,6,'2017-04-24 21:10:37','test pass',0),(41,2,6,'2017-04-24 21:10:37','certification passed',1),(42,2,4,'2017-04-24 21:10:37','test pass',0),(43,2,4,'2017-04-24 21:10:37','certification passed',1),(44,2,6,'2017-04-24 21:11:05','test pass',0),(45,2,6,'2017-04-24 21:11:05','certification passed',1),(46,2,4,'2017-04-24 21:11:05','test pass',0),(47,2,4,'2017-04-24 21:11:05','certification passed',1),(48,2,6,'2017-04-24 21:11:17','test pass',0),(49,2,6,'2017-04-24 21:11:17','certification passed',1),(50,2,4,'2017-04-24 21:11:17','test pass',0),(51,2,4,'2017-04-24 21:11:17','certification passed',1),(52,2,6,'2017-04-24 21:13:45','test pass',0),(53,2,6,'2017-04-24 21:13:45','certification passed',1),(54,2,4,'2017-04-24 21:13:45','test pass',0),(55,2,4,'2017-04-24 21:13:45','certification passed',1),(56,2,6,'2017-04-24 21:14:20','test pass',0),(57,2,6,'2017-04-24 21:14:20','certification passed',1),(58,2,4,'2017-04-24 21:14:20','test pass',0),(59,2,4,'2017-04-24 21:14:20','certification passed',1),(60,2,6,'2017-04-24 21:16:21','test pass',0),(61,2,6,'2017-04-24 21:16:21','certification passed',1),(62,2,4,'2017-04-24 21:16:21','test pass',0),(63,2,4,'2017-04-24 21:16:21','certification passed',1),(64,2,6,'2017-04-24 21:19:05','test pass',0),(65,2,6,'2017-04-24 21:19:05','certification passed',1),(66,2,4,'2017-04-24 21:19:05','test pass',0),(67,2,4,'2017-04-24 21:19:05','certification passed',1),(68,2,6,'2017-04-24 21:32:27','test pass',0),(69,2,6,'2017-04-24 21:32:27','certification passed',1),(70,2,4,'2017-04-24 21:32:27','test pass',0),(71,2,4,'2017-04-24 21:32:27','certification passed',1),(72,2,6,'2017-04-24 21:32:49','test pass',0),(73,2,6,'2017-04-24 21:32:49','certification passed',1),(74,2,4,'2017-04-24 21:32:49','test pass',0),(75,2,4,'2017-04-24 21:32:49','certification passed',1),(76,2,6,'2017-04-24 21:36:14','test pass',0),(77,2,6,'2017-04-24 21:36:14','certification passed',1),(78,2,4,'2017-04-24 21:36:14','test pass',0),(79,2,4,'2017-04-24 21:36:14','certification passed',1),(80,2,6,'2017-04-24 21:37:21','test pass',0),(81,2,6,'2017-04-24 21:37:21','certification passed',1),(82,2,4,'2017-04-24 21:37:21','test pass',0),(83,2,4,'2017-04-24 21:37:21','certification passed',1),(84,2,6,'2017-04-24 21:37:52','test pass',0),(85,2,6,'2017-04-24 21:37:52','certification passed',1),(86,2,4,'2017-04-24 21:37:52','test pass',0),(87,2,4,'2017-04-24 21:37:52','certification passed',1),(88,2,6,'2017-04-24 21:38:14','test pass',0),(89,2,6,'2017-04-24 21:38:14','certification passed',1),(90,2,4,'2017-04-24 21:38:14','test pass',0),(91,2,4,'2017-04-24 21:38:14','certification passed',1),(92,2,6,'2017-04-24 21:39:05','test pass',0),(93,2,6,'2017-04-24 21:39:05','certification passed',1),(94,2,4,'2017-04-24 21:39:05','test pass',0),(95,2,4,'2017-04-24 21:39:05','certification passed',1),(96,2,6,'2017-04-24 21:39:55','test pass',0),(97,2,6,'2017-04-24 21:39:55','certification passed',1),(98,2,4,'2017-04-24 21:39:55','test pass',0),(99,2,4,'2017-04-24 21:39:55','certification passed',1),(100,2,6,'2017-04-24 21:40:27','test pass',0),(101,2,6,'2017-04-24 21:40:27','certification passed',1),(102,2,4,'2017-04-24 21:40:27','test pass',0),(103,2,4,'2017-04-24 21:40:27','certification passed',1),(104,2,6,'2017-04-24 21:40:58','test pass',0),(105,2,6,'2017-04-24 21:40:58','certification passed',1),(106,2,4,'2017-04-24 21:40:58','test pass',0),(107,2,4,'2017-04-24 21:40:58','certification passed',1),(108,2,6,'2017-04-24 21:41:09','test pass',0),(109,2,6,'2017-04-24 21:41:09','certification passed',1),(110,2,4,'2017-04-24 21:41:09','test pass',0),(111,2,4,'2017-04-24 21:41:09','certification passed',1),(112,2,6,'2017-04-24 21:41:17','test pass',0),(113,2,6,'2017-04-24 21:41:17','certification passed',1),(114,2,4,'2017-04-24 21:41:17','test pass',0),(115,2,4,'2017-04-24 21:41:17','certification passed',1),(116,2,6,'2017-04-24 21:44:12','test pass',0),(117,2,6,'2017-04-24 21:44:12','certification passed',1),(118,2,4,'2017-04-24 21:44:12','test pass',0),(119,2,4,'2017-04-24 21:44:12','certification passed',1),(120,2,6,'2017-04-24 21:48:43','test pass',0),(121,2,6,'2017-04-24 21:48:43','certification passed',1),(122,2,4,'2017-04-24 21:48:43','test pass',0),(123,2,4,'2017-04-24 21:48:43','certification passed',1),(124,2,6,'2017-04-24 21:53:08','test pass',0),(125,2,6,'2017-04-24 21:53:08','certification passed',1),(126,2,4,'2017-04-24 21:53:08','test pass',0),(127,2,4,'2017-04-24 21:53:08','certification passed',1),(128,2,6,'2017-04-24 21:53:15','test pass',0),(129,2,6,'2017-04-24 21:53:15','certification passed',1),(130,2,4,'2017-04-24 21:53:15','test pass',0),(131,2,4,'2017-04-24 21:53:15','certification passed',1),(132,2,6,'2017-04-24 22:06:14','test pass',0),(133,2,6,'2017-04-24 22:06:14','certification passed',1),(134,2,4,'2017-04-24 22:06:14','test pass',0),(135,2,4,'2017-04-24 22:06:14','certification passed',1),(136,2,6,'2017-04-24 22:11:56','test pass',0),(137,2,6,'2017-04-24 22:11:56','certification passed',1),(138,2,4,'2017-04-24 22:11:56','test pass',0),(139,2,4,'2017-04-24 22:11:56','certification passed',1),(140,2,6,'2017-04-24 22:12:57','test pass',0),(141,2,6,'2017-04-24 22:12:57','certification passed',1),(142,2,4,'2017-04-24 22:12:57','test pass',0),(143,2,4,'2017-04-24 22:12:57','certification passed',1),(144,2,6,'2017-04-24 22:15:25','test pass',0),(145,2,6,'2017-04-24 22:15:25','certification passed',1),(146,2,4,'2017-04-24 22:15:25','test pass',0),(147,2,4,'2017-04-24 22:15:25','certification passed',1),(148,2,6,'2017-04-24 22:16:13','test pass',0),(149,2,6,'2017-04-24 22:16:13','certification passed',1),(150,2,4,'2017-04-24 22:16:13','test pass',0),(151,2,4,'2017-04-24 22:16:13','certification passed',1),(152,2,6,'2017-04-24 22:16:41','test pass',0),(153,2,6,'2017-04-24 22:16:41','certification passed',1),(154,2,4,'2017-04-24 22:16:41','test pass',0),(155,2,4,'2017-04-24 22:16:41','certification passed',1),(156,2,6,'2017-04-24 22:17:50','test pass',0),(157,2,6,'2017-04-24 22:17:50','certification passed',1),(158,2,4,'2017-04-24 22:17:50','test pass',0),(159,2,4,'2017-04-24 22:17:50','certification passed',1),(160,2,6,'2017-04-24 22:18:12','test pass',0),(161,2,6,'2017-04-24 22:18:12','certification passed',1),(162,2,4,'2017-04-24 22:18:12','test pass',0),(163,2,4,'2017-04-24 22:18:12','certification passed',1),(164,2,6,'2017-04-24 22:19:19','test pass',0),(165,2,6,'2017-04-24 22:19:19','certification passed',1),(166,2,4,'2017-04-24 22:19:19','test pass',0),(167,2,4,'2017-04-24 22:19:19','certification passed',1),(168,2,6,'2017-04-24 22:19:32','test pass',0),(169,2,6,'2017-04-24 22:19:32','certification passed',1),(170,2,4,'2017-04-24 22:19:32','test pass',0),(171,2,4,'2017-04-24 22:19:32','certification passed',1),(172,2,6,'2017-04-24 22:20:42','test pass',0),(173,2,6,'2017-04-24 22:20:42','certification passed',1),(174,2,4,'2017-04-24 22:20:42','test pass',0),(175,2,4,'2017-04-24 22:20:42','certification passed',1),(176,2,6,'2017-04-24 22:22:08','test pass',0),(177,2,6,'2017-04-24 22:22:08','certification passed',1),(178,2,4,'2017-04-24 22:22:08','test pass',0),(179,2,4,'2017-04-24 22:22:08','certification passed',1),(180,2,6,'2017-04-24 22:23:43','test pass',0),(181,2,6,'2017-04-24 22:23:43','certification passed',1),(182,2,4,'2017-04-24 22:23:43','test pass',0),(183,2,4,'2017-04-24 22:23:43','certification passed',1),(184,2,6,'2017-04-24 22:27:23','test pass',0),(185,2,6,'2017-04-24 22:27:23','certification passed',1),(186,2,4,'2017-04-24 22:27:23','test pass',0),(187,2,4,'2017-04-24 22:27:23','certification passed',1),(188,2,6,'2017-04-24 22:27:50','test pass',0),(189,2,6,'2017-04-24 22:27:50','certification passed',1),(190,2,4,'2017-04-24 22:27:50','test pass',0),(191,2,4,'2017-04-24 22:27:50','certification passed',1),(192,2,6,'2017-04-24 22:40:37','test pass',0),(193,2,6,'2017-04-24 22:40:37','certification passed',1),(194,2,4,'2017-04-24 22:40:37','test pass',0),(195,2,4,'2017-04-24 22:40:37','certification passed',1),(196,2,6,'2017-04-24 22:41:23','test pass',0),(197,2,6,'2017-04-24 22:41:23','certification passed',1),(198,2,4,'2017-04-24 22:41:23','test pass',0),(199,2,4,'2017-04-24 22:41:23','certification passed',1),(200,2,6,'2017-04-24 22:41:48','test pass',0),(201,2,6,'2017-04-24 22:41:48','certification passed',1),(202,2,4,'2017-04-24 22:41:48','test pass',0),(203,2,4,'2017-04-24 22:41:48','certification passed',1),(204,2,6,'2017-04-24 22:42:35','test pass',0),(205,2,6,'2017-04-24 22:42:35','certification passed',1),(206,2,4,'2017-04-24 22:42:35','test pass',0),(207,2,4,'2017-04-24 22:42:35','certification passed',1),(208,2,6,'2017-04-24 22:43:29','test pass',0),(209,2,6,'2017-04-24 22:43:29','certification passed',1),(210,2,4,'2017-04-24 22:43:29','test pass',0),(211,2,4,'2017-04-24 22:43:29','certification passed',1),(212,2,6,'2017-04-24 22:45:22','test pass',0),(213,2,6,'2017-04-24 22:45:22','certification passed',1),(214,2,4,'2017-04-24 22:45:22','test pass',0),(215,2,4,'2017-04-24 22:45:22','certification passed',1),(216,2,6,'2017-04-24 22:46:38','test pass',0),(217,2,6,'2017-04-24 22:46:38','certification passed',1),(218,2,4,'2017-04-24 22:46:38','test pass',0),(219,2,4,'2017-04-24 22:46:38','certification passed',1),(220,2,6,'2017-04-24 22:52:48','test pass',0),(221,2,6,'2017-04-24 22:52:48','certification passed',1),(222,2,4,'2017-04-24 22:52:48','test pass',0),(223,2,4,'2017-04-24 22:52:48','certification passed',1),(224,2,6,'2017-04-24 22:53:12','test pass',0),(225,2,6,'2017-04-24 22:53:12','certification passed',1),(226,2,4,'2017-04-24 22:53:12','test pass',0),(227,2,4,'2017-04-24 22:53:12','certification passed',1),(228,2,6,'2017-04-24 23:21:27','test pass',0),(229,2,6,'2017-04-24 23:21:27','certification passed',1),(230,2,4,'2017-04-24 23:21:27','test pass',0),(231,2,4,'2017-04-24 23:21:27','certification passed',1),(232,2,6,'2017-04-24 23:24:52','test pass',0),(233,2,6,'2017-04-24 23:24:52','certification passed',1),(234,2,4,'2017-04-24 23:24:52','test pass',0),(235,2,4,'2017-04-24 23:24:52','certification passed',1),(236,2,6,'2017-04-24 23:25:04','test pass',0),(237,2,6,'2017-04-24 23:25:04','certification passed',1),(238,2,4,'2017-04-24 23:25:04','test pass',0),(239,2,4,'2017-04-24 23:25:04','certification passed',1),(240,2,6,'2017-04-24 23:25:32','test pass',0),(241,2,6,'2017-04-24 23:25:32','certification passed',1),(242,2,4,'2017-04-24 23:25:32','test pass',0),(243,2,4,'2017-04-24 23:25:32','certification passed',1),(244,2,6,'2017-04-24 23:27:31','test pass',0),(245,2,6,'2017-04-24 23:27:31','certification passed',1),(246,2,4,'2017-04-24 23:27:31','test pass',0),(247,2,4,'2017-04-24 23:27:31','certification passed',1),(248,2,6,'2017-04-24 23:28:03','test pass',0),(249,2,6,'2017-04-24 23:28:03','certification passed',1),(250,2,4,'2017-04-24 23:28:03','test pass',0),(251,2,4,'2017-04-24 23:28:03','certification passed',1),(252,2,6,'2017-04-24 23:29:14','test pass',0),(253,2,6,'2017-04-24 23:29:14','certification passed',1),(254,2,4,'2017-04-24 23:29:14','test pass',0),(255,2,4,'2017-04-24 23:29:14','certification passed',1),(256,2,6,'2017-04-24 23:30:44','test pass',0),(257,2,6,'2017-04-24 23:30:44','certification passed',1),(258,2,4,'2017-04-24 23:30:44','test pass',0),(259,2,4,'2017-04-24 23:30:44','certification passed',1),(260,2,6,'2017-04-24 23:30:59','test pass',0),(261,2,6,'2017-04-24 23:30:59','certification passed',1),(262,2,4,'2017-04-24 23:30:59','test pass',0),(263,2,4,'2017-04-24 23:30:59','certification passed',1),(264,2,6,'2017-04-24 23:31:27','test pass',0),(265,2,6,'2017-04-24 23:31:27','certification passed',1),(266,2,4,'2017-04-24 23:31:27','test pass',0),(267,2,4,'2017-04-24 23:31:27','certification passed',1),(268,2,6,'2017-04-25 20:40:13','test pass',0),(269,2,6,'2017-04-25 20:40:13','certification passed',1),(270,2,4,'2017-04-25 20:40:13','test pass',0),(271,2,4,'2017-04-25 20:40:13','certification passed',1),(272,2,6,'2017-04-25 20:40:40','test pass',0),(273,2,6,'2017-04-25 20:40:40','certification passed',1),(274,2,4,'2017-04-25 20:40:40','test pass',0),(275,2,4,'2017-04-25 20:40:40','certification passed',1),(276,2,6,'2017-04-25 20:40:52','test pass',0),(277,2,6,'2017-04-25 20:40:52','certification passed',1),(278,2,4,'2017-04-25 20:40:52','test pass',0),(279,2,4,'2017-04-25 20:40:52','certification passed',1),(280,2,6,'2017-04-25 20:41:10','test pass',0),(281,2,6,'2017-04-25 20:41:10','certification passed',1),(282,2,4,'2017-04-25 20:41:10','test pass',0),(283,2,4,'2017-04-25 20:41:10','certification passed',1),(284,2,6,'2017-04-25 20:42:36','test pass',0),(285,2,6,'2017-04-25 20:42:36','certification passed',1),(286,2,4,'2017-04-25 20:42:36','test pass',0),(287,2,4,'2017-04-25 20:42:36','certification passed',1),(288,2,6,'2017-04-25 20:42:48','test pass',0),(289,2,6,'2017-04-25 20:42:48','certification passed',1),(290,2,4,'2017-04-25 20:42:48','test pass',0),(291,2,4,'2017-04-25 20:42:48','certification passed',1),(292,2,6,'2017-04-25 20:43:49','test pass',0),(293,2,6,'2017-04-25 20:43:49','certification passed',1),(294,2,4,'2017-04-25 20:43:49','test pass',0),(295,2,4,'2017-04-25 20:43:49','certification passed',1),(296,2,6,'2017-04-25 20:43:54','test pass',0),(297,2,6,'2017-04-25 20:43:54','certification passed',1),(298,2,4,'2017-04-25 20:43:54','test pass',0),(299,2,4,'2017-04-25 20:43:54','certification passed',1),(300,2,6,'2017-04-25 20:45:22','test pass',0),(301,2,6,'2017-04-25 20:45:22','certification passed',1),(302,2,4,'2017-04-25 20:45:22','test pass',0),(303,2,4,'2017-04-25 20:45:22','certification passed',1),(304,2,6,'2017-04-25 20:46:29','test pass',0),(305,2,6,'2017-04-25 20:46:29','certification passed',1),(306,2,4,'2017-04-25 20:46:29','test pass',0),(307,2,4,'2017-04-25 20:46:29','certification passed',1),(308,2,6,'2017-04-25 20:47:31','test pass',0),(309,2,6,'2017-04-25 20:47:31','certification passed',1),(310,2,4,'2017-04-25 20:47:31','test pass',0),(311,2,4,'2017-04-25 20:47:31','certification passed',1),(312,2,6,'2017-04-25 20:49:24','test pass',0),(313,2,6,'2017-04-25 20:49:24','certification passed',1),(314,2,4,'2017-04-25 20:49:24','test pass',0),(315,2,4,'2017-04-25 20:49:24','certification passed',1),(316,2,6,'2017-04-25 20:49:52','test pass',0),(317,2,6,'2017-04-25 20:49:52','certification passed',1),(318,2,4,'2017-04-25 20:49:52','test pass',0),(319,2,4,'2017-04-25 20:49:52','certification passed',1),(320,2,6,'2017-04-25 20:50:02','test pass',0),(321,2,6,'2017-04-25 20:50:02','certification passed',1),(322,2,4,'2017-04-25 20:50:02','test pass',0),(323,2,4,'2017-04-25 20:50:02','certification passed',1),(324,2,6,'2017-04-25 20:53:58','test pass',0),(325,2,6,'2017-04-25 20:53:58','certification passed',1),(326,2,4,'2017-04-25 20:53:58','test pass',0),(327,2,4,'2017-04-25 20:53:58','certification passed',1),(328,2,6,'2017-04-25 20:55:03','test pass',0),(329,2,6,'2017-04-25 20:55:03','certification passed',1),(330,2,4,'2017-04-25 20:55:03','test pass',0),(331,2,4,'2017-04-25 20:55:03','certification passed',1),(332,2,6,'2017-04-25 21:00:53','test pass',0),(333,2,6,'2017-04-25 21:00:53','certification passed',1),(334,2,4,'2017-04-25 21:00:53','test pass',0),(335,2,4,'2017-04-25 21:00:53','certification passed',1),(336,2,6,'2017-04-25 21:01:51','test pass',0),(337,2,6,'2017-04-25 21:01:51','certification passed',1),(338,2,4,'2017-04-25 21:01:51','test pass',0),(339,2,4,'2017-04-25 21:01:51','certification passed',1),(340,2,6,'2017-04-25 21:03:16','test pass',0),(341,2,6,'2017-04-25 21:03:16','certification passed',1),(342,2,4,'2017-04-25 21:03:16','test pass',0),(343,2,4,'2017-04-25 21:03:16','certification passed',1),(344,2,6,'2017-04-25 21:04:02','test pass',0),(345,2,6,'2017-04-25 21:04:02','certification passed',1),(346,2,4,'2017-04-25 21:04:02','test pass',0),(347,2,4,'2017-04-25 21:04:02','certification passed',1),(348,2,6,'2017-04-25 21:05:14','test pass',0),(349,2,6,'2017-04-25 21:05:14','certification passed',1),(350,2,4,'2017-04-25 21:05:14','test pass',0),(351,2,4,'2017-04-25 21:05:14','certification passed',1),(352,2,6,'2017-04-25 21:05:24','test pass',0),(353,2,6,'2017-04-25 21:05:24','certification passed',1),(354,2,4,'2017-04-25 21:05:24','test pass',0),(355,2,4,'2017-04-25 21:05:24','certification passed',1),(356,2,6,'2017-04-25 21:05:28','test pass',0),(357,2,6,'2017-04-25 21:05:28','certification passed',1),(358,2,4,'2017-04-25 21:05:28','test pass',0),(359,2,4,'2017-04-25 21:05:28','certification passed',1),(360,2,6,'2017-04-25 21:05:33','test pass',0),(361,2,6,'2017-04-25 21:05:33','certification passed',1),(362,2,4,'2017-04-25 21:05:33','test pass',0),(363,2,4,'2017-04-25 21:05:33','certification passed',1),(364,2,6,'2017-04-25 21:10:27','test pass',0),(365,2,6,'2017-04-25 21:10:27','certification passed',1),(366,2,4,'2017-04-25 21:10:27','test pass',0),(367,2,4,'2017-04-25 21:10:27','certification passed',1),(368,2,6,'2017-04-25 21:11:41','test pass',0),(369,2,6,'2017-04-25 21:11:41','certification passed',1),(370,2,4,'2017-04-25 21:11:41','test pass',0),(371,2,4,'2017-04-25 21:11:41','certification passed',1),(372,2,6,'2017-04-25 21:11:54','test pass',0),(373,2,6,'2017-04-25 21:11:54','certification passed',1),(374,2,4,'2017-04-25 21:11:54','test pass',0),(375,2,4,'2017-04-25 21:11:54','certification passed',1),(376,2,6,'2017-04-25 21:12:22','test pass',0),(377,2,6,'2017-04-25 21:12:22','certification passed',1),(378,2,4,'2017-04-25 21:12:22','test pass',0),(379,2,4,'2017-04-25 21:12:22','certification passed',1),(380,2,6,'2017-04-25 21:12:43','test pass',0),(381,2,6,'2017-04-25 21:12:43','certification passed',1),(382,2,4,'2017-04-25 21:12:43','test pass',0),(383,2,4,'2017-04-25 21:12:43','certification passed',1),(384,2,6,'2017-04-25 21:13:00','test pass',0),(385,2,6,'2017-04-25 21:13:00','certification passed',1),(386,2,4,'2017-04-25 21:13:00','test pass',0),(387,2,4,'2017-04-25 21:13:00','certification passed',1),(388,2,6,'2017-04-25 21:15:24','test pass',0),(389,2,6,'2017-04-25 21:15:24','certification passed',1),(390,2,4,'2017-04-25 21:15:24','test pass',0),(391,2,4,'2017-04-25 21:15:24','certification passed',1),(392,2,6,'2017-04-25 21:17:41','test pass',0),(393,2,6,'2017-04-25 21:17:41','certification passed',1),(394,2,4,'2017-04-25 21:17:41','test pass',0),(395,2,4,'2017-04-25 21:17:41','certification passed',1),(396,2,6,'2017-04-25 21:18:15','test pass',0),(397,2,6,'2017-04-25 21:18:15','certification passed',1),(398,2,4,'2017-04-25 21:18:15','test pass',0),(399,2,4,'2017-04-25 21:18:15','certification passed',1),(400,2,6,'2017-04-25 21:20:21','test pass',0),(401,2,6,'2017-04-25 21:20:21','certification passed',1),(402,2,4,'2017-04-25 21:20:21','test pass',0),(403,2,4,'2017-04-25 21:20:21','certification passed',1),(404,2,6,'2017-04-25 21:28:16','test pass',0),(405,2,6,'2017-04-25 21:28:16','certification passed',1),(406,2,4,'2017-04-25 21:28:16','test pass',0),(407,2,4,'2017-04-25 21:28:16','certification passed',1),(408,2,6,'2017-04-25 21:28:55','test pass',0),(409,2,6,'2017-04-25 21:28:55','certification passed',1),(410,2,4,'2017-04-25 21:28:55','test pass',0),(411,2,4,'2017-04-25 21:28:55','certification passed',1),(412,2,6,'2017-04-25 21:33:53','test pass',0),(413,2,6,'2017-04-25 21:33:53','certification passed',1),(414,2,4,'2017-04-25 21:33:53','test pass',0),(415,2,4,'2017-04-25 21:33:53','certification passed',1),(416,2,6,'2017-04-25 21:34:23','test pass',0),(417,2,6,'2017-04-25 21:34:23','certification passed',1),(418,2,4,'2017-04-25 21:34:23','test pass',0),(419,2,4,'2017-04-25 21:34:23','certification passed',1),(420,2,6,'2017-04-25 21:37:42','test pass',0),(421,2,6,'2017-04-25 21:37:42','certification passed',1),(422,2,4,'2017-04-25 21:37:42','test pass',0),(423,2,4,'2017-04-25 21:37:42','certification passed',1),(424,2,6,'2017-04-25 21:41:09','test pass',0),(425,2,6,'2017-04-25 21:41:09','certification passed',1),(426,2,4,'2017-04-25 21:41:09','test pass',0),(427,2,4,'2017-04-25 21:41:09','certification passed',1),(428,2,6,'2017-04-25 22:01:07','test pass',0),(429,2,6,'2017-04-25 22:01:07','certification passed',1),(430,2,4,'2017-04-25 22:01:07','test pass',0),(431,2,4,'2017-04-25 22:01:07','certification passed',1),(432,2,6,'2017-04-25 22:02:45','test pass',0),(433,2,6,'2017-04-25 22:02:45','certification passed',1),(434,2,4,'2017-04-25 22:02:45','test pass',0),(435,2,4,'2017-04-25 22:02:45','certification passed',1),(436,2,6,'2017-04-25 22:07:38','test pass',0),(437,2,6,'2017-04-25 22:07:38','certification passed',1),(438,2,4,'2017-04-25 22:07:38','test pass',0),(439,2,4,'2017-04-25 22:07:38','certification passed',1),(440,2,6,'2017-04-25 22:07:46','test pass',0),(441,2,6,'2017-04-25 22:07:46','certification passed',1),(442,2,4,'2017-04-25 22:07:46','test pass',0),(443,2,4,'2017-04-25 22:07:46','certification passed',1),(444,2,6,'2017-04-25 22:10:28','test pass',0),(445,2,6,'2017-04-25 22:10:28','certification passed',1),(446,2,4,'2017-04-25 22:10:28','test pass',0),(447,2,4,'2017-04-25 22:10:28','certification passed',1),(448,2,6,'2017-04-25 22:10:52','test pass',0),(449,2,6,'2017-04-25 22:10:52','certification passed',1),(450,2,4,'2017-04-25 22:10:52','test pass',0),(451,2,4,'2017-04-25 22:10:52','certification passed',1),(452,2,6,'2017-04-25 22:11:54','test pass',0),(453,2,6,'2017-04-25 22:11:54','certification passed',1),(454,2,4,'2017-04-25 22:11:54','test pass',0),(455,2,4,'2017-04-25 22:11:54','certification passed',1),(456,2,6,'2017-04-25 22:17:37','test pass',0),(457,2,6,'2017-04-25 22:17:37','certification passed',1),(458,2,4,'2017-04-25 22:17:37','test pass',0),(459,2,4,'2017-04-25 22:17:37','certification passed',1),(460,2,6,'2017-04-25 22:18:05','test pass',0),(461,2,6,'2017-04-25 22:18:05','certification passed',1),(462,2,4,'2017-04-25 22:18:05','test pass',0),(463,2,4,'2017-04-25 22:18:05','certification passed',1),(464,2,6,'2017-04-25 22:18:42','test pass',0),(465,2,6,'2017-04-25 22:18:42','certification passed',1),(466,2,4,'2017-04-25 22:18:42','test pass',0),(467,2,4,'2017-04-25 22:18:42','certification passed',1),(468,2,6,'2017-04-25 22:23:25','test pass',0),(469,2,6,'2017-04-25 22:23:25','certification passed',1),(470,2,4,'2017-04-25 22:23:25','test pass',0),(471,2,4,'2017-04-25 22:23:25','certification passed',1),(472,2,6,'2017-04-25 22:27:50','test pass',0),(473,2,6,'2017-04-25 22:27:50','certification passed',1),(474,2,4,'2017-04-25 22:27:50','test pass',0),(475,2,4,'2017-04-25 22:27:50','certification passed',1),(476,2,6,'2017-04-25 22:28:07','test pass',0),(477,2,6,'2017-04-25 22:28:07','certification passed',1),(478,2,4,'2017-04-25 22:28:07','test pass',0),(479,2,4,'2017-04-25 22:28:07','certification passed',1),(480,2,6,'2017-04-25 22:29:07','test pass',0),(481,2,6,'2017-04-25 22:29:07','certification passed',1),(482,2,4,'2017-04-25 22:29:07','test pass',0),(483,2,4,'2017-04-25 22:29:07','certification passed',1),(484,2,6,'2017-04-25 22:29:30','test pass',0),(485,2,6,'2017-04-25 22:29:30','certification passed',1),(486,2,4,'2017-04-25 22:29:30','test pass',0),(487,2,4,'2017-04-25 22:29:30','certification passed',1),(488,2,6,'2017-04-25 22:30:14','test pass',0),(489,2,6,'2017-04-25 22:30:14','certification passed',1),(490,2,4,'2017-04-25 22:30:14','test pass',0),(491,2,4,'2017-04-25 22:30:14','certification passed',1),(492,2,6,'2017-04-25 22:30:58','test pass',0),(493,2,6,'2017-04-25 22:30:58','certification passed',1),(494,2,4,'2017-04-25 22:30:58','test pass',0),(495,2,4,'2017-04-25 22:30:58','certification passed',1),(496,2,6,'2017-04-25 22:31:03','test pass',0),(497,2,6,'2017-04-25 22:31:03','certification passed',1),(498,2,4,'2017-04-25 22:31:03','test pass',0),(499,2,4,'2017-04-25 22:31:03','certification passed',1),(500,2,6,'2017-04-25 22:31:13','test pass',0),(501,2,6,'2017-04-25 22:31:13','certification passed',1),(502,2,4,'2017-04-25 22:31:13','test pass',0),(503,2,4,'2017-04-25 22:31:13','certification passed',1),(504,2,6,'2017-04-25 22:31:43','test pass',0),(505,2,6,'2017-04-25 22:31:43','certification passed',1),(506,2,4,'2017-04-25 22:31:43','test pass',0),(507,2,4,'2017-04-25 22:31:43','certification passed',1),(508,2,6,'2017-04-25 22:34:27','test pass',0),(509,2,6,'2017-04-25 22:34:27','certification passed',1),(510,2,4,'2017-04-25 22:34:27','test pass',0),(511,2,4,'2017-04-25 22:34:27','certification passed',1),(512,2,6,'2017-04-25 22:35:10','test pass',0),(513,2,6,'2017-04-25 22:35:10','certification passed',1),(514,2,4,'2017-04-25 22:35:10','test pass',0),(515,2,4,'2017-04-25 22:35:10','certification passed',1),(516,2,6,'2017-04-25 22:36:38','test pass',0),(517,2,6,'2017-04-25 22:36:38','certification passed',1),(518,2,4,'2017-04-25 22:36:38','test pass',0),(519,2,4,'2017-04-25 22:36:38','certification passed',1),(520,2,6,'2017-04-25 22:36:51','test pass',0),(521,2,6,'2017-04-25 22:36:51','certification passed',1),(522,2,4,'2017-04-25 22:36:51','test pass',0),(523,2,4,'2017-04-25 22:36:51','certification passed',1),(524,2,6,'2017-04-25 22:37:31','test pass',0),(525,2,6,'2017-04-25 22:37:31','certification passed',1),(526,2,4,'2017-04-25 22:37:31','test pass',0),(527,2,4,'2017-04-25 22:37:31','certification passed',1),(528,2,6,'2017-04-25 22:37:44','test pass',0),(529,2,6,'2017-04-25 22:37:44','certification passed',1),(530,2,4,'2017-04-25 22:37:44','test pass',0),(531,2,4,'2017-04-25 22:37:44','certification passed',1),(532,2,6,'2017-04-25 22:38:06','test pass',0),(533,2,6,'2017-04-25 22:38:06','certification passed',1),(534,2,4,'2017-04-25 22:38:06','test pass',0),(535,2,4,'2017-04-25 22:38:06','certification passed',1),(536,2,6,'2017-04-25 22:39:50','test pass',0),(537,2,6,'2017-04-25 22:39:50','certification passed',1),(538,2,4,'2017-04-25 22:39:50','test pass',0),(539,2,4,'2017-04-25 22:39:50','certification passed',1),(540,2,6,'2017-04-25 22:40:46','test pass',0),(541,2,6,'2017-04-25 22:40:46','certification passed',1),(542,2,4,'2017-04-25 22:40:46','test pass',0),(543,2,4,'2017-04-25 22:40:46','certification passed',1),(544,2,6,'2017-04-25 22:41:02','test pass',0),(545,2,6,'2017-04-25 22:41:02','certification passed',1),(546,2,4,'2017-04-25 22:41:02','test pass',0),(547,2,4,'2017-04-25 22:41:02','certification passed',1),(548,2,6,'2017-04-25 22:44:08','test pass',0),(549,2,6,'2017-04-25 22:44:08','certification passed',1),(550,2,4,'2017-04-25 22:44:08','test pass',0),(551,2,4,'2017-04-25 22:44:08','certification passed',1),(552,2,6,'2017-04-25 22:45:52','test pass',0),(553,2,6,'2017-04-25 22:45:52','certification passed',1),(554,2,4,'2017-04-25 22:45:52','test pass',0),(555,2,4,'2017-04-25 22:45:52','certification passed',1),(556,2,6,'2017-04-25 22:45:56','test pass',0),(557,2,6,'2017-04-25 22:45:56','certification passed',1),(558,2,4,'2017-04-25 22:45:56','test pass',0),(559,2,4,'2017-04-25 22:45:56','certification passed',1),(560,2,6,'2017-04-25 22:46:23','test pass',0),(561,2,6,'2017-04-25 22:46:23','certification passed',1),(562,2,4,'2017-04-25 22:46:23','test pass',0),(563,2,4,'2017-04-25 22:46:23','certification passed',1),(564,2,6,'2017-04-25 22:47:17','test pass',0),(565,2,6,'2017-04-25 22:47:17','certification passed',1),(566,2,4,'2017-04-25 22:47:17','test pass',0),(567,2,4,'2017-04-25 22:47:17','certification passed',1),(568,2,6,'2017-04-27 22:13:38','test pass',0),(569,2,6,'2017-04-27 22:13:38','certification passed',1),(570,2,4,'2017-04-27 22:13:38','test pass',0),(571,2,4,'2017-04-27 22:13:38','certification passed',1),(572,2,6,'2017-04-27 23:42:06','test pass',0),(573,2,6,'2017-04-27 23:42:06','certification passed',1),(574,2,4,'2017-04-27 23:42:06','test pass',0),(575,2,4,'2017-04-27 23:42:06','certification passed',1),(576,2,6,'2017-04-29 23:28:37','test pass',0),(577,2,6,'2017-04-29 23:28:37','certification passed',1),(578,2,4,'2017-04-29 23:28:37','test pass',0),(579,2,4,'2017-04-29 23:28:37','certification passed',1),(580,2,6,'2017-04-30 23:05:23','test pass',0),(581,2,6,'2017-04-30 23:05:23','certification passed',1),(582,2,4,'2017-04-30 23:05:23','test pass',0),(583,2,4,'2017-04-30 23:05:23','certification passed',1),(584,2,6,'2017-05-01 17:55:19','test pass',0),(585,2,6,'2017-05-01 17:55:19','certification passed',1),(586,2,4,'2017-05-01 17:55:19','test pass',0),(587,2,4,'2017-05-01 17:55:19','certification passed',1),(588,2,6,'2017-05-01 17:55:48','test pass',0),(589,2,6,'2017-05-01 17:55:48','certification passed',1),(590,2,4,'2017-05-01 17:55:48','test pass',0),(591,2,4,'2017-05-01 17:55:48','certification passed',1),(592,2,6,'2017-05-01 18:04:19','test pass',0),(593,2,6,'2017-05-01 18:04:19','certification passed',1),(594,2,4,'2017-05-01 18:04:19','test pass',0),(595,2,4,'2017-05-01 18:04:19','certification passed',1),(596,2,6,'2017-05-01 18:04:36','test pass',0),(597,2,6,'2017-05-01 18:04:36','certification passed',1),(598,2,4,'2017-05-01 18:04:36','test pass',0),(599,2,4,'2017-05-01 18:04:36','certification passed',1),(600,2,6,'2017-05-01 18:10:12','test pass',0),(601,2,6,'2017-05-01 18:10:12','certification passed',1),(602,2,4,'2017-05-01 18:10:12','test pass',0),(603,2,4,'2017-05-01 18:10:12','certification passed',1),(604,2,6,'2017-05-01 18:31:14','test pass',0),(605,2,6,'2017-05-01 18:31:14','certification passed',1),(606,2,4,'2017-05-01 18:31:14','test pass',0),(607,2,4,'2017-05-01 18:31:14','certification passed',1),(608,2,6,'2017-05-01 20:27:49','test pass',0),(609,2,6,'2017-05-01 20:27:49','certification passed',1),(610,2,4,'2017-05-01 20:27:49','test pass',0),(611,2,4,'2017-05-01 20:27:49','certification passed',1),(612,2,6,'2017-05-01 20:35:41','test pass',0),(613,2,6,'2017-05-01 20:35:41','certification passed',1),(614,2,4,'2017-05-01 20:35:41','test pass',0),(615,2,4,'2017-05-01 20:35:41','certification passed',1),(616,2,6,'2017-05-01 20:35:52','test pass',0),(617,2,6,'2017-05-01 20:35:52','certification passed',1),(618,2,4,'2017-05-01 20:35:52','test pass',0),(619,2,4,'2017-05-01 20:35:52','certification passed',1),(620,2,6,'2017-05-01 20:35:56','test pass',0),(621,2,6,'2017-05-01 20:35:56','certification passed',1),(622,2,4,'2017-05-01 20:35:56','test pass',0),(623,2,4,'2017-05-01 20:35:56','certification passed',1),(624,2,6,'2017-05-03 21:13:44','test pass',0),(625,2,6,'2017-05-03 21:13:44','certification passed',1),(626,2,4,'2017-05-03 21:13:44','test pass',0),(627,2,4,'2017-05-03 21:13:44','certification passed',1),(628,2,6,'2017-05-03 21:14:42','test pass',0),(629,2,6,'2017-05-03 21:14:42','certification passed',1),(630,2,4,'2017-05-03 21:14:42','test pass',0),(631,2,4,'2017-05-03 21:14:42','certification passed',1);
/*!40000 ALTER TABLE `tbl_house_cert` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_house_deliverable`
--

DROP TABLE IF EXISTS `tbl_house_deliverable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_house_deliverable` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `house` bigint(20) NOT NULL DEFAULT '0',
  `deliverable` bigint(20) NOT NULL DEFAULT '0',
  `qty` int(11) NOT NULL DEFAULT '0',
  `desc` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `house` (`house`,`deliverable`),
  KEY `tbl_house_deliverable_house` (`house`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_house_deliverable`
--

LOCK TABLES `tbl_house_deliverable` WRITE;
/*!40000 ALTER TABLE `tbl_house_deliverable` DISABLE KEYS */;
INSERT INTO `tbl_house_deliverable` VALUES (1,4,2,2,'1把钥匙'),(2,4,1,3,'2把钥匙'),(6,6,5,2,'交付物说明'),(7,6,3,4,'交付物说明'),(8,4,3,2,'一把钥匙');
/*!40000 ALTER TABLE `tbl_house_deliverable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_house_event`
--

DROP TABLE IF EXISTS `tbl_house_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_house_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `house` bigint(20) NOT NULL DEFAULT '0',
  `sender` bigint(20) NOT NULL DEFAULT '0',
  `receiver` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `desc` varchar(200) NOT NULL DEFAULT '',
  `read_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tbl_house_event_house` (`house`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_house_event`
--

LOCK TABLES `tbl_house_event` WRITE;
/*!40000 ALTER TABLE `tbl_house_event` DISABLE KEYS */;
INSERT INTO `tbl_house_event` VALUES (1,1,2,3,'2017-01-22 12:06:10',2,'1st event',NULL),(5,1,3,4,'2017-01-22 13:53:42',3,'2nd event',NULL),(6,1,3,4,'2017-01-22 13:54:20',3,'3th event',NULL),(7,2,0,2,'2017-01-22 15:16:34',2,'收取第一期房租，共计 1000元','2017-01-23 20:55:52'),(8,2,0,1,'2017-01-23 20:37:53',1,'测试',NULL);
/*!40000 ALTER TABLE `tbl_house_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_house_event_process`
--

DROP TABLE IF EXISTS `tbl_house_event_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_house_event_process` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `who` bigint(20) NOT NULL DEFAULT '0',
  `when` datetime NOT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `desc` varchar(200) NOT NULL DEFAULT '',
  `event_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_house_event_process`
--

LOCK TABLES `tbl_house_event_process` WRITE;
/*!40000 ALTER TABLE `tbl_house_event_process` DISABLE KEYS */;
INSERT INTO `tbl_house_event_process` VALUES (1,1,'2017-01-22 11:50:06',1,'test',1),(2,1,'2017-01-22 12:28:51',1,'测试',1);
/*!40000 ALTER TABLE `tbl_house_event_process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_house_facility`
--

DROP TABLE IF EXISTS `tbl_house_facility`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_house_facility` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `house` bigint(20) NOT NULL DEFAULT '0',
  `facility` bigint(20) NOT NULL DEFAULT '0',
  `qty` int(11) NOT NULL DEFAULT '0',
  `desc` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `house` (`house`,`facility`),
  KEY `tbl_house_facility_house` (`house`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_house_facility`
--

LOCK TABLES `tbl_house_facility` WRITE;
/*!40000 ALTER TABLE `tbl_house_facility` DISABLE KEYS */;
INSERT INTO `tbl_house_facility` VALUES (4,2,3,222,'fdesc_0000'),(5,2,4,333,'fdesc_1111'),(6,2,6,666,'fdesc_6666'),(7,6,6,2,'沙发茶几说明'),(8,6,4,3,'电视机说明'),(9,6,3,10,'测试说明');
/*!40000 ALTER TABLE `tbl_house_facility` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_house_recommend`
--

DROP TABLE IF EXISTS `tbl_house_recommend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_house_recommend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `house` bigint(20) NOT NULL DEFAULT '0',
  `who` bigint(20) NOT NULL DEFAULT '0',
  `when` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `house` (`house`)
) ENGINE=InnoDB AUTO_INCREMENT=141 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_house_recommend`
--

LOCK TABLES `tbl_house_recommend` WRITE;
/*!40000 ALTER TABLE `tbl_house_recommend` DISABLE KEYS */;
INSERT INTO `tbl_house_recommend` VALUES (1,1,1,'2017-01-21 22:31:23'),(140,2,6,'2017-05-03 21:14:42');
/*!40000 ALTER TABLE `tbl_house_recommend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_house_tag`
--

DROP TABLE IF EXISTS `tbl_house_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_house_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `house` bigint(20) NOT NULL DEFAULT '0',
  `tag` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `tbl_house_tag_house` (`house`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_house_tag`
--

LOCK TABLES `tbl_house_tag` WRITE;
/*!40000 ALTER TABLE `tbl_house_tag` DISABLE KEYS */;
INSERT INTO `tbl_house_tag` VALUES (1,1,1),(2,2,2),(3,1,3);
/*!40000 ALTER TABLE `tbl_house_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_pic_set`
--

DROP TABLE IF EXISTS `tbl_pic_set`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_pic_set` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pic_id` bigint(20) NOT NULL DEFAULT '0',
  `size` int(11) NOT NULL DEFAULT '0',
  `url` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `tbl_pic_set_pic_id_size` (`pic_id`,`size`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_pic_set`
--

LOCK TABLES `tbl_pic_set` WRITE;
/*!40000 ALTER TABLE `tbl_pic_set` DISABLE KEYS */;
INSERT INTO `tbl_pic_set` VALUES (56,29,1,'201702/12100319545040.jpg'),(57,29,2,'201702/12100319545040_s.jpg'),(58,29,4,'201702/12100319545040_l.jpg'),(59,30,1,'201702/12100406057008.png'),(60,30,2,'201702/12100406057008_s.jpg'),(61,30,4,'201702/12100406057008_l.jpg'),(62,31,1,'201702/12100423782379.png'),(63,31,2,'201702/12100423782379_s.jpg'),(64,31,4,'201702/12100423782379_l.jpg'),(65,32,1,'201702/12100711398996.png'),(66,32,2,'201702/12100711398996_s.jpg'),(67,32,4,'201702/12100711398996_l.jpg'),(68,33,1,'201702/12100721018347.png'),(69,33,2,'201702/12100721018347_s.jpg'),(70,33,4,'201702/12100721018347_l.jpg'),(71,34,1,'201702/12100933406606.png'),(72,34,2,'201702/12100933406606_s.jpg'),(73,34,4,'201702/12100933406606_l.jpg'),(74,35,1,'201702/12101113988770.jpg'),(75,35,2,'201702/12101113988770.jpg'),(76,35,4,'201702/12101113988770_l.jpg'),(77,36,1,'201702/12101146361942.jpg'),(78,36,2,'201702/12101146361942.jpg'),(79,36,4,'201702/12101146361942_l.jpg'),(80,37,1,'201702/12101253185265.jpg'),(81,37,2,'201702/12101253185265_s.jpg'),(82,37,4,'201702/12101253185265_l.jpg');
/*!40000 ALTER TABLE `tbl_pic_set` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_pictures`
--

DROP TABLE IF EXISTS `tbl_pictures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_pictures` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type_major` int(11) NOT NULL DEFAULT '0',
  `type_miner` int(11) NOT NULL DEFAULT '0',
  `ref_id` bigint(20) NOT NULL DEFAULT '0',
  `desc` varchar(100) NOT NULL DEFAULT '',
  `submit` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `tbl_pictures_type_major_type_miner_ref_id` (`type_major`,`type_miner`,`ref_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_pictures`
--

LOCK TABLES `tbl_pictures` WRITE;
/*!40000 ALTER TABLE `tbl_pictures` DISABLE KEYS */;
INSERT INTO `tbl_pictures` VALUES (29,200,2,4,'客厅','2017-02-12 10:03:19'),(30,200,2,4,'客厅','2017-02-12 10:04:07'),(31,200,2,4,'客厅','2017-02-12 10:04:24'),(32,200,2,4,'客厅','2017-02-12 10:07:12'),(33,200,2,4,'客厅','2017-02-12 10:07:21'),(34,200,2,4,'客厅','2017-02-12 10:09:33'),(35,200,2,4,'客厅','2017-02-12 10:11:14'),(36,200,2,4,'客厅','2017-02-12 10:11:47'),(37,200,2,4,'客厅','2017-02-12 10:12:54');
/*!40000 ALTER TABLE `tbl_pictures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_property`
--

DROP TABLE IF EXISTS `tbl_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '',
  `address` varchar(200) NOT NULL DEFAULT '',
  `desc` varchar(1000) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_property`
--

LOCK TABLES `tbl_property` WRITE;
/*!40000 ALTER TABLE `tbl_property` DISABLE KEYS */;
INSERT INTO `tbl_property` VALUES (1,'世茂.蝶湖湾','',''),(2,'长顺.滨江皇冠','长江南路 666号','1234'),(3,'长江花园','',''),(4,'長江花園','',''),(5,'1234','abcd','abcd1234'),(6,'-','',''),(7,'昌建园','就是','K歌'),(8,'_','',''),(9,'%E9%95%BF%E6%B1%9F%E8%8A%B1%E5%9B%AD','',''),(10,'6ZW35rGf6Iqx5ZyS\n','',''),(11,'長江花','',''),(12,'長江花園2','珠江南路','怡景灣'),(13,'長江花園21','珠江南路','怡景灣'),(14,'上海公馆','啊','吧');
/*!40000 ALTER TABLE `tbl_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_rental`
--

DROP TABLE IF EXISTS `tbl_rental`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_rental` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `house_id` bigint(20) NOT NULL DEFAULT '0',
  `rental_bid` int(11) NOT NULL DEFAULT '0',
  `rental_bottom` int(11) NOT NULL DEFAULT '0',
  `who` int(11) NOT NULL DEFAULT '0',
  `when` datetime NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `tbl_rental_house_id` (`house_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_rental`
--

LOCK TABLES `tbl_rental` WRITE;
/*!40000 ALTER TABLE `tbl_rental` DISABLE KEYS */;
INSERT INTO `tbl_rental` VALUES (1,1,120000,110000,1,'2017-01-19 22:05:46',1),(2,1,115000,110000,1,'2017-01-19 22:06:45',1),(3,1,135000,110000,1,'2017-01-19 22:07:12',1),(4,2,150000,140000,2,'2017-01-22 21:49:10',1),(5,2,140000,130000,2,'2017-01-23 20:16:17',1);
/*!40000 ALTER TABLE `tbl_rental` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_sms_code`
--

DROP TABLE IF EXISTS `tbl_sms_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_sms_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `phone` varchar(50) NOT NULL DEFAULT '',
  `sms_code` varchar(50) NOT NULL DEFAULT '',
  `expire` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_sms_code`
--

LOCK TABLES `tbl_sms_code` WRITE;
/*!40000 ALTER TABLE `tbl_sms_code` DISABLE KEYS */;
INSERT INTO `tbl_sms_code` VALUES (1,'15306261804','200312','2017-05-03 21:24:42');
/*!40000 ALTER TABLE `tbl_sms_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_tag`
--

DROP TABLE IF EXISTS `tbl_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_tag`
--

LOCK TABLES `tbl_tag` WRITE;
/*!40000 ALTER TABLE `tbl_tag` DISABLE KEYS */;
INSERT INTO `tbl_tag` VALUES (1,'满二唯一'),(2,'地铁房'),(3,'江景房');
/*!40000 ALTER TABLE `tbl_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_user`
--

DROP TABLE IF EXISTS `tbl_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(64) NOT NULL DEFAULT '',
  `name` varchar(64) NOT NULL DEFAULT '',
  `salt` varchar(32) NOT NULL DEFAULT '',
  `pass_login` varchar(50) NOT NULL DEFAULT '',
  `pass_trasn` varchar(50) NOT NULL DEFAULT '',
  `id_no` varchar(50) NOT NULL DEFAULT '',
  `phone` varchar(50) NOT NULL DEFAULT '',
  `head` varchar(50) NOT NULL DEFAULT '',
  `role` int(11) NOT NULL DEFAULT '0',
  `pass` varchar(32) NOT NULL DEFAULT '',
  `salt_tmp` varchar(32) NOT NULL DEFAULT '',
  `enable` tinyint(1) NOT NULL DEFAULT '1',
  `session` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `login_name` (`login_name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_user`
--

LOCK TABLES `tbl_user` WRITE;
/*!40000 ALTER TABLE `tbl_user` DISABLE KEYS */;
INSERT INTO `tbl_user` VALUES (2,'15300000000','','','','','','15300000000','',0,'','',1,''),(4,'15306261804','','f1c0fb8578e356746e0f98ce07b7a27f','b2c031f342560c15c1ffaef567672ef7','','','15306261804','',10,'','',1,'09cea368f923635738acd6402402c1d1'),(5,'13862601604','','','','','','','',0,'','',1,''),(6,'13888888888','','kennytest','','','','13888888888','',0,'','',1,''),(9,'13777777777','','','','','','13777777777','',0,'','',1,''),(11,'13333333333','','','','','','13333333333','',0,'','',1,'');
/*!40000 ALTER TABLE `tbl_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_user_group`
--

DROP TABLE IF EXISTS `tbl_user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_user_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL DEFAULT '',
  `admin` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_user_group`
--

LOCK TABLES `tbl_user_group` WRITE;
/*!40000 ALTER TABLE `tbl_user_group` DISABLE KEYS */;
INSERT INTO `tbl_user_group` VALUES (1,'管理员',1),(2,'经纪人',0);
/*!40000 ALTER TABLE `tbl_user_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_user_group_member`
--

DROP TABLE IF EXISTS `tbl_user_group_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_user_group_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_user_group_member`
--

LOCK TABLES `tbl_user_group_member` WRITE;
/*!40000 ALTER TABLE `tbl_user_group_member` DISABLE KEYS */;
INSERT INTO `tbl_user_group_member` VALUES (4,2,4),(7,1,4),(8,1,5),(9,2,6),(10,2,11);
/*!40000 ALTER TABLE `tbl_user_group_member` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-04 21:54:23
