package com.lvl6.mobsters.application.protobuf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.lvl6.mobsters.eventproto.EventDungeonProto.BeginDungeonRequestProto;


public class FromFile01 {
	public static final int REPEAT_COUNT = 150000;
	
    public static void main(String[] args) throws Exception {
    	doTest();
    }
    
    public static BeginDungeonRequestProto doTest() throws FileNotFoundException, IOException {
    	byte[] buf = new byte[73];
    	FileInputStream input = new FileInputStream("output.dat");
    	input.read(buf, 0, buf.length);
    	input.close();

	    BeginDungeonRequestProto protoIn = null;
    	
    	for (int ii=0; ii<REPEAT_COUNT; ii++) {
	    	protoIn = BeginDungeonRequestProto.parseFrom(buf);
	    }
    	
    	return protoIn;
    }
}