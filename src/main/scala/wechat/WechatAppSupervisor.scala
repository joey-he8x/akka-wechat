package wechat

import akka.actor.{Props, ActorRef, ActorLogging, Actor}
import wechat.model.WechatMsg

/**
 * Created by joey on 14-8-29.
 * features:
 * 1) 负责查找WechatApp并转发消息
 * 2）负责supervisor所有wechatApp，创建他们，并处理异常
 */

class WechatAppSupervisor(activitySupervisor: ActorRef,clubSupervisor: ActorRef) extends Actor with ActorLogging{

  def receive: Receive = {
    case x:WechatMsg =>
      context.child(x.appId.toString) match {
        case Some(app) => app forward x.msg
        case None =>
          log.info(s"create wechatapp actor[$x.appId]")
          val appProp = WechatAppActor.props(x.appId,activitySupervisor,clubSupervisor)
          val app = context.actorOf(appProp,x.appId.toString)
          log.info("forward message")
          app forward x.msg
      }
  }
}
object WechatAppSupervisor{
  def props(activitySupervisor: ActorRef,clubSupervisor: ActorRef): Props = Props(classOf[WechatAppSupervisor],activitySupervisor,clubSupervisor)
}