package wechat

import akka.actor.{Actor, Props}
import bz.ActivitySupervisor.ActivityDetailQuery
import wechat.model.WechatTextMsg
import wechat.model.command.{ActivityDetail, ClubCreateExportor}

import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by joey on 14-9-18.
 */
class WechatAppActor(id: Int) extends Actor{
  def activitySupervisor = context.actorSelection("/activitySupervisor")
  def receive:Receive = {
    case textMsg:WechatTextMsg =>
      textMsg match {
        case ActivityDetail(futureCmd) =>
          futureCmd onSuccess {
            case cmd => activitySupervisor forward ActivityDetailQuery(cmd.activityId,cmd.user)
          }
        case ClubCreateExportor(fClubCreate) =>
          fClubCreate onSuccess {
            case cmd =>
          }

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
  def props(appId: Int): Props = Props(classOf[WechatAppActor],appId)
}