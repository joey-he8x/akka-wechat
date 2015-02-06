package web

import akka.actor.Props
import akka.io.IO
import api.{WechatAuth, RoutedHttpService, WechatApi}
import com.typesafe.config.ConfigFactory
import core.{Core, CoreActors}
import spray.can.Http

//object Rest extends App with BootedCore
object Rest extends App with Core with CoreActors with WechatApi{
	val config = ConfigFactory.load()
	val host = config.getString("http.host")
	val port = config.getInt("http.port")
	val rootService = system.actorOf(Props(new RoutedHttpService(routes)))
  WechatAuth.load

    IO(Http)(system) ! Http.Bind(rootService, interface = host, port = port)
}