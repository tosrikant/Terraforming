package net.aicomp.entity

class Game {
  val map = new Map(7)

  def moveCommand(args: List[String]) {
    val x = args(0).toInt
    val y = args(1).toInt
    val d = args(2)
    map.moveSquad(new Point(x, y), Direction.fromString(d))
  }

}
