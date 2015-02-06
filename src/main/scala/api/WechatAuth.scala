/**
 * Created by joey on 14-9-2.
 */
package api

import api.util.Sha1Digest
import bz.dao.WechatAppDao
import bz.model.WechatApp
import org.slf4j.LoggerFactory
import shapeless._
import spray.routing._
import spray.routing.directives.{RouteDirectives, ParameterDirectives, BasicDirectives}

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global

trait WechatAuth{
  import BasicDirectives._
  import ParameterDirectives._
  import RouteDirectives._

  def wechatIdentification(app_id:Int): Directive0 = {
    val wechatSignParameters: Directive[String :: Long :: String :: HNil] =
      parameters('signature,'timestamp.as[Long],'nonce.as[String])
    val token = WechatAuth.tokenList(app_id)
    wechatSignParameters.hflatMap[HNil] {
      case signature :: timestamp :: nonce :: HNil => {
        if(token == None){
          return reject(new WechatAuth.WechatAuthFailRejection(signature::timestamp::nonce::HNil))
        }
        val tmpStr = (token :: timestamp.toString :: nonce :: Nil)
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
  val logger = LoggerFactory.getLogger(classOf[WechatAuth])
  type WechatAuthChain = String :: Long :: String :: HNil
  case class WechatAuthFailRejection(parameters: WechatAuthChain) extends Rejection
  //private val tokenList = Map(1 -> "mJB53wkOCSrpQWklPOyLj1B8JcKh70Cn")
  var tokenList:Map[Int,String] = Map.empty
  def load: Unit = {
    WechatAppDao.findAll().map({
      case apps:List[WechatApp] =>
        val tmpList = mutable.Map.empty[Int,String]
        apps.foreach(app => tmpList(app._id)=app.secret)
        tokenList = tmpList.toMap
    }).onFailure({case e=> logger.error(e.getMessage)})
  }
}