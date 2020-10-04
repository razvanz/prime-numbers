# prime-numbers

A group of services exposing prime numbers under an http endpoint.

Components:

- `prime-streamer-service`: gRPC server returning a stream of prime numbers
- `proxy-server`: HTTP server handling requests to fetch prime numbers by using the previous gRPC server

### Prerequisites

- [sbt](https://www.scala-sbt.org/)

### Usage

```
# Compiling the project
sbt compile

# run the services (use new shell as they are interactive):
sbt "runMain com.example.proxy_server.ProxyServer"
sbt "runMain com.example.prime_streamer_service.PrimeStreamerServer"

# call the endpoing
curl -i http://localhost:8080/primes/9
```
