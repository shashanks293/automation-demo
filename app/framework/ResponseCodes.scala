package framework

sealed trait ResponseCodes {
  def asInt: Int
}

case object Success extends ResponseCodes {
  override def asInt: Int = 200
}

case object BadRequest extends ResponseCodes {
  override def asInt: Int = 500
}

case object ServerError extends ResponseCodes {
  override def asInt: Int = 500
}
