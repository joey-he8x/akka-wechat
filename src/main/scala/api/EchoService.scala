package api

import akka.actor.ActorRef
import akka.util.Timeout
import spray.routing.Directives

import scala.concurrent.ExecutionContext

/**
 * Created by joey on 14-8-27.
 */
class EchoService(echo: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with WechatAuth  with DefaultJsonFormats {

  import scala.concurrent.duration._
  implicit val timeout = Timeout(2.seconds)

  //implicit val httpRequestFormat = jsonFormat5(HttpRequest)

  val route =
    path("echo-post-parameter") {
      post {
        parameter('color) { color =>
            println(color)
            complete(color)
        }
      }
    } ~
    path("echo-get-parameter") {
      get {
        parameter('color){ color =>
          complete(color)
        }
      }
    } ~
    path("echo") { ctx =>
      ctx.complete(ctx.request.toString())
    } ~
    path("wechat") {
      wechatIdentification{ ctx =>
        ctx.complete(ctx.request.toString())
      }
    }
}
