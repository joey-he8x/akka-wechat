package api

import akka.actor.ActorRef
import akka.util.Timeout
import spray.http.HttpCharsets._
import spray.http.MediaTypes._
import spray.http.{ContentType, HttpEntity}
import spray.httpx.marshalling._
import spray.httpx.unmarshalling._
import spray.routing.{Directive0, Directives}
import wechat.model.{BaseWechatMsg, WechatMsg, WechatMsgFormator, WechatResponse}

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

//  val fixDefaultEncoding: Directive0 = mapHttpResponseEntity{
//    entity =>
//      HttpEntity(ContentType(`text/xml`, `UTF-8`),entity.data)
//  }
  val fixDefaultEncoding2:Directive0 = mapRequest{
    request =>
      request.withEntity(HttpEntity(ContentType(`text/xml`, `UTF-8`),request.entity.data.asString(`UTF-8`)))
  }
  val fixDefaultEncoding:Directive0 = optionalHeaderValueByName("Content-Type").flatMap {
    header =>
      if (!header.contains("charset"))
      mapHttpResponseEntity{
        entity =>
          HttpEntity(ContentType(`text/xml`, `UTF-8`),"str")
      }else pass

//      header match {
//        case Some(ctype) if !ctype.contains("charset") =>
//          mapHttpResponseEntity{
//            entity =>
//              HttpEntity(ContentType(`text/xml`, `UTF-8`),entity.data)
//          }
//        case _ => pass
//      }
  }


  val route =
    path("wx-api" / IntNumber) { appId =>
      wechatIdentification(appId) {
        get {
          parameter('echostr){
            echostr =>
            complete(echostr)
          }
        } ~
          fixDefaultEncoding2{
          post {
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
        }}
      }
    }
}
