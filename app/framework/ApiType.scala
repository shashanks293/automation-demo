package framework

sealed trait ApiType {
  def asString: String

  final override def toString: String = asString
}

case object GET extends ApiType {
  override def asString: String = "GET"
}

case object POST extends ApiType {
  override def asString: String = "POST"
}
