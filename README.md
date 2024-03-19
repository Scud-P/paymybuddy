# Openclassrooms - Parcours de développeur d'applications Java  - Projet 6

## Solution choisie


### Langages de programmation

Backend: Java 18
Frontend: HTML/CSS


### Stack technique

Spring Boot
Thymeleaf
Maven
MySQL
Lombok
JaCoCo
Surefire
Bootstrap


### Architecture

### Approche transactionnelle

### Gestion des erreurs

## Galerie des fonctionnalités

[Signup](https://imgur.com/a/dn5zhs2)

[Login](https://imgur.com/a/tBLp5ii)

[Profile](https://imgur.com/a/bhLSpyc)

[Transfer](https://imgur.com/a/fEbwKu6)

[Add a connection](https://imgur.com/a/fMmVtm4)

## Diagramme de classe UML

![ClassDiagram](https://github.com/Scud-P/paymybuddy/assets/129103727/9de8abce-308c-42b3-8254-b61b15ca7473)

## Modèle physique de données

![Database](https://github.com/Scud-P/paymybuddy/assets/129103727/abd4d777-e3ad-4985-9857-fc90ef6721ae)

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
  `fee` double NOT NULL,
  PRIMARY KEY (`transaction_number`),
  KEY `transaction_ibfk_1` (`sender_user_id`),
  KEY `transaction_ibfk_2` (`receiver_user_id`),
  CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`sender_user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `transaction_ibfk_2` FOREIGN KEY (`receiver_user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

## Insertion de valeurs dans la base de données

J'ai fais le choix de peupler les tables de ma base de données de manière programmatique dans la classe MockDBService mais vous trouverez les requêtes SQL ci-dessous.

### Table users

INSERT INTO `users` VALUES (1,'John','Smith','john.smith@example.com',0,'12345'),(2,'Emily','Johnson','emily.johnson@example.com',10,'12345'),(3,'Michael','Williams','michael.williams@example.com',20,'12345'),(4,'Sarah','Brown','sarah.brown@example.com',30,'12345'),(5,'Christopher','Jones','christopher.jones@example.com',40,'12345'),(6,'Jessica','Miller','jessica.miller@example.com',50,'12345'),(7,'Matthew','Davis','matthew.davis@example.com',60,'12345'),(8,'Ashley','Garcia','ashley.garcia@example.com',70,'12345'),(9,'William','Rodriguez','william.rodriguez@example.com',80,'12345'),(10,'Amanda','Martinez','amanda.martinez@example.com',90,'12345');

### Table transaction

INSERT INTO `transaction` VALUES (1,'2000-01-01 05:00:00',5,'Beer',1,3,0.03),(2,'2000-01-02 05:00:00',5,'Wine',2,5,0.03),(3,'2000-01-03 05:00:00',5,'Pastis',3,4,0.03),(4,'2000-01-04 05:00:00',5,'Rhum',4,3,0.03),(5,'2000-01-05 05:00:00',5,'Ti Punch',5,2,0.03),(6,'2000-01-06 05:00:00',6,'Margarita',6,4,0.03),(7,'2000-01-07 05:00:00',7,'Negroni',1,5,0.04),(8,'2000-01-08 05:00:00',8,'White Russian',2,1,0.04),(9,'2000-01-09 05:00:00',9,'Bloody Caesar',3,2,0.04),(10,'2000-01-10 05:00:00',10,'Champagne',1,4,0.05),(11,'2000-01-11 05:00:00',11,'Pack of Smokes',1,2,0.06),(12,'2000-01-12 05:00:00',12,'Pack of Beer',1,3,0.06),(13,'2000-01-13 05:00:00',13,'Movie',1,4,0.07),(14,'2000-01-14 05:00:00',14,'Bottle of Wine',1,5,0.07),(15,'2000-01-15 05:00:00',15,'Bottle of Rum',1,2,0.07),(16,'2000-01-16 05:00:00',16,'Bottle of Tequila',1,3,0.08),(17,'2000-01-17 05:00:00',17,'Bottle of Cognac',1,4,0.09),(18,'2000-01-18 05:00:00',18,'Bottle of Champagne',1,5,0.09),(19,'2000-01-19 05:00:00',19,'Bottle of Whisky',1,2,0.1),(20,'2000-01-20 05:00:00',20,'Slip Kangourou en Soie',1,3,0.1);

### Table partnership

INSERT INTO `partnership` VALUES (1,1,2),(2,1,3),(3,1,4),(4,1,5),(5,1,6),(6,2,1),(7,2,3),(8,2,4),(9,2,5),(10,2,6),(11,3,1),(12,3,2),(13,3,4),(14,3,5),(15,3,6),(16,4,1),(17,4,2),(18,4,3),(19,4,5),(20,4,6),(21,5,1),(22,5,2),(23,5,3),(24,5,4),(25,5,6),(26,6,1),(27,6,2),(28,6,3),(29,6,4),(30,6,5);
 
