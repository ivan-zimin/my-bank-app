package ru.misis.bank.repository

import ru.misis.bank.models._
import java.util.UUID
import scala.concurrent.Future
import scala.util.Either

trait BankRepository {
  def list(): Future[Seq[Account]]
  def get(id: UUID):  Future[Account]
  def create(account: CreateAccount):  Future[Account]
  def updateUsername(account: UpdateAccountUsername):  Future[Option[Account]]
  def updateBalancePlus(account: UpdateAccountBalancePlus):  Future[Either[String,Account]]
  def updateBalanceMinus(account: UpdateAccountBalanceMinus):  Future[Either[String,Account]]
  def moneyTransaction(transactions: Transaction):  Future[Either[String,ChangeAccountResult]]
  def delete(id: UUID):  Future[Unit]
}