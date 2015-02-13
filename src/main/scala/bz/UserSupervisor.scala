package bz

import akka.actor.Actor
import bz.dao.UserDao
import bz.model.User
import org.joda.time.DateTime
import reactivemongo.extensions.dsl.BsonDsl._
import scala.concurrent.ExecutionContext.Implicits.global
import wechat.model.ValidWechatMsg

/**
 * Created by joey on 15-2-13.
 */
class UserSupervisor extends Actor{
  def receive = {
    case UserSupervisor.WechatUserInteractEvent(appId,msg) =>
      UserDao.findRandom("openId" $eq msg.fromUserName).map{
        case Some(u) =>
          u
        case None =>
          val u = User(openId= msg.fromUserName, appId= appId, lastUpdateTime= DateTime.now)
          UserDao.insert(u)
          u
      }
  }
}

object UserSupervisor{
  case class WechatUserInteractEvent(appId:Int,msg: ValidWechatMsg)
}
