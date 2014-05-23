package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.AnimatedSpriteOffset;
@Component public class AnimatedSpriteOffsetRepository extends BaseDynamoRepository<AnimatedSpriteOffset>{
	public AnimatedSpriteOffsetRepository(){
		super(AnimatedSpriteOffset.class);
	}

}