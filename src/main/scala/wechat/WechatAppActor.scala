package wechat

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import bz.ActivitySupervisor.ActivityDetailQuery
import bz.ClubSupervisor
import bz.dao.UserDao
import bz.model.User
import org.joda.time.DateTime
import wechat.model.command.{ActivityDetail, ClubCreateExportor}
import wechat.model.{WechatEventMsg, WechatTextMsg}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
 * Created by joey on 14-9-18.
 */
class WechatAppActor(id: Int,activitySupervisor: ActorRef,clubSupervisor: ActorRef) extends Actor with ActorLogging{
//  def activitySupervisor = context.actorSelection("/user/activitySupervisor")
//  def clubSupervisor = context.actorSelection("/user/clubSupervisor")
  def receive:Receive = {
    case textMsg:WechatTextMsg =>
      textMsg match {
        case ActivityDetail(futureCmd) =>
          log.info("recognize ActivityDetail")
          futureCmd onSuccess {
            case cmd => activitySupervisor forward ActivityDetailQuery(cmd.activityId,cmd.user)
          }
        case ClubCreateExportor(fClubCreate) =>
          log.info("recognize ClubCreate")
          fClubCreate onSuccess {
            case cmd =>
              log.info("forward ClubCreateEvent")
              clubSupervisor forward ClubSupervisor.ClubCreateEvent(cmd.cb,cmd.user)
          }
        case _ => sender ! "该命令不正确"

//        case ActivityCreateExportor(cmd) =>
//          activitySupervisor
//        case QueryMyActivity(cmd) => cmd

      }
    case e:WechatEventMsg if e.event=="subscribe" =>
      log.info("recognize subscribe event")
      val u = User(openId = e.fromUserName,appId = id,subscribeTime = new DateTime(),lastUpdateTime = new DateTime)
      UserDao.insert(u).onComplete {
        case Failure(e) =>
          log.error("Fail to insert User",e.getMessage)
        case Success(lastError) =>
          log.info("subscribe success")
          sender ! "ok"
      }
  }

//  def exec:PartialFunction[WechatTextMsg,Any] = {
//    case ActivityDetail(cmd:ActivityDetail) =>
//      println(u)
//      cmd
//    case QueryMyActivity(cmd:QueryMyActivity) => cmd
//  }

}

object WechatAppActor{
  def props(appId: Int,activitySupervisor: ActorRef,clubSupervisor: ActorRef): Props = Props(classOf[WechatAppActor],appId,activitySupervisor,clubSupervisor)
}