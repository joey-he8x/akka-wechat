package wechat.model.command

import wechat.model.{User, WechatTextMsg}

/**
 * Created by joey on 15-1-30.
 */
case class QueryMyActivity(user:User) extends ValidCmd

object QueryMyActivity extends CmdRule("QueryMyActivity","my",CmdRule.CmdRuleType.PlainText){
  def unapply(input:WechatTextMsg): Option[QueryMyActivity]={
    if(input.content.trim == pattern){
      val u = User.find(input.fromUserName)
      Some(QueryMyActivity(u))
    }else{
      None
    }
  }
}