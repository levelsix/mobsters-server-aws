package com.lvl6.mobsters.noneventproto.utils;

import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.MonsterHealingForUser;
import com.lvl6.mobsters.info.IMonsterLevelInfo;
import com.lvl6.mobsters.info.Monster;
import com.lvl6.mobsters.info.MonsterBattleDialogue;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.FullUserMonsterProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.MonsterBattleDialogueProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.MonsterLevelInfoProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.MonsterProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserMonsterHealingProto;


public interface NoneventMonsterProtoSerializer
{

	public MonsterProto createMonsterProto( Monster aMonster );
	
	public MonsterLevelInfoProto createMonsterLevelInfoProto( IMonsterLevelInfo mli );
	
	public MonsterBattleDialogueProto createMonsterBattleDialogueProto( MonsterBattleDialogue mbd );

	//BEGIN USER DATA SERIALIZATION
	
	public FullUserMonsterProto createFullUserMonsterProtoFromUserMonster(MonsterForUser mfu);
	
	public UserMonsterHealingProto createUserMonsterHealingProto(MonsterHealingForUser mhfu);
}
