//package com.lvl6.mobsters.controllers;
//
//import static com.google.common.base.Preconditions.checkArgument;
//
//import java.util.Date;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import com.google.common.base.Preconditions;
//import com.lvl6.mobsters.common.utils.Director;
//import com.lvl6.mobsters.eventproto.EventStructureProto.SpawnObstacleRequestProto;
//import com.lvl6.mobsters.eventproto.EventStructureProto.SpawnObstacleResponseProto;
//import com.lvl6.mobsters.eventproto.EventStructureProto.SpawnObstacleResponseProto.SpawnObstacleStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.SpawnObstacleRequestEvent;
//import com.lvl6.mobsters.events.response.SpawnObstacleResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventStructureProto.CoordinateProto;
//import com.lvl6.mobsters.noneventproto.NoneventStructureProto.MinimumObstacleProto;
//import com.lvl6.mobsters.noneventproto.NoneventStructureProto.StructOrientation;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//import com.lvl6.mobsters.utility.common.TimeUtils;
//import com.lvl6.mobsters.services.structure.StructureService;
//import com.lvl6.mobsters.services.structure.StructureService.CreateObstacleCollectionBuilder;
//import com.lvl6.mobsters.services.structure.StructureService.CreateObstaclesReplyBuilder;
//
//
//@Component
//public class SpawnObstacleController extends EventController {
//
//    private static Logger LOG = LoggerFactory.getLogger(SpawnObstacleController.class);
//
//    @Autowired
//    protected StructureService structureService;
//
//    /*
//     * @Autowired protected EventWriter eventWriter;
//     */
//
//    public SpawnObstacleController() {}
//
//    @Override
//    public RequestEvent createRequestEvent() {
//        return new SpawnObstacleRequestEvent();
//    }
//
//    @Override
//    public EventProtocolRequest getEventType() {
//        return EventProtocolRequest.C_SPAWN_OBSTACLE_EVENT;
//    }
//
//    @Override
//    protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
//    {
//        final SpawnObstacleRequestProto reqProto =
//            ((SpawnObstacleRequestEvent) event).getSpawnObstacleRequestProto();
//        final MinimumUserProto sender = reqProto.getSender();
//        final String userUuid = sender.getUserUuid();
//        final Date clientTime = 
//            TimeUtils.createDateFromTime(
//            	reqProto.getCurTime());
//        final List<MinimumObstacleProto> mopList = reqProto.getProspectiveObstaclesList();
//
//		// prepare to send response back to client
//		CreateObstaclesReplyBuilderImpl replyBuilder =
//			new CreateObstaclesReplyBuilderImpl(sender, event.getTag());
//
//        // TODO: Check values client sent for syntax errors.  Complete service call only
//        // if syntax checks out ok, otherwise throw an Exception otherwise.		
//        structureService.createObstaclesForUser(
//        	replyBuilder, userUuid, 
//        	new Director<CreateObstacleCollectionBuilder>() {
//				@Override
//				public void apply(final CreateObstacleCollectionBuilder builder) {
//					for (final MinimumObstacleProto mop : mopList) {
//						final CoordinateProto coordPair = mop.getCoordinate();
//						final int obstacleId = mop.getObstacleId();
//						final float x = coordPair.getX();
//						final float y = coordPair.getY();
//						final StructOrientation orientation = mop.getOrientation();
//						
//						checkArgument(
//							(obstacleId > 0) && 
//							(x > 0) && 
//							(y > 0) && 
//							(
//								(orientation == StructOrientation.POSITION_1) || 
//								(orientation == StructOrientation.POSITION_2)
//							)
//						);
//						
//						builder.addObstacle(obstacleId, x, y, orientation.name());
//					}
//				}
//        	}
//        );
//
//        // write to client
//        SpawnObstacleResponseEvent resEvent = replyBuilder.buildResponseEvent();
//        LOG.info("Writing event: %s", resEvent);
//        eventWriter.writeEvent(resEvent);
//    }
//    
//    private static class CreateObstaclesReplyBuilderImpl implements CreateObstaclesReplyBuilder {
//    	private final SpawnObstacleResponseProto.Builder protoBuilder;
//		private final String userUuid;
//		private final int tag;
//		
//    	public CreateObstaclesReplyBuilderImpl(MinimumUserProto senderProto, int tag) {
//    		protoBuilder = SpawnObstacleResponseProto.newBuilder();
//    		protoBuilder.setStatus(SpawnObstacleStatus.FAIL_OTHER);
//    		protoBuilder.setSender(senderProto);
//    		userUuid = senderProto.getUserUuid();
//			this.tag = tag;
//		}
//    	
//    	public CreateObstaclesReplyBuilder resultOk()
//    	{
//    		protoBuilder.setStatus(SpawnObstacleStatus.SUCCESS);
//    		return this;
//    	}
//
//		SpawnObstacleResponseEvent buildResponseEvent()
//    	{
//	        final SpawnObstacleResponseEvent retVal =
//	            new SpawnObstacleResponseEvent(userUuid, tag, protoBuilder);
//
//    		return retVal;
//    	}
//    }
//    
//    
//
//    void setStructureService( final StructureService structureService ) {
//        this.structureService = structureService;
//    }
//}
