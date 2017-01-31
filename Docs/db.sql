-- MySQL dump 10.13  Distrib 5.7.17, for Linux (x86_64)
--
-- Host: localhost    Database: rtdb
-- ------------------------------------------------------
-- Server version	5.7.13-0ubuntu0.16.04.2

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
  `for_sale` int(11) NOT NULL DEFAULT '0',
  `for_rent` int(11) NOT NULL DEFAULT '0',
  `rent_stat` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `property_id` (`property_id`,`building_no`,`house_no`),
  KEY `tbl_house_property_id` (`property_id`),
  KEY `tbl_house_agency_id` (`agency_id`),
  KEY `tbl_house_for_sale` (`for_sale`),
  KEY `tbl_house_for_rent` (`for_rent`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_house`
--

LOCK TABLES `tbl_house` WRITE;
/*!40000 ALTER TABLE `tbl_house` DISABLE KEYS */;
INSERT INTO `tbl_house` VALUES (2,175,35,17,'1505',3,2,2,13148,2,1,4,4,'2017-01-26 07:39:04','2017-01-27 21:24:38','2017-01-27 20:45:49',1,1,1),(3,177,35,17,'1505',3,2,2,13148,0,1,4,4,'2017-01-26 12:31:55',NULL,NULL,1,1,2);
/*!40000 ALTER TABLE `tbl_house` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_house_recommend`
--

LOCK TABLES `tbl_house_recommend` WRITE;
/*!40000 ALTER TABLE `tbl_house_recommend` DISABLE KEYS */;
INSERT INTO `tbl_house_recommend` VALUES (1,1,1,'2017-01-21 22:31:23'),(7,2,4,'2017-01-30 20:08:38');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_pic_set`
--

LOCK TABLES `tbl_pic_set` WRITE;
/*!40000 ALTER TABLE `tbl_pic_set` DISABLE KEYS */;
INSERT INTO `tbl_pic_set` VALUES (1,1,1,'connect.jpg'),(2,1,3,'introduce.jpg'),(3,1,2,'list.jpg');
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
  PRIMARY KEY (`id`),
  KEY `tbl_pictures_type_major_type_miner_ref_id` (`type_major`,`type_miner`,`ref_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_pictures`
--

LOCK TABLES `tbl_pictures` WRITE;
/*!40000 ALTER TABLE `tbl_pictures` DISABLE KEYS */;
INSERT INTO `tbl_pictures` VALUES (1,200,1,1,'房型图'),(2,200,2,1,'客厅');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_property`
--

LOCK TABLES `tbl_property` WRITE;
/*!40000 ALTER TABLE `tbl_property` DISABLE KEYS */;
INSERT INTO `tbl_property` VALUES (1,'世茂.蝶湖湾','',''),(2,'长顺.滨江皇冠','长江南路 666号','1234');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_sms_code`
--

LOCK TABLES `tbl_sms_code` WRITE;
/*!40000 ALTER TABLE `tbl_sms_code` DISABLE KEYS */;
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `login_name` (`login_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_user`
--

LOCK TABLES `tbl_user` WRITE;
/*!40000 ALTER TABLE `tbl_user` DISABLE KEYS */;
INSERT INTO `tbl_user` VALUES (4,'15306261804','','f1c0fb8578e356746e0f98ce07b7a27f','','','','15306261804','',10,'','',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_user_group_member`
--

LOCK TABLES `tbl_user_group_member` WRITE;
/*!40000 ALTER TABLE `tbl_user_group_member` DISABLE KEYS */;
INSERT INTO `tbl_user_group_member` VALUES (1,1,4);
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

-- Dump completed on 2017-01-31 15:48:44
