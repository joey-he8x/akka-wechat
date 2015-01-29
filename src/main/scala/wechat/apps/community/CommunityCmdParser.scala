package wechat.apps.community

import akka.actor.Actor
import wechat.model.CmdParser

/**
 * Created by joey on 15-1-13.
 */
trait CommunityCmdParser extends CmdParser{
  self: Actor =>
  def exec = {
    case _ => {
      context.actorOf(h)
    }
  }
}
