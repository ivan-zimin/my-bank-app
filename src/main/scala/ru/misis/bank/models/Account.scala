package ru.misis.bank.models

import java.util.UUID

package object models {}

case class Account(id: UUID = UUID.randomUUID(), username: String, balance: Int)
case class CreateAccount(username: String, balance: Int)
case class UpdateAccountUsername(id: UUID, username: String)
case class UpdateAccountBalancePlus(id: UUID, balance: Int)
case class UpdateAccountBalanceMinus(id: UUID, balance: Int)