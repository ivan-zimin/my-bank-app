package ru.misis.bank.repository

import ru.misis.bank.models._
import ru.misis.bank.repository._

import java.util.UUID
import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import scala.util.Either

class BankRepositoryInMemory(implicit val ec: ExecutionContext) extends BankRepository {
  private val myCart = mutable.Map[UUID, Account]()

  override def list():  Future[scala.List[Account]] = Future{
    myCart.values.toList
  }

  override def get(id: UUID): Future[Account] = Future{
    myCart(id)
  }

  override def create(createAccount: CreateAccount): Future[Account] = Future{
    val account = Account(
      id = UUID.randomUUID(),
      username = createAccount.username,
      balance = createAccount.balance
    )
    myCart.put(account.id, account)
    account
  }

  override def delete(id: UUID): Future[Unit] = Future {
    myCart.remove(id)
  }

  override def updateUsername(update: UpdateAccountUsername): Future[Option[Account]] = Future{
    myCart.get(update.id).map { account =>
      val updated = account.copy(username = update.username)
      myCart.put(account.id, updated)
      updated
    }
  }

  override def updateBalancePlus(update: UpdateAccountBalancePlus): Future[Either[String,Account]] = Future{
    myCart.get(update.id).map { account =>
      val updated = account.copy(balance = account.balance + update.balance)
      myCart.put(account.id, updated)
      Right(updated)
    }.getOrElse(Left("Не найден элемент"))
  }

  override def updateBalanceMinus(update: UpdateAccountBalanceMinus): Future[Either[String,Account]] = Future{
    myCart.get(update.id).map { account =>
      val updated = account.copy(balance = account.balance - update.balance)
      myCart.put(account.id, updated)
      Right(updated)
    }.getOrElse(Left("Не найден элемент"))
  }

  override def moneyTransaction(transaction: Transaction): Future[Either[String,ChangeAccountResult]] = ???
}