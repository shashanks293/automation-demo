package framework

sealed trait ApiDefinitions {
  def apiPath: String

  def apiType: ApiType

  def service: BackendService

  def url: String
}

case object CreateTicket extends ApiDefinitions {
  override def apiPath: String = "/rest/api/3/issue"

  override def apiType: ApiType = POST

  override def service: BackendService = JiraService

  override val url = s"${service.baseUri}$apiPath"
}
