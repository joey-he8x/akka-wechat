/**
 * Created by joey on 14-9-2.
 */

import api.util.Sha1Digest
import shapeless._
import spray.routing._

trait WechatAuth extends Directives{
  def WechatIdentification: Directive0 = {
    val wechatSignParameters: Directive[String :: Long :: String :: String :: HNil] =
      parameters('signature,'timestamp.as[Long],'nonce.as[String],'echostr.as[String])

    wechatSignParameters.hrequire{
      case signature :: timestamp :: nonce :: echostr :: HNil => {
        val tmpStr = (WechatAuth.token :: timestamp.toString :: nonce :: Nil)
          .sortWith(_.compareTo(_) < 0).mkString

        val encListByte = Sha1Digest.hashMessage(new Sha1Digest.Bytes(tmpStr.getBytes.toList))
        Sha1Digest.listToByte(encListByte).toString == signature
      }
    }



  }
}
object WechatAuth{
  private val token = "mJB53wkOCSrpQWklPOyLj1B8JcKh70Cn"
}