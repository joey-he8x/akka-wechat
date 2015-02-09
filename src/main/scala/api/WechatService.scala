package api

import akka.actor.ActorRef
import akka.util.Timeout
import spray.routing.Directives
import wechat.model.{WechatMsg, WechatMsgFormator}

import scala.concurrent.ExecutionContext
import scala.xml.NodeSeq

/**
 * Created by joey on 14-8-27.
 */
class WechatService(wechat: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with WechatAuth {

  import scala.concurrent.duration._
  import akka.pattern.ask
  implicit val timeout = Timeout(5.seconds)


  val route =
    path("wx-api" / IntNumber) { appId =>
      wechatIdentification(appId) {
        get {
          parameter('echostr){
            echostr =>
            complete(echostr)
          }
        } ~
        post{
          handleWith {
            xml:NodeSeq =>
              (wechat ? WechatMsg(appId,WechatMsgFormator(xml))).mapTo[String]
            }
        }
      }
    }
}
