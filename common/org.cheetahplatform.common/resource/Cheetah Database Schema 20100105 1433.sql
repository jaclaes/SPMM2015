-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.1.39-community


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema bp_notation
--

CREATE DATABASE IF NOT EXISTS bp_notation;
USE bp_notation;

--
-- Definition of table `audittrail_entry`
--

DROP TABLE IF EXISTS `audittrail_entry`;
CREATE TABLE `audittrail_entry` (
  `database_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `process_instance` bigint(20) unsigned NOT NULL,
  `timestamp` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `workflow_element` text,
  `originator` text,
  `data` text,
  PRIMARY KEY (`database_id`) USING BTREE,
  KEY `FK_process_instance` (`process_instance`),
  CONSTRAINT `FK_process_instance` FOREIGN KEY (`process_instance`) REFERENCES `process_instance` (`database_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=63396 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `audittrail_entry`
--

/*!40000 ALTER TABLE `audittrail_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `audittrail_entry` ENABLE KEYS */;


--
-- Definition of table `id`
--

DROP TABLE IF EXISTS `id`;
CREATE TABLE `id` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `id`
--

/*!40000 ALTER TABLE `id` DISABLE KEYS */;
/*!40000 ALTER TABLE `id` ENABLE KEYS */;


--
-- Definition of table `log`
--

DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `message` text,
  `trace` mediumtext,
  `attributes` text,
  `host` text,
  `timestamp` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `log`
--

/*!40000 ALTER TABLE `log` DISABLE KEYS */;
/*!40000 ALTER TABLE `log` ENABLE KEYS */;


--
-- Definition of table `paragraph`
--

DROP TABLE IF EXISTS `paragraph`;
CREATE TABLE `paragraph` (
  `database_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `xml` text NOT NULL,
  PRIMARY KEY (`database_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `paragraph`
--

/*!40000 ALTER TABLE `paragraph` DISABLE KEYS */;
/*!40000 ALTER TABLE `paragraph` ENABLE KEYS */;


--
-- Definition of table `paragraph_mapping`
--

DROP TABLE IF EXISTS `paragraph_mapping`;
CREATE TABLE `paragraph_mapping` (
  `audittrail_entry` bigint(20) unsigned NOT NULL,
  `paragraph` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`audittrail_entry`) USING BTREE,
  CONSTRAINT `FK_audittrail_entry` FOREIGN KEY (`audittrail_entry`) REFERENCES `audittrail_entry` (`database_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `paragraph_mapping`
--

/*!40000 ALTER TABLE `paragraph_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `paragraph_mapping` ENABLE KEYS */;


--
-- Definition of table `process`
--

DROP TABLE IF EXISTS `process`;
CREATE TABLE `process` (
  `database_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` text NOT NULL,
  `data` text,
  PRIMARY KEY (`database_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `process`
--

/*!40000 ALTER TABLE `process` DISABLE KEYS */;
/*!40000 ALTER TABLE `process` ENABLE KEYS */;


--
-- Definition of table `process_instance`
--

DROP TABLE IF EXISTS `process_instance`;
CREATE TABLE `process_instance` (
  `database_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `process` bigint(20) unsigned NOT NULL,
  `id` text NOT NULL,
  `data` text,
  PRIMARY KEY (`database_id`),
  KEY `FK_process` (`process`),
  CONSTRAINT `FK_process` FOREIGN KEY (`process`) REFERENCES `process` (`database_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2059 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `process_instance`
--

/*!40000 ALTER TABLE `process_instance` DISABLE KEYS */;
/*!40000 ALTER TABLE `process_instance` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
