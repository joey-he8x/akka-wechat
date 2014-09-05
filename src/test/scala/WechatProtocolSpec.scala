import api.WechatAuth
import org.specs2.mutable.Specification
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest


class WechatProtocolTest extends Specification with Specs2RouteTest with HttpService with WechatAuth{
  def actorRefFactory = system // connect the DSL to the test ActorSystem

  val smallRoute =
    get {
      pathSingleSlash {
        wechatIdentification{
          complete {
            <html>
              <body>
                <h1>Say hello to <i>spray</i>!</h1>
              </body>
            </html>
          }
        }
      } ~
        path("ping") {
          complete("PONG!")
        }
    }
  "The service" should {
    "return ok for signature pass" in {
      Get("/?signature=0f02c83641a0cbd46c17eac46d5ed2d7f26d307d&echostr=4598100252485630911&timestamp=1409817535&nonce=457762123") ~>
        wechatIdentification(complete("ok")) ~> check {
        responseAs[String] === "ok"
      }
    }
    "return fail for signature fail" in {
      Get("/?signature=0f02c83611a0cbd46c17eac46d5ed2d7f26d307d&echostr=4598100252485630911&timestamp=1409817535&nonce=457762123") ~>
        wechatIdentification(complete("ok")) ~> check {
        handled must beFalse
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
