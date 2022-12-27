package ru.misis.bank.routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

class HelloRoute {
  def route =
    (path("hello") & get){
      complete("Hello scala world!")
    }
}