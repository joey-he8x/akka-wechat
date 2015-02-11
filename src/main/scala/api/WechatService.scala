package api

import akka.actor.ActorRef
import akka.util.Timeout
import spray.http.MediaTypes._
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._
import spray.routing.Directives
import wechat.model.{WechatResponse, BaseWechatMsg, WechatMsg, WechatMsgFormator}

import scala.concurrent.ExecutionContext
import scala.xml.NodeSeq

/**
 * Created by joey on 14-8-27.
 */
class WechatService(wechat: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with WechatAuth {

  import akka.pattern.ask

  import scala.concurrent.duration._
  implicit val timeout = Timeout(5.seconds)

  implicit val wechatMsgUnmarshaller =
    Unmarshaller.delegate[NodeSeq,BaseWechatMsg](`text/xml`)(WechatMsgFormator.apply)

  implicit val wechatmsgMarshaller =
    Marshaller.delegate[WechatResponse,NodeSeq](`text/xml`)(_.toXml)


  val route =
    path("wx-api" / IntNumber) { appId =>
      wechatIdentification(appId) {
        get {
          parameter('echostr){
            echostr =>
            complete(echostr)
          }
        } ~
        post{
          handleWith {
            msg:BaseWechatMsg =>
              (wechat ? WechatMsg(appId,msg)).mapTo[WechatResponse]
//              <xml>
//                <ToUserName>{(xml \ "FromUserName").text}</ToUserName>
//                <FromUserName>{(xml \ "ToUserName").text}</FromUserName>
//                <CreateTime>{(new DateTime).toDate.getTime / 1000}</CreateTime>
//                <MsgType><![CDATA[text]]></MsgType>
//                <Content><![CDATA[hello]]></Content>
//              </xml>
            }
        }
      }
    }
}
