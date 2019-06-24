package nu.tengstrand.tetrisanalyzer.game

trait GameInfoReceiver {
  def setPaused(paused: Boolean)
  def setSeed(seed: Long)
  def setSliding(enabled: Boolean)
  def setBoardSize(width: Int, height: Int)
  def setNumberOfPieces(pieces: Long)
  def setTotalNumberOfPieces(pieces: Long)
  def setNumberOfGamesAndRowsInLastGame(games: Long, rows: Long, totalClearedRows: Long, minRows: Long, maxRows: Long)
  def setNumberOfClearedRows(rows: Long)
  def setTotalNumberOfClearedRows(rows: Long)
  def setTimePassed(seconds: Double)
  def setSpeed(speedIndex: Int, isMaxSpeed: Boolean)
}