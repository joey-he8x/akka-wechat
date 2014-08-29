package api

import akka.actor.ActorRef
import akka.util.Timeout
import spray.routing.Directives


import scala.concurrent.{Await, ExecutionContext}

/**
 * Created by joey on 14-8-27.
 */
class EchoService(echo: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats {

  import akka.pattern.ask
  import scala.concurrent.duration._
  implicit val timeout = Timeout(2.seconds)

  val route =
    path("echo") {
      get {
        handleWith { body: String =>
          println(body)
          val ret = (echo ? body).mapTo[String]
          Await.result[String](ret,1 seconds)
        }
      }
    }
}
