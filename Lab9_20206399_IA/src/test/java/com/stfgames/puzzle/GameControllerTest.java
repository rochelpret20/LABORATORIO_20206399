package com.stfgames.puzzle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(properties="spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE")
class GameControllerTest {
 @Autowired GameController c; @Autowired JdbcTemplate db;
 @Test void gameLifecycle() throws Exception {
  String img="data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(getClass().getResourceAsStream("/static/default.jpg").readAllBytes());
  assertThrows(ResponseStatusException.class,()->c.start(new GameController.Start(2,img)));
  assertThrows(ResponseStatusException.class,()->c.start(new GameController.Start(3,"bad")));
  var g=c.start(new GameController.Start(3,img)); assertEquals("12345678X".length(),g.board().length()); assertEquals(g,c.get());
  var initial=g.board(); assertThrows(ResponseStatusException.class,()->c.move(new GameController.Move(0,initial)));
  assertThrows(ResponseStatusException.class,()->c.move(new GameController.Move(4,"stale")));
  int[] delta={-3,-1,-3,-1,3,1,3,1};
  for(int step=0;step<delta.length;step++){int i=g.board().indexOf('X')+delta[step]; var r=c.move(new GameController.Move(i,g.board())); assertEquals(step==7,r.completed());g=r.game();assertEquals(g,c.get());}
  assertEquals(initial,g.board()); assertEquals(1,db.queryForObject("select count(*) from stf_puzzle_sv",Integer.class));
  var large=c.start(new GameController.Start(8,img));assertEquals(64,large.board().split(",").length);
 }
}
