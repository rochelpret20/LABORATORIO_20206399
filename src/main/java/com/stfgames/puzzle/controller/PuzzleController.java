package com.stfgames.puzzle.controller;
import com.stfgames.puzzle.model.PuzzleState;
import com.stfgames.puzzle.model.IniciarJuego;
import com.stfgames.puzzle.model.Movimiento;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/puzzle")
public class PuzzleController {
    private final JdbcTemplate jdbc;
    public PuzzleController(JdbcTemplate jdbc){
        this.jdbc=jdbc;
    }
    @GetMapping
    public PuzzleState obtener(){
        List<Map<String, Object>> filas = jdbc.queryForList("SELECT*FROM stf_puzzle_sv");
    }
    @PostMapping("/imagen")

    @PostMapping("/iniciar")
    public PuzzleState iniciar(@RequestBody IniciarJuego datos){
        int n=datos.getSize();
        String imagen=datos.getImage();

    }
}
