package com.example.proxy_server

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}

class RouteHandler()(implicit val system: ActorSystem[_]) {
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
      complete("Here go the primes")
    }
  }
}
