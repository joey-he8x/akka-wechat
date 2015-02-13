package wechat

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import bz.ActivitySupervisor.ActivityDetailQuery
import bz.ClubSupervisor
import bz.dao.UserDao
import bz.helper.UserHelper
import bz.model.User
import org.joda.time.DateTime
import wechat.model.command.{ActivityDetail, ClubCreateExportor}
import wechat.model.{ValidWechatMsg, WechatEventMsg, WechatTextMsg, WechatTextResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}


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
          val u = User(openId = e.fromUserName, appId = id, subscribeTime = Some(new DateTime()), lastUpdateTime = new DateTime)
          UserDao.insert(u).onComplete {
            case Failure(e) =>
              log.error("Fail to insert User", e.getMessage)
            case Success(lastError) =>
              log.info("subscribe success")
              sender ! "ok"
          }
      }
  }
}

object WechatAppActor {
  def props(appId: Int, activitySupervisor: ActorRef, clubSupervisor: ActorRef): Props = Props(classOf[WechatAppActor], appId, activitySupervisor, clubSupervisor)
}