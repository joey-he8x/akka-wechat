package bz

import akka.actor.Actor
import bz.model.User

/**
 * Created by joey on 15-1-30.
 */
class ActivitySupervisor extends Actor{
  def receive = {
    case ActivitySupervisor.ActivityDetailQuery(id,user) =>

  }
}

object ActivitySupervisor{
  case class ActivityDetailQuery(id:Int,user:User)
  case class ActivityCreateEvent(name:String,expireDays:Int,user:User)
}