package framework

import java.util

import play.api.libs.json.{JsError, JsSuccess, Json, Reads, Writes}
import play.api.libs.ws.WSResponse

import scala.util.Random

object Utils {

  implicit class OnList[T](l: List[T]) {
    def asJavaArrayList(): util.ArrayList[T] = {
      val b = new util.ArrayList[T]
      l map b.add
      b
    }

    def getRandomElement: T = l(new Random().nextInt(l.length))
  }

  implicit class OnWsResponse(res: WSResponse) {
    /** Parses the Response into case class provided as T */
    def parseResponse[T](implicit reads: Reads[T]): T = {
      Json.parse(res.body).validate[T] match {
        case JsSuccess(value, _) => value
        case JsError(errors) => throw new Exception(s"[Could not parse Api Response]\nResponse Body : \n${res.body}\nErrors : ${errors.mkString(", ")}")
      }
    }
  }

  def toJsonString[DTO](dto: DTO)(implicit dtoWrites: Writes[DTO]): String = Json.stringify(Json.toJson(dto))

  object ContentType {
    val contentTypeKey = "Content-Type"
    val json: Map[String, String] = Map(contentTypeKey -> "application/json")
    val plainText: Map[String, String] = Map(contentTypeKey -> "text/plain")
  }

}
