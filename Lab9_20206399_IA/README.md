# Stf-Puzzle Simplified Version

Aplicación Spring Boot / Java 17 con API REST y base de datos MySQL persistente.

## Ejecución

Requisitos: JDK 17, Maven 3 y MySQL 8. Ejecute `creacion_bd.sql` en MySQL Workbench o con `mysql -u root -p < creacion_bd.sql`. Configure usuario y contraseña en `src/main/resources/application.properties` o mediante las variables de entorno `DB_USERNAME` y `DB_PASSWORD`. Por defecto usa `root` sin contraseña, base `stf_games_CODIGO` y puerto 3306; `DB_URL` permite cambiar la conexión. Después ejecute `mvn spring-boot:run` y abra http://localhost:8080. Si reemplaza CODIGO por su código, actualice también la URL JDBC.

Ingrese dimensiones cuadradas de 3x3 a 8x8. Seleccione opcionalmente una imagen JPG de hasta 7 MB y un color; al iniciar se reemplaza la partida guardada. Tras cada movimiento válido se guarda el tablero. Al completar el rompecabezas aparece el mensaje de victoria y se crea automáticamente una nueva partida con la misma imagen y tamaño. El color se conserva en el navegador.

La tabla `stf_puzzle_sv` tiene exactamente las columnas requeridas; para 3x3 el tablero se guarda como `12345678X`. Para tamaños mayores se separan los números con comas para evitar ambigüedades. Solo se mantiene una fila.

## Servicios

* `GET /api/game`: partida actual o respuesta vacía si no existe.
* `POST /api/game`: `{ "size": 3, "image": "data:image/jpeg;base64,..." }` inicia una partida.
* `POST /api/game/move`: `{ "index": 0, "expectedBoard": "..." }` mueve una casilla (índice desde cero), valida adyacencia y evita actualizaciones obsoletas. Devuelve `{game, completed}`.

`SOLUCION.txt` describe la secuencia para resolver la posición inicial. Ejecutar pruebas con `mvn test` (usan H2 en memoria). El proyecto se entrega localmente; no incluye publicación en GitHub.
