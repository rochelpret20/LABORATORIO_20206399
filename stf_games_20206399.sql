CREATE DATABASE IF NOT EXISTS stf_games_20206399;
USE stf_games_20206399;

-- para una fila y 2 columnas
CREATE TABLE IF NOT EXISTS stf_puzzle_sv(
	stf_game_board_structure VARCHAR(100) NOT NULL,
    image LONGTEXT NULL);

-- La imagen queda en NULL y luego el backend carga la imagn por
-- defecto al abrirlo.

TRUNCATE TABLE stf_puzzle_sv;
INSERT INTO stf_puzzle_sv(stf_game_board_structure,image)
VALUES ('12345678X',NULL);
-- 1 2 3
-- 4 5 6
-- 7 8 X.  DONDE X INDICA LA CASILLA VACÍA

SELECT * FROM stf_puzzle_sv;




