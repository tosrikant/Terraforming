package net.aicomp.terraforming.util.misc;

import java.awt.Image

import scala.collection.Seq
import scala.collection.mutable

import net.exkazuu.gameaiarena.gui.Renderer

object ImageLoader {
  def prefetch(render: Renderer) = {
    val fetchedImages = mutable.ListBuffer[Image]()
    for (method <- ImageLoader.getClass().getMethods()) {
      if (method.getName().startsWith("load")) {
        fetchedImages ++= dig(method.invoke(this, render))
      }
    }
    fetchedImages.toList
  }

  def dig(obj: Any): Seq[Image] = {
    obj match {
      case m: Map[_, _] => m.values.toSeq.flatten(dig(_))
      case s: Seq[_] => s.flatten(dig(_))
      case i: Image => Seq(i)
    }
  }

  private var _background: Image = null

  def loadBackground(render: Renderer) = {
    if (_background == null) {
      _background = render.loadImage("img/map.png")
    }
    _background
  }

  private var _tiles: Map[String, Image] = null

  def loadTiles(render: Renderer) = {
    if (_tiles == null) {
      _tiles = Map(
        "48" -> render.loadImage("img/hex/hex48.png"),
        "48_0" -> render.loadImage("img/hex/hex48_0.png"),
        "48_1" -> render.loadImage("img/hex/hex48_1.png"),
        "48_2" -> render.loadImage("img/hex/hex48_2.png"))
    }
    _tiles
  }

  private var _installations: Map[String, Image] = null
  def loadInstallations(render: Renderer) = {
    if (_installations == null) {
      _installations = Map(
        "hole" -> render.loadImage("img/installation/hole.png"),
        "hole_18" -> render.loadImage("img/installation/hole18.png"),
        "tile_0" -> render.loadImage("img/installation/tile0.png"),
        "tile_1" -> render.loadImage("img/installation/tile1.png"),
        "tile_2" -> render.loadImage("img/installation/tile2.png"),
        "vp_0" -> render.loadImage("img/installation/vp0.png"),
        "vp_1" -> render.loadImage("img/installation/vp1.png"),
        "vp_2" -> render.loadImage("img/installation/vp2.png"),
        "additionalScore_0" -> render.loadImage("img/installation/townvp0.png"),
        "additionalScore_1" -> render.loadImage("img/installation/townvp1.png"),
        "additionalScore_2" -> render.loadImage("img/installation/townvp2.png"),
        "installation_0" -> render.loadImage("img/installation/installation0.png"),
        "installation_1" -> render.loadImage("img/installation/installation1.png"),
        "installation_2" -> render.loadImage("img/installation/installation2.png"),
        "initial_0" -> render.loadImage("img/installation/largerobotmaker0.png"),
        "initial_1" -> render.loadImage("img/installation/largerobotmaker1.png"),
        "initial_2" -> render.loadImage("img/installation/largerobotmaker2.png"),
        "robotmaker_0" -> render.loadImage("img/installation/robotmaker0.png"),
        "robotmaker_1" -> render.loadImage("img/installation/robotmaker1.png"),
        "robotmaker_2" -> render.loadImage("img/installation/robotmaker2.png"),
        "excavator_0" -> render.loadImage("img/installation/excavator0.png"),
        "excavator_1" -> render.loadImage("img/installation/excavator1.png"),
        "excavator_2" -> render.loadImage("img/installation/excavator2.png"),
        "tower_0" -> render.loadImage("img/installation/attack0.png"),
        "tower_1" -> render.loadImage("img/installation/attack1.png"),
        "tower_2" -> render.loadImage("img/installation/attack2.png"),
        "bridge_0" -> render.loadImage("img/installation/bridge0.png"),
        "bridge_1" -> render.loadImage("img/installation/bridge1.png"),
        "bridge_2" -> render.loadImage("img/installation/bridge2.png"),
        "house_0" -> render.loadImage("img/installation/house0.png"),
        "house_1" -> render.loadImage("img/installation/house1.png"),
        "house_2" -> render.loadImage("img/installation/house2.png"),
        "town_0" -> render.loadImage("img/installation/town0.png"),
        "town_1" -> render.loadImage("img/installation/town1.png"),
        "town_2" -> render.loadImage("img/installation/town2.png"))
    }
    _installations
  }

  private var _roundParts: Map[String, Image] = null

  def loadRoundParts(renderer: Renderer) = {
    if (_roundParts == null) {
      _roundParts = Map("Title" -> renderer.loadImage("img/round/round.png"),
        "Slash" -> renderer.loadImage("img/round/rnslash.png"))
    }
    _roundParts
  }

  private var _roundNumber: Map[Int, Image] = null

  def loadRoundNumber(render: Renderer) = {
    if (_roundNumber == null) {
      _roundNumber = Range(0, 10).map(num =>
        (num, render.loadImage(
          "img/round/rn" + num.toString + ".png"))).toMap
    }
    _roundNumber
  }

  private val _playerIndices = (-1 to 2)

  private var _robots: Map[Int, Image] = null
  def loadRobots(render: Renderer) = {
    if (_robots == null) {
      _robots =
        _playerIndices
          .filter { _ >= 0 }
          .map { t => (t, render.loadImage("img/robot/robot" + t + ".png")) }
          .toMap
    }
    _robots
  }

  private var _largeRobots: Map[Int, Image] = null
  def loadLargeRobots(render: Renderer) = {
    if (_largeRobots == null) {
      _largeRobots =
        _playerIndices
          .filter { _ >= 0 }
          .map { t => (t, render.loadImage("img/robot/lrobot" + t + ".png")) }
          .toMap
    }
    _largeRobots
  }

  private var _numbers: Map[(Int, Int), Image] = null
  def loadNumbers(render: Renderer) = {
    if (_numbers == null) {
      _numbers =
        _playerIndices
          .flatMap {
            p =>
              (0 to 9).map {
                n => ((p, n), render.loadImage("img/number/num" + p + n + ".png"))
              }
          }
          .toMap
    }
    _numbers
  }

  private var _statusNumbers: Map[(Int, Int), Image] = null
  def loadStatusNumbers(render: Renderer) = {
    if (_statusNumbers == null) {
      _statusNumbers =
        _playerIndices
          .flatMap {
            p =>
              (0 to 9).map {
                n => ((p, n), render.loadImage("img/lnumber/num" + p + n + ".png"))
              }
          }
          .toMap
    }
    _statusNumbers
  }

  private var _playerInformationBackgrounds: Map[Int, Image] = null
  def loadPlayerInformationBackgrounds(render: Renderer) = {
    if (_playerInformationBackgrounds == null) {
      _playerInformationBackgrounds =
        _playerIndices
          .filter { _ >= 0 }
          .map { t => (t, render.loadImage("img/playerInfo/p" + t + ".png")) }
          .toMap
    }
    _playerInformationBackgrounds
  }

  private var _title: Image = null
  def loadTitle(render: Renderer) = {
    if (_title == null) {
      _title = render.loadImage("img/start/title.jpg")
    }
    _title
  }
}
