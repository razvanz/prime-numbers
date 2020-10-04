package com.example.prime_streamer_service

object PrimesGenerator {
  val primes: LazyList[Int] = 2 #:: LazyList.from(3, 2).filter(isPrime)

  private def isPrime(n: Int): Boolean =
    n >= 2 && (2 to math.sqrt(n).toInt).forall(n % _ != 0)
}
