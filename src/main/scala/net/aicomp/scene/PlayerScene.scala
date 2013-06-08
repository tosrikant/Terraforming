package net.aicomp.scene

import jp.ac.waseda.cs.washi.gameaiarena.gui.Scene
import net.aicomp.entity.{Game,GameEnvironment,GameSetting,Player,Field}
import net.aicomp.util.misc.DateUtils
import net.aicomp.util.settings.Defaults


abstract class PlayerScene(nextScene: Scene[GameEnvironment], setting: GameSetting = GameSetting())
  extends AbstractScene {
  override def initialize() {
    describe("Enter player names")
    displayLine("Please enter player names with space delimiters.")
  }

  override def runWithArgs(names: List[String]) = {
    if (names.size <= 1) {
      displayLine("Please enter two or more names.")
      this
    } else {
      val players = names.map(new Player(_))
      val field = Field.generate(7, players)
      env.game = new Game(field, players)
      displayLine(names.size + " players have joined the game. (" + names.mkString(", ") + ")")
      nextScene
    }
  }
}
