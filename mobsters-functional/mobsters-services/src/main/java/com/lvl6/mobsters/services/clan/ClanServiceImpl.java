package com.lvl6.mobsters.services.clan;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ClanForUser;
import com.lvl6.mobsters.dynamo.repository.ClanForUserRepository;

@Component
public class ClanServiceImpl implements ClanService
{

	private static Logger LOG = LoggerFactory.getLogger(ClanServiceImpl.class);

	@Autowired
	private ClanForUserRepository clanForUserRepository;

	// NON CRUD LOGIC******************************************************************

	// CRUD LOGIC******************************************************************

	/**************************************************************************/

	@Override
	public List<ClanForUser> findByUserId( String userId )
	{
		// return clanForUserRepository.load
		return clanForUserRepository.findByUserId(userId);
	}

	//
	// @Override
	// public Map<Integer, Collection<ClanJobForUser>> findByUserIdAndClanIdIn(
	// String userId,
	// Collection<Integer> clanIds )
	// {
	// List<ClanJobForUser> qjfuList = clanJobForUserRepository.findByUserIdAndClanIdIn(userId, clanIds);
	//
	// Map<Integer, Collection<ClanJobForUser>> clanIdToQjfuList =
	// new HashMap<Integer, Collection<ClanJobForUser>>();
	//
	// //map by clan id to ClanJobForUser
	// for (ClanJobForUser qjfu : qjfuList) {
	// int clanId = qjfu.getClanId();
	//
	// if (!clanIdToQjfuList.containsKey(clanId)) {
	// clanIdToQjfuList.put(clanId,
	// new ArrayList<ClanJobForUser>());
	// }
	//
	// Collection<ClanJobForUser> groupedQjfuList =
	// clanIdToQjfuList.get(clanId);
	// groupedQjfuList.add(qjfu);
	// }
	//
	// return clanIdToQjfuList;
	// }

	public ClanForUserRepository getClanForUserRepository()
	{
		return clanForUserRepository;
	}

	public void setClanForUserRepository( ClanForUserRepository clanForUserRepository )
	{
		this.clanForUserRepository = clanForUserRepository;
	}

}
