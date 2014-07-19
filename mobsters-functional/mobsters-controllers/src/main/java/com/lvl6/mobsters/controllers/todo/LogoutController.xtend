package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.server.EventController
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class LogoutController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(LogoutController))
{
  }
  new(){
    numAllocatedThreads=4
  }
  @Resource(name='playersByPlayerId') protected var Map<Integer,ConnectedPlayer> playersByPlayerId
  def getPlayersByPlayerId(){
    playersByPlayerId
  }
  def setPlayersByPlayerId(  Map<Integer,ConnectedPlayer> playersByPlayerId){
    this.playersByPlayerId=playersByPlayerId
  }
  @Autowired protected var DataServiceTxManager svcTxManager
  @Autowired protected var HazelcastPvpUtil hazelcastPvpUtil
  @Autowired protected var PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil
  override createRequestEvent(){
    new LogoutRequestEvent()
  }
  override getEventType(){
    EventProtocolRequest.C_LOGOUT_EVENT
  }
  protected override processRequestEvent(  RequestEvent event,   EventsToDispatch eventWriter) throws Exception {
    val reqProto=((event as LogoutRequestEvent)).logoutRequestProto
    val sender=reqProto.sender
    val userUuid=sender.userUuid
    val lastLogout=new Timestamp(new Date().time)
    if (userUuid > 0) {
      svcTxManager.beginTransaction
      try {
        val user=RetrieveUtils::userRetrieveUtils.getUserById(userUuid)
        if (null !== user) {
          if (!user.updateLastLogout(lastLogout)) {
            LOG.error("problem with updating user's last logout time for user " + userUuid)
          }
          if (!InsertUtils::get.insertLastLoginLastLogoutToUserSessions(user.id, null, lastLogout)) {
            LOG.error('problem with inserting last logout time for user ' + user + ', logout='+ lastLogout)
          }
          val udid=user.udid
          val isLogin=false
          val isNewUser=false
          InsertUtils::get.insertIntoLoginHistory(udid, userUuid, lastLogout, isLogin, isNewUser)
        }
        LOG.info('Player logged out: ' + userUuid)
        playersByPlayerId.remove(userUuid)
      }
 catch (      Exception e) {
        LOG.error('exception in updating user logout', e)
      }
 finally {
        svcTxManager.commit
      }
    }
 else {
      LOG.error('cannot update last logout because playerid of sender:' + sender.name + " is <= 0, it's "+ userUuid)
    }
  }
  def getHazelcastPvpUtil(){
    hazelcastPvpUtil
  }
  def setHazelcastPvpUtil(  HazelcastPvpUtil hazelcastPvpUtil){
    this.hazelcastPvpUtil=hazelcastPvpUtil
  }
  def getPvpLeagueForUserRetrieveUtil(){
    pvpLeagueForUserRetrieveUtil
  }
  def setPvpLeagueForUserRetrieveUtil(  PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil){
    this.pvpLeagueForUserRetrieveUtil=pvpLeagueForUserRetrieveUtil
  }
}
