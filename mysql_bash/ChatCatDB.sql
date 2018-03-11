-- MySQL dump 10.13  Distrib 5.1.73, for redhat-linux-gnu (i386)
--
-- Host: localhost    Database: ChatCatDB
-- ------------------------------------------------------
-- Server version	5.1.73

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
-- Table structure for table `FrndMsg`
--

DROP TABLE IF EXISTS `FrndMsg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FrndMsg` (
  `Usr1` smallint(6) NOT NULL,
  `Usr2` smallint(6) NOT NULL,
  KEY `Usr2_UsrNum` (`Usr2`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FrndMsg`
--

LOCK TABLES `FrndMsg` WRITE;
/*!40000 ALTER TABLE `FrndMsg` DISABLE KEYS */;
INSERT INTO `FrndMsg` VALUES (10001,10002),(10001,10004),(10001,10003),(10002,10003),(10003,10004),(10002,20001),(10001,20001),(10003,20001),(10004,20001),(10002,10004);
/*!40000 ALTER TABLE `FrndMsg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SmplMsg`
--

DROP TABLE IF EXISTS `SmplMsg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SmplMsg` (
  `UsrNum` smallint(6) NOT NULL,
  `Passwd` varchar(10) NOT NULL,
  `Name` varchar(20) NOT NULL,
  `HeadID` smallint(6) NOT NULL,
  `Sex` tinyint(4) NOT NULL DEFAULT '0',
  `Signature` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`UsrNum`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SmplMsg`
--

LOCK TABLES `SmplMsg` WRITE;
/*!40000 ALTER TABLE `SmplMsg` DISABLE KEYS */;
INSERT INTO `SmplMsg` VALUES (10001,'3838438','葬爱烂仔、刘知昊',2,1,'葬爱**拽、少/非常狂暴，掉炸云天'),(10002,'woshijilao','葬爱Ga贤y',1,1,'不知何事萦怀抱，醒也无聊，醉也无聊，梦也何曾到谢桥。'),(10003,'mwjzhenjbs','马维真菌',3,1,'在整个世界被倾盆大雨荡涤的那天，我做了个甜美的梦。'),(10004,'123abc','木子吉吉雨文',4,0,'祝平安健康，也愿得所求。'),(20001,'3838438','葬爱家族、猫咪',6,0,''),(10086,'123','大哥救我',3,0,'这个人太懒了,没有个性签名'),(10068,'maweijun','秀',5,1,'牛逼阿'),(10009,'3838438','小熊维尼',5,1,'这个人太懒了,没有个性签名');
/*!40000 ALTER TABLE `SmplMsg` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-10 22:33:48
