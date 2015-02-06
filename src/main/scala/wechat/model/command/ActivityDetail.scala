package wechat.model.command

import bz.dao.UserDao
import bz.model.User
import wechat.model.WechatTextMsg
import wechat.model.command.CmdRule.CmdRuleType
import reactivemongo.extensions.dsl.BsonDsl._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


/**
 * Created by joey on 15-1-30.
 */
case class ActivityDetail(activityId:Int,user:User) extends ValidCmd

object ActivityDetail extends CmdRule("ActivityDetail","""^ad (\d+)""",CmdRuleType.RegExpr){

  def unapply(input:WechatTextMsg): Option[Future[ActivityDetail]]={
    val re = pattern.r
    input.content.trim match {
      case re(id) =>
        Some(
          UserDao.findRandom("openId" $eq input.fromUserName) map {
            case user:User => ActivityDetail(id.toInt, user)
          }
        )
      case _ => None
    }
  }
}
