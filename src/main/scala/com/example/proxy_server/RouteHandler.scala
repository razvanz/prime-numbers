package com.example.proxy_server

import akka.actor.typed.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import akka.util.ByteString
import com.example.prime_streamer.{ListPrimesRequest, PrimeStreamerClient}

class RouteHandler()(implicit val system: ActorSystem[_]) {
  val primeStreamerClient: PrimeStreamerClient = PrimeStreamerClient(
    GrpcClientSettings
      .connectToServiceAt("localhost", 8081)
      .withTls(false)
  )

  val routes: Route =
    pathPrefix("primes") {
      path(Segment) { max =>
        get { listPrimesHandler(max) }
      }
    }

  def listPrimesHandler(max: String): StandardRoute = {
    if (!max.forall(_.isDigit) || max.toInt < 2) {
      complete(HttpResponse(StatusCodes.BadRequest, entity = "Error: max must be a number >= 2"))
    }
    else {
      val byteStringSource = primeStreamerClient.listPrimes(ListPrimesRequest(max.toInt))
        .map(r => r.number.toString)
        .map(s => ByteString(s + "\n"))

      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, byteStringSource))
    }
  }
}
