package ru.misis.bank.routes

import akka.actor.Status
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model._
import de.heikoseeberger.akkahttpcirce._
import io.circe.generic.auto._
import ru.misis.bank.models._
import ru.misis.bank.repository._

import scala.concurrent.ExecutionContext

class BankRoute(repository: BankRepository)(implicit ec: ExecutionContext) extends FailFastCirceSupport {
  def route =
    (path("accounts")& get) {
      val list =  repository.list()
      complete(list)
    } ~
      path("account"){
        (post & entity(as[CreateAccount])) {newAccount =>
          complete(repository.create(newAccount))
        }
      } ~
      path("account" / JavaUUID){id =>
        get{
          complete(repository.get(id))
        }
      } ~
      path("account"){
        (put & entity(as[UpdateAccountUsername])){
          updateUsername =>
            complete(repository.updateUsername(updateUsername))
        }
      } ~
      path("account" / JavaUUID){id =>
        delete{
          complete(repository.delete(id))
        }

      } ~
      path("account" / "balance" / "plus"){
        (put & entity(as[UpdateAccountBalancePlus])){updateBalancePlus =>
          onSuccess(repository.updateBalancePlus(updateBalancePlus)){
            case Right(value) => complete(value)
            case Left(s) => complete(StatusCodes.NotAcceptable, s)
          }
        }
      } ~
      path("account" /"balance" / "minus"){
        (put & entity(as[UpdateAccountBalanceMinus])){updateBalanceMinus =>
          onSuccess(repository.updateBalanceMinus(updateBalanceMinus)){
            case Right(value) => complete(value)
            case Left(s) => complete(StatusCodes.NotAcceptable, s)
          }
        }
      }
}