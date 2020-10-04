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

  def listPrimesHandler(maxString: String): StandardRoute = {
    val max = maxString.toIntOption.getOrElse(-1)

    if (max < 0) {
      complete(HttpResponse(StatusCodes.BadRequest, entity = s"Error: max must be a positive int32 (range: 0 - ${Int.MaxValue.toString})"))
    }
    else {
      val byteStringSource = primeStreamerClient.listPrimes(ListPrimesRequest(max))
        .map(r => r.number.toString)
        .map(s => ByteString(s))
        .intersperse(ByteString(","))

      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, byteStringSource))
    }
  }
}
