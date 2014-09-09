package com.lvl6.mobsters.application.protobuf;

import java.io.FileOutputStream;
import java.util.UUID;

import com.lvl6.mobsters.eventproto.EventMonsterProto.SubmitMonsterEnhancementRequestProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserEnhancementItemProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithMaxResources;
import com.lvl6.mobsters.utility.common.TimeUtils;


public class WriteSubmitEnhancements {
    public static void main(String[] args) throws Exception {
    	SubmitMonsterEnhancementRequestProto.Builder bldr =
    		SubmitMonsterEnhancementRequestProto.newBuilder();
    	MinimumUserProtoWithMaxResources.Builder rsrcUserBldr =
    		MinimumUserProtoWithMaxResources.newBuilder();
    	MinimumUserProto.Builder userBldr =
    		MinimumUserProto.newBuilder();
    	userBldr
    		.setUserUuid(
    			UUID.randomUUID().toString())
    		.setName("Shay Daddy");
    		//.setAvatarMonsterId(2011);
    	rsrcUserBldr.setMaxCash(52000)
    		.setMaxOil(35000)
    		.setMinUserProto(
    			userBldr.build());
    	bldr.setSender(
    		rsrcUserBldr.build()
    	).setGemsSpent(2)
    	.setOilChange(-430);
    	
    	long currentTime =
    		TimeUtils.currentTimeMillis();
    	String monsterUUID =
    		UUID.randomUUID().toString();

    	UserEnhancementItemProto.Builder userItemProto =
    		UserEnhancementItemProto.newBuilder()
    			.setEnhancingCost(120)
    			.setExpectedStartTimeMillis(currentTime)
    			.setUserMonsterUuid(monsterUUID);

    	for( int ii=0; ii<2048; ii++ ) {
    		bldr.addUeipDelete(
    			userItemProto.build()
    		).addUeipNew(
    			userItemProto.build()
    		).addUeipUpdate(
    			userItemProto.build()
    		);
    	}
    	
    	SubmitMonsterEnhancementRequestProto protoOut = bldr.build();
    	System.out.println(
    		String.format("%s\n%s", protoOut, protoOut.toString()));

    	byte[] buf = protoOut.toByteArray();
    	FileOutputStream output =
    		new FileOutputStream("submitEnhancementsReq.dat");
    	output.write(buf);
    	output.close();
    }
}