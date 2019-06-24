package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.settings.GameSettings
import nu.tengstrand.tetrisanalyzer.piece.{Point, Piece, PieceEmpty}
import nu.tengstrand.tetrisanalyzer.move.Move
import startpiece.StartPiece

object Wall {
  val Number = 9.toByte
  val Character = '#'

  val Left = 6
  val Right = 2
  val Bottom = 2
}

object RankedPiece {
  val Number = 10.toByte
  val Character = '*'
}

object Dot {
  val Characters = Array[Char] (
    Piece(0).character,
    Piece(1).character,
    Piece(2).character,
    Piece(3).character,
    Piece(4).character,
    Piece(5).character,
    Piece(6).character,
    Piece(7).character,
    Piece(8).character,
    Wall.Character,
    RankedPiece.Character
  )
}

object Position {
  def apply(boardWidth: Int = 10, boardHeight: Int = 20): Position = {
    val positionView = Array.tabulate[Byte](boardHeight + Wall.Bottom, boardWidth + Wall.Left + Wall.Right) (
      (y,x) => if (x < Wall.Left || x >= boardWidth + Wall.Left || y >= boardHeight) Wall.Number else PieceEmpty.Number
    )
    new Position(boardWidth, boardHeight, positionView)
  }

  // Copy constructor
  def apply(position: Position): Position = {
    new Position(position.boardWidth, position.boardHeight, position.playfieldCopy)
  }
}

/**
 * A board with walls and current piece, used by the GUI.
 * If next piece is activated that piece is also shown.
 */
class Position(val boardWidth: Int, val boardHeight: Int, playfield: Array[Array[Byte]]) extends ColoredPosition {
  def setDot(dot: Point, move: Move, number: Byte) { playfield(dot.y + move.y)(dot.x + move.x + Wall.Left) = number }
  def getDot(x: Int, y: Int) = { playfield(y)(x) }
  def getRow(y: Int): Array[Byte] = playfield(y)

  def width = boardWidth + Wall.Left + Wall.Right
  def height = boardHeight + Wall.Bottom
  def colorValue(x: Int, y: Int): Int = playfield(y)(x).toInt

  def setStartPieceIfFree(startPiece: StartPiece, settings: GameSettings) {
    val piece = startPiece.firstPiece
    val startMove = settings.pieceStartMove(boardWidth, piece)
    val isFree = piece.shape(startMove.rotation).dots.foldLeft(0) {(sum,dot) => sum + emptyOrOccupied(dot.x, dot.y)} == 0
    if (isFree)
      setPiece(piece, startMove)
  }

  def setOrRemoveNextPiece(startPiece: StartPiece) {
    if (!startPiece.hasNext) {
      setNextPieceSquare(Wall.Number)
    } else {
      setNextPieceSquare(PieceEmpty.Number)
      val piece = startPiece.secondPiece
      piece.shape(0).dots.foreach(dot => playfield(dot.y)(dot.x + 1) = piece.number)
    }
  }

  private def setNextPieceSquare(number: Byte) {
    for (y <- 0 to 1) {
      for (x <- 0 to 4) {
        playfield(y)(x) = number
      }
    }
  }

  def setSelectedMove(piece: Piece, move: Move) {
    piece.shape(move.rotation).dots.foreach(dot => setDot(dot, move, (RankedPiece.Number + piece.number).toByte))
  }

  def setPiece(piece: Piece, move: Move) {
    piece.shape(move.rotation).dots.foreach(dot => setDot(dot, move, piece.number))
  }

  def isCompleteRow(y: Int): Boolean = {
    (0 until boardWidth).foldLeft(0) { (sum,x) => sum + emptyOrOccupied(x,y) } == boardWidth
  }

  private def emptyOrOccupied(x: Int, y: Int) = if (playfield(y)(x + Wall.Left) == PieceEmpty.Number) 0 else 1

  def clearRow(y: Int) {
    (Wall.Left until Wall.Left + boardWidth).foreach(x => playfield(y)(x) = PieceEmpty.Number)
  }

  private def copyRow(fromY: Int, toY: Int) {
    (Wall.Left until Wall.Left + boardWidth).foreach(x => playfield(toY)(x) = playfield(fromY)(x))
  }

  def copyRow(y: Int, fromPosition: Position) {
    (Wall.Left until Wall.Left + boardWidth).foreach(x => playfield(y)(x) = fromPosition.getDot(x,y))
  }

  /**
   * Clears completed rows and returns number of cleared rows.
   * This method is called after a piece has been placed on the board.
   *   pieceY: the y position of the piece.
   *   pieceHeight: height of the piece.
   */
  def clearRows(pieceY: Int, pieceHeight: Int): Int = {
    var clearedRows = 0
    var y1 = pieceY + pieceHeight

    // Find first row to clear
    do {
      y1 -= 1
      if (isCompleteRow(y1))
        clearedRows += 1
    } while (clearedRows == 0 && y1 > pieceY)

    // Clear rows
    if (clearedRows > 0) {
      var y2 = y1

      while (y1 >= 0) {
        y2 -= 1
        while (y2 >= pieceY && isCompleteRow(y2)) {
          clearedRows += 1
          y2 -= 1
        }
        if (y2 >= 0)
          copyRow(y2, y1)
        else
          clearRow(y1)

        y1 -= 1
      }
    }
    clearedRows
  }

  def copyFrom(position: Position) {
    for (y <- 0 until height) {
      position.getRow(y).copyToArray(playfield(y))
    }
  }

  def playfieldCopy = {
    val newPlayfield: Array[Array[Byte]] = Array.ofDim[Byte](height, width)

    for (y <- 0 until playfield.length)
      playfield(y).copyToArray(newPlayfield(y))

    newPlayfield
  }

  override def toString = {
    var result = "";
    var newRow = "";
    for (y <- 0 until height) {
      result += newRow;
      for (x <- 0 until width) {
        result += Dot.Characters(playfield(y)(x))
      }
      newRow = "\n"
    }
    result
  }
}
