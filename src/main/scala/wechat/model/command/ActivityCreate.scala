package wechat.model.command

import bz.model.{Activity, User}
import wechat.model.WechatTextMsg
import wechat.model.command.CmdRule.CmdRuleType

/**
 * Created by joey on 15/2/1.
 */
case class ActivityCreate(ac:Activity,user:User) extends ValidCmd

object ActivityCreateExportor extends CmdRule("ActivityDetail","""^ac (\w+)""",CmdRuleType.RegExpr){

  def unapply(input:WechatTextMsg): Option[ActivityCreate]={
    val re = pattern.r
    input.content.trim match {
      case re(id) =>
        val u = User(null,null)
        val ac = new Activity
        Some(ActivityCreate(ac,u))
      case _ => None
    }
  }
}