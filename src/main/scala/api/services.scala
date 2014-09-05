package api

import akka.actor.{Actor, ActorLogging}
import shapeless.{:: => concat}
import spray.http.StatusCodes._
import spray.http._
import spray.routing._
import spray.util.LoggingContext

import scala.util.control.NonFatal

/**
 * Holds potential error response with the HTTP status and optional body
 *
 * @param responseStatus the status code
 * @param response the optional body
 */
case class ErrorResponseException(responseStatus: StatusCode, response: Option[HttpEntity]) extends Exception

/**
 * Allows you to construct Spray ``HttpService`` from a concatenation of routes; and wires in the error handler.
 * It also logs all internal server errors using ``SprayActorLogging``.
 *
 * @param route the (concatenated) route
 */
class RoutedHttpService(route: Route) extends Actor with HttpService with ActorLogging {

  def actorRefFactory = context

  implicit val handler = ExceptionHandler {
    case NonFatal(ErrorResponseException(statusCode, entity)) => ctx =>
      ctx.complete((statusCode, entity))

    case NonFatal(e) => ctx => {
      log.error(e, InternalServerError.defaultMessage)
      ctx.complete(InternalServerError)
    }
  }

  val myWechatRejectionHandler = RejectionHandler{
    case WechatAuth.WechatAuthFailRejection(list) :: _ =>
      list match {
        case (signature , timestamp , nonce , echostr) => log.error("wechat signature invalid: signature:{},timestamp:{},nonce:{},echostr:{}",signature,timestamp,nonce,echostr)
      }
      complete(Unauthorized, "Signature not match")

  }
  def rejectionHandler: RejectionHandler = myWechatRejectionHandler orElse RejectionHandler.Default


  def receive: Receive =
    runRoute(route)(handler, rejectionHandler, context, RoutingSettings.default, LoggingContext.fromActorRefFactory)


}

