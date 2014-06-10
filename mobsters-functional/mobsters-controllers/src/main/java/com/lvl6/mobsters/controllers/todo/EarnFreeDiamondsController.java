package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.EarnFreeDiamondsRequestProto;
import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.EarnFreeDiamondsResponseProto;
import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.EarnFreeDiamondsResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.EarnFreeDiamondsResponseProto.EarnFreeDiamondsStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.EarnFreeDiamondsRequestEvent;
import com.lvl6.mobsters.events.response.EarnFreeDiamondsResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventInAppPurchaseProto.EarnFreeDiamondsType;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class EarnFreeDiamondsController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(EarnFreeDiamondsController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	// private static String LVL6_SHARED_SECRET =
	// "mister8conrad3chan9is1a2very4great5man";
	// private Mac hmacSHA1WithLVL6Secret = null;

	// private static String ADCOLONY_V4VC_SECRET_KEY =
	// "v4vc5ec0f36707ad4afaa5452e";

	// private static String KIIP_CONSUMER_KEY =
	// "d6c7530ce4dc64ecbff535e521a241e3";
	// private static String KIIP_CONSUMER_SECRET =
	// "da8d864f948ae2b4e83c1b6e6a8151ed";
	// private static String KIIP_VERIFY_ENDPOINT =
	// "https://api.kiip.me/1.0/transaction/verify";
	// private static String KIIP_INVALIDATE_ENDPOINT =
	// "https://api.kiip.me/1.0/transaction/invalidate";
	// private static String KIIP_JSON_APP_KEY_KEY = "app_key";
	// private static String KIIP_JSON_SUCCESS_KEY = "success";
	// private static String KIIP_JSON_RECEIPT_KEY = "receipt";
	// private static String KIIP_JSON_CONTENT_KEY = "content";
	// private static String KIIP_JSON_SIGNATURE_KEY = "signature";
	// private static String KIIP_JSON_TRANSACTION_ID_KEY = "transaction_id";
	// private static String KIIP_JSON_QUANTITY_KEY = "quantity";

	// private OAuthService oAuthService = null;

	public EarnFreeDiamondsController()
	{
		numAllocatedThreads = 1;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new EarnFreeDiamondsRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_EARN_FREE_DIAMONDS_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final EarnFreeDiamondsRequestProto reqProto =
		    ((EarnFreeDiamondsRequestEvent) event).getEarnFreeDiamondsRequestProto();
		final MinimumUserProto senderProto = reqProto.getSender();

		final EarnFreeDiamondsType freeDiamondsType = reqProto.getFreeDiamondsType();
		final Timestamp clientTime = new Timestamp(reqProto.getClientTime());

		final String kiipReceiptString = null; // (reqProto.hasKiipReceipt() &&
		// reqProto.getKiipReceipt().length() >
		// 0) ? reqProto.getKiipReceipt() :
		// null;

		final String adColonyDigest = null; // (reqProto.hasAdColonyDigest() &&
		// reqProto.getAdColonyDigest().length() >
		// 0) ? reqProto.getAdColonyDigest() :
		// null;

		// // //// //TODO:
		// kiipReceiptString =
		// "{\"signature\":\"a525d6cbb8ec18d5c4e47266d736162cf18a3ff7\",\"content\":\"reward_gold\",\"quantity\":\"2\",\"transaction_id\":\"4fe924dc4972e91ed6000147\"}";
		// freeDiamondsType = EarnFreeDiamondsType.KIIP;

		final EarnFreeDiamondsResponseProto.Builder resBuilder =
		    EarnFreeDiamondsResponseProto.newBuilder();
		resBuilder.setSender(senderProto);

		svcTxManager.beginTransaction();

		try {
			final User user = RetrieveUtils.userRetrieveUtils()
			    .getUserById(senderProto.getUserUuid());
			int previousGems = 0;
			int previousCash = 0;

			final boolean legitFreeDiamondsEarn =
			    checkLegitFreeDiamondsEarnBasic(resBuilder, freeDiamondsType, clientTime, user,
			        kiipReceiptString, adColonyDigest);// ,
			                                           // adColonyAmountEarned,
			                                           // adColonyRewardType);

			// JSONObject kiipConfirmationReceipt = null;

			if (legitFreeDiamondsEarn) {
				resBuilder.setFreeDiamondsType(freeDiamondsType);

				// if (freeDiamondsType == EarnFreeDiamondsType.KIIP) {
				// kiipConfirmationReceipt =
				// getLegitKiipRewardReceipt(resBuilder, user,
				// kiipReceiptString);
				// if (kiipConfirmationReceipt == null) legitFreeDiamondsEarn =
				// false;
				// else {
				// invalidateKiipTransaction(kiipConfirmationReceipt);
				// }
				// }
				// if (freeDiamondsType == EarnFreeDiamondsType.ADCOLONY) {
				// if (!signaturesAreEqual(resBuilder, user, adColonyDigest,
				// adColonyAmountEarned, adColonyRewardType, clientTime)) {
				// legitFreeDiamondsEarn = false;
				// } else if
				// (AdColonyRecentHistoryRetrieveUtils.checkIfDuplicateDigest(adColonyDigest))
				// {
				// resBuilder.setStatus(EarnFreeDiamondsStatus.OTHER_FAIL);
				// legitFreeDiamondsEarn = false;
				// }
				// }
			}

			final EarnFreeDiamondsResponseEvent resEvent =
			    new EarnFreeDiamondsResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setEarnFreeDiamondsResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error("fatal exception in EarnFreeDiamondsController.processRequestEvent",
				    e);
			}

			if (legitFreeDiamondsEarn) {
				previousGems = user.getGems();
				previousCash = user.getCash();

				final Map<String, Integer> money = new HashMap<String, Integer>();
				final List<String> keys = new ArrayList<String>();
				// writeChangesToDB(user, freeDiamondsType,
				// kiipConfirmationReceipt, adColonyAmountEarned,
				// adColonyRewardType, money, keys);
				writeChangesToDB(user, freeDiamondsType, money, keys);
				// null PvpLeagueFromUser means will pull from hazelcast instead
				final UpdateClientUserResponseEvent resEventUpdate =
				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(user,
				        null);
				resEventUpdate.setTag(event.getTag());
				// write to client
				LOG.info("Writing event: "
				    + resEventUpdate);
				try {
					eventWriter.writeEvent(resEventUpdate);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in EarnFreeDiamondsController.processRequestEvent", e);
				}

				// writeToDBHistory(user, freeDiamondsType, clientTime,
				// kiipConfirmationReceipt, adColonyDigest, adColonyRewardType,
				// adColonyAmountEarned);
				writeToUserCurrencyHistory(user, clientTime, money, keys, freeDiamondsType,
				    previousGems, previousCash);
			}
		} catch (final Exception e) {
			LOG.error("exception in earn free gold processEvent", e);
		} finally {
			svcTxManager.commit();
		}
	}

	// private void invalidateKiipTransaction(JSONObject
	// kiipConfirmationReceipt) {
	// try {
	// oAuthService = getOAuthService();
	//
	// Token token = new Token("", "");
	//
	// OAuthRequest request = new OAuthRequest(Verb.POST,
	// KIIP_INVALIDATE_ENDPOINT);
	// request.addBodyParameter(KIIP_JSON_APP_KEY_KEY, KIIP_CONSUMER_KEY);
	// request.addBodyParameter(KIIP_JSON_RECEIPT_KEY,
	// kiipConfirmationReceipt.toString());
	// oAuthService.signRequest(token, request);
	// Response response = request.send();
	// if (response.getCode() == 200) {
	// String responseJSONString = response.getBody();
	// if (responseJSONString != null && responseJSONString.length() > 0) {
	// JSONObject kiipResponse = new JSONObject(responseJSONString);
	// if (!kiipResponse.getBoolean(KIIP_JSON_SUCCESS_KEY)) {
	// LOG.error("problem with invalidating kiip transaction with receipt " +
	// kiipConfirmationReceipt);
	// }
	// } else {
	// LOG.error("problem with invalidating kiip transaction with receipt " +
	// kiipConfirmationReceipt);
	// }
	// }
	// } catch (Exception e) {
	// LOG.error("problem with invalidating kiip transaction with receipt " +
	// kiipConfirmationReceipt, e);
	// }
	//
	// }

	// private boolean signaturesAreEqual(Builder resBuilder, User user, String
	// adColonyDigest, int adColonyAmountEarned,
	// AdColonyRewardType adColonyRewardType, Timestamp clientTime) {
	// String serverAdColonyDigest = null;
	// String prepareString = user.getId() + user.getReferralCode() +
	// adColonyAmountEarned + adColonyRewardType.getNumber() +
	// clientTime.getTime();
	//
	// serverAdColonyDigest = getHMACSHA1DigestWithLVL6Secret(prepareString);
	//
	// if (serverAdColonyDigest == null ||
	// !serverAdColonyDigest.equals(adColonyDigest)) {
	// resBuilder.setStatus(EarnFreeDiamondsStatus.OTHER_FAIL);
	// LOG.error("failure in confirming adColony digest. server's digest is " +
	// serverAdColonyDigest
	// + ", client's is " + adColonyDigest);
	// return false;
	// }
	// return true;
	// }

	// private JSONObject getLegitKiipRewardReceipt(Builder resBuilder, User
	// user, String kiipReceipt) {
	//
	// try {
	// oAuthService = getOAuthService();
	//
	// Token token = new Token("", "");
	//
	// OAuthRequest request = new OAuthRequest(Verb.POST, KIIP_VERIFY_ENDPOINT);
	// request.addBodyParameter(KIIP_JSON_APP_KEY_KEY, KIIP_CONSUMER_KEY);
	// request.addBodyParameter(KIIP_JSON_RECEIPT_KEY, kiipReceipt);
	// oAuthService.signRequest(token, request);
	// Response response = request.send();
	//
	// if (response.getCode() == 200) {
	// String responseJSONString = response.getBody();
	// if (responseJSONString != null && responseJSONString.length() > 0) {
	// JSONObject kiipResponse = new JSONObject(responseJSONString);
	// if (kiipResponse.getBoolean(KIIP_JSON_SUCCESS_KEY))
	// return kiipResponse.getJSONObject(KIIP_JSON_RECEIPT_KEY);
	// }
	// }
	// } catch (Exception e) {
	// resBuilder.setStatus(EarnFreeDiamondsStatus.OTHER_FAIL);
	// LOG.error("problem with checking kiip reward", e);
	// return null;
	// }
	// resBuilder.setStatus(EarnFreeDiamondsStatus.OTHER_FAIL);
	// LOG.error("problem with getting kiip Receipt, input kiipreceipt is=" +
	// kiipReceipt);
	// return null;
	// }

	// private void writeChangesToDB(User user, EarnFreeDiamondsType
	// freeDiamondsType, JSONObject kiipReceipt, int adColonyAmountEarned,
	// AdColonyRewardType adColonyRewardType, Map<String, Integer> money,
	// List<String> keys) throws JSONException {
	private void writeChangesToDB( final User user,
	    final EarnFreeDiamondsType freeDiamondsType, final Map<String, Integer> money,
	    final List<String> keys )
	{
		// if (freeDiamondsType == EarnFreeDiamondsType.KIIP) {
		// int diamondChange = kiipReceipt.getInt(KIIP_JSON_QUANTITY_KEY);
		// if (!user.updateRelativeDiamondsForFree(diamondChange,
		// freeDiamondsType)) {
		// LOG.error("problem with updating diamonds. diamondChange=" +
		// diamondChange
		// + ", freeDiamondsType=" + freeDiamondsType);
		// } else {
		// String key = MiscMethods.gold;
		// money.put(key, diamondChange);
		// keys.add(key);
		// }
		// }
		// if (freeDiamondsType == EarnFreeDiamondsType.ADCOLONY) {
		// if (adColonyRewardType == AdColonyRewardType.DIAMONDS) {
		// if (!user.updateRelativeDiamondsForFree(adColonyAmountEarned,
		// freeDiamondsType)) {
		// LOG.error("problem with updating diamonds. diamondChange=" +
		// adColonyAmountEarned
		// + ", freeDiamondsType=" + freeDiamondsType);
		// } else {
		// String key = MiscMethods.gold;
		// money.put(key, adColonyAmountEarned);
		// keys.add(key);
		// }
		// } else if (adColonyRewardType == AdColonyRewardType.COINS) {
		// if
		// (!user.updateRelativeCoinsAdcolonyvideoswatched(adColonyAmountEarned,
		// 1)) {
		// LOG.error("problem with updating coins. coin change=" +
		// adColonyAmountEarned
		// + ", Adcolonyvideoswatched=" + 1);
		// } else {
		// String key = MiscMethods.silver;
		// money.put(key, adColonyAmountEarned);
		// keys.add(key);
		// }
		// }
		// }
		if (EarnFreeDiamondsType.FB_CONNECT == freeDiamondsType) {
			final int diamondChange = ControllerConstants.EARN_FREE_DIAMONDS__FB_CONNECT_REWARD;
			if (!user.updateRelativeDiamondsForFree(diamondChange, freeDiamondsType)) {
				LOG.error("unexpected error: user was not awarded for connecting to facebook");
			} else {
				final String key = MiscMethods.gems;
				money.put(key, diamondChange);
				keys.add(key);
			}
		}
	}

	// private void writeToDBHistory(User user, EarnFreeDiamondsType
	// freeDiamondsType, Timestamp clientTime, JSONObject
	// kiipConfirmationReceipt, String adColonyDigest,
	// AdColonyRewardType adColonyRewardType, int adColonyAmountEarned) {
	// if (freeDiamondsType == EarnFreeDiamondsType.KIIP) {
	// try {
	// String content =
	// kiipConfirmationReceipt.getString(KIIP_JSON_CONTENT_KEY);
	// String signature =
	// kiipConfirmationReceipt.getString(KIIP_JSON_SIGNATURE_KEY);
	// int quantity = kiipConfirmationReceipt.getInt(KIIP_JSON_QUANTITY_KEY);
	// String transactionId =
	// kiipConfirmationReceipt.getString(KIIP_JSON_TRANSACTION_ID_KEY);
	//
	// if (!InsertUtils.get().insertKiipHistory(user.getId(), clientTime,
	// content, signature, quantity, transactionId)) {
	// LOG.error("problem with saving kiip reward into history. user=" + user +
	// ", clientTime=" + clientTime
	// + ", kiipConfirmationReceipt=" + kiipConfirmationReceipt);
	// }
	// } catch (Exception e) {
	// LOG.error("problem with trying to save kiip reward in db. kiipConfirmationReceipt="
	// + kiipConfirmationReceipt);
	// }
	// }
	// if (freeDiamondsType == EarnFreeDiamondsType.ADCOLONY) {
	// if (!InsertUtils.get().insertAdcolonyRecentHistory(user.getId(),
	// clientTime, adColonyAmountEarned, adColonyRewardType, adColonyDigest)) {
	// LOG.error("problem with saving adcolony rewarding into recent history. user="
	// + user + ", clientTime=" + clientTime
	// + ", amountEarned=" + adColonyAmountEarned + ", adColonyRewardType=" +
	// adColonyRewardType + ", digest=" + adColonyDigest);
	// }
	// }
	// }

	private boolean checkLegitFreeDiamondsEarnBasic( final Builder resBuilder,
	    final EarnFreeDiamondsType freeDiamondsType, final Timestamp clientTime,
	    final User user, final String kiipReceiptString, final String adColonyDigest )
	{ // , int adColonyDiamondsEarned, AdColonyRewardType adColonyRewardType) {
		if ((freeDiamondsType == null)
		    || (clientTime == null)
		    || (user == null)) {
			resBuilder.setStatus(EarnFreeDiamondsStatus.OTHER_FAIL);
			LOG.error("parameter passed in is null. freeDiamondsType is "
			    + freeDiamondsType
			    + ", clientTime="
			    + clientTime
			    + ", user="
			    + user);
			return false;
		}
		if (!MiscMethods.checkClientTimeAroundApproximateNow(clientTime)) {
			resBuilder.setStatus(EarnFreeDiamondsStatus.CLIENT_TOO_APART_FROM_SERVER_TIME);
			LOG.error("client time too apart of server time. client time="
			    + clientTime
			    + ", servertime~="
			    + new Date());
			return false;
		}
		/*
		 * if (freeDiamondsType == EarnFreeDiamondsType.KIIP) { if
		 * (!checkLegitKiipRedeem(resBuilder, kiipReceiptString)) { return
		 * false; } } else if (freeDiamondsType ==
		 * EarnFreeDiamondsType.ADCOLONY) { if
		 * (!checkLegitAdColonyRedeem(resBuilder, adColonyDigest,
		 * adColonyDiamondsEarned, adColonyRewardType, user, clientTime)) {
		 * return false; } // } else if (freeDiamondsType ==
		 * EarnFreeDiamondsType.FB_INVITE) { // } else if (freeDiamondsType ==
		 * EarnFreeDiamondsType.TAPJOY) { // } else if (freeDiamondsType ==
		 * EarnFreeDiamondsType.FLURRY_VIDEO) { // } else if (freeDiamondsType
		 * == EarnFreeDiamondsType.TWITTER) { } else
		 */
		if (EarnFreeDiamondsType.FB_CONNECT == freeDiamondsType) {
			if (user.isHasReceivedfbReward()) {
				LOG.error("user error: user already received fb connect diamonds");
				return false;
			}
		} else {
			resBuilder.setStatus(EarnFreeDiamondsStatus.METHOD_NOT_SUPPORTED);
			LOG.error("earn free gold type passed in not supported. type="
			    + freeDiamondsType);
			return false;
		}
		resBuilder.setStatus(EarnFreeDiamondsStatus.SUCCESS);
		return true;
	}

	// private boolean checkLegitAdColonyRedeem(Builder resBuilder, String
	// adColonyDigest, int adColonyAmountEarned, AdColonyRewardType
	// adColonyRewardType, User user, Timestamp clientTime) {
	// if (adColonyDigest == null || (adColonyRewardType !=
	// AdColonyRewardType.DIAMONDS && adColonyRewardType !=
	// AdColonyRewardType.COINS)) {
	// resBuilder.setStatus(EarnFreeDiamondsStatus.OTHER_FAIL);
	// LOG.error("no digest given for AdColony");
	// return false;
	// }
	// if (adColonyRewardType == AdColonyRewardType.DIAMONDS) {
	// if ((user.getNumAdColonyVideosWatched()+1) %
	// ControllerConstants.EARN_FREE_DIAMONDS__NUM_VIDEOS_FOR_DIAMOND_REWARD !=
	// 0) {
	// resBuilder.setStatus(EarnFreeDiamondsStatus.OTHER_FAIL);
	// LOG.error("not supposed to get diamonds yet, user before this try has only watched "
	// + user.getNumAdColonyVideosWatched() + " videos");
	// return false;
	// }
	// }
	// if (adColonyAmountEarned <= 0) {
	// resBuilder.setStatus(EarnFreeDiamondsStatus.OTHER_FAIL);
	// LOG.error("<= 0 diamonds given from AdColony");
	// return false;
	// }
	// return true;
	// }

	// private boolean checkLegitKiipRedeem(Builder resBuilder, String
	// kiipReceiptString) {
	// if (kiipReceiptString == null) {
	// resBuilder.setStatus(EarnFreeDiamondsStatus.OTHER_FAIL);
	// LOG.error("kiip receipt passed in is null");
	// return false;
	// }
	// JSONObject kiipJSONReceipt;
	// try {
	// kiipJSONReceipt = new JSONObject(kiipReceiptString);
	// if (kiipJSONReceipt.getInt(KIIP_JSON_QUANTITY_KEY) <= 0 ||
	// kiipJSONReceipt.getString(KIIP_JSON_CONTENT_KEY).length() <= 0 ||
	// kiipJSONReceipt.getString(KIIP_JSON_TRANSACTION_ID_KEY).length() <= 0 ||
	// kiipJSONReceipt.getString(KIIP_JSON_SIGNATURE_KEY).length() <= 0) {
	// resBuilder.setStatus(EarnFreeDiamondsStatus.OTHER_FAIL);
	// LOG.error("kiip receipt passed in quantity may be <=0. kiipReceiptString="
	// + kiipReceiptString);
	// return false;
	// }
	// } catch (JSONException e) {
	// resBuilder.setStatus(EarnFreeDiamondsStatus.OTHER_FAIL);
	// LOG.error("kiip receipt passed in has an error. kiipReceiptString=" +
	// kiipReceiptString, e);
	// return false;
	// } catch (Exception e) {
	// resBuilder.setStatus(EarnFreeDiamondsStatus.OTHER_FAIL);
	// LOG.error("kiip receipt passed in has an error. kiipReceiptString=" +
	// kiipReceiptString, e);
	// return false;
	// }
	// return true;
	// }

	//
	// private String getHMACSHA1DigestWithLVL6Secret(String prepareString) {
	// try {
	// Mac mac = getHMACSHA1WithLVL6Secret();
	// if (mac == null) return null;
	//
	// byte[] text = prepareString.getBytes();
	//
	// return new String(Base64.encodeBase64(mac.doFinal(text))).trim();
	// } catch (Exception e) {
	// LOG.error("exception when trying to create hash for " + prepareString,
	// e);
	// return null;
	// }
	// }

	// private Mac getHMACSHA1WithLVL6Secret() {
	// if (hmacSHA1WithLVL6Secret == null) {
	// SecretKey secretKey = null;
	//
	// byte[] keyBytes = LVL6_SHARED_SECRET.getBytes();
	// secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
	//
	// try {
	// hmacSHA1WithLVL6Secret = Mac.getInstance("HmacSHA1");
	// hmacSHA1WithLVL6Secret.init(secretKey);
	// } catch (Exception e) {
	// LOG.error("exception when trying to create mac with our secret", e);
	// return null;
	// }
	// }
	// return hmacSHA1WithLVL6Secret;
	// }

	// private OAuthService getOAuthService() {
	// if (oAuthService == null) {
	// oAuthService = new ServiceBuilder()
	// .provider(TwoLeggedOAuth.class)
	// .apiKey(KIIP_CONSUMER_KEY)
	// .apiSecret(KIIP_CONSUMER_SECRET)
	// .build();
	// }
	// return oAuthService;
	// }

	private void writeToUserCurrencyHistory( final User aUser, final Timestamp date,
	    final Map<String, Integer> money, final List<String> keys,
	    final EarnFreeDiamondsType freeDiamondsType, final int previousGems,
	    final int previousCash )
	{
		try {
			if (keys.isEmpty()) {
				return;
			}
			final String userUuid = aUser.getId();
			final String resourceType = keys.get(0);
			final int currencyChange = money.get(resourceType);
			int previousCurrency = previousGems;
			int currencyAfter = aUser.getGems();
			String reasonForChange = "earn free diamonds controller";
			final String details = "";

			if (resourceType.equals(MiscMethods.cash)) {
				previousCurrency = previousCash;
				currencyAfter = aUser.getCash();
			}

			/*
			 * if (freeDiamondsType == EarnFreeDiamondsType.KIIP) {
			 * reasonForChange =
			 * ControllerConstants.UCHRFC__EARN_FREE_DIAMONDS_KIIP; } else if
			 * (freeDiamondsType == EarnFreeDiamondsType.ADCOLONY) {
			 * reasonForChange =
			 * ControllerConstants.UCHRFC__EARN_FREE_DIAMONDS_ADCOLONY; } else
			 */
			if (EarnFreeDiamondsType.FB_CONNECT == freeDiamondsType) {
				reasonForChange = ControllerConstants.UCHRFC__EARN_FREE_DIAMONDS_FB_CONNECT;
			}

			final int inserted =
			    InsertUtils.get()
			        .insertIntoUserCurrencyHistory(userUuid, date, resourceType,
			            currencyChange, previousCurrency, currencyAfter, reasonForChange,
			            details);

			LOG.info("Should be 1. Rows inserted into user_currency_history: "
			    + inserted);
		} catch (final Exception e) {
			LOG.error("Maybe table's not there or duplicate keys? ", e);
		}
	}

}
