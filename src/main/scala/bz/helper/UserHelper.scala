package bz.helper

import bz.dao.UserDao
import bz.model.User
import org.joda.time.DateTime
import reactivemongo.extensions.dsl.BsonDsl._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by joey on 15-2-13.
 */
trait UserHelper {
  def findUser(appId:Int,openId:String) = {
    UserDao.findRandom("openId" $eq openId).map{
      case Some(u) =>
        u
      case None =>
        val u = User(openId= openId, appId= appId, lastUpdateTime= DateTime.now)
        UserDao.insert(u)
        u
    }
  }
}
