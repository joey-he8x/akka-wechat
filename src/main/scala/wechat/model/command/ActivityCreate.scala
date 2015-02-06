package wechat.model.command

import bz.dao.UserDao
import bz.model.{Activity, User}
import wechat.model.WechatTextMsg
import wechat.model.command.CmdRule.CmdRuleType
import reactivemongo.extensions.dsl.BsonDsl._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by joey on 15/2/1.
 */
case class ActivityCreate(ac:Activity,user:User) extends ValidCmd

object ActivityCreateExportor extends CmdRule("ActivityCreate","""^ac (\w+)""",CmdRuleType.RegExpr){

  def unapply(input:WechatTextMsg): Option[Future[ActivityCreate]]={
    val re = pattern.r
    input.content.trim match {
      case re(name) =>
        val ac = new Activity
        Some(
          UserDao.findRandom("openId" $eq input.fromUserName) map {
            case user:User => ActivityCreate(ac,user)
          }
        )
      case _ => None
    }
  }
}