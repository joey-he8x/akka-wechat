package api

import akka.actor.ActorRef
import akka.util.Timeout
import spray.routing.Directives

import scala.concurrent.ExecutionContext
import scala.xml.NodeSeq

/**
 * Created by joey on 14-8-27.
 */
class WechatService(wechatRoute: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with WechatAuth {

  import scala.concurrent.duration._
  import akka.pattern.ask
  implicit val timeout = Timeout(2.seconds)


  val route =
    path("wx-api" / IntNumber) { app_id =>
      wechatIdentification(app_id) {
        get {
          parameter('echostr){
            echostr =>
            complete(echostr)
          }
        } ~
        post{
          handleWith {
            xml:NodeSeq =>
              (wechatRoute ? xml).mapTo[NodeSeq]
            }
        }
      }
    }
}
