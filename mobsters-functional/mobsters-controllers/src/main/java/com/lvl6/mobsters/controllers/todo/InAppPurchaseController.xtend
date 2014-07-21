//package com.lvl6.mobsters.controllers.todo
//
//import com.amazonaws.util.json.JSONObject
//import com.lvl6.mobsters.dynamo.User
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.InAppPurchaseRequestProto
//import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.InAppPurchaseResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.InAppPurchaseResponseProto.InAppPurchaseStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.InAppPurchaseRequestEvent
//import com.lvl6.mobsters.events.response.InAppPurchaseResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.server.EventController
//import com.lvl6.properties.Globals
//import com.lvl6.properties.IAPValues
//import com.lvl6.properties.KabamProperties
//import java.io.BufferedReader
//import java.io.InputStreamReader
//import java.io.OutputStreamWriter
//import java.net.URL
//import java.security.NoSuchAlgorithmException
//import java.sql.Timestamp
//import java.util.ArrayList
//import java.util.Date
//import java.util.HashMap
//import java.util.List
//import org.apache.http.Consts
//import org.apache.http.NameValuePair
//import org.apache.http.client.entity.UrlEncodedFormEntity
//import org.apache.http.client.methods.HttpPost
//import org.apache.http.impl.client.DefaultHttpClient
//import org.apache.http.message.BasicNameValuePair
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class InAppPurchaseController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(InAppPurchaseController))
//	static val SANDBOX_URL = 'https://sandbox.itunes.apple.com/verifyReceipt'
//	static val PRODUCTION_URL = 'https://buy.itunes.apple.com/verifyReceipt'
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//	@Autowired
//	protected var InsertUtil insertUtils
//
//	def setInsertUtils(InsertUtil insertUtils)
//	{
//		this.insertUtils = insertUtils
//	}
//
//	new()
//	{
//		numAllocatedThreads = 2
//	}
//
//	override createRequestEvent()
//	{
//		new InAppPurchaseRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_IN_APP_PURCHASE_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as InAppPurchaseRequestEvent)).inAppPurchaseRequestProto
//		val senderProto = reqProto.sender
//		val receipt = reqProto.receipt
//		val resBuilder = InAppPurchaseResponseProto::newBuilder
//		resBuilder.sender = senderProto
//		resBuilder.receipt = reqProto.receipt
//		svcTxManager.beginTransaction
//		try
//		{
//			val user = RetrieveUtils::userRetrieveUtils.getUserById(senderProto.userUuid)
//			var JSONObject response
//			val jsonReceipt = new JSONObject()
//			jsonReceipt.put(IAPValues::RECEIPT_DATA, receipt)
//			LOG.info('Processing purchase: ' + jsonReceipt.toString(4))
//			var url = new URL(PRODUCTION_URL)
//			LOG.info('Sending purchase request to: ' + url.toString)
//			var conn = url.openConnection
//			conn.doOutput = true
//			var wr = new OutputStreamWriter(conn.outputStream)
//			wr.write(jsonReceipt.toString)
//			wr.flush
//			var rd = new BufferedReader(new InputStreamReader(conn.inputStream))
//			var responseString = ''
//			var String line
//			while ((line = rd.readLine) !== null)
//			{
//				responseString += line
//			}
//			LOG.info('Response: ' + responseString)
//			response = new JSONObject(responseString)
//			if ((response.getInt(IAPValues::STATUS) === 21007) ||
//				(response.getInt(IAPValues::STATUS) === 21008))
//			{
//				wr.close
//				rd.close
//				url = new URL(SANDBOX_URL)
//				conn = url.openConnection
//				conn.doOutput = true
//				wr = new OutputStreamWriter(conn.outputStream)
//				wr.write(jsonReceipt.toString)
//				wr.flush
//				rd = new BufferedReader(new InputStreamReader(conn.inputStream))
//				responseString = ''
//				while ((line = rd.readLine) !== null)
//				{
//					responseString += line
//				}
//				response = new JSONObject(responseString)
//			}
//			var JSONObject receiptFromApple = null
//			if (response.getInt(IAPValues::STATUS) === 0)
//			{
//				receiptFromApple = response.getJSONObject(IAPValues::RECEIPT)
//				if (!IAPHistoryRetrieveUtils::
//					checkIfDuplicateTransaction(
//						Long::parseLong(receiptFromApple.getString(IAPValues::TRANSACTION_ID))))
//				{
//					try
//					{
//						val packageName = receiptFromApple.getString(IAPValues::PRODUCT_ID)
//						val diamondChange = IAPValues::getDiamondsForPackageName(packageName)
//						val coinChange = IAPValues::getCoinsForPackageName(packageName)
//						val realLifeCashCost = IAPValues::
//							getCashSpentForPackageName(packageName)
//						val isBeginnerSale = IAPValues::packageIsBeginnerSale(packageName)
//						val previousCurrency = new HashMap<String, Integer>()
//						val currencyChangeMap = new HashMap<String, Integer>()
//						if (diamondChange > 0)
//						{
//							previousCurrency.put(MiscMethods::gems, user.gems)
//							resBuilder.diamondsGained = diamondChange
//							user.
//								updateRelativeDiamondsBeginnerSale(diamondChange, isBeginnerSale)
//							currencyChangeMap.put(MiscMethods::gems, diamondChange)
//						}
//						else
//						{
//							previousCurrency.put(MiscMethods::cash, user.cash)
//							resBuilder.coinsGained = coinChange
//							user.updateRelativeCoinsBeginnerSale(coinChange, isBeginnerSale)
//							currencyChangeMap.put(MiscMethods::cash, coinChange)
//						}
//						if (!insertUtils.insertIAPHistoryElem(receiptFromApple, diamondChange,
//							coinChange, user, realLifeCashCost))
//						{
//							LOG.error(
//								'problem with logging in-app purchase history for receipt:' +
//									receiptFromApple.toString(4) + ' and user ' + user)
//						}
//						resBuilder.status = InAppPurchaseStatus.SUCCESS
//						resBuilder.packageName = receiptFromApple.getString(
//							IAPValues::PRODUCT_ID)
//						resBuilder.packagePrice = realLifeCashCost
//						LOG.info(
//							'successful in-app purchase from user ' + user.id + ' for package ' +
//								receiptFromApple.getString(IAPValues::PRODUCT_ID))
//						val date = new Timestamp((new Date()).time)
//						writeToUserCurrencyHistory(user, packageName, date, currencyChangeMap,
//							previousCurrency)
//					}
//					catch (Exception e)
//					{
//						LOG.error('problem with in app purchase flow', e)
//					}
//				}
//				else
//				{
//					resBuilder.status = InAppPurchaseStatus.DUPLICATE_RECEIPT
//					LOG.error('duplicate receipt from user ' + user)
//				}
//			}
//			else
//			{
//				LOG.error(
//					'problem with in-app purchase that client sent, with receipt ' + receipt)
//			}
//			wr.close
//			rd.close
//			if (!resBuilder.status)
//			{
//				resBuilder.status = InAppPurchaseStatus.FAIL
//			}
//			val resProto = resBuilder.build
//			val resEvent = new InAppPurchaseResponseEvent(senderProto.userUuid)
//			resEvent.tag = event.tag
//			resEvent.inAppPurchaseResponseProto = resProto
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error('fatal exception in InAppPurchaseController.processRequestEvent', e)
//			}
//			if (Globals::KABAM_ENABLED)
//			{
//				if ((receiptFromApple !== null) &&
//					(resBuilder.status === InAppPurchaseStatus::SUCCESS))
//				{
//					val logJson = getKabamJsonLogObject(reqProto, resBuilder, receiptFromApple)
//					val queryParams = getKabamQueryParams(receipt, user, logJson)
//					doKabamPost(queryParams, 0)
//				}
//			}
//			val resEventUpdate = MiscMethods::
//				createUpdateClientUserResponseEventAndUpdateLeaderboard(user, null)
//			resEventUpdate.tag = event.tag
//			LOG.info('Writing event: ' + resEventUpdate)
//			try
//			{
//				eventWriter.writeEvent(resEventUpdate)
//			}
//			catch (Throwable e)
//			{
//				LOG.error('fatal exception in InAppPurchaseController.processRequestEvent', e)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in InAppPurchaseController processEvent', e)
//		}
//		finally
//		{
//			svcTxManager.commit
//		}
//	}
//
//	private def doKabamPost(List<NameValuePair> queryParams, int numTries)
//	{
//		LOG.info('Posting to Kabam')
//		val host = if (Globals::IS_SANDBOX)
//				KabamProperties.SANDBOX_PAYMENT_URL
//			else
//				KabamProperties.PRODUCTION_PAYMENT_URL
//		val client = new DefaultHttpClient()
//		val post = new HttpPost(host)
//		try
//		{
//			LOG.info('Sending post query: ' + queryParams)
//			post.entity = new UrlEncodedFormEntity(queryParams, Consts::UTF_8)
//			val response = client.execute(post)
//			val rd = new BufferedReader(new InputStreamReader(response.entity.content))
//			var responseString = ''
//			var String line
//			while ((line = rd.readLine) !== null)
//			{
//				responseString += line
//			}
//			LOG.info('Received response: ' + responseString)
//			val jsonResponse = new JSONObject(responseString)
//			if (!jsonResponse.getBoolean('success'))
//			{
//				LOG.error(
//					'Failed to log kabam payment with errorcode: ' +
//						jsonResponse.getInt('errorcode') + ' and errormessage: ' +
//						jsonResponse.getString('errormessage'))
//				if (numTries < 10)
//				{
//					doKabamPost(queryParams, numTries + 1)
//				}
//				else
//				{
//					LOG.error('Giving up..')
//				}
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('Error doing Kabam post', e)
//		}
//	}
//
//	private def getKabamQueryParams(String receipt, User user, JSONObject logJson)
//	throws NoSuchAlgorithmException {
//		LOG.info('Generating Post parameters')
//		val gameid = if (Globals::IS_SANDBOX)
//				KabamProperties.SANDBOX_CLIENT_ID
//			else
//				KabamProperties.PRODUCTION_CLIENT_ID
//		val secret = if (Globals::IS_SANDBOX)
//				KabamProperties.SANDBOX_SECRET
//			else
//				KabamProperties.PRODUCTION_SECRET
//		val time = new Date().time / 1000
//		val queryParams = new ArrayList<NameValuePair>()
//		queryParams.add(new BasicNameValuePair('gameid', '' + gameid))
//		queryParams.add(new BasicNameValuePair('log', logJson.toString))
//		queryParams.add(new BasicNameValuePair('mobileid', user.udid))
//		queryParams.add(new BasicNameValuePair('receipt', receipt))
//		queryParams.add(new BasicNameValuePair('timestamp', '' + time))
//		queryParams.add(new BasicNameValuePair('userid', '' + user.kabamNaid))
//		var str = ''
//		for (key : queryParams)
//		{
//			str += key.name + key.value
//		}
//		str += secret
//		queryParams.add(new BasicNameValuePair('sig', sha1(str)))
//		queryParams
//	}
//
//	private def getKabamJsonLogObject(InAppPurchaseRequestProto reqProto,
//		InAppPurchaseResponseProto ::Builder resBuilder
//,   JSONObject receiptFromApple) throws JSONException {
//    val logParams=new TreeMap<String,Object>()
//    logParams.put('serverid', '1')
//    logParams.put('localcents', reqProto.localcents)
//    logParams.put('localcurrency', reqProto.localcurrency)
//    logParams.put('igc', if(resBuilder.diamondsGained) resBuilder.diamondsGained else resBuilder.coinsGained)
//    logParams.put('igctype', if(resBuilder.diamondsGained) 'gold' else 'silver')
//    logParams.put('transactionid', receiptFromApple.get(IAPValues::TRANSACTION_ID))
//    logParams.put('platform', 'itunes')
//    logParams.put('locale', reqProto.locale)
//    logParams.put('lang', 'en')
//    logParams.put('ipaddr', reqProto.ipaddr)
//    val logJson=new JSONObject(logParams)
//    logJson
//  }
//
//private
//
//static def sha1(  String input) throws NoSuchAlgorithmException {
//    val mDigest=MessageDigest::getInstance('SHA1')
//    val result=mDigest.digest(input.bytes)
//    val sb=new StringBuilder()
//    for (     element : result) {
//      sb.append(Integer::toString((element.bitwiseAnd(0xff)) + 0x100, 16).substring(1))
//    }
//    sb.toString
//  }
//
//private def writeToUserCurrencyHistory(  User aUser,   String packageName,   Timestamp date,   Map<String,Integer> currencyChangeMap,   Map<String,Integer> previousCurrency){
//    val userUuid=aUser.id
//    val currentCurrencyMap=new HashMap<String,Integer>()
//    val changeReasonsMap=new HashMap<String,String>()
//    val detailsMap=new HashMap<String,String>()
//    val gems=MiscMethods::gems
//    val cash=MiscMethods::cash
//    val reasonForChange=ControllerConstants.UCHRFC__IN_APP_PURCHASE
//    currentCurrencyMap.put(gems, aUser.gems)
//    currentCurrencyMap.put(cash, aUser.cash)
//    changeReasonsMap.put(gems, reasonForChange)
//    changeReasonsMap.put(cash, reasonForChange)
//    detailsMap.put(gems, packageName)
//    detailsMap.put(cash, packageName)
//    MiscMethods::writeToUserCurrencyOneUser(userUuid, date, currencyChangeMap, previousCurrency, currentCurrencyMap, changeReasonsMap, detailsMap)
//  }
//}
