package com.lvl6.mobsters.services.user

import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.services.common.Lvl6MobstersStatusCode
import org.slf4j.Logger
import org.springframework.stereotype.Component

import static com.google.common.base.Preconditions.*
import static com.lvl6.mobsters.services.common.Lvl6MobstersConditions.*

@Component
class UserExtensionLib {
	def User checkCanSpendGems(
		User u, int gemsToSpend, Logger log, ()=>String spendPurposeLambda
	) {
		lvl6Precondition(
			u.canSpendGems(gemsToSpend),
		    Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_GEMS,
			log,
			[ | 
				return String.format(
					"user has %d gems; trying to spend %d as part of cost to %s",
					u.gems, gemsToSpend, spendPurposeLambda.apply())]
		)
				
		return u
	}
	
	def User checkCanSpendGems( User u, int gemsToSpend, Logger log )
	{
		lvl6Precondition(
			u.canSpendGems(gemsToSpend),
		    Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_GEMS,
			log,
			"user has %d gems; trying to spend %d",
			u.gems, gemsToSpend
		)
				
		return u
	}
	
	def User checkCanSpendCash(
		User u, int cashToSpend, Logger log, ()=>String spendPurposeLambda
	) {
		lvl6Precondition(
			u.canSpendCash(cashToSpend),
		    Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_CASH,
			log,
			[ | 
				return String.format(
					"user has %d cash; trying to spend %d as part of cost to %s",
					u.cash, cashToSpend, spendPurposeLambda.apply())]
		)
				
		return u
	}
	
	def User checkCanSpendCash( User u, int cashToSpend, Logger log )
	{
		lvl6Precondition(
			u.canSpendCash(cashToSpend),
		    Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_CASH,
			log,
			"user has %d gems; trying to spend %d",
			u.cash, cashToSpend
		)
				
		return u
	}
	
	def User checkCanSpendOil(
		User u, int oilToSpend, Logger log, ()=>String spendPurposeLambda
	) {
		lvl6Precondition(
			u.canSpendOil(oilToSpend),
		    Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_OIL,
			log,
			[ | 
				return String.format(
					"user has %d oil; trying to spend %d as part of cost to %s",
					u.oil, oilToSpend, spendPurposeLambda.apply())]
		)
				
		return u
	}
	
	def User checkCanSpendOil( User u, int oilToSpend, Logger log )
	{
		lvl6Precondition(
			u.canSpendOil(oilToSpend),
		    Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_OIL,
			log,
			"user has %d oil; trying to spend %d",
			u.oil, oilToSpend
		)
				
		return u
	}
	
	def boolean canSpendGems( User u, int gemsToSpend )
	{
		checkArgument(gemsToSpend >= 0)
		
		return (u.gems >= gemsToSpend)
	}
	
	def boolean canSpendCash( User u, int cashToSpend )
	{
		checkArgument(cashToSpend >= 0)
		
		return (u.cash >= cashToSpend)
	}
	
	def boolean canSpendOil( User u, int oilToSpend )
	{
		checkArgument(oilToSpend >= 0)
		
		return (u.oil >= oilToSpend)
	}
	
	def User spendGems( User u, int gemsToSpend, Logger log ) {		
		u.checkCanSpendGems(gemsToSpend, log)
		u.gems = u.gems - gemsToSpend
		return u
	}
	
	def boolean spendGems( User u, int gemsToSpend ) {		
		var retVal = false
		if (u.canSpendGems(gemsToSpend)) {
			u.gems = u.gems - gemsToSpend			
			retVal = true
		}
		
		return retVal
	}
	
	def User spendCash( User u, int cashToSpend, Logger log) {		
		u.checkCanSpendCash(cashToSpend, log)
		u.cash = u.cash - cashToSpend
		return u
	}
	
	def boolean spendCash( User u, int cashToSpend ) {		
		var retVal = false
		if (u.canSpendCash(cashToSpend)) {
			u.cash = u.cash - cashToSpend			
			retVal = true
		}
		
		return retVal
	}
	
	def User spendOil( User u, int oilToSpend, Logger log) {		
		u.checkCanSpendOil(oilToSpend, log)
		u.oil = u.oil - oilToSpend
		return u
	}
	
	def boolean spendOil( User u, int oilToSpend) {		
		var retVal = false
		if (u.canSpendOil(oilToSpend)) {
			u.oil = u.oil - oilToSpend			
			retVal = true
		}
		
		return retVal
	}
}