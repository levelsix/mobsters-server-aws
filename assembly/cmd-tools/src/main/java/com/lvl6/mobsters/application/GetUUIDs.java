package com.lvl6.mobsters.application;

import java.util.UUID;

public class GetUUIDs {

	public static void main(String[] args) {
		final int uuidCount = Integer.parseInt(args[0]);
		for (int ii=0; ii<uuidCount; ii++) {
			final String nextUUID = UUID.randomUUID().toString();
			System.out.println(nextUUID);
		}
	}

}
