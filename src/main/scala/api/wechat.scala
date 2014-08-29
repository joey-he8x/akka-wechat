package api

import core.{Core, CoreActors}
import spray.routing.HttpService

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by joeyhe on 14-8-26.
 */
trait WechatApi extends HttpService with CoreActors with Core {

//  protected implicit val system : ActorSystem
  val routes =
    new EchoService(echo).route
//  val routes =
//    new RegistrationService(registration).route ~
//      new MessengerService(messenger).route ~
//      new FruitService(fruit).fruitroute


}