package wechat.model.command

import wechat.model.command.CmdRule.CmdRuleType
import wechat.model.command.CmdRule.CmdRuleType.RuleType


/**
 * Created by joey on 15-1-30.
 * 1) 从数据库中读取rule并创建对象
 */
abstract class CmdRule(val name:String,val pattern:String,val ruleType: RuleType){
  val args:List[Any] = List.empty

  def isMatch(input:String):Boolean = {
    if(ruleType == CmdRuleType.PlainText){
      input.trim == pattern
    }else if(ruleType == CmdRuleType.RegExpr){
      pattern.r.findFirstMatchIn(input).isEmpty
    }else{
      false
    }
  }
}
abstract class ValidCmd(){
}



object CmdRule{
  //定义规则的枚举类型
  object CmdRuleType extends Enumeration{
    type RuleType = Value
    val PlainText,RegExpr = Value
  }


}