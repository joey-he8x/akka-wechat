package wechat

import akka.actor.{Props, Actor}
import wechat.model.BaseWechatMsg

/**
 * Created by joey on 14-9-18.
 */
case class WechatApp(id: String) extends Actor{
  def receive:Receive = {
    case msg:BaseWechatMsg => sender ! msg
  }

}

object WechatApp{
  def props(appId: Int): Props = Props(classOf[WechatApp],appId)
}