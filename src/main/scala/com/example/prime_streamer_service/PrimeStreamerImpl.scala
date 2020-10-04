package com.example.prime_streamer_service

import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.stream.scaladsl.Source
import com.example.prime_streamer.{PrimeStreamer, ListPrimesRequest, ListPrimesResponse}

class PrimeStreamerImpl(system: ActorSystem[_]) extends PrimeStreamer {
  private implicit val sys: ActorSystem[_] = system

  override def listPrimes(request: ListPrimesRequest): Source[ListPrimesResponse, NotUsed] = {
    system.log.info("Request listPrimes(max:{})", request.max)

    Source(PrimesGenerator.primes.takeWhile(_ <= request.max))
      .map(i => ListPrimesResponse(i))
  }
}
