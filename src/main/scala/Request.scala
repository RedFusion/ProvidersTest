/**
  * @author Menkin
  * @since 27.08.2017
  * Класс отвечает за формирование и отправку запроса по указанному адресу
  */
class Request {
  /**
    * Метод выполняет запрос по указанному адресу
    * @throws java.io.IOException
    * @throws java.net.SocketTimeoutException
    * @return код состояния HTTP, ответ на запрос
    */
  @throws(classOf[java.io.IOException])
  @throws(classOf[java.net.SocketTimeoutException])
  def getCodeAndURLContent(url: String): (Int, String) = {
    import java.net.{HttpURLConnection, URL}
    val connection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]
    connection.setRequestMethod("GET")
    val inputStream = connection.getInputStream
    val content = io.Source.fromInputStream(inputStream)("UTF-8").mkString
    if (inputStream != null) inputStream.close()
    (connection.getResponseCode, content)
  }
}
