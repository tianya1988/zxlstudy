package base

/**
 * Created by jason on 18-7-2.
 */
object Test {
  def main(args: Array[String]) {

    var responseListeners: List[Int] = Nil
    val a = 1
    responseListeners ::= a
    val b = 2
    responseListeners ::= b // 把b追加到列表头
    println("length is : " + responseListeners.length) //length is : 2
    println(responseListeners(0)) //2
    println(responseListeners(1)) //1
  }

}
