package com.example.prime_streamer_service

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success
import com.example.prime_streamer.PrimeStreamerHandler

object PrimeStreamerServer {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem[Nothing](Behaviors.empty, "PrimeStreamerServer")

    new PrimeStreamerServer(system).run()
  }
}

class PrimeStreamerServer(system: ActorSystem[_]) {
  def run(): Future[Http.ServerBinding] = {
    implicit val sys: ActorSystem[_] = system
    implicit val ec: ExecutionContext = system.executionContext

    val bound: Future[Http.ServerBinding] = Http(system)
      .newServerAt("localhost", 8081)
      .bind(PrimeStreamerHandler(new PrimeStreamerImpl(system)))

    bound.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("prime-streamer-service gRPC server online at {}:{}", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind gRPC endpoint, terminating system", ex)
        system.terminate()
    }

    bound
  }
}
