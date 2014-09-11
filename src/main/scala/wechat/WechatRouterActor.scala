package wechat

import akka.actor.Actor
import com.github.nscala_time.time.Imports._

/**
 * Created by joey on 14-8-29.
 */
abstract class BaseWechatMsg(ToUserName:String,FromUserName:String,CreateTime:DateTime,MsgType:String)
case class WechatTextMsg(ToUserName:String,FromUserName:String,CreateTime:DateTime,MsgType:String,Content:String,MsgId:Long)
  extends BaseWechatMsg(ToUserName,FromUserName,CreateTime,MsgType)
case class WechatEventMsg(ToUserName:String,FromUserName:String,CreateTime:DateTime,MsgType:String,Event:String,EventKey:String,Ticket:String)
  extends BaseWechatMsg(ToUserName,FromUserName,CreateTime,MsgType)
case class WechatGeoEventMsg(ToUserName:String,FromUserName:String,CreateTime:DateTime,MsgType:String,Event:String,Latitude:Double,Longitude:Double,Precision:Double)
  extends BaseWechatMsg(ToUserName,FromUserName,CreateTime,MsgType)


class WechatRouterActor extends Actor {
  def receive: Receive = {
    case x => sender ! x
  }
}
