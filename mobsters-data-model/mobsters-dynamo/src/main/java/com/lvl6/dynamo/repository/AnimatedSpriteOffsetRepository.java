package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.AnimatedSpriteOffset;
@Component public class AnimatedSpriteOffsetRepository extends BaseDynamoRepository<AnimatedSpriteOffset>{
	public AnimatedSpriteOffsetRepository(){
		super(AnimatedSpriteOffset.class);
	}

}