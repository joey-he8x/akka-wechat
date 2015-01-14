package wechat.model

import akka.actor.Actor

/**
 * Created by joey on 15-1-13.
 */
trait CmdParser {
  def exec:PartialFunction[String,Any]
}
