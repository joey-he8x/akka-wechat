package api

import akka.actor.ActorRef
import akka.util.Timeout

import scala.concurrent.ExecutionContext

/**
 * Created by joey on 14-8-27.
 */
class WechatService(echo: ActorRef)(implicit executionContext: ExecutionContext)
  extends WechatAuth  with DefaultJsonFormats {

  import scala.concurrent.duration._
  implicit val timeout = Timeout(2.seconds)

  //implicit val httpRequestFormat = jsonFormat5(HttpRequest)

  val route =
    path("wx-api") {
      wechatIdentification {
        get {
          parameter('echostr){
            echostr =>
            complete(echostr)
          }
        }
      }
    }
}
