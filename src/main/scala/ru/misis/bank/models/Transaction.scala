package ru.misis.bank.models

import java.util.UUID
import ru.misis.bank.models._

case class Transaction(idRecipient: UUID,idSender: UUID, balanceChange: Int)
case class ChangeAccountResult(idSender: UUID, balanceResult: Int)