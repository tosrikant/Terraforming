import java.util.List;
import java.util.Random;
import java.util.Scanner;

import net.aicomp.terraforming.ai.entity.Direction;
import net.aicomp.terraforming.ai.entity.Field;
import net.aicomp.terraforming.ai.entity.Game;
import net.aicomp.terraforming.ai.entity.Installation;
import net.aicomp.terraforming.ai.entity.Landform;
import net.aicomp.terraforming.ai.entity.Point;
import net.aicomp.terraforming.ai.entity.Tile;

public class Main {
  public static void main(String[] args) {
    Random rand = new Random("aicomp".hashCode());

    Scanner scanner = new Scanner(System.in);

    boolean firstTime = true;
    while (scanner.hasNext()) {
      Game game = parseGame(scanner);
      if(firstTime){
        firstTime = false;
        System.out.println("Your player name"); // 表示名
        continue;
      }
      // Can conduct only one kind of commands, 'move' or 'build'
      if (rand.nextInt(1) == 0) {
        List<Point> points = game.field.getPointsWithRobots(game.myId); // 自軍のロボットのいるセルを列挙
        for (Point point : points) {
          if (rand.nextInt(1) == 0) {
            Tile tile = game.field.tiles.get(point);
            if(tile.isHole)continue; // 穴からは動けない
            int dirIndex = rand.nextInt(Direction.values().length);
            move(point, Direction.values()[dirIndex], rand.nextInt(tile.robot) + 1); // 適当に移動
          }
        }
      } else if (rand.nextInt(3) == 0) {
        List<Point> points = game.field.getPointsWithRobots(game.myId); // 自軍のロボットのいるセルを列挙
        for (Point point : points) {
          int instIndex = rand.nextInt(Installation.values().length - 1) + 1;
          // Can conduct only one command of 'build'
          if (rand.nextInt(3) == 0) {
            build(point, Installation.values()[instIndex]); // 適当に建設
            // Avoid to conduct two or more than 'build' commands
            break;
          }
        }
      }
      System.out.println("finish");
    }
    scanner.close();
  }

  private static void move(Point point, Direction dir, int robot) {
    System.out.println("move " + point.x + " " + point.y + " " + dir.command + " " + robot);
  }

  private static void build(Point point, Installation inst) {
    System.out.println("build " + point.x + " " + point.y + " " + inst.name().toLowerCase());
  }

  /*
  // START
  // ターン番号 最大ターン番号 プレイヤーのID
  // フィールドの幅 タイルの数
  // タイル1のx座標 y座標 所有者のID ロボットの数 資源数 地形情報 基地
  // タイル2のx座標 y座標 所有者のID ロボットの数 資源数 地形情報 基地
  // ...
  // タイルnのx座標 y座標 所有者のID ロボットの数 資源数 地形情報 基地
  // EOS
  */
  private static Game parseGame(Scanner scanner) {
    if (!scanner.next().equals("START")) {
      throw new RuntimeException("START should be retrieved.");
    }    
    
    int turn = scanner.nextInt();
    int maxTurn = scanner.nextInt();
    int myId = scanner.nextInt();
    int radius = scanner.nextInt();
    Game game = new Game(turn, maxTurn, myId, new Field(radius));
    Field field = game.field;

    int nTiles = scanner.nextInt();
    for (int i = 0; i < nTiles; i++) {
      Point point = new Point(scanner.nextInt(), scanner.nextInt());

      int playerId = scanner.nextInt();
      int robot = scanner.nextInt();
      int resource = scanner.nextInt();
      boolean isHole = false;

      String landformName = scanner.next();
      String capitalizedlandformName =
          landformName.substring(0, 1).toUpperCase() + landformName.substring(1);
      Landform landform = Landform.valueOf(capitalizedlandformName);

      String instName = scanner.next();
      String capitalizedInstName = instName.substring(0, 1).toUpperCase() + instName.substring(1);
      Installation inst = null;
      if (capitalizedInstName.equals("Hole")) {
        isHole = true;
      } else {
        inst = Installation.valueOf(capitalizedInstName);
      }

      Tile tile = new Tile(playerId, robot, resource, isHole, landform, inst);
      field.tiles.put(point, tile);
    }
    if (!scanner.next().equals("EOS")) {
      throw new RuntimeException("EOS should be retrieved.");
    }
    return game;
  }
}
