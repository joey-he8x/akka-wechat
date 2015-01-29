package wechat

import akka.actor.Actor
import reactivemongo.api.MongoDriver
import wechat.model.{WechatMsg, WechatTextMsg}

/**
 * Created by joey on 14-8-29.
 * features:
 * 1) 负责查找WechatApp并转发消息
 * 2）负责supervisor所有wechatApp，创建他们，并处理异常
 */

class WechatRouterActor extends Actor {
  val driver = new MongoDriver

  def receive: Receive = {
    case x:WechatMsg => {
      val appProp = WechatApp.props(x.appId)
      val app = context.actorOf(appProp)
      x.msg match {
        case msg:WechatTextMsg => app forward msg
      }
    }
  }
}
