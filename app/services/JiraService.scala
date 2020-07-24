package services

import com.google.inject.{ImplementedBy, Inject, Singleton}
import framework.Utils.{ContentType, OnWsResponse}
import framework._
import play.api.libs.json.{Format, Json}
import services.JiraService.{CreateTicketInput, CreateTicketResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@ImplementedBy(classOf[JiraServiceImpl])
trait JiraService {
  def create(): Future[List[CreateTicketResponse]]
}

@Singleton
class JiraServiceImpl @Inject()(apiController: ApiController) extends JiraService {
  override def create(): Future[List[CreateTicketResponse]] = {
    val priorityIdList = JiraService.allPriorities
    val issueTypeIdList = JiraService.allIssueTypes
    Future.sequence( for {
      priorityId <- priorityIdList
      issueTypeId <- issueTypeIdList
      inputBody = CreateTicketInput(priorityId, issueTypeId)
    } yield {
      apiController.execute(CreateTicket, WithAttributes(Success, None, None, Some(ContentType.json), None,
        Some(Utils.toJsonString(inputBody)))).map(res =>
        res.parseResponse[CreateTicketResponse]
      )
    })
  }
}

object JiraService {

  case class CreateTicketInput(fields: Fields)

  object CreateTicketInput {
    implicit val _format: Format[CreateTicketInput] = Json.format[CreateTicketInput]

    def apply(priorityId: PriorityLike, issueTypeId: IssueTypeLike): CreateTicketInput = {
      val customField = issueTypeId match {
        case Epic => Some("ST-1")
        case _ => None
      }
      sample.copy(sample.fields.copy(customfield_10011 = customField, priority = IssueType(priorityId.asString), issuetype = IssueType(issueTypeId.asString)))
    }

    private val sample: CreateTicketInput = CreateTicketInput(fields = Fields(
      summary = "Automation Test",
      issuetype = IssueType(id = "10000"),
      components = List(IssueType(id = "10000")),
      project = IssueType(id = "10002"),
      customfield_10011 = Some("ST-1"),
      description = Description(`type` = "doc", version = 1, content = List(Contents(`type` = "paragraph", content = List(Content(text = "Automation Test Interview", `type` = "text"))))),
      reporter = IssueType(id = "5eb97ba3c5c6230baa5d00fe"),
      priority = IssueType(id = "3"),
      labels = List("bugfix", "blitz_test")
    ))
  }

  case class Fields(summary: String, issuetype: IssueType, components: List[IssueType], project: IssueType,
                    customfield_10011: Option[String], description: Description, reporter: IssueType, priority: IssueType,
                    labels: List[String])

  object Fields {
    implicit val _format: Format[Fields] = Json.format[Fields]
  }

  case class Description(`type`: String, version: Double, content: List[Contents])

  object Description {
    implicit val _format: Format[Description] = Json.format[Description]
  }

  case class Contents(`type`: String, content: List[Content])

  object Contents {
    implicit val _format: Format[Contents] = Json.format[Contents]
  }

  case class Content(text: String, `type`: String)

  object Content {
    implicit val _format: Format[Content] = Json.format[Content]
  }

  case class IssueType(id: String)

  object IssueType {
    implicit val _format: Format[IssueType] = Json.format[IssueType]
  }

  case class CreateTicketResponse(id: String, key: String, self: String)

  object CreateTicketResponse {
    implicit val _format: Format[CreateTicketResponse] = Json.format[CreateTicketResponse]
  }

  sealed trait PriorityLike {
    def asString: String
  }
  case object PriorityOne extends PriorityLike {
    override def asString: String = "1"
  }

  case object PriorityTwo extends PriorityLike {
    override def asString: String = "2"
  }

  case object PriorityThree extends PriorityLike {
    override def asString: String = "3"
  }

  case object PriorityFour extends PriorityLike {
    override def asString: String = "4"
  }

  case object PriorityFive extends PriorityLike {
    override def asString: String = "5"
  }

  val allPriorities: List[PriorityLike] = List(PriorityOne, PriorityTwo, PriorityThree, PriorityFour, PriorityFive)

  sealed trait IssueTypeLike {
    def asString: String
  }

  case object Epic extends IssueTypeLike {
    override def asString = "10000"
  }

  case object Story extends IssueTypeLike {
    override def asString = "10001"
  }

  case object Task extends IssueTypeLike {
    override def asString = "10002"
  }

  case object Bug extends IssueTypeLike {
    override def asString = "10004"
  }

  val allIssueTypes: List[IssueTypeLike] = List(Epic, Story, Task, Bug)

}
