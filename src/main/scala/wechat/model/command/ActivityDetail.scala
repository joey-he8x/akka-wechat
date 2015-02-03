package wechat.model.command

import bz.model.User
import wechat.model.WechatTextMsg
import wechat.model.command.CmdRule.CmdRuleType


/**
 * Created by joey on 15-1-30.
 */
case class ActivityDetail(activityId:Int,user:User) extends ValidCmd

object ActivityDetail extends CmdRule("ActivityDetail","""^ad (\d+)""",CmdRuleType.RegExpr){

  def unapply(input:WechatTextMsg): Option[ActivityDetail]={
    val re = pattern.r
    input.content.trim match {
      case re(id) =>
        val u = User(null,null)
        Some(ActivityDetail(id.toInt,u))
      case _ => None
    }
  }
}
