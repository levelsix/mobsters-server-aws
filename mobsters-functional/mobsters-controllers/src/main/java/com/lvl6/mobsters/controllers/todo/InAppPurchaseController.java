//package com.lvl6.mobsters.controllers.todo;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.URL;
//import java.net.URLConnection;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//import org.apache.http.Consts;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.InAppPurchaseRequestEvent;
//import com.lvl6.mobsters.events.response.InAppPurchaseResponseEvent;
//import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.misc.MiscMethods;
//import com.lvl6.properties.ControllerConstants;
//import com.lvl6.properties.Globals;
//import com.lvl6.properties.IAPValues;
//import com.lvl6.properties.KabamProperties;
//import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.InAppPurchaseRequestProto;
//import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.InAppPurchaseResponseProto;
//import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.InAppPurchaseResponseProto.InAppPurchaseStatus;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.retrieveutils.IAPHistoryRetrieveUtils;
//import com.lvl6.server.Locker;
//import com.lvl6.utils.RetrieveUtils;
//import com.lvl6.utils.utilmethods.InsertUtil;
//
//@Component
//@DependsOn("gameServer")
//public class InAppPurchaseController extends EventController {
//
//	private static Logger LOG = LoggerFactory.getLogger(InAppPurchaseController.class);
//  }.getClass().getEnclosingClass());
//
//  private static final String SANDBOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
//  private static final String PRODUCTION_URL = "https://buy.itunes.apple.com/verifyReceipt";
//
//  @Autowired
// protected DataServiceTxManager svcTxManager;
//
// @Autowired
//  protected InsertUtil insertUtils;
//
//  public void setInsertUtils(final InsertUtil insertUtils) {
//    this.insertUtils = insertUtils;
//  }
//
//  public InAppPurchaseController() {
//    numAllocatedThreads = 2;
//  }
//
//  @Override
//  public RequestEvent createRequestEvent() {
//    return new InAppPurchaseRequestEvent();
//  }
//
//  @Override
//  public EventProtocolRequest getEventType() {
//    return EventProtocolRequest.C_IN_APP_PURCHASE_EVENT;
//  }
//
//  /*
//   * db stuff done before sending event to eventwriter/client because the
//   * client's not waiting on it immediately anyways
//   */
//  // @SuppressWarnings("deprecation")
//  @Override
//  protected void processRequestEvent( final RequestEvent event, final EventsToDispatch eventWriter ) throws Exception {
//    final InAppPurchaseRequestProto reqProto = ((InAppPurchaseRequestEvent) event)
//        .getInAppPurchaseRequestProto();
//
//    final MinimumUserProto senderProto = reqProto.getSender();
//    final String receipt = reqProto.getReceipt();
//
//    final InAppPurchaseResponseProto.Builder resBuilder = InAppPurchaseResponseProto.newBuilder();
//    resBuilder.setSender(senderProto);
//    resBuilder.setReceipt(reqProto.getReceipt());
//
//    // Lock this player's ID
//    svcTxManager.beginTransaction();
//    try {
//      final User user = RetrieveUtils.userRetrieveUtils().getUserById(senderProto.getUserUuid());
//      
//      JSONObject response;
//      final JSONObject jsonReceipt = new JSONObject();
//      jsonReceipt.put(IAPValues.RECEIPT_DATA, receipt);
//      LOG.info("Processing purchase: " + jsonReceipt.toString(4));
//      // Send data
//      URL url = new URL(PRODUCTION_URL);
//
//      LOG.info("Sending purchase request to: " + url.toString());
//
//      URLConnection conn = url.openConnection();
//      conn.setDoOutput(true);
//      OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//      wr.write(jsonReceipt.toString());
//      wr.flush();
//
//      // Get the response
//      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//      String responseString = "";
//      String line;
//      while ((line = rd.readLine()) != null) {
//        responseString += line;
//      }
//      LOG.info("Response: " + responseString);
//
//      response = new JSONObject(responseString);
//
//      if ((response.getInt(IAPValues.STATUS) == 21007) || (response.getInt(IAPValues.STATUS) == 21008)) {
//        wr.close();
//        rd.close();
//        url = new URL(SANDBOX_URL);
//        conn = url.openConnection();
//        conn.setDoOutput(true);
//        wr = new OutputStreamWriter(conn.getOutputStream());
//        wr.write(jsonReceipt.toString());
//        wr.flush();
//        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        responseString = "";
//        while ((line = rd.readLine()) != null) {
//          responseString += line;
//        }
//        response = new JSONObject(responseString);
//      }
//
//      JSONObject receiptFromApple = null;
//      if (response.getInt(IAPValues.STATUS) == 0) {
//        receiptFromApple = response.getJSONObject(IAPValues.RECEIPT);
//        if (!IAPHistoryRetrieveUtils.checkIfDuplicateTransaction(Long.parseLong(receiptFromApple
//            .getString(IAPValues.TRANSACTION_ID)))) {
//          try {
//            final String packageName = receiptFromApple.getString(IAPValues.PRODUCT_ID);
//            final int diamondChange = IAPValues.getDiamondsForPackageName(packageName);
//            final int coinChange = IAPValues.getCoinsForPackageName(packageName);
//            final double realLifeCashCost = IAPValues.getCashSpentForPackageName(packageName);
//            final boolean isBeginnerSale = IAPValues.packageIsBeginnerSale(packageName);
//
//            final Map<String, Integer> previousCurrency =
//            		new HashMap<String, Integer>();
//            final Map<String, Integer> currencyChangeMap =
//            		new HashMap<String, Integer>();
//            if (diamondChange > 0) {
//            	previousCurrency.put(MiscMethods.gems, user.getGems());
//            	
//            	resBuilder.setDiamondsGained(diamondChange);
//            	user.updateRelativeDiamondsBeginnerSale(diamondChange, isBeginnerSale);
//            	currencyChangeMap.put(MiscMethods.gems, diamondChange);
//            } else {
//            	previousCurrency.put(MiscMethods.cash, user.getCash());
//            	
//            	resBuilder.setCoinsGained(coinChange);
//            	user.updateRelativeCoinsBeginnerSale(coinChange, isBeginnerSale);
//            	currencyChangeMap.put(MiscMethods.cash, coinChange);
//            }
//
//            if (!insertUtils.insertIAPHistoryElem(receiptFromApple,
//            		diamondChange, coinChange, user, realLifeCashCost)) {
//              LOG.error("problem with logging in-app purchase history for receipt:"
//                  + receiptFromApple.toString(4) + " and user " + user);
//            }
//            resBuilder.setStatus(InAppPurchaseStatus.SUCCESS);
//            resBuilder.setPackageName(receiptFromApple.getString(IAPValues.PRODUCT_ID));
//
//            resBuilder.setPackagePrice(realLifeCashCost);
//            LOG.info("successful in-app purchase from user " + user.getId() + " for package "
//                + receiptFromApple.getString(IAPValues.PRODUCT_ID));
//
//            final Timestamp date = new Timestamp((new Date()).getTime());
//            writeToUserCurrencyHistory(user, packageName, date,
//            		currencyChangeMap, previousCurrency);
//          } catch (final Exception e) {
//            LOG.error("problem with in app purchase flow", e);
//          }
//        } else {
//          resBuilder.setStatus(InAppPurchaseStatus.DUPLICATE_RECEIPT);
//          LOG.error("duplicate receipt from user " + user);
//        }
//      } else {
//        LOG.error("problem with in-app purchase that client sent, with receipt " + receipt);
//      }
//
//      wr.close();
//      rd.close();
//
//      if (!resBuilder.hasStatus()) {
//        resBuilder.setStatus(InAppPurchaseStatus.FAIL);
//      }
//
//      final InAppPurchaseResponseProto resProto = resBuilder.build();
//
//      final InAppPurchaseResponseEvent resEvent = new InAppPurchaseResponseEvent(senderProto.getUserUuid());
//      resEvent.setTag(event.getTag());
//      resEvent.setInAppPurchaseResponseProto(resProto);
//      // write to client
//      LOG.info("Writing event: " + resEvent);
//      try {
//          eventWriter.writeEvent(resEvent);
//      } catch (final Throwable e) {
//          LOG.error("fatal exception in InAppPurchaseController.processRequestEvent", e);
//      }
//
//      if (Globals.KABAM_ENABLED()) {
//        if ((receiptFromApple != null) && (resBuilder.getStatus() == InAppPurchaseStatus.SUCCESS)) {
//          final JSONObject logJson = getKabamJsonLogObject(reqProto, resBuilder, receiptFromApple);
//          final List<NameValuePair> queryParams = getKabamQueryParams(receipt, user, logJson);
//          doKabamPost(queryParams, 0);
//        }
//      }
//
//      //null PvpLeagueFromUser means will pull from hazelcast instead
//      final UpdateClientUserResponseEvent resEventUpdate = MiscMethods
//          .createUpdateClientUserResponseEventAndUpdateLeaderboard(user, null);
//      resEventUpdate.setTag(event.getTag());
//      // write to client
//      LOG.info("Writing event: " + resEventUpdate);
//      try {
//          eventWriter.writeEvent(resEventUpdate);
//      } catch (final Throwable e) {
//          LOG.error("fatal exception in InAppPurchaseController.processRequestEvent", e);
//      }
//
//      //      //in case user has a mentor, check if user completed mentor's quest
//      //      if (null != receiptFromApple && resBuilder.getStatus() == InAppPurchaseStatus.SUCCESS) {
//      //        MenteeQuestType type = MenteeQuestType.BOUGHT_A_PACKAGE;
//      //        MiscMethods.sendMenteeFinishedQuests(senderProto, type, server);
//      //      }
//    } catch (final Exception e) {
//      LOG.error("exception in InAppPurchaseController processEvent", e);
//    } finally {
//      // Unlock this player
//      svcTxManager.commit();
//    }
//  }
//
//  private void doKabamPost(final List<NameValuePair> queryParams, final int numTries) {
//    LOG.info("Posting to Kabam");
//    final String host = Globals.IS_SANDBOX() ? KabamProperties.SANDBOX_PAYMENT_URL : KabamProperties.PRODUCTION_PAYMENT_URL;
//    final HttpClient client = new DefaultHttpClient();
//    final HttpPost post = new HttpPost(host);
//    try {
//      LOG.info ("Sending post query: " + queryParams);
//      post.setEntity(new UrlEncodedFormEntity(queryParams, Consts.UTF_8));
//      final HttpResponse response = client.execute(post);
//      final BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//      String responseString = "";
//      String line;
//      while ((line = rd.readLine()) != null) {
//        responseString += line;
//      }
//      LOG.info("Received response: " + responseString);
//
//      final JSONObject jsonResponse = new JSONObject(responseString);
//      if (!jsonResponse.getBoolean("success")) {
//        LOG.error("Failed to log kabam payment with errorcode: "+jsonResponse.getInt("errorcode")+ " and errormessage: "+jsonResponse.getString("errormessage"));
//        if (numTries < 10) {
//          doKabamPost(queryParams, numTries+1);
//        } else {
//          LOG.error("Giving up..");
//        }
//      }
//    } catch (final Exception e) {
//      LOG.error("Error doing Kabam post", e);
//    }
//  }
//
//  private List<NameValuePair> getKabamQueryParams(final String receipt, final User user, final JSONObject logJson)throws NoSuchAlgorithmException {
//    LOG.info("Generating Post parameters");
//    final int gameid = Globals.IS_SANDBOX() ? KabamProperties.SANDBOX_CLIENT_ID : KabamProperties.PRODUCTION_CLIENT_ID;
//    final String secret = Globals.IS_SANDBOX() ? KabamProperties.SANDBOX_SECRET : KabamProperties.PRODUCTION_SECRET;
//    final long time = new Date().getTime() / 1000;
//    final List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//    queryParams.add(new BasicNameValuePair("gameid", ""+gameid));
//    queryParams.add(new BasicNameValuePair("log", logJson.toString()));
//    queryParams.add(new BasicNameValuePair("mobileid", user.getUdid()));
//    queryParams.add(new BasicNameValuePair("receipt", receipt));
//    queryParams.add(new BasicNameValuePair("timestamp", "" + time));
//    queryParams.add(new BasicNameValuePair("userid", "" + user.getKabamNaid()));
//    String str = "";
//    for (final NameValuePair key : queryParams) {
//      str += key.getName() + key.getValue();
//    }
//    str += secret;
//    queryParams.add(new BasicNameValuePair("sig", sha1(str)));
//    return queryParams;
//  }
//
//  private JSONObject getKabamJsonLogObject(final InAppPurchaseRequestProto reqProto,
//      final InAppPurchaseResponseProto.Builder resBuilder, final JSONObject receiptFromApple) throws JSONException {
//    final Map<String, Object> logParams = new TreeMap<String, Object>();
//    logParams.put("serverid", "1");
//    logParams.put("localcents", reqProto.getLocalcents());
//    logParams.put("localcurrency", reqProto.getLocalcurrency());
//    logParams.put("igc", resBuilder.hasDiamondsGained() ? resBuilder.getDiamondsGained() : resBuilder.getCoinsGained());
//    logParams.put("igctype", resBuilder.hasDiamondsGained() ? "gold" : "silver");
//    logParams.put("transactionid", receiptFromApple.get(IAPValues.TRANSACTION_ID));
//    logParams.put("platform", "itunes");
//    logParams.put("locale", reqProto.getLocale());
//    logParams.put("lang", "en");
//    logParams.put("ipaddr", reqProto.getIpaddr());
//    final JSONObject logJson = new JSONObject(logParams);
//    return logJson;
//  }
//
//  private static String sha1(final String input) throws NoSuchAlgorithmException {
//    final MessageDigest mDigest = MessageDigest.getInstance("SHA1");
//    final byte[] result = mDigest.digest(input.getBytes());
//    final StringBuilder sb = new StringBuilder();
//    for (final byte element : result) {
//      sb.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
//    }
//
//    return sb.toString();
//  }
//
//  private void writeToUserCurrencyHistory(final User aUser, final String packageName,
//		  final Timestamp date, final Map<String, Integer> currencyChangeMap,
//		  final Map<String, Integer> previousCurrency) {
//	  
//	  final String userUuid = aUser.getId();
//	  final Map<String, Integer> currentCurrencyMap = new HashMap<String, Integer>();
//	  final Map<String, String> changeReasonsMap = new HashMap<String, String>();
//	  final Map<String, String> detailsMap = new HashMap<String, String>();
//	  final String gems = MiscMethods.gems;
//	  final String cash = MiscMethods.cash;
//	  final String reasonForChange = ControllerConstants.UCHRFC__IN_APP_PURCHASE;
//	  
//	  currentCurrencyMap.put(gems, aUser.getGems());
//	  currentCurrencyMap.put(cash, aUser.getCash());
//	  changeReasonsMap.put(gems, reasonForChange);
//	  changeReasonsMap.put(cash, reasonForChange);
//	  detailsMap.put(gems, packageName);
//	  detailsMap.put(cash, packageName);
//	  
//	  MiscMethods.writeToUserCurrencyOneUser(userUuid, date, currencyChangeMap,
//			  previousCurrency, currentCurrencyMap, changeReasonsMap,
//			  detailsMap);
//  }
//  
//}
