/**
 * Created by joey on 14-9-2.
 */
package api

import api.util.Sha1Digest
import shapeless._
import spray.routing._
import spray.routing.directives.{RouteDirectives, ParameterDirectives, BasicDirectives}

trait WechatAuth{
  import BasicDirectives._
  import ParameterDirectives._
  import RouteDirectives._

  def wechatIdentification: Directive0 = {
    val wechatSignParameters: Directive[String :: Long :: String :: HNil] =
      parameters('signature,'timestamp.as[Long],'nonce.as[String])

    wechatSignParameters.hflatMap[HNil] {
      case signature :: timestamp :: nonce :: HNil => {
        val tmpStr = (WechatAuth.token :: timestamp.toString :: nonce :: Nil)
          .sortWith(_.compareTo(_) < 0).mkString
        val encListByte = Sha1Digest.hashMessage(new Sha1Digest.Bytes(tmpStr.getBytes.toList))
        if (Sha1Digest.listToByte(encListByte).toString == signature)
          pass
        else
          reject(new WechatAuth.WechatAuthFailRejection((signature,timestamp,nonce)))
      }
    }
  }
}
object WechatAuth{
  case class WechatAuthFailRejection(parameters:Tuple3[String,Long,String]) extends Rejection
  private val token = "mJB53wkOCSrpQWklPOyLj1B8JcKh70Cn"
}