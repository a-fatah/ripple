package com.orangefox.ripple.consolidation.contract.model


abstract class ContractMeta
class Child(val parent: Contract): ContractMeta()
class CreditContract(): ContractMeta()
class DebitContract(): ContractMeta()
class Recurring(val strategy: RecurringStrategy): ContractMeta()
class Descendible: ContractMeta()
class Locked: ContractMeta()
