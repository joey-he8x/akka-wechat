package wechat.model

/**
 * Created by joey on 15-1-13.
 */
abstract trait CmdParser {

  def exec:PartialFunction[WechatTextMsg,Any]
}
