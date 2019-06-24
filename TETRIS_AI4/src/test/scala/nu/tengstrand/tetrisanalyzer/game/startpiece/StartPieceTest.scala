package nu.tengstrand.tetrisanalyzer.game.startpiece

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test
import nu.tengstrand.tetrisanalyzer.piece.{PieceL, PieceT}

class StartPieceTest extends BaseTest {

  @Test def hasNext_onePiece() {
    val startPiece = new StartPiece(PieceT())
    startPiece.nextPiece()
    startPiece.hasNext should be (false)
  }

  @Test def hasNext_twoPieces() {
    val startPiece = new StartPiece(PieceT(), PieceT())
    startPiece.hasNext should be (true)
  }

  @Test def nextOnePiece() {
    val startPiece = new StartPiece(PieceT())
    startPiece.nextPiece should be (PieceT())
  }

  @Test def nextTwoPieces() {
    val startPiece = new StartPiece(PieceT(), PieceL())
    startPiece.nextPiece should be (PieceT())
    startPiece.nextPiece should be (PieceL())
  }
}