package wechat.model.command

import wechat.model.WechatTextMsg
import wechat.model.command.CmdRule.CmdRuleType

/**
 * Created by joey on 15/2/1.
 */
case class ClubCreate(name:String) extends ValidCmd

object ClubCreateExportor extends CmdRule("ClubCreate","""^cc\s+(.+)""",CmdRuleType.RegExpr){

  def unapply(input:WechatTextMsg): Option[ClubCreate]={
    val re = pattern.r
    input.content.trim match {
      case re(name) =>
        Some(ClubCreate(name))
      case _ => None
    }
  }
}