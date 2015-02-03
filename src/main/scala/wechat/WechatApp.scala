package wechat

import akka.actor.{Actor, Props}
import bz.ActivitySupervisor.ActivityDetailQuery
import wechat.model.WechatTextMsg
import wechat.model.command.{ActivityCreateExportor, ActivityDetail, QueryMyActivity}

/**
 * Created by joey on 14-9-18.
 */
class WechatApp(id: String) extends Actor{
  val activitySupervisor = context.actorSelection("/user/activitySupervisor")
  def receive:Receive = {
    case textMsg:WechatTextMsg =>
      textMsg match {
        case ActivityDetail(cmd) =>
          activitySupervisor forward ActivityDetailQuery(cmd.activityId,cmd.user)
//        case ActivityCreateExportor(cmd) =>
//          activitySupervisor
        case QueryMyActivity(cmd) => cmd

      }

  }

//  def exec:PartialFunction[WechatTextMsg,Any] = {
//    case ActivityDetail(cmd:ActivityDetail) =>
//      println(u)
//      cmd
//    case QueryMyActivity(cmd:QueryMyActivity) => cmd
//  }

}

object WechatApp{
  def props(appId: Int): Props = Props(classOf[WechatApp],appId)
}