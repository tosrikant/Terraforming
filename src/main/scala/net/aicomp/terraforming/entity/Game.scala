package net.aicomp.terraforming.entity

class Game(val field: Field, val players: IndexedSeq[Player], val maxTurn: Int) {
  private var _currentPlayerIndex = 0
  private var _currentTurn = 0
  private var _isMoving = false
  private var _isBuilding = false

  def currentTurn = _currentTurn
  def currentPlayerIndex = _currentPlayerIndex
  def currentPlayer = players(_currentPlayerIndex)
  def isFinished = currentTurn == maxTurn

  def acceptCommand(command: Command) {
    command match {
      case MoveCommand(p, dir, amount) => {
        checkCanMove
        _isMoving = true
        field.moveSquad(currentPlayer, p, dir, amount)
      }
      case BuildCommand(p, t) => {
        checkCanBuild
        _isBuilding = true
        field.build(currentPlayer, p, t)
      }
      case FinishCommand() => finishTurn
    }
  }

  def changePlayerIndex() = {
    _currentPlayerIndex = (_currentPlayerIndex + 1) % players.length
    _currentPlayerIndex
  }

  def startTurn() {
    field.produceRobot(currentPlayer)
    field.clearMovedRobots()
    _isMoving = false
    _isBuilding = false
    _currentTurn += 1
  }

  private def finishTurn() {
    changePlayerIndex()
    if (!isFinished) startTurn
  }

  private def checkCanMove() {
    if (_isBuilding) throw new CommandException("Robots cannot move after build command")
  }

  private def checkCanBuild() {
    if (_isMoving) throw new CommandException("Installations cannot be built after some robots moved")
    if (_isBuilding) throw new CommandException("Only one installations can be built in one turn")
  }

  def stringify(playerId: Int): String = "START\n" +
    currentTurn + " " + maxTurn + " " + playerId + "\n" +
    field.stringify +
    "EOS"
}