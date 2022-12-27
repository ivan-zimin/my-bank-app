package ru.misis.bank.routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model._
import de.heikoseeberger.akkahttpcirce._
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import scala.util.{Success,Failure}
import scala.concurrent.ExecutionContext

import ru.misis.bank.models._
import ru.misis.bank.repository._
import akka.actor.Status

class TransactionRoute(repository: BankRepository)(implicit ec: ExecutionContext) extends FailFastCirceSupport {
  def route =
    path("transaction"){
      (put & entity(as[Transaction])){moneyTransaction =>
        onSuccess(repository.moneyTransaction(moneyTransaction)){
          case Right(value) => complete(value)
          case Left(s) => complete(StatusCodes.NotAcceptable, s)
        }
      }
    }
}