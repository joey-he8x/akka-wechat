package wechat

import akka.actor.Actor

/**
 * Created by joey on 14-9-18.
 */
case class WechatApp(name: String) extends Actor{
  def receive:Receive = {
    case cmd => sender ! cmd
  }

}
