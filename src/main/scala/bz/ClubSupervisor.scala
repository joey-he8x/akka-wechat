package bz

import akka.actor.{ActorLogging, Actor}
import bz.dao.ClubDao
import bz.model.{Club, User}
import scala.util.{Success,Failure}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by joey on 15-2-6.
 */
class ClubSupervisor extends Actor with ActorLogging{
  def receive = {
    case ClubSupervisor.ClubCreateEvent(cb,user) =>
      log.info(s"receive ClubCreateEvent: $cb,$user")
      ClubDao.insert(cb).onComplete {
        case Failure(e) =>
          log.error("Fail to insert Club",e.getMessage)
          sender ! "创建Club失败"
        case Success(lastError) =>
          sender ! s"成功创建Club:$cb"
      }
  }
}

object ClubSupervisor{
  case class ClubCreateEvent(club:Club,user: User)
}