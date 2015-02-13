package bz

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import bz.dao.ClubDao
import bz.helper.ClubHelper
import bz.model.{Club, User}
import wechat.model.{WechatTextMsg, WechatTextResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

/**
 * Created by joey on 15-2-6.
 */
class ClubSupervisor extends Actor with ActorLogging with ClubHelper{
  def receive = {
    case ClubSupervisor.ClubCreateEvent(msg,name,fUser) =>
      implicit val m = msg
      log.info(s"receive ClubCreateEvent: $name")
      checkUserLimit(msg.fromUserName).flatMap(can =>
        if (can)
          (for{
            user <- fUser
            cb = new Club(name=name,creatorId = user.openId,limit=50)
            res <- ClubDao.insert(cb)
          } yield res).map{
            e =>
              if (e.ok){
                log.error(s"成功创建Club:$name")
                new WechatTextResponse(s"成功创建Club:$name")
              }else{
                log.error("Fail to insert Club",e.getMessage)
                new WechatTextResponse(e.errMsg.get)
              }
          }
        else{
          Future {
            log.error("Fail to insert Club, reach limit per user")
            new WechatTextResponse("创建社团已达上限，创建失败")
          }
        }
      ) pipeTo sender
  }
}

object ClubSupervisor{
  case class ClubCreateEvent(msg:WechatTextMsg,name:String,fUser: Future[User])
}