package com.stfgames.puzzle;

import java.util.*;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController @RequestMapping("/api/game")
public class GameController {
 private final JdbcTemplate db;
 public GameController(JdbcTemplate db){this.db=db;}
 public record Game(String board, String image) {}
 public record Start(int size, String image) {}
 public record Move(int index, String expectedBoard) {}
 public record Result(Game game, boolean completed) {}
 private ResponseStatusException bad(String m){return new ResponseStatusException(HttpStatus.BAD_REQUEST,m);}
 private Game current(){var rows=db.query("SELECT stf_game_board_structure, image FROM stf_puzzle_sv",(rs,n)->new Game(rs.getString(1),rs.getString(2))); return rows.isEmpty()?null:rows.get(0);}
 @GetMapping public synchronized Game get(){return current();}
 private void save(Game g){db.update("DELETE FROM stf_puzzle_sv");db.update("INSERT INTO stf_puzzle_sv (stf_game_board_structure,image) VALUES (?,?)",g.board(),g.image());}
 private static String encode(List<Integer> a){if(a.size()==9){StringBuilder s=new StringBuilder();for(int n:a)s.append(n==0?"X":n);return s.toString();} return String.join(",",a.stream().map(n->n==0?"X":Integer.toString(n)).toList());}
 private static List<Integer> decode(String s){String[] parts=s.contains(",")?s.split(","):s.split("");List<Integer> a=new ArrayList<>();for(String x:parts)a.add(x.equals("X")?0:Integer.parseInt(x));return a;}
 private static Game newGame(int n,String image){List<Integer> a=new ArrayList<>();for(int i=1;i<n*n;i++)a.add(i);a.add(0);int blank=n*n-1;int[] delta={-1,-n,-1,-n,1,n,1,n};for(int d:delta){int next=blank+d;Collections.swap(a,blank,next);blank=next;}return new Game(encode(a),image);}
 @PostMapping @Transactional public synchronized Game start(@RequestBody Start in){if(in.size()<3||in.size()>8)throw bad("La dimensión debe estar entre 3x3 y 8x8"); if(in.image()==null || in.image().length()>10000000 || !Pattern.compile("^data:image/jpeg;base64,[A-Za-z0-9+/]+={0,2}$").matcher(in.image()).matches())throw bad("Se requiere una imagen JPG válida de hasta 7 MB");try{byte[] bytes=Base64.getDecoder().decode(in.image().substring(in.image().indexOf(',')+1)); if(bytes.length<4 || (bytes[0]&255)!=255 || (bytes[1]&255)!=216 || (bytes[bytes.length-2]&255)!=255 || (bytes[bytes.length-1]&255)!=217 || javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(bytes))==null)throw bad("Imagen JPG inválida");}catch(java.io.IOException|IllegalArgumentException e){throw bad("Imagen JPG inválida");} Game g=newGame(in.size(),in.image());save(g);return g;}
 @PostMapping("/move") @Transactional public synchronized Result move(@RequestBody Move m){Game g=current();if(g==null)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No hay juego activo");if(!Objects.equals(g.board(),m.expectedBoard()))throw new ResponseStatusException(HttpStatus.CONFLICT,"El tablero cambió; recargue el juego");List<Integer>a=decode(g.board());int n=(int)Math.sqrt(a.size()), blank=a.indexOf(0), i=m.index();if(i<0||i>=a.size()||Math.abs(i/n-blank/n)+Math.abs(i%n-blank%n)!=1)throw bad("Movimiento no válido");Collections.swap(a,i,blank);boolean won=true;for(int j=0;j<a.size()-1;j++)if(a.get(j)!=j+1)won=false;if(a.get(a.size()-1)!=0)won=false;Game next=won?newGame(n,g.image()):new Game(encode(a),g.image());save(next);return new Result(next,won);}
}
