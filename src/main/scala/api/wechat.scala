package api

import core.{Core, CoreActors}

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by joeyhe on 14-8-26.
 */
trait WechatApi extends CoreActors with Core {

  val routes =
    new WechatService(wechat).route
//  val routes =
//    new RegistrationService(registration).route ~
//      new MessengerService(messenger).route ~
//      new FruitService(fruit).fruitroute
}
