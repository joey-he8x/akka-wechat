package wechat.model.command

import bz.dao.UserDao
import bz.model.{Club, User}
import reactivemongo.extensions.dsl.BsonDsl._
import wechat.model.WechatTextMsg
import wechat.model.command.CmdRule.CmdRuleType

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by joey on 15/2/1.
 */
case class ClubCreate(cb:Club,user:User) extends ValidCmd

object ClubCreateExportor extends CmdRule("ClubCreate","""^cc (\w+)""",CmdRuleType.RegExpr){

  def unapply(input:WechatTextMsg): Option[Future[ClubCreate]]={
    val re = pattern.r
    input.content.trim match {
      case re(name) =>
        Some(
          UserDao.findRandom("openId" $eq input.fromUserName) map {
            case user:User =>
              val cb = new Club(name=name,creatorId = user._id,limit=50)
              ClubCreate(cb,user)
          }
        )
      case _ => None
    }
  }
}