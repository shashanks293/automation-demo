package framework

import com.google.inject.{ImplementedBy, Inject, Singleton}
import play.api.http.Writeable
import play.api.libs.ws.{EmptyBody, InMemoryBody, WSAuthScheme, WSBody, WSClient, WSResponse}

import scala.concurrent.Future

@ImplementedBy(classOf[ApiControllerImpl])
trait ApiController {
  def execute(api: ApiDefinitions, attributes: WithAttributes): Future[WSResponse]
}

@Singleton
class ApiControllerImpl @Inject()(wsClient: WSClient) extends ApiController {
  override def execute(api: ApiDefinitions, attributes: WithAttributes): Future[WSResponse] = {

    def getUrlWithPathParams: String = {
      attributes.pathParamOpt.fold(api.url) { params =>
        params.foldLeft(api.url) { case (acc, (k, v)) =>
          val (key, escapedKey) = (s"{$k}", s"\\{$k\\}")
          if (acc.contains(key)) acc.replaceAll(escapedKey, v)
          else throw new Exception(s"Error!! $key not found in api path")
        }
      }
    }

    val queryString = attributes.queryParamOpt.getOrElse(Nil).toSeq

    val headers = attributes.headersOpt.getOrElse(Nil).toSeq

    val cookies = attributes.cookiesOpt.getOrElse(Nil).toSeq

    def getBody(implicit wrt: Writeable[String]): WSBody = attributes.reqBodyOpt match {
      case Some(s) => InMemoryBody(wrt.transform(s))
      case None => EmptyBody
    }

    wsClient
      .url(getUrlWithPathParams)
      .withHttpHeaders(headers ++ cookies: _*)
      .withAuth("ggunjan2103@gmail.com", "9MG5d8zSLmbcW3wLI6JwFF5F", WSAuthScheme.BASIC)
      .withQueryStringParameters(queryString: _*)
      .withMethod(api.apiType.asString)
      .withBody(getBody)
      .execute()
  }
}
