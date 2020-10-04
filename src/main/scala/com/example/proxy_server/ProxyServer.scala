package com.example.proxy_server

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object ProxyServer {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem[Nothing](Behaviors.empty, "ProxyServer")

    new ProxyServer(system).run()
  }
}

class ProxyServer(system: ActorSystem[_]) {
  def run(): Future[Http.ServerBinding] = {
    implicit val sys: ActorSystem[_] = system
    implicit val ec: ExecutionContext = system.executionContext

    val bound: Future[Http.ServerBinding] = Http(system)
      .newServerAt("localhost", 8080)
      .bind(new RouteHandler()(system).routes)

    bound.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("proxy-server HTTP service online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }

    bound
  }
}
