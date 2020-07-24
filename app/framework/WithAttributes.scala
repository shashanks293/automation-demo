package framework


case class WithAttributes(expectedResponseCode    : ResponseCodes,
                          queryParamOpt           : Option[Map[String, String]],
                          pathParamOpt            : Option[Map[String, String]],
                          headersOpt              : Option[Map[String, String]],
                          cookiesOpt              : Option[Map[String, String]],
                          reqBodyOpt              : Option[String])


