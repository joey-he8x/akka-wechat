package wechat

import akka.actor.Actor

/**
 * Created by joey on 14-8-29.
 */
class WechatRouterActor extends Actor {
  def receive: Receive = {
    case x => sender ! x
  }
}
