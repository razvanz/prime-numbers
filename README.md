# prime-numbers

A group of services exposing prime numbers under an http endpoint.

Components:

- `proxy-server`: HTTP server handling requests to fetch prime numbers by using the following services over gRPC
- `prime-streamer-service`: gRPC server handling requests to fetch prime numbers and returning a stream of prime numbers

# Prerequisites

- [sbt](https://www.scala-sbt.org/)

# Usage

Compiling the project

```
sbt compile
```
