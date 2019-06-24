package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{Color, Graphics2D, Dimension}
import nu.tengstrand.tetrisanalyzer.game.{Speed, GameInfoReceiver}
import java.util.Random

case class SpeedInfo(secondsPassed: Double, piecesTotal: Long, clearedRowsTotal: Long)

class GameInfoView extends GameInfoReceiver {
  private val random = new Random
  private var seed = 0L
  private var speedIndex = 1
  private var isMaxSpeed = false
  private var slidingEnabled = false
  private var boardSize: Dimension = new Dimension(10, 20)
  private var pieces = 0L
  private var piecesTotal = 0L
  private var clearedRows = 0L
  private var clearedRowsTotal = 0L
  private var clearedRowsTotalInFinishedGames = 0L

  private var games = 0L
  private var minRows = 0L
  private var maxRows = 0L

  private var secondsPassed = 0.0
  private var speedInfo = SpeedInfo(0.0, 0, 0)

  private val textPainter = new TextPainter

  private val numberSeparator = new NumberSeparator

  private var paused = true
  private var showNextPiece = false

  private val speedBarColor = new Color(150, 255, 150)
  private val speedBarColorMax = new Color(255, 150, 150)

  var showView = true

  def toggleShowView() { showView = !showView }

  def width = if (showView) 235 else 0

  def setSeed(seed: Long) { this.seed = random.nextInt(7)}
  def setSliding(enabled: Boolean) { slidingEnabled = enabled }
  def setShowNextPiece(show: Boolean) { showNextPiece = show }
  def setBoardSize(width: Int, height: Int) { boardSize = new Dimension(width, height) }
  def setNumberOfPieces(pieces: Long) { this.pieces = pieces }
  def setTotalNumberOfPieces(piecesTotal: Long) { this.piecesTotal = piecesTotal }
  def setNumberOfClearedRows(clearedRows: Long) { this.clearedRows = clearedRows }
  def setTotalNumberOfClearedRows(clearedRowsTotal: Long) { this.clearedRowsTotal = clearedRowsTotal }
  def setTimePassed(seconds: Double) { secondsPassed = seconds }
  def setPaused(paused: Boolean) { this.paused = paused }

  def setSpeed(speedIndex: Int, isMaxSpeed: Boolean) {
    this.speedIndex = speedIndex
    this.isMaxSpeed = isMaxSpeed

    // Used to calculate the speed
    speedInfo = SpeedInfo(secondsPassed, piecesTotal, clearedRowsTotal)
  }

  def setNumberOfGamesAndRowsInLastGame(games: Long, rows: Long, totalClearedRows: Long, minRows: Long, maxRows: Long) {
    this.games = games
    this.minRows = minRows
    this.maxRows = maxRows
    this.clearedRowsTotal = totalClearedRows
    this.clearedRowsTotalInFinishedGames = totalClearedRows
  }

  def paintGameInfo(origoX: Int, g: Graphics2D) {
    if (showView)
      paintGraphics(origoX, g)
  }

  private def paintGraphics(origoX: Int, g: Graphics2D) {
    textPainter.prepareDrawText(origoX, g)

    textPainter.drawInfo("Rows:", withSpaces(clearedRows), 1, g)
    textPainter.drawInfo("Pieces:", withSpaces(pieces), 2, g)

    textPainter.drawInfo("Rows total:", withSpaces(clearedRowsTotal), 4, g)
    textPainter.drawInfo("Pieces total:", withSpaces(piecesTotal), 5, g)

    textPainter.drawInfo("Games:", withSpaces(games), 7, g)
    textPainter.drawInfo("Rows/game:", if (games == 0) "" else withSpaces(clearedRowsTotalInFinishedGames / games), 8, g)
    textPainter.drawInfo("Min rows:", if (games == 0) "" else withSpaces(minRows), 9, g)
    textPainter.drawInfo("Max rows:", if (games == 0) "" else withSpaces(maxRows), 10, g)

    textPainter.drawInfo("Board:", boardSize.width + " x " + boardSize.height, 12, g)
    textPainter.drawInfo("Sliding:", if (slidingEnabled) "On" else "Off", 13, g)
    textPainter.drawInfo("Random seed:", seed, 14, g)

    textPainter.drawInfo("Next:", if (showNextPiece) "On" else "Off", 16, g)

    textPainter.drawInfo("Speed:", "", 18, g)
    textPainter.drawInfo("Rows/sec:", calculateUnitsPerSec(clearedRowsTotal - speedInfo.clearedRowsTotal), 19, g)
    textPainter.drawInfo("Pieces/sec:", calculateUnitsPerSec(piecesTotal - speedInfo.piecesTotal), 20, g)

    textPainter.drawInfo("Pause:", if (paused) "On" else "", 22, g)



    textPainter.drawInfo("Elapsed time:", calculateElapsedTime(secondsPassed), 31, g)

    textPainter.drawText("[F1] Help (toggle)", 33, g)
    textPainter.drawText("[F2] Miniature board (toggle)", 34, g)
    textPainter.drawText("[F3] Show game info (toggle)", 35, g)

    drawSpeed(18, origoX, g)
  }

  private def drawSpeed(row: Int, origoX: Int, g: Graphics2D) {
    val factor = 10

    if (isMaxSpeed)
      g.setColor(speedBarColorMax)
    else
      g.setColor(speedBarColor)

    val y1 = textPainter.getY(row) - 10
    val x1 = origoX + 55
    val width = if (isMaxSpeed) Speed.MaxSpeedIndex * factor else speedIndex * factor
    val height = 10
    val x2 = x1 + Speed.MaxSpeedIndex * factor
    val y2 = y1 + height
    g.fillRect(x1+1, y1+1, width, height)
    g.setColor(Color.BLACK)
    g.drawLine(x1,y1, x2,y1)
    g.drawLine(x1,y2, x2,y2)
    g.drawLine(x1,y1, x1,y2)
    g.drawLine(x2,y1, x2,y2)
  }

  private def withSpaces(number: Long) = numberSeparator.withSpaces(number)

  private def calculateElapsedTime(seconds: Double): String = {
    val sec: Int = (seconds*10 % 600).toInt
    val min: Int = (seconds/60 % 60).toInt
    val hours: Int = (seconds/3600).toInt
    hours + "h " + min + "m " + (sec/10) + "." + (sec%10) + "s"
  }

  private def calculateUnitsPerSec(total: Double): Any = {
    val seconds = secondsPassed - speedInfo.secondsPassed

    if (seconds == 0 || total == 0) {
      0
    } else {
      if (total / seconds >= 100)
        withSpaces(scala.math.round(total / seconds))
      else
        (scala.math.round(total * 10 / seconds) / 10.0)
    }
  }
}