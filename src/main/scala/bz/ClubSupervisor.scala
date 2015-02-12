package bz

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import bz.dao.ClubDao
import bz.model.{Club, User}
import wechat.model.{WechatTextMsg, WechatTextResponse}

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by joey on 15-2-6.
 */
class ClubSupervisor extends Actor with ActorLogging{
  def receive = {
    case ClubSupervisor.ClubCreateEvent(msg,cb,user) =>
      implicit val m = msg
      log.info(s"receive ClubCreateEvent: $cb,$user")
      val promise = ClubDao.insert(cb).map{
        e =>
          if (e.ok){
            log.error(s"成功创建Club:$cb")
            new WechatTextResponse(s"成功创建Club:$cb")
          }else{
            log.error("Fail to insert Club",e.getMessage)
            new WechatTextResponse(e.errMsg.get)
          }
      }
      promise pipeTo sender
//      promise.onComplete {
//        case Failure(e) =>
//          log.error("Fail to insert Club",e.getMessage)
//          boundSender ! new WechatTextResponse("创建Club失败")
//        case Success(lastError) =>
//          boundSender ! new WechatTextResponse(s"成功创建Club:$cb")
//      }
  }
}

object ClubSupervisor{
  case class ClubCreateEvent(msg:WechatTextMsg,club:Club,user: User)
}