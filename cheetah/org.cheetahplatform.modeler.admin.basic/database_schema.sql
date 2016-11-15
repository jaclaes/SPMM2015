-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.1.41-community


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema temp
--

CREATE DATABASE IF NOT EXISTS temp;
USE temp;

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
  PRIMARY KEY (`database_id`),
  KEY `FK_process_instance` (`process_instance`),
  CONSTRAINT `FK_process_instance` FOREIGN KEY (`process_instance`) REFERENCES `process_instance` (`database_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `audittrail_entry`
--

/*!40000 ALTER TABLE `audittrail_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `audittrail_entry` ENABLE KEYS */;


--
-- Definition of table `audittrailentry_for_note`
--

DROP TABLE IF EXISTS `audittrailentry_for_note`;
CREATE TABLE `audittrailentry_for_note` (
  `ppmnote` bigint(20) unsigned NOT NULL,
  `entryid` bigint(20) unsigned NOT NULL,
  KEY `FK_audittrailentry` (`entryid`),
  KEY `FK_ppmnote` (`ppmnote`),
  CONSTRAINT `FK_ppmnote` FOREIGN KEY (`ppmnote`) REFERENCES `ppmnote` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_audittrailentry` FOREIGN KEY (`entryid`) REFERENCES `audittrail_entry` (`database_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `audittrailentry_for_note`
--

/*!40000 ALTER TABLE `audittrailentry_for_note` DISABLE KEYS */;
/*!40000 ALTER TABLE `audittrailentry_for_note` ENABLE KEYS */;


--
-- Definition of table `edge_condition_mapping`
--

DROP TABLE IF EXISTS `edge_condition_mapping`;
CREATE TABLE `edge_condition_mapping` (
  `process_instance` int(10) unsigned NOT NULL,
  `edge` int(10) unsigned NOT NULL,
  `edge_condition` int(10) unsigned NOT NULL,
  PRIMARY KEY (`process_instance`,`edge`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `edge_condition_mapping`
--

/*!40000 ALTER TABLE `edge_condition_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `edge_condition_mapping` ENABLE KEYS */;


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
-- Definition of table `notecategory`
--

DROP TABLE IF EXISTS `notecategory`;
CREATE TABLE `notecategory` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `parent` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_notecategory_parent` (`parent`),
  CONSTRAINT `FK_notecategory_parent` FOREIGN KEY (`parent`) REFERENCES `notecategory` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `notecategory`
--

/*!40000 ALTER TABLE `notecategory` DISABLE KEYS */;
/*!40000 ALTER TABLE `notecategory` ENABLE KEYS */;


--
-- Definition of table `paragraph`
--

DROP TABLE IF EXISTS `paragraph`;
CREATE TABLE `paragraph` (
  `database_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `process` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `possible_activity_names` text NOT NULL,
  `color` varchar(255) NOT NULL,
  `default_model_element` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`database_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  PRIMARY KEY (`audittrail_entry`),
  CONSTRAINT `FK_audittrail_entry` FOREIGN KEY (`audittrail_entry`) REFERENCES `audittrail_entry` (`database_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `paragraph_mapping`
--

/*!40000 ALTER TABLE `paragraph_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `paragraph_mapping` ENABLE KEYS */;


--
-- Definition of table `ppmnote`
--

DROP TABLE IF EXISTS `ppmnote`;
CREATE TABLE `ppmnote` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `text` text NOT NULL,
  `startTime` varchar(255) NOT NULL,
  `endTime` varchar(255) DEFAULT NULL,
  `category` bigint(20) unsigned DEFAULT NULL,
  `originator` varchar(255) DEFAULT NULL,
  `parent` bigint(20) unsigned DEFAULT NULL,
  `processInstance` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ppmnote_parent` (`parent`),
  KEY `FK_category` (`category`),
  CONSTRAINT `FK_category` FOREIGN KEY (`category`) REFERENCES `notecategory` (`id`),
  CONSTRAINT `FK_ppmnote_parent` FOREIGN KEY (`parent`) REFERENCES `ppmnote` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ppmnote`
--

/*!40000 ALTER TABLE `ppmnote` DISABLE KEYS */;
/*!40000 ALTER TABLE `ppmnote` ENABLE KEYS */;


--
-- Definition of table `process`
--

DROP TABLE IF EXISTS `process`;
CREATE TABLE `process` (
  `database_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` text NOT NULL,
  `data` text,
  PRIMARY KEY (`database_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  PRIMARY KEY (`database_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `process_instance`
--

/*!40000 ALTER TABLE `process_instance` DISABLE KEYS */;
/*!40000 ALTER TABLE `process_instance` ENABLE KEYS */;

--
-- Definition of table `transcript`
--

DROP TABLE IF EXISTS `transcript`;
CREATE TABLE `transcript` (
  `database_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `process_instance` bigint(20) unsigned NOT NULL,
  `startTime` varchar(255) NOT NULL,
  `endTime` varchar(255) NOT NULL,
  `originator` varchar(255) NOT NULL,
  `text` text NOT NULL,
  PRIMARY KEY (`database_id`) USING BTREE,
  KEY `FK_instance` (`process_instance`),
  CONSTRAINT `FK_instance` FOREIGN KEY (`process_instance`) REFERENCES `process_instance` (`database_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=505 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `transcript`
--

/*!40000 ALTER TABLE `transcript` DISABLE KEYS */;
/*!40000 ALTER TABLE `transcript` ENABLE KEYS */;


--
-- Definition of table `xml_log`
--

DROP TABLE IF EXISTS `xml_log`;
CREATE TABLE `xml_log` (
  `database_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `log` longblob NOT NULL,
  PRIMARY KEY (`database_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `xml_log`
--

/*!40000 ALTER TABLE `xml_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `xml_log` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
