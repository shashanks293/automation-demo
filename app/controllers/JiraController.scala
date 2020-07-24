package controllers

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, BaseController, ControllerComponents, Request}
import services.JiraService

import scala.concurrent.ExecutionContext.Implicits.global

class JiraController @Inject()(val controllerComponents: ControllerComponents,
                               service: JiraService)
  extends BaseController {

  def create() = Action.async { implicit request: Request[AnyContent] =>
    service.create().map(res => Ok(Json.toJson(res)))
  }
}
