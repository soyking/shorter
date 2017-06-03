-- MySQL Script generated by MySQL Workbench
-- Sat Jun  3 14:45:48 2017
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema shorter
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema shorter
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `shorter` DEFAULT CHARACTER SET utf8 ;
USE `shorter` ;

-- -----------------------------------------------------
-- Table `shorter`.`author`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shorter`.`author` (
  `id` VARCHAR(128) NOT NULL,
  `name` VARCHAR(64) NOT NULL,
  `created_at` BIGINT(32) NOT NULL,
  `key` VARCHAR(128) NOT NULL,
  `secret` VARCHAR(128) NOT NULL,
  `init_vector` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shorter`.`sheet`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shorter`.`sheet` (
  `id` VARCHAR(128) NOT NULL,
  `created_at` BIGINT(32) NOT NULL,
  `author` VARCHAR(128) NOT NULL,
  `type` VARCHAR(32) NOT NULL,
  `text` TEXT(1024) NOT NULL,
  `link` TEXT(1024) NULL,
  `token` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `created_at` (`created_at` ASC),
  INDEX `author_idx` (`author` ASC),
  UNIQUE INDEX `token_UNIQUE` (`token` ASC),
  CONSTRAINT `author`
    FOREIGN KEY (`author`)
    REFERENCES `shorter`.`author` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
