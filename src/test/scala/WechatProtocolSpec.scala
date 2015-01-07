import akka.actor.Props
import api.{RoutedHttpService, WechatApi}
import core.{Core, CoreActors}
import org.specs2.mutable.Specification
import spray.http.{HttpEntity, MediaTypes}
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest


class WechatProtocolTest extends Specification with Specs2RouteTest with HttpService {
  def actorRefFactory = system // connect the DSL to the test ActorSystem

  object RouteContainer extends Core with CoreActors with WechatApi{
    val rootService = actorRefFactory.actorOf(Props(new RoutedHttpService(routes)))
  }

  "The service" should {
    "return ok for signature pass" in {
      Get("/wx-api?signature=0f02c83641a0cbd46c17eac46d5ed2d7f26d307d&echostr=4598100252485630911&timestamp=1409817535&nonce=457762123") ~>
        RouteContainer.routes ~> check {
        responseAs[String] === "4598100252485630911"
      }
    }
    "return fail for signature fail" in {
      Get("/wx-api?signature=0f02c83611a0cbd46c17eac46d5ed2d7f26d307d&echostr=4598100252485630911&timestamp=1409817535&nonce=457762123") ~>
        RouteContainer.routes ~> check {
        handled must beFalse
      }
    }
    "return content type" in {
      Post("/wx-api?signature=0f02c83641a0cbd46c17eac46d5ed2d7f26d307d&echostr=4598100252485630911&timestamp=1409817535&nonce=457762123",
      HttpEntity(MediaTypes.`text/xml`,
        """<xml><ToUserName><![CDATA[gh_b8159b34bcd2]]></ToUserName>
          |<FromUserName><![CDATA[o9XkzuBBGMtnaM4zAJhKnQjMlWhE]]></FromUserName>
          |<CreateTime>1410493716</CreateTime>
          |<MsgType><![CDATA[text]]></MsgType>
          |<Content><![CDATA[hello]]></Content>
          |<MsgId>6058024381633874779</MsgId>
          |</xml>""")) ~> RouteContainer.routes ~> check {
        responseAs[String] === "<string>text</string>"
      }
      Post("/wx-api?signature=0f02c83641a0cbd46c17eac46d5ed2d7f26d307d&echostr=4598100252485630911&timestamp=1409817535&nonce=457762123",
        HttpEntity(MediaTypes.`text/xml`,
          """<xml><ToUserName><![CDATA[gh_b8159b34bcd2]]></ToUserName>
            |<FromUserName><![CDATA[o9XkzuBBGMtnaM4zAJhKnQjMlWhE]]></FromUserName>
            |<CreateTime>1410493716</CreateTime>
            |<MsgType><![CDATA[event]]></MsgType>
            |<Content><![CDATA[hello]]></Content>
            |<MsgId>6058024381633874779</MsgId>
            |</xml>""")) ~> RouteContainer.routes ~> check {
        responseAs[String] === "<string>event</string>"
      }
    }


//    "return a greeting for GET requests to the root path" in {
//      Get() ~> smallRoute ~> check {
//        responseAs[String] must contain("Say hello")
//      }
//    }
//
//    "return a 'PONG!' response for GET requests to /ping" in {
//      Get("/ping") ~> smallRoute ~> check {
//        responseAs[String] === "PONG!"
//      }
//    }
//
//    "leave GET requests to other paths unhandled" in {
//      Get("/kermit") ~> smallRoute ~> check {
//        handled must beFalse
//      }
//    }
//
//    "return a MethodNotAllowed error for PUT requests to the root path" in {
//      Put() ~> sealRoute(smallRoute) ~> check {
//        status === MethodNotAllowed
//        responseAs[String] === "HTTP method not allowed, supported methods: GET"
//      }
//    }
  }
}
