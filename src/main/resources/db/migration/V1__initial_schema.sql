CREATE TABLE IF NOT EXISTS `author` (
                                        `id` bigint NOT NULL AUTO_INCREMENT,
                                        `birth_date` date DEFAULT NULL,
                                        `country` varchar(50) NOT NULL,
                                        `first_name` varchar(50) NOT NULL,
                                        `last_name` varchar(80) NOT NULL,
                                        PRIMARY KEY (`id`)
);

INSERT INTO `author` (`id`, `birth_date`, `country`, `first_name`, `last_name`) VALUES
                                                                                    (3, '1923-03-06', 'Colombia', 'Gabriel', 'Garcia'),
                                                                                    (4, '1899-08-24', 'Argentina', 'Jorge Luis', 'Borges');


CREATE TABLE IF NOT EXISTS `book`
(
    `id`               bigint       NOT NULL AUTO_INCREMENT,
    `genre`            varchar(20)  NOT NULL,
    `pages`            int          NOT NULL,
    `publication_year` varchar(4)   NOT NULL,
    `title`            varchar(100) NOT NULL,
    `author_id`        bigint NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_Book_Author` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`)
);


INSERT INTO `book` (`id`, `genre`, `pages`, `publication_year`, `title`, `author_id`) VALUES
                                                                                          (1, 'Realismo Mágico', 450, '1967', 'Cem Anos de Solidão', 3),
                                                                                          (2, 'Romance', 400, '1985', 'El amor en los tiempos del cólera', 3);

