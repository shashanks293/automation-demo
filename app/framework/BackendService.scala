package framework

sealed trait BackendService {
  def baseUri: String
}

case object JiraService extends BackendService {
  override def baseUri: String = "https://garima-test.atlassian.net"
}
