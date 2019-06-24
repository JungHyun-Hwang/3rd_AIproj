package nu.tengstrand.tetrisanalyzer.piecegenerator

import nu.tengstrand.tetrisanalyzer.piece.Piece
import java.util.Random

import scala.util.control.Breaks

/**
 * Uses the standard random generator in Java.
 */
class JavaPieceGenerator  extends PieceGenerator {
  private val random = new Random
  var loop = new Breaks

  def nextPieceNumber = {
    var random_limit = (returnArray(Piece.Getrandom_limit))
    var tmp = 0
    while(true){
      tmp = random.nextInt(Piece.NumberOfPieceTypes) + 1
      if(random_limit(tmp-1)==0){
        random_limit(tmp-1)==1
        loop.break
      }
    }
    Piece.Setrandom_limit(random_limit)
    tmp
  }
  def returnArray(random_limit: Array[Int])={
    var cnt = 0;
    for(i <- 0 to 6){
      if(random_limit(i)==1){
        cnt+=1
      }
    }
    if(cnt >= 7){
      Array(0,0,0,0,0,0,0)
    }
    else{
      random_limit
    }
  }
}