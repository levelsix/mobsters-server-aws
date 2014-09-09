package com.lvl6.mobsters.application.protobuf;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;

import com.google.protobuf.GeneratedMessage;
import com.lvl6.mobsters.eventproto.EventDungeonProto.BeginDungeonRequestProto;
import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.Element;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;


public class WriteProto {
    public static void main(String[] args) throws Exception {
    	BeginDungeonRequestProto.Builder bldr =
    		BeginDungeonRequestProto.newBuilder();
    	MinimumUserProto.Builder userBldr =
    		MinimumUserProto.newBuilder();
    	userBldr
    		.setUserUuid(
    			UUID.randomUUID().toString())
    		.setName("Shay Daddy");
    		//.setAvatarMonsterId(2011);

    	bldr.setSender(
    		userBldr.build())
    		.setClientTime(1408484212000L)
			.setTaskId(2)
			.setIsEvent(false)
			.setPersistentEventId(0)
			.setGemsSpent(0)
			.setElem(Element.FIRE)
			.setForceEnemyElem(false)
			.setAlreadyCompletedMiniTutorialTask(false);
    	;
    	
    	BeginDungeonRequestProto protoOut = bldr.build();
    	System.out.println(
    		String.format("%s\n%s", protoOut, protoOut.toString()));

    	byte[] buf = protoOut.toByteArray();
    	FileOutputStream output = new FileOutputStream("output.dat");
    	output.write(buf);
    	output.close();
    	
    	
    	
    	FileInputStream input = new FileInputStream("output.dat");
    	input.read(buf, 0, buf.length);
    	final BeginDungeonRequestProto protoIn = 
    		BeginDungeonRequestProto.parseFrom(buf);
    	
    	System.out.println(
    		String.format("%s\n%s", protoIn, protoIn.toString()));
    	System.out.println(protoOut.equals(protoIn));
    	
    	

    	GeneratedMessage.Builder<?> bldr3 = BeginDungeonRequestProto.newBuilder();
    	BeginDungeonRequestProto protoTest = (BeginDungeonRequestProto) bldr3.mergeFrom(buf).build();
   	
    	System.out.println(
    		String.format("%s\n%s", protoTest, protoTest.toString()));
    	System.out.println(protoOut.equals(protoTest));

    }
}