package ru.misis.bank.db

import ru.misis.bank.db.BankDb.accountTable
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}


class InitDb(implicit val ec: ExecutionContext, db: Database) {
  def prepare(): Future[_] = {
    db.run(accountTable.schema.createIfNotExists)
  }
}



/*
package ru.misis.bank.db

object Connection {
  val db = Database.forConfig("postgres")
}
 */