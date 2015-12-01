package module.slick

/**
 * Created by huodong on 15/7/27.
 */
object TypeExtends {

  def unpackPgArray(array: String): Array[Int] = array.drop(1).dropRight(1).split(',').map(_.toInt)

  def packPgArray(array: Array[Int]): String = {
    val sb = StringBuilder.newBuilder
    sb.append("{")
    array.foreach(i => {sb.append(i); sb.append(",")})
    sb.replace(sb.length-1, sb.length, "}")
    sb.toString()
  }

  def pushPgArray(array: String, value: Int): String = new StringBuilder(array).insert(array.length - 1, "," + value.toString).toString()

}
