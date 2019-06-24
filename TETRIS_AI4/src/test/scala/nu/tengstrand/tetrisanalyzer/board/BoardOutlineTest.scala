package nu.tengstrand.tetrisanalyzer.board

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.{Before, Test}

class BoardOutlineTest extends BaseTest {
  var board: Board = _

  @Before def setUp() {
    board = Board(Array(
      "#------x#",
      "#-----xx#",
      "#-x---xx#",
      "#x-x--xx#",
      "#########"))
  }

  @Test def boarderOutline() {
    BoardOutline(board) should be (new BoardOutline(Array(3,2,3,4,4,1,0,0), 0))
  }

  @Test def minY() {
    BoardOutline(board).minY should be (0)
  }
}