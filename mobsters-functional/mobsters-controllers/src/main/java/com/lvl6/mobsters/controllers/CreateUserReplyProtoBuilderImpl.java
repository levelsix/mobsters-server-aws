package com.lvl6.mobsters.controllers;

import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateResponseProto;
import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateResponseProto.UserCreateStatus;

public class CreateUserReplyProtoBuilderImpl implements CreateUserReplyBuilder {
    UserCreateResponseProto.Builder protoBuilder = UserCreateResponseProto.newBuilder();

	@Override
	public CreateUserReplyBuilder status(int status) {
		protoBuilder.setStatus(UserCreateStatus.valueOf(status));
		return this;
	}
	
	public UserCreateResponseProto build() {
		return protoBuilder.build();
	}
}
