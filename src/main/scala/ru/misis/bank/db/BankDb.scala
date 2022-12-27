package ru.misis.bank.db

import ru.misis.bank.models.Account
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

import java.util.UUID

object BankDb {
  class AccountTable(tag: Tag) extends Table[Account](tag, Some("accounts"), "Account") { // accounts - имя schema, Account - имя таблицы
    def id = column[UUID]("id", O.PrimaryKey)
    def username = column[String]("username")
    def balance = column[Int]("balance")

    // mapping function to the case class
    override def * = (id, username, balance) <> (Account.tupled, Account.unapply) // почитать про <> в slick
  }
  // API entry point
  lazy val accountTable = TableQuery[AccountTable]
}