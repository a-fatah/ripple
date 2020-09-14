package com.orangefox.ripple.consolidation.contract.infrastructure

import com.orangefox.ripple.consolidation.agency.model.Agencies
import com.orangefox.ripple.consolidation.agency.model.AgencyId
import com.orangefox.ripple.consolidation.contract.infrastructure.ContractState.*
import com.orangefox.ripple.consolidation.contract.model.*
import com.orangefox.ripple.consolidation.policy.application.FindPolicyById
import com.orangefox.ripple.consolidation.policy.model.PolicyId
import org.springframework.stereotype.Component
import com.orangefox.ripple.consolidation.contract.model.Contract as DomainModel

@Component("contractDomainMapper")
class DomainMapper(
  private val contractRepository: ContractsRepository,
  private val agencies: Agencies,
  private val policies: FindPolicyById) {

  // TODO write unit test
  fun mapToDomain(contract: Contract): DomainModel {
    val id = ContractId(contract.id!!)
    val metaData = metaData(contract)
    val contractor = findContractor(contract)
    val consolidator = findConsolidator(contract)
    val contractCode = ContractCode(contract.code)
    val contractPeriod = ContractPeriod(contract.startDate, NumberOfDays(contract.periodLength))
    val valueCap = ValueCap(contract.valueCap)
    val balance = Balance(contract.balance)
    val haps = NumberOfHaps(contract.accessPoints.size)
    val policies = contract.policies.map {
      policies.find(PolicyId(it.policy))
      ?: throw IllegalStateException("Could not find policy with while mapping to ActiveContract")
    }

    return when(contract.state) {
      ACTIVE -> ActiveContract(id, contractor, consolidator, contractCode, contractPeriod, valueCap, balance, metaData, policies)
      DRAFT -> ContractDraft(id, contractor, consolidator, contractCode, contractPeriod, valueCap, metaData, haps)
      SUSPENDED -> SuspendedContract(id, contractor, consolidator, contractCode)
      EXPIRED -> ExpiredContract(id, contractor, consolidator, contractCode)
      BLOCKED -> BlockedContract(id, contractor, consolidator, contractCode)
      else -> throw IllegalStateException("Cannot map Contract in invalid state to domain model")
    }
  }

  private fun metaData(entity: Contract): Set<ContractMeta> {
    val meta = hashSetOf<ContractMeta>()

    meta.add(
      when(entity.type) {
        ContractType.CREDIT -> CreditContract()
        ContractType.DEBIT -> DebitContract()
      }
    )

    if (entity.allowDistribution) {
      meta.add(Descendible())
    }

    if (entity.recurrent) {
      meta.add(Recurring(NumberOfCycles(entity.numberOfCycles, entity.currentCycle)))
    }

    if (entity.locked) {
      meta.add(Locked())
    }

    if (entity.parent != null) {
      val parent = parent(entity)
      meta.add(Child(parent = mapToDomain(parent)))
    }

    return meta
  }

  private fun parent(child: Contract): Contract {
    if (child.parent != null) {
      val parent = contractRepository.findById(child.parent)
      if (parent.isPresent) {
        return parent.get()
      }
    }
    throw IllegalStateException("Parent not found while mapping Contract to domain model")
  }

  private fun findConsolidator(contract: Contract): Consolidator {
    return agencies.findConsolidator(AgencyId(contract.consolidator.uuid))
      ?: throw IllegalStateException("Could not find Consolidator for Contract")
  }

  private fun findContractor(contract: Contract): Contractor {
    return agencies.findContractor(AgencyId(contract.contractor.uuid))
      ?: throw IllegalStateException("Could not find Contractor for Contract")
  }

}
