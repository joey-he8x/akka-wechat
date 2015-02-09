package wechat.model

import com.github.nscala_time.time.Imports._

import scala.xml.NodeSeq

/**
 * Created by joey on 15-1-13.
 */
case class WechatMsg(appId:Int,msg: BaseWechatMsg){
}
object WechatMsgFormator{
  val toWechatMsg:PartialFunction[NodeSeq,BaseWechatMsg] ={
    case x:NodeSeq if (x \ "MsgType").text == "text" => {
      val toUsername = (x \ "ToUserName").text
      val fromUserName = (x \ "FromUserName").text
      val createTime = new DateTime((x \ "CreateTime").text.toLong * 1000)
      val msgType = (x \ "MsgType").text
      val content = (x \ "Content").text
      val msgId = (x \ "MsgId").text.toLong
      WechatTextMsg(toUsername,fromUserName,createTime,msgType,content,msgId)
    }
    case x:NodeSeq if (x \ "MsgType").text == "event" => {
      val toUsername = (x \ "ToUserName").text
      val fromUserName = (x \ "FromUserName").text
      val createTime = new DateTime((x \ "CreateTime").text.toLong * 1000)
      val msgType = (x \ "MsgType").text
      val content = (x \ "Content").text
      val event = (x \ "Event").text
      val eventKey = (x \ "EventKey").text
      val ticket = (x \ "Ticket").text
      WechatEventMsg(toUsername,fromUserName,createTime,msgType,event,eventKey,ticket)
    }
    case x:NodeSeq if (x \ "MsgType").text == "event" && (x \ "Latitude").nonEmpty => {
      val toUsername = (x \ "ToUserName").text
      val fromUserName = (x \ "FromUserName").text
      val createTime = new DateTime((x \ "CreateTime").text.toLong * 1000)
      val msgType = (x \ "MsgType").text
      val content = (x \ "Content").text
      val event = (x \ "Event").text
      val latitude = (x \ "Latitude").text.toDouble
      val longitude = (x \ "Longitude").text.toDouble
      val precision = (x \ "Precision").text.toDouble
      WechatGeoEventMsg(toUsername,fromUserName,createTime,msgType,event,latitude,longitude,precision)
    }
    case x:NodeSeq => UnknownMsg(x)
  }

  def apply(data:NodeSeq):BaseWechatMsg = {
    toWechatMsg(data)
  }
}

abstract class BaseWechatMsg
case class WechatTextMsg(toUserName:String,fromUserName:String,createTime:DateTime,msgType:String,content:String,msgId:Long)
  extends BaseWechatMsg
case class WechatEventMsg(toUserName:String,fromUserName:String,createTime:DateTime,msgType:String,event:String,eventKey:String,ticket:String)
  extends BaseWechatMsg
case class WechatGeoEventMsg(toUserName:String,fromUserName:String,createTime:DateTime,msgType:String,event:String,latitude:Double,longitude:Double,precision:Double)
  extends BaseWechatMsg()
case class UnknownMsg(raw: NodeSeq)
  extends BaseWechatMsg

abstract class WechatResponse
class WechatTextResponse(toUserName:String,fromUserName:String,createTime:Long,msgType:String,content:String)