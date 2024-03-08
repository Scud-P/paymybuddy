# Openclassrooms - Parcours de développeur d'applications Java  - Projet 6 

## Diagramme de classe UML

![UML_Class_Diagram](https://github.com/Scud-P/paymybuddy/assets/129103727/bc150617-8d2e-4781-ae36-7855f63e9019)

## Modèle physique de données

![Database](https://github.com/Scud-P/paymybuddy/assets/129103727/894664a5-cbc4-4bb5-8d70-527f24d44382)

## Script SQL de création de la base de donnée

```
CREATE DATABASE IF NOT EXISTS `paymybuddy` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `paymybuddy`;

-- Table structure for table `users`

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `email` varchar(45) NOT NULL,
  `balance` double NOT NULL DEFAULT '0',
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Table that stores users of the PayMyBuddy App';

-- Table structure for table `partnership`

DROP TABLE IF EXISTS `partnership`;
CREATE TABLE `partnership` (
  `partnership_id` int(11) NOT NULL AUTO_INCREMENT,
  `owner_id` int(11) NOT NULL,
  `partner_id` int(11) NOT NULL,
  PRIMARY KEY (`partnership_id`),
  KEY `user_id_idx` (`owner_id`),
  KEY `user_id_idx1` (`partner_id`),
  CONSTRAINT `user_id` FOREIGN KEY (`owner_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table structure for table `transaction`

DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction` (
  `transaction_number` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL,
  `amount` double NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `sender_user_id` int(11) NOT NULL,
  `receiver_user_id` int(11) NOT NULL,
  PRIMARY KEY (`transaction_number`),
  KEY `transaction_ibfk_1` (`sender_user_id`),
  KEY `transaction_ibfk_2` (`receiver_user_id`),
  CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`sender_user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `transaction_ibfk_2` FOREIGN KEY (`receiver_user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

