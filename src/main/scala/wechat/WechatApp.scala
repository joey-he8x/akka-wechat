package wechat

import akka.actor.{Actor, Props}
import bz.ActivitySupervisor.ActivityDetailQuery
import wechat.model.WechatTextMsg
import wechat.model.command.{ActivityDetail, QueryMyActivity}

/**
 * Created by joey on 14-9-18.
 */
class WechatApp(id: String) extends Actor{
  def receive:Receive = {
    case textMsg:WechatTextMsg =>
      textMsg match {
        case ActivityDetail(cmd) =>
          context.actorSelection("/user/activitySupervisor") forward ActivityDetailQuery(cmd.activityId,cmd.user)
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