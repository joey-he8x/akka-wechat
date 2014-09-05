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
  extends Directives with WechatAuth with DefaultJsonFormats {

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
        } ~
        post{
              entity(as[NodeSeq]) {
                xml =>
                        complete(xml.toString())
//                  ctx =>
//                    println(ctx.request.toString)
//                    ctx.complete(ctx.request.toString)
              }
        }
      }
    }
}
