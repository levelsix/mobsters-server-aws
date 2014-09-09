package com.lvl6.mobsters.services.quest

import com.google.common.collect.HashMultimap
import com.lvl6.mobsters.domain.config.IConfigurationRegistry
import com.lvl6.mobsters.info.IQuest
import java.util.ArrayList
import java.util.Collections
import java.util.HashSet
import java.util.List
import java.util.Set

class QuestGraph {
	val ArrayList<IQuest> conditionalQuests
	val ArrayList<IQuest> freeQuests
	val HashMultimap<IQuest, IQuest> prereqToQuests

	new(Set<? extends IQuest> quests) {
		conditionalQuests = new ArrayList<IQuest>(quests.size())
		freeQuests = new ArrayList<IQuest>(quests.size())
		prereqToQuests = HashMultimap.create(quests.size(), 5);
		
		quests.forEach[
			if (it.questsRequiredForThis.nullOrEmpty) {
				freeQuests.add(it)
			} else {
				conditionalQuests.add(it)
				it.questsRequiredForThis.forEach[IQuest preReq | prereqToQuests.put(preReq, it)]
			}
		]
		
		freeQuests.trimToSize
		conditionalQuests.trimToSize
	}

	def List<Integer> getQuestsAvailable(
		List<Integer> redeemed, 
		List<Integer> inProgress, 
		extension IConfigurationRegistry configurationRegistry
	) {
		// Identify all quests that are neither redeemed nor in progress.

		var Iterable<IQuest> available;
		if (redeemed.nullOrEmpty) {
			if (inProgress.nullOrEmpty) {
				available = freeQuests
			} else {
				val Set<IQuest> unavailable = inProgress.questMeta.toSet
				available = 
					freeQuests.filter[unavailable.contains(it) == false]
			}
		} else {
			val Set<IQuest> finished = redeemed.questMeta.toSet
			val Set<IQuest> unavailable =
				if (inProgress.nullOrEmpty) {
					finished
				} else {
					new HashSet<IQuest>(finished) => [
						it.addAll(
							inProgress.questMeta
						)
					]
				}

			// From the MultiMap of Prerequesite->Quest, derive a subset of quests that might have all their prerequesites met
			// by deriving the subset that have had at least one of their pre-requesites met.  From this subset, filter out those
			// that are currently either in progress or redeemed as well as those whose pre-requesites are not fully covered by
			// the redeemed list.
			// Before returning to the caller, concatenate the sublist of tasks with no pre-requesites that are neither already
			// redeemed nor currently in progress.
			available =
				#[
					finished
						.map[prereqToQuests.get(it)]
						.flatten
						.toSet
						.filter[
							! unavailable.contains(it) &&
							finished.containsAll(
								it.questsRequiredForThis ?: Collections.emptyList())],
					freeQuests
						.filter[! unavailable.contains(it)]		
				].flatten
					
		}

		// Caller expects integers Ids at present, so map the Iterable<Quest> derived above to a List<Integer>.
		return available
			.map[
				Integer.valueOf(it.id)]
			.toList
	}

	override toString() {
		return
			String.format(
				"%s; %s",
				freeQuests.map[
					String.format("%d:{}", it.id)
				].join("; "),
				conditionalQuests.map[ 
					String.format(
						"%d:{%s}",
						it.id,
						it.questsRequiredForThis
							.map[it.id]
							.join(", ")
					)
				].join("; "))
	}
}