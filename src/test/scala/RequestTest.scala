import junit.framework.Assert
import org.junit.{Before, Test}
import play.api.libs.json.{JsArray, JsValue, Json}

/**
  * @author Menkin
  * @since 27.08.2017
  * Класс для тестирования Request.scala
  */
class RequestTest {
  var request: Request = _
  var requestTransfer:(Int, JsValue) = _
  var requestInternet:(Int, JsValue) = _
  var requestCharity:(Int, JsValue) = _

  val baseUrl = "https://www.tinkoff.ru/api/v1/providers?groups="
  val transfer = "Переводы"
  val internet = "Интернет"
  val charity = "Благотворительность"

  /**
    * инициализация переменных для тестирования
    */
  @Before
  def init() = {
    request = new Request
    requestTransfer = (request.getCodeAndURLContent(baseUrl + transfer)._1, Json.parse(request.getCodeAndURLContent(baseUrl + transfer)._2))
    requestInternet = (request.getCodeAndURLContent(baseUrl + internet)._1, Json.parse(request.getCodeAndURLContent(baseUrl + internet)._2))
    requestCharity = (request.getCodeAndURLContent(baseUrl + charity)._1, Json.parse(request.getCodeAndURLContent(baseUrl + charity)._2))
  }

  /**
    * метод проверяет, что HTTP код состояния соответствует успеху
    * @param code код ответа сервера при HTTP запросе
    */
  def expectStatusCodeIn2xx(code: Int) = {
    Assert.assertTrue(is2xx(code))
  }

  /**
    * метод проверяет, что документ возвращается в формате json
    * @param json документ в json формате
    */
  def expectedDocumentInJSON(json: JsValue) = {
    Assert.assertTrue(json.isInstanceOf[JsValue])
  }

  /**
    * метод проверяет, что значение resultCode равно OK
    * @param json документ в json формате
    */
  def expectedResultCodeIsOK(json: JsValue) = {
    Assert.assertTrue((json \ "resultCode").asOpt[String].contains("OK"))
  }

  /**
    * Метод проверяет, что все значения groupId равны параметру GROUP_NAME
    * @param json документ в json формате
    * @param requestType параметр GROUP_NAME
    */
  def expectedGroupIdIsRequestType(json: JsValue, requestType: String) = {
    Assert.assertTrue((json \\ "groupId").forall(x => {
      x.as[String] == requestType
    }))
  }


  /**
    * Метод проверяет, что для каждого id равного lastName,
    * параметр name содержит подстроку Фамилия
    * @param json документ в json формате
    */
  def expectedForIdLastNameContainsFamily(json: JsValue) = {
    // -> assertTrue
    Assert.assertFalse((json \\ "providerFields").forall(x => {
      x.as[JsArray].value.forall(y => {
        (y \ "id").asOpt[String].contains("lastName") &&
          (y \ "name").asOpt[String].getOrElse("None").contains("Фамилия")
      })
    }))
  }

  /**
    * Метод запускает последовательно проверки в следующем порядке:
    * 2)	HTTP код состояния соответствует успеху
    * 3)	Возвращается документ в формате json
    * 1)	Значение resultCode равно OK
    * 5)	Все значения groupId равны параметру GROUP_NAME
    * 4)	Для каждого id равного lastName, параметр name содержит подстроку Фамилия
    * @param response - ответ за запрос, содержащий код ответа сервера и документ в json формате
    * @param requestGroupName - параметр GROUP_NAME
    */
  def testProvider(response: (Int, JsValue), requestGroupName: String) = {
    expectStatusCodeIn2xx(response._1)
    expectedDocumentInJSON(response._2)
    expectedResultCodeIsOK(response._2)
    expectedGroupIdIsRequestType(response._2, requestGroupName)
    expectedForIdLastNameContainsFamily(response._2)
  }

  @Test
  def testProvidersGroups() = {
    testProvider(requestTransfer, "Переводы")
    testProvider(requestInternet, "Интернет")
    testProvider(requestCharity, "Благотворительность")
  }

  /**
    * @param code код ответа сервера при HTTP запросе
    * @return попадает ли код ответа в диапазон 2хх (является успешным)
    */
  def is2xx(code: Int): Boolean = code >= 200 && code <= 299
}
