package com.lvl6.mobsters.dynamo.tests.manual;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.transactions.Transaction;
import com.amazonaws.services.dynamodbv2.transactions.Transaction.IsolationLevel;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.lvl6.mobsters.dynamo.setup.Lvl6Transaction;
import com.lvl6.mobsters.dynamo.tests.manual.SingleHashKeyStrategy.ChildOne;
import com.lvl6.mobsters.dynamo.tests.manual.SingleHashKeyStrategy.ParentOne;

@Component
public class SingleHashKeyRepository extends
    BaseParentChildRepository<ParentOne, ChildOne>
{
	@SuppressWarnings("unused")
    private static Logger LOG = LoggerFactory.getLogger(SingleHashKeyRepository.class);

	public SingleHashKeyRepository()
	{
		super(ParentOne.class, ChildOne.class);
	}

	@Override
	public void saveParent( final ParentOne obj )
	{
		final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
		if (t1 != null) {
			t1.save(obj);
		} else {
			mapper.save(obj);
		}
	}

	@Override
	public ParentOne loadParent( final String hashKey )
	{
		final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
		ParentOne retVal;
		if (t1 != null) {
			retVal = t1.load(pClass, hashKey);
		} else {
			retVal = repoTxManager.load(pClass, hashKey, IsolationLevel.COMMITTED);
		}

		return retVal;
	}

	@Override
	public void deleteParent( final ParentOne item )
	{
		final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
		if (t1 != null) {
			t1.delete(item);
		} else {
			mapper.delete(item);
		}
	}

	@Override
	public void saveChild( final String parentHashKey, final ChildOne obj )
	{
		obj.setUserId(parentHashKey);
		obj.setId(UUID.randomUUID().toString());
		final Transaction t1 = repoTxManager.getActiveTransaction();
		if (t1 != null) {
			t1.save(obj);
		} else {
			mapper.save(obj);
		}
	}

	@Override
	public void saveChildren( final String parentHashKey, final Iterable<ChildOne> children )
	{
		final Transaction t1 = repoTxManager.getActiveTransaction();
		if (t1 != null) {
			for (final ChildOne obj : children) {
				obj.setUserId(parentHashKey);
				obj.setId(UUID.randomUUID().toString());
				t1.save(obj);
			}
		} else {
			for (final ChildOne obj : children) {
				// Avoiding batchSave() because it does not check 
				// optimistic lock versions.
				obj.setUserId(parentHashKey);
				obj.setId(UUID.randomUUID().toString());
				mapper.save(obj);
				
				System.out.println(
					String.format("Saved child<%s> of parent <%s>=<%s>\n", obj.getId(), parentHashKey, obj.getUserId()));
			}
		}
	}

	@Override
	public ChildOne loadChild( final String parentHashKey, final String childId )
	{
		final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
		ChildOne retVal;
		if (t1 != null) {
			retVal = t1.load(cClass, parentHashKey, childId);
		} else {
			retVal =
			    repoTxManager.load(cClass, parentHashKey, childId, IsolationLevel.COMMITTED);
		}

		return retVal;
	}

	@Override
	public List<ChildOne> loadChildren( final String parentHashKey,
	    final Iterable<String> childIds )
	{
		final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
		final ArrayList<ChildOne> retVal = new ArrayList<>();
		if (t1 != null) {
			for (final String childRangeKey : childIds) {
				retVal.add(t1.load(cClass, parentHashKey, childRangeKey));
			}
		} else {
			for (final String childRangeKey : childIds) {
				retVal.add(repoTxManager.load(cClass, parentHashKey, childRangeKey,
				    IsolationLevel.COMMITTED));
			}
		}

		return retVal;
	}

	/**
	 * With this implementation, there is no way to achieve a transactional
	 * read because the Query API is not offered and there is no way to
	 * identify the set of records to read without it since no such list is
	 * maintained in object state.
	 */
	@Override
	public List<ChildOne> loadAllChildren( final String parentHashKey )
	{
		final ChildOne hashKey = new ChildOne();
		hashKey.setUserId(parentHashKey);

		final DynamoDBQueryExpression<ChildOne> query =
		    new DynamoDBQueryExpression<ChildOne>().withHashKeyValues(hashKey)
		        /*.withRangeKeyCondition("id",
		            new Condition().withComparisonOperator(ComparisonOperator.LT)
		                .withAttributeValueList(new AttributeValue("z")))*/
		        .withConsistentRead(true);
		// LOG.info("Query: {}", query.toString());
		final PaginatedQueryList<ChildOne> retVal = childQuery(query);
		retVal.loadAllResults();
		return retVal;
	}

	private static Function<ChildOne, String> CHILD_TO_ID_FUNCTION =
	    new Function<ChildOne, String>() {
		    @Override
		    public String apply( final ChildOne input )
		    {
			    return input.getId();
		    }
	    };

	@Override
	public List<String> getAllChildren( final String parentHashKey )
	{
		return convertToIds(loadAllChildren(parentHashKey));
	}

	@Override
	public void deleteChild( final String parentHashKey, final ChildOne child )
	{
		// Read->Verify->Write->Commit transaction pattern as
		// alternative to Expected Check Constraints on DeleteItemRequest
		// low-level API call.
		final ChildOne obj = loadChild(parentHashKey, child.getId());
		if (obj != null) {
			final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
			if (t1 != null) {
				t1.delete(obj);
			} else {
				// TODO: Just because we read obj in a previous transaction
				// with COMMITTED isolation level does not mean its still in
				// a delete-ready state b/c the transaction used to perform
				// that COMMITTED read has ended by now.
				//
				// We _should_ fail here by now, but lets not. There IS a
				// better solution, in the form a of delete/save helpers on
				// TxManager that launch a temporary transaction and use it
				// to encapsulate a read followed by verify and delete steps
				// _before closing the transaction or returning_.
				//
				// The tough part in that chain is generalizing the
				// validation step between load and save without returning
				// the control flow back to the user by method return. What
				// that tells me is that the validation logic needs be
				// captured in a caller-provided lambda, to be given the
				// result of the load and either accepting or rejecting it
				// as suitable for proceeding with the rest of the delete
				// action.
				mapper.delete(obj);
			}
		}
	}

	@Override
	public void deleteChildren( final String parentHashKey,
	    final Iterable<ChildOne> childIds )
	{
		// Read->Verify->Write->Commit transaction pattern as
		// alternative to Expected Check Constraints on DeleteItemRequest
		// low-level API call.
		final List<ChildOne> objList =
		    loadChildren(parentHashKey, FluentIterable.from(childIds)
		        .transform(CHILD_TO_ID_FUNCTION));
		final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
		if (t1 != null) {
			for (final ChildOne obj : objList) {
				t1.delete(obj);
			}
		} else {
			// TODO: See comment block in deleteChild(). Same applies
			// here too.
			for (final ChildOne obj : objList) {
				mapper.delete(obj);
			}
		}
	}

	@Override
	public List<ChildOne> deleteAllChildren( final String parentHashKey )
	{
		// Read->Verify->Write->Commit transaction pattern as
		// alternative to Expected Check Constraints on DeleteItemRequest
		// low-level API call.
		final List<ChildOne> objList = loadAllChildren(parentHashKey);
		final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
		if (t1 != null) {
			for (final ChildOne obj : objList) {
				t1.delete(obj);
			}
		} else {
			// TODO: See comment block in deleteChild(). Same applies
			// here too.
			for (final ChildOne obj : objList) {
				mapper.delete(obj);
			}
		}

		return objList;
	}

	@Override
	public List<String> removeAllChildren( final String parentHashKey )
	{
		return convertToIds(deleteAllChildren(parentHashKey));
	}

	private List<String> convertToIds( final Iterable<ChildOne> objIter )
	{
		final ArrayList<String> retVal = new ArrayList<String>();
		Iterables.addAll(retVal, FluentIterable.from(objIter)
		    .transform(CHILD_TO_ID_FUNCTION));
		return retVal;
	}
}