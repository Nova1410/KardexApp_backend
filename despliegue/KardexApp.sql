-- MySQL dump 10.13  Distrib 8.0.15, for Win64 (x86_64)
--
-- Host: localhost    Database: kardexapp
-- ------------------------------------------------------
-- Server version	8.0.15

create database kardexapp;

use kardexapp;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;


--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cant` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  `categ_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

alter table product add constraint FKthxjxl0sen3qqhetdhvjh2ixo foreign key (categ_id) references category (id);

--
-- Table structure for table `kardex`
--

DROP TABLE IF EXISTS `kardex`;
CREATE TABLE `kardex` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `totalCost` float DEFAULT NULL,
  `transDate` date DEFAULT NULL,
  `transType` int(11) DEFAULT NULL,
  `unitCost` float DEFAULT NULL,
  `units` int(11) DEFAULT NULL,
  `prod_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

alter table kardex add constraint FKtqv56k9puv8ix8pxno40e5dky foreign key (prod_id) references product (id);
