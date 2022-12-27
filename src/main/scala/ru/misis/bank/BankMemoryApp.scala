package ru.misis.bank

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn
import io.circe.syntax._
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce._
import ru.misis.bank.models._
import ru.misis.bank.repository._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import ru.misis.bank.routes.HelloRoute
import ru.misis.bank.routes._

object BankMemoryApp extends App with FailFastCirceSupport {
  implicit val system: ActorSystem = ActorSystem("AccountApp")
  implicit val ec = system.dispatcher
  val repository = new BankRepositoryInMemory
  val helloRoute = new HelloRoute().route
  val accountRoute = new BankRoute(repository).route

  Http().newServerAt("0.0.0.0", port = 8080).bind(helloRoute ~ accountRoute)
  println("[info] server is running, enter any character to disable it")
  StdIn.readLine()
}