package ru.misis.bank

import java.util.UUID

case class Account(id: UUID = UUID.randomUUID(), username: String, balance: Int)


object SlickTables {
  import slick.jdbc.PostgresProfile.api._

  class AccountTable(tag: Tag) extends Table[Account](tag, Some("accounts"), "Account") { // accounts - имя schema, Account - имя таблицы
    def id = column[UUID]("id", O.PrimaryKey) // исправить?
    def username = column[String]("username")
    def balance = column[Int]("balance")

    // mapping function to the case class
    override def * = (id, username, balance) <> (Account.tupled, Account.unapply) // почитать про <> в slick
  }
  // API entry point
  lazy val accountTable = TableQuery[AccountTable]
}