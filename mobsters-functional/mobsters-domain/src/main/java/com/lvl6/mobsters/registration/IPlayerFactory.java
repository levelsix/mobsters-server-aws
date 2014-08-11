//package com.lvl6.mobsters.registration;
//
//import com.lvl6.mobsters.info.Structure;
//import com.lvl6.mobsters.services.user.UserService.CreateUserOptionsBuilder;
//import com.lvl6.mobsters.utility.lambda.Director;
//
//public interface IPlayerFactory {
//    public interface CreateUserOptionsBuilder {
//
//	}
//
//	String registerFacebookPlayer(
//		String facebookId, String udid, String name, String deviceToken, 
//		int cash, int oil, int gems, Director<CreateUserOptionsBuilder> options
//	);
//
//    String registerUdidPlayer(
//		String udid, String name, String deviceToken, 
//		int cash, int oil, int gems, Director<CreateUserOptionsBuilder> options
//	);
//    
//    public interface CreateUserOptionsBuider {
//    	CreateUserOptionsBuilder withStructure(Structure structure, float xPosition, float yPosition);
//    }
//
//}
