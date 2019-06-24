package nu.tengstrand.tetrisanalyzer.piece

object PieceL {
  def apply() = new PieceL
}

class PieceL extends Piece {
  val number = 5.toByte
  val character = 'L'
  protected val widths = Array(3, 2, 3, 2)
  protected val heights = Array(2, 3, 2, 3)
  protected val shapes = Array(
    new PieceShape(Array(Point(0,0), Point(1,0), Point(2,0), Point(0,1))),
    new PieceShape(Array(Point(0,0), Point(0,1), Point(0,2), Point(1,2))),
    new PieceShape(Array(Point(2,0), Point(0,1), Point(1,1), Point(2,1))),
    new PieceShape(Array(Point(0,0), Point(1,0), Point(1,1), Point(1,2)))
  )
}
