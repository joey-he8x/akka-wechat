package wechat.model.command

import bz.dao.UserDao
import bz.model.User
import wechat.model.WechatTextMsg
import reactivemongo.extensions.dsl.BsonDsl._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by joey on 15-1-30.
 */
case class QueryMyActivity(user:User) extends ValidCmd

object QueryMyActivity extends CmdRule("QueryMyActivity","my",CmdRule.CmdRuleType.PlainText){
  def unapply(input:WechatTextMsg): Option[Future[QueryMyActivity]]={
    if(input.content.trim == pattern){
      Some(
        UserDao.findRandom("openId" $eq input.fromUserName) map {
          case user:User => QueryMyActivity(user)
        }
      )
    }else{
      None
    }
  }
}