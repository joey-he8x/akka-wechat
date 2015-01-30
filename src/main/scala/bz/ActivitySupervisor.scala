package bz

import akka.actor.Actor
import bz.ActivitySupervisor.ActivityDetailQuery
import wechat.model.User

/**
 * Created by joey on 15-1-30.
 */
class ActivitySupervisor extends Actor{
  def receive = {
    case ActivityDetailQuery(id,user) =>

  }
}

object ActivitySupervisor{
  case class ActivityDetailQuery(id:Int,user:User)
}