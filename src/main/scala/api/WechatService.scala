package api

import akka.actor.ActorRef
import akka.util.Timeout
import spray.routing.Directives

import scala.concurrent.ExecutionContext
import scala.xml.NodeSeq

/**
 * Created by joey on 14-8-27.
 */
class WechatService(echo: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with WechatAuth with WechatXmlContent {

  import scala.concurrent.duration._
  implicit val timeout = Timeout(2.seconds)


  val route =
    path("wx-api") {
      wechatIdentification {
        get {
          parameter('echostr){
            echostr =>
            complete(echostr)
          }
        } ~
        post{
          handleWith {
            xml:NodeSeq =>
              xml
            }
        }
      }
    }
}
