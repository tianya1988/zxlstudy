package base

/**
 * Created by jason on 17-5-24.
 */
object FoldTest {
  def main(args: Array[String]) {
    val numbers = List(1, 2, 3, 4, 5)
    // foldRight从集合右边遍历，且函数中右边参数为累加器
    val res1 = numbers.foldRight(0)((z, i) => {

      println("元素是z： " + z)
      println("             累加器结果i： " + i)
      z + i
    }
    )
    println("res1 = " + res1)

    println("")
    println("=============")
    println("")

    // foldLeft从集合左边遍历，且函数左边参数为累加器
    val res2 = numbers.foldLeft(0)((z, i) => {
      println("元素是i： " + i)
      println("             累加器结果z： " + z)
      z + i
    }
    )
    println("res2 = " + res2)
  }


  /**
   *
元素是z： 5
             累加器结果i： 0
元素是z： 4
             累加器结果i： 5
元素是z： 3
             累加器结果i： 9
元素是z： 2
             累加器结果i： 12
元素是z： 1
             累加器结果i： 14
res1 = 15

=============

元素是i： 1
             累加器结果z： 0
元素是i： 2
             累加器结果z： 1
元素是i： 3
             累加器结果z： 3
元素是i： 4
             累加器结果z： 6
元素是i： 5
             累加器结果z： 10
res2 = 15

   */
}
