CREATE SCHEMA  IF NOT EXISTS  `logs` /*!40100 DEFAULT CHARACTER SET utf8 */;


CREATE TABLE IF NOT EXISTS `logs`.`log_file` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `path` VARCHAR(255) NOT NULL,
    `last_update_time` DATETIME NOT NULL,
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;

  
  
CREATE TABLE IF NOT EXISTS `logs`.`log_data` (
    `line_number` INT(11) NOT NULL,
    `log_file_id` INT(11) NOT NULL,
    `log_date` DATETIME(3) NOT NULL,
    `ip` VARCHAR(255) NOT NULL,
    `request` VARCHAR(255) NOT NULL,
    `status` INT(11) NOT NULL,
    `user_agent` VARCHAR(255) NOT NULL
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE IF NOT EXISTS `logs`.`search_results` (
    `start_date` DATETIME(3) NOT NULL,
    `end_date` DATETIME(3) NOT NULL,
    `search_type` VARCHAR(45) NOT NULL,
    `ip` VARCHAR(255) NOT NULL,
    `ip_count` INT NOT NULL,
    `threshold` INT NOT NULL,
    `search_date` DATETIME(3) NOT NULL,
    `comment` VARCHAR(255) NOT NULL 
) ENGINE=INNODB DEFAULT CHARSET=UTF8;