package net.aicomp.terraforming.entity

import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write

class Game(val field: Field, val players: IndexedSeq[Player], private val _maxTurn: Int) {
  private var _currentPlayerIndex = 0
  private var _currentTurn = 0
  private var _isMoving = false
  private var _isBuilding = false
  private var _isFinished = false
  private var _modified = true
  private var _initGame: Game = null

  def currentTurn = (_currentTurn + 2) / players.size
  def maxTurn = _maxTurn
  def currentPlayerIndex = _currentPlayerIndex
  def currentPlayer = players(_currentPlayerIndex)
  def isFinished = _isFinished

  def copy() = {
    new Game(field.copy(), players, _maxTurn)
  }

  def checkModified() = {
    val modified = _modified
    _modified = false
    modified
  }

  def acceptCommand(command: Command) = {
    command match {
      case MoveCommand(p, dir, amount) => {
        checkCanMove
        _isMoving = true
        _modified = true
        field.moveSquad(currentPlayer, p, dir, amount)
      }
      case BuildCommand(p, t) => {
        checkCanBuild
        _isBuilding = true
        _modified = true
        field.build(currentPlayer, p, t)
      }
      case ResetCommand() => {
        _isMoving = false
        _isBuilding = false
        _modified = true
        _initGame
      }
      case FinishCommand() => {
        _isMoving = false
        _isBuilding = false
        _modified = true
        finishTurn()
      }
    }
  }

  def changePlayerIndex() = {
    _currentPlayerIndex = (_currentPlayerIndex + 1) % players.length
    _currentPlayerIndex
  }

  def startTurn(): String = {
    field.produceRobot(currentPlayer)
    field.attack(currentPlayer)
    field.clearMovedRobots()
    _initGame = copy()
    _currentTurn += 1
    "----------------- Start " + currentPlayer.name + "'s turn #" + currentTurn + " -----------------"
  }

  private def finishTurn() = {
    changePlayerIndex()
    if (isContinued()) {
      startTurn()
    } else {
      _isFinished = true
    }
  }

  private def isContinued() = _currentTurn < _maxTurn * 3 &&
    players.map(field.calculateScore(_)).max < 100

  private def checkCanMove() {
    if (_isBuilding) throw new CommandException("Robots cannot move after build command")
  }

  private def checkCanBuild() {
    if (_isMoving) throw new CommandException("Installations cannot be built after some robots moved")
    if (_isBuilding) throw new CommandException("Only one installations can be built in one turn")
  }

  def stringify(player: Player): String = "START\n" +
    currentTurn + " " + maxTurn + " " + player.id + "\n" +
    field.stringify +
    "EOS"

  def toJson(player: Player) = {
    implicit val formats = Serialization.formats(NoTypeHints)
    write(Map(
      "cTurn" -> currentTurn,
      "mTurn" -> maxTurn,
      "pid" -> player.id,
      "tiles" -> field.toPartialJson))
  }
}
