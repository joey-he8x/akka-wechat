package web

import api.{WechatApi, RoutedHttpService}
import core.{Core, CoreActors}
import com.typesafe.config.ConfigFactory
import akka.io.IO
import akka.actor.{Props}
import spray.can.Http

//object Rest extends App with BootedCore
object Rest extends App with Core with CoreActors with WechatApi{
	val config = ConfigFactory.load()
	val host = config.getString("http.host")
	val port = config.getInt("http.port")
	val rootService = system.actorOf(Props(new RoutedHttpService(routes)))

    IO(Http)(system) ! Http.Bind(rootService, interface = host, port = port)
}