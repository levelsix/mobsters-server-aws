package com.lvl6.mobsters.noneventproto.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.info.BoosterPack;
import com.lvl6.mobsters.info.IBoosterDisplayItem;
import com.lvl6.mobsters.info.IBoosterItem;
import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.Quality;
import com.lvl6.mobsters.noneventproto.NoneventBoosterPackProto.BoosterDisplayItemProto;
import com.lvl6.mobsters.noneventproto.NoneventBoosterPackProto.BoosterItemProto;
import com.lvl6.mobsters.noneventproto.NoneventBoosterPackProto.BoosterPackProto;

@Component
public class NoneventBoosterPackProtoSerializerImpl implements NoneventBoosterPackProtoSerializer 
{
	private static Logger LOG =
		LoggerFactory.getLogger(NoneventBoosterPackProtoSerializerImpl.class);

	@Override
	public BoosterPackProto createBoosterPackProto( BoosterPack bp )
	{
		BoosterPackProto.Builder b = BoosterPackProto.newBuilder();
	    b.setBoosterPackId(bp.getId());

	    String str = bp.getName();
	    if (null != str && !str.isEmpty()) {
	      b.setBoosterPackName(str);
	    }

	    b.setGemPrice(bp.getGemPrice());

	    str = bp.getListBackgroundImgName();
	    if (null != str && !str.isEmpty()) {
	      b.setListBackgroundImgName(str);
	    }

	    str = bp.getListDescription();
	    if (null != str && !str.isEmpty()) {
	      b.setListDescription(str);
	    }

	    str = bp.getNavBarImgName();
	    if (null != str && !str.isEmpty()) {
	      b.setNavBarImgName(str);
	    }

	    str = bp.getNavTitleImgName();
	    if (null != str && !str.isEmpty()) {
	      b.setNavTitleImgName(str);
	    }

	    str = bp.getMachineImgName();
	    if (null != str && !str.isEmpty()) {
	      b.setMachineImgName(str);
	    }

	    Collection<BoosterItemProto> specialItemsList =
	    	createSpecialBoosterItemProtos(bp.getPackItems());
	    b.addAllSpecialItems(specialItemsList);
	    
	    Collection<BoosterDisplayItemProto> displayItemsList =
	    	createBoosterDisplayItemProtos(bp.getPackDisplayItems());
	    b.addAllDisplayItems(displayItemsList);
	    
		return b.build();
	}

	protected Collection<BoosterItemProto> createSpecialBoosterItemProtos(
		List<IBoosterItem> biList)
	{
		Collection<BoosterItemProto> itemProtoList = new ArrayList<BoosterItemProto>();
		for(IBoosterItem bi : biList) {
			//only want special booster items
			if (bi.isSpecial()) {
				BoosterItemProto bip = createBoosterItemProto(bi); 
				itemProtoList.add(bip);
			}
		}
		return itemProtoList;
	}

	@Override
	public BoosterItemProto createBoosterItemProto(IBoosterItem bi)
	{
		BoosterItemProto.Builder b = BoosterItemProto.newBuilder();
		b.setBoosterItemId(bi.getId());
		b.setBoosterPackId(bi.getBoosterPack().getId());
		b.setMonsterId(bi.getMonster().getId());
		b.setNumPieces(bi.getNumPieces());
		b.setIsComplete(bi.isComplete());
		b.setIsSpecial(bi.isSpecial());
		b.setGemReward(bi.getGemReward());
		b.setCashReward(bi.getCashReward());
		b.setChanceToAppear(bi.getChanceToAppear());
		return b.build();
	}
	
	protected Collection<BoosterDisplayItemProto> createBoosterDisplayItemProtos(
		List<IBoosterDisplayItem> bdiList)
	{
		Collection<BoosterDisplayItemProto> displayItemList = new ArrayList<BoosterDisplayItemProto>();
		for (IBoosterDisplayItem bdi : bdiList) {
	        BoosterDisplayItemProto bdip = createBoosterDisplayItemProto(bdi);
	        displayItemList.add(bdip);
		}
		return displayItemList;
	}
	
	@Override
	public BoosterDisplayItemProto createBoosterDisplayItemProto(
		IBoosterDisplayItem bdi)
	{
		BoosterDisplayItemProto.Builder b = BoosterDisplayItemProto.newBuilder();

		b.setBoosterPackId(bdi.getBoosterPack().getId());
		b.setIsMonster(bdi.isMonster());
		b.setIsComplete(bdi.isComplete());

		String monsterQuality = bdi.getMonsterQuality();
		if (null != monsterQuality) {
			try {
				Quality mq = Quality.valueOf(monsterQuality);
				b.setQuality(mq);
			} catch (Throwable e){
				LOG.error(
					String.format(
						"invalid monster quality. boosterDisplayItem=%s", bdi),
					e);
			}
		}

		b.setGemReward(bdi.getGemReward());
		b.setQuantity(bdi.getQuantity());

		return b.build();
	}
}
