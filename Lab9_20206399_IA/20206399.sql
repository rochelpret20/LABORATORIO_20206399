-- MySQL 8: ejecutar con un usuario autorizado a crear bases y tablas.
-- Sustituya CODIGO por su código también en application.properties si corresponde.
CREATE DATABASE IF NOT EXISTS `stf_games_20206399`
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `stf_games_20206399`;
CREATE TABLE IF NOT EXISTS `stf_puzzle_sv` (
  `stf_game_board_structure` VARCHAR(512) NOT NULL,
  `image` LONGTEXT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
