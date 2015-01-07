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

  def wechatIdentification(app_id:Int): Directive0 = {
    val wechatSignParameters: Directive[String :: Long :: String :: HNil] =
      parameters('signature,'timestamp.as[Long],'nonce.as[String])
    val token = WechatAuth.tokenList.get(app_id)
    wechatSignParameters.hflatMap[HNil] {
      case signature :: timestamp :: nonce :: HNil => {
        if(token == None){
          return reject(new WechatAuth.WechatAuthFailRejection(signature::timestamp::nonce::HNil))
        }
        val tmpStr = (token.get :: timestamp.toString :: nonce :: Nil)
          .sortWith(_.compareTo(_) < 0).mkString
        val encListByte = Sha1Digest.hashMessage(new Sha1Digest.Bytes(tmpStr.getBytes.toList))
        if (Sha1Digest.listToByte(encListByte).toString == signature)
          pass
        else
//          reject(new WechatAuth.WechatAuthFailRejection((signature,timestamp,nonce)))
          reject(new WechatAuth.WechatAuthFailRejection(signature::timestamp::nonce::HNil))
      }
    }
  }
}
object WechatAuth{
  type WechatAuthChain = String :: Long :: String :: HNil
//  case class WechatAuthFailRejection(parameters:Tuple3[String,Long,String]) extends Rejection
  case class WechatAuthFailRejection(parameters: WechatAuthChain) extends Rejection
  private val token = "mJB53wkOCSrpQWklPOyLj1B8JcKh70Cn"
  private val tokenList = Map(1 -> "mJB53wkOCSrpQWklPOyLj1B8JcKh70Cn")
}