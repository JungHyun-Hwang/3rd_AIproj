package nu.tengstrand.tetrisanalyzer.piece

object PieceT {
  def apply() = new PieceT
}

class PieceT extends Piece {
  val number = 6.toByte
  val character = 'T'
  protected val widths = Array(3, 2, 3, 2)
  protected val heights = Array(2, 3, 2, 3)
  protected val shapes = Array(
    new PieceShape(Array(Point(0,0), Point(1,0), Point(2,0), Point(1,1))),
    new PieceShape(Array(Point(0,0), Point(0,1), Point(1,1), Point(0,2))),
    new PieceShape(Array(Point(1,0), Point(0,1), Point(1,1), Point(2,1))),
    new PieceShape(Array(Point(1,0), Point(0,1), Point(1,1), Point(1,2)))
  )
}
