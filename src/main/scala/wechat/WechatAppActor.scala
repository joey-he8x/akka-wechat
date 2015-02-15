package wechat

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern._
import bz.ActivitySupervisor.ActivityDetailQuery
import bz.ClubSupervisor
import bz.helper.UserHelper
import wechat.model._
import wechat.model.command.{ActivityDetail, ClubCreateExportor}

import scala.concurrent.ExecutionContext.Implicits.global


/**
 * Created by joey on 14-9-18.
 */
class WechatAppActor(id: Int, activitySupervisor: ActorRef, clubSupervisor: ActorRef) extends Actor with ActorLogging
with UserHelper {
  //  def activitySupervisor = context.actorSelection("/user/activitySupervisor")
  //  def clubSupervisor = context.actorSelection("/user/clubSupervisor")
  def receive: Receive = {
    case validMsg: ValidWechatMsg =>
      val fUser = findUser(id, validMsg.fromUserName)
      validMsg match {
        case textMsg: WechatTextMsg =>
          implicit val ori = textMsg
          textMsg match {
            case ActivityDetail(futureCmd) =>
              log.info("recognize ActivityDetail")
              futureCmd onSuccess {
                case cmd => activitySupervisor forward ActivityDetailQuery(cmd.activityId, cmd.user)
              }
            case ClubCreateExportor(clubCreate) =>
              log.info("recognize ClubCreate")
              clubSupervisor forward ClubSupervisor.ClubCreateEvent(textMsg, clubCreate.name, fUser)
            case _ => sender ! new WechatTextResponse("not recognized")

          }
        case e: WechatEventMsg if e.event == "subscribe" =>
          log.info("recognize subscribe event")
          newSubscribeUser(id,e).map(
            lasterror=>
              if(lasterror.ok){
                log.info("subscribe success")
                new WechatTextResponse("欢迎使用小伙伴服务")
              }else{
                log.error("Fail to insert User", lasterror.message)
                new WechatEmptyResponse
              }
          ) pipeTo sender
      }
  }
}

object WechatAppActor {
  def props(appId: Int, activitySupervisor: ActorRef, clubSupervisor: ActorRef): Props = Props(classOf[WechatAppActor], appId, activitySupervisor, clubSupervisor)
}