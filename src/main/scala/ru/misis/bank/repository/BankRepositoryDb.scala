package ru.misis.bank.repository

import ru.misis.bank.db.BankDb._
import ru.misis.bank.models._
import slick.jdbc.PostgresProfile.api._
import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}


class BankRepositoryDb(implicit val ec: ExecutionContext, db: Database) extends BankRepository {

  override def list(): Future[Seq[Account]] = {
    db.run(accountTable.result)
  }

  override def get(id: UUID): Future[Account] = {
    db.run(accountTable.filter(i => i.id === id).result.head)
  }

  def find(id: UUID): Future[Option[Account]] = {
    db.run(accountTable.filter(i => i.id === id).result.headOption)
  }

  override def create(createAccount: CreateAccount): Future[Account] = {
    val account = Account(username = createAccount.username, balance = createAccount.balance)
    for {
      _ <- db.run(accountTable += account)
      res <- get(account.id)
    } yield res
  }

  override def updateUsername(account: UpdateAccountUsername): Future[Option[Account]] = {
    for {
      _ <- db.run {
        accountTable
          .filter(_.id === account.id)
          .map(_.username)
          .update(account.username)
      }
      res <- find(account.id)
    } yield res
  }

  override def updateBalancePlus(account: UpdateAccountBalancePlus): Future[Either[String,Account]] = {
    val query = accountTable
      .filter(_.id === account.id)
      .map(_.balance)
    for {
      oldBalanceOption <- db.run(query.result.headOption)
      incomingSum = account.balance
      updateBalance = oldBalanceOption.map{oldBalance =>
        Right(oldBalance + incomingSum)
      }.getOrElse(Left("Не найдено элемент"))
      future = updateBalance.map(balance => db.run {query.update(balance)}) match {
        case Right(futute) => futute.map(Right(_))
        case Left(s) => Future.successful(Left(s))

      }
      updated <- future
      res <- find(account.id)
    } yield updated.map(_ => res.get)
  }

  override def updateBalanceMinus(account: UpdateAccountBalanceMinus): Future[Either[String,Account]] = {
    val query = accountTable
      .filter(_.id === account.id)
      .map(_.balance)
    for {
      oldBalanceOption <- db.run(query.result.headOption)
      outcomingSum = account.balance
      updateBalance = oldBalanceOption.map{oldBalans =>
        if ((oldBalans - outcomingSum) < 0)
          Left("Недостаточно денег на счету")
        else Right( oldBalans - outcomingSum)
      }.getOrElse(Left("Не найдено элемент"))
      future = updateBalance.map(balance => db.run {query.update(balance)}) match {
        case Right(futute) => futute.map(Right(_))
        case Left(s) => Future.successful(Left(s))

      }
      updated <- future
      res <- find(account.id)
    } yield updated.map(_ => res.get)
  }


  override def moneyTransaction(transaction: Transaction): Future[Either[String,ChangeAccountResult]] = {
    val senderAccount = // аккаунт отправителя денег
      accountTable.filter(_.id === transaction.idSender).map(_.balance)
    val recipientAccount = // аккаунт получателя денег
      accountTable.filter(_.id === transaction.idRecipient).map(_.balance)
    for {
      senderAccountOption <- db.run(senderAccount.result.headOption)
      recipientAccountOption <- db.run(recipientAccount.result.headOption)
      transactionMoney = transaction.balanceChange
      senderAccountUpdated = senderAccountOption.map{ sendingMoney =>
      {
        if(sendingMoney >= transactionMoney)
          Right(sendingMoney - transactionMoney)
        else
          Left("Недостаточно денег для перевода")
      }
      }.getOrElse(Left("Не найдено элемент"))
      recipientAccountUpdated = senderAccountOption.map{ recipientMoney =>
      {
        Right(recipientMoney + transactionMoney)
      }
      }.getOrElse(Left("Не найдено элемент"))
      senderAccountFuture = senderAccountUpdated.map{balance =>
        db.run{senderAccount.update(balance)}
      } match{
        case Right(future) => {
          recipientAccountUpdated.map(balance =>
            db.run{recipientAccount.update(balance)}
          ) match{
            case Right(future) => future.map(Right(_))
            case Left(s) =>Future.successful(Left(s))
          }
        }
        case Left(s) => Future.successful(Left(s))
      }
      updated <- senderAccountFuture
      res <- find(transaction.idRecipient)
    } yield updated.map(_=>
      ChangeAccountResult(transaction.idRecipient, res.get.balance))
  }

  override def delete(id: UUID): Future[Unit] ={
    db.run(accountTable.filter(_.id === id).delete).map(_ => ())
  }
}