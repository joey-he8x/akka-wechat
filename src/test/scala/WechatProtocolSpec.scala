import akka.actor.Props
import api.{RoutedHttpService, WechatApi, WechatAuth}
import core.{Core, CoreActors}
import org.specs2.mutable.Specification
import spray.http.HttpHeaders.RawHeader
import spray.http._
import spray.http.MediaTypes._
import spray.http.HttpCharsets._
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class WechatProtocolTest extends Specification with Specs2RouteTest with HttpService {
  def actorRefFactory = system // connect the DSL to the test ActorSystem

  object RouteContainer extends Core with CoreActors with WechatApi{
    Await.ready(WechatAuth.load, Duration.Inf)
    val rootService = actorRefFactory.actorOf(Props(new RoutedHttpService(routes)))
  }

  "The service" should {
    "return ok for signature pass" in {
      Get("/wx-api/1?signature=0f02c83641a0cbd46c17eac46d5ed2d7f26d307d&echostr=4598100252485630911&timestamp=1409817535&nonce=457762123") ~>
        RouteContainer.routes ~> check {
        responseAs[String] === "4598100252485630911"
      }
    }
    "return fail for signature fail" in {
      Get("/wx-api/1?signature=0f02c83611a0cbd46c17eac46d5ed2d7f26d307d&echostr=4598100252485630911&timestamp=1409817535&nonce=457762123") ~>
        RouteContainer.routes ~> check {
        handled must beFalse
      }
    }
    "return valid xml" in {
      HttpRequest(
        method = HttpMethods.POST,
        uri = "/wx-api/1?signature=0f02c83641a0cbd46c17eac46d5ed2d7f26d307d&timestamp=1409817535&nonce=457762123",
        entity = HttpEntity(MediaTypes.`text/xml`,
          """<xml><ToUserName><![CDATA[gh_b8159b34bcd2]]></ToUserName>
            |<FromUserName><![CDATA[o9XkzuBBGMtnaM4zAJhKnQjMlWhE]]></FromUserName>
            |<CreateTime>1410493716</CreateTime>
            |<MsgType><![CDATA[text]]></MsgType>
            |<Content><![CDATA[hello中文]]></Content>
            |<MsgId>6058024381633874779</MsgId>
            |</xml>"""),
        headers = List(RawHeader("Content-Type","text/xml"))) ~> RouteContainer.routes ~> check {
        responseAs[String].contains("<![CDATA[not recognized]]>") must beTrue
      }
    }
    "return valid xml when create cc" in {
      Post("/wx-api/1?signature=0f02c83641a0cbd46c17eac46d5ed2d7f26d307d&timestamp=1409817535&nonce=457762123",
        HttpEntity(ContentType(`text/xml`, `UTF-8`),
          """<?xml version="1.0" encoding="UTF-8"?><xml><ToUserName><![CDATA[gh_b8159b34bcd2]]></ToUserName>
            |<FromUserName><![CDATA[o9XkzuBBGMtnaM4zAJhKnQjMlWhE]]></FromUserName>
            |<CreateTime>1410493716</CreateTime>
            |<MsgType><![CDATA[text]]></MsgType>
            |<Content><![CDATA[cc abcd中文]]></Content>
            |<MsgId>6058024381633874779</MsgId>
            |</xml>""")) ~> RouteContainer.routes ~> check {
        println(responseAs[String])
        responseAs[String].contains("成功创建Club") must beTrue
      }
    }


  }
}
