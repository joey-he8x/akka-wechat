package wechat

import akka.actor.{ActorLogging, Actor, ActorRef, Props}
import bz.ActivitySupervisor.ActivityDetailQuery
import bz.ClubSupervisor
import wechat.model.WechatTextMsg
import wechat.model.command.{ActivityDetail, ClubCreateExportor}

import scala.concurrent.ExecutionContext.Implicits.global
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