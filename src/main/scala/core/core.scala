package core

import akka.actor.{ActorSystem, Props}
import bz.{ClubSupervisor, ActivitySupervisor}
import wechat.{EchoActor, WechatAppSupervisor}

trait Core {

  /**
   * Construct the ActorSystem we will use in our application
   */
  implicit lazy val system = ActorSystem("akka-spray")

  /**
   * Ensure that the constructed ActorSystem is shut down when the JVM shuts down
   */
  sys.addShutdownHook(system.shutdown())

}


/**
 * This trait contains the actors that make up our application; it can be mixed in with
 * ``BootedCore`` for running code or ``TestKit`` for unit and integration tests.
 */
trait CoreActors {
  this: Core =>
    val echo = system.actorOf(Props[EchoActor])
    val activitySupervisor = system.actorOf(Props[ActivitySupervisor],"activitySupervisor")
    val clubSupervisor = system.actorOf(Props[ClubSupervisor],"clubSupervisor")
    val wechat = system.actorOf(WechatAppSupervisor.props(activitySupervisor,clubSupervisor),"wechatAppSupervisor")

  //  val registration = system.actorOf(Props[RegistrationActor])
//  val messenger    = system.actorOf(Props[MessengerActor])

}