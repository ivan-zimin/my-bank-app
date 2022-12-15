package ru.misis.bank

import java.util
import java.util.UUID
import java.util.concurrent.Executors
import scala.util.{Failure, Success, Try}
import scala.concurrent.{ExecutionContext, Future}

object MyEexecutionContext {
  val executor = Executors.newFixedThreadPool(4)
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(executor)
}

object Main {
  import slick.jdbc.PostgresProfile.api._
  import MyEexecutionContext._



  def InsertAccount(): Unit = {
    val ivan_account = Account(UUID.randomUUID(), "ivan", 1000)
    val queryDescriptor = SlickTables.accountTable += ivan_account
    val futureId: Future[Int] = Connection.db.run(queryDescriptor)
    futureId.onComplete {
      case Success(newAccountId) => println(s"Запрос выполнен успешно, новый id: $newAccountId")
      case Failure(ex) => println(s"Запрос не выполнен, произошла ошибка: $ex")
    }
    Thread.sleep(1000)
  }

  def ReadAllAccounts(): Unit = {
    val resultFuture: Future[Seq[Account]] = Connection.db.run(SlickTables.accountTable.result) // SELECT * FROM ___
    resultFuture.onComplete {
      case Success(accounts) => println(s"Получено:  ${accounts.mkString(",")}")
      case Failure(ex) => println(s"Произошла ошибка: $ex")
    }
    Thread.sleep(1000)
  }

  def UpdateAccount(): Unit = {
    val ivan_account = Account(UUID.fromString("c222c47a-dbe3-4009-a739-7069fe04b78e"), "Ivan", 1000)
    val queryDescriptor = SlickTables.accountTable.filter(_.username === "Ivan").update(ivan_account.copy(balance = 10000))
    val futureId: Future[Int] = Connection.db.run(queryDescriptor)
    futureId.onComplete {
      case Success(newAccountId) => println(s"Запрос выполнен успешно, новый ID: $newAccountId")
      case Failure(ex) => println(s"Произошла ошибка: $ex")
    }
    Thread.sleep(1000)
  }

  def main(args: Array[String]): Unit = {
    //InsertAccount()
    //ReadAllAccounts()
    UpdateAccount()
  }
}
