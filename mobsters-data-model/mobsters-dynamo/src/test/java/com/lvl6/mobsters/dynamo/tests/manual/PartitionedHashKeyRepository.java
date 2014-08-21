package com.lvl6.mobsters.dynamo.tests.manual;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.transactions.Transaction;
import com.amazonaws.services.dynamodbv2.transactions.Transaction.IsolationLevel;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.lvl6.mobsters.dynamo.setup.Lvl6Transaction;
import com.lvl6.mobsters.dynamo.tests.manual.PartitionedHashKeyStrategy.ChildTwo;
import com.lvl6.mobsters.dynamo.tests.manual.PartitionedHashKeyStrategy.ParentTwo;

@Component
public class PartitionedHashKeyRepository
	extends BaseParentChildRepository<ParentTwo, ChildTwo>
{
	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory
			.getLogger(PartitionedHashKeyRepository.class);
	private final int numParts;
	private final ExecutorService threadPool = Executors.newCachedThreadPool();

	public PartitionedHashKeyRepository()
	{
		super(ParentTwo.class, ChildTwo.class);
		numParts = 2;
	}

	public PartitionedHashKeyRepository(final int numParts)
	{
		super(ParentTwo.class, ChildTwo.class);
		this.numParts = numParts;
	}

	@Override
	public void saveParent(final ParentTwo obj) {
		final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
		if (t1 != null) {
			t1.save(obj);
		} else {
			mapper.save(obj);
		}
	}

	@Override
	public ParentTwo loadParent(final String hashKey) {
		final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
		ParentTwo retVal;
		if (t1 != null) {
			retVal = t1.load(pClass, hashKey);
		} else {
			retVal = repoTxManager.load(pClass, hashKey,
					IsolationLevel.COMMITTED);
		}

		return retVal;
	}

	@Override
	public void deleteParent(final ParentTwo item) {
		// TODO: Clean up the child table as well!

		final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
		if (t1 != null) {
			t1.delete(item);
		} else {
			mapper.delete(item);
		}
	}

	@Override
	public void saveChild(final String parentHashKey, final ChildTwo obj) {
		setPartitionedKey(parentHashKey, obj);
		final Transaction t1 = repoTxManager.getActiveTransaction();
		if (t1 != null) {
			t1.save(obj);
		} else {
			mapper.save(obj);
		}
	}

	@Override
	public void saveChildren(final String parentHashKey,
			final Iterable<ChildTwo> children) {
		final Transaction t1 = repoTxManager.getActiveTransaction();
		if (t1 != null) {
			for (final ChildTwo obj : children) {
				setPartitionedKey(parentHashKey, obj);
				t1.save(obj);
			}
		} else {
			for (final ChildTwo obj : children) {
				setPartitionedKey(parentHashKey, obj);
				mapper.save(obj);
			}
		}
	}

	@Override
	public ChildTwo loadChild(final String parentHashKey,
			final String childRangeKey) {
		final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
		final String partitionedHashKey = getPartitionedHashKey(parentHashKey,
				childRangeKey);
		final ChildTwo retVal;
		if (t1 != null) {
			retVal = t1.load(cClass, partitionedHashKey, childRangeKey);
		} else {
			retVal = repoTxManager.load(cClass, partitionedHashKey,
					childRangeKey, IsolationLevel.COMMITTED);
		}

		return retVal;
	}

	@Override
	public List<ChildTwo> loadChildren(final String parentHashKey,
			final Iterable<String> childRangeKeys) {
		final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
		final ArrayList<ChildTwo> retVal = new ArrayList<>();
		if (t1 != null) {
			for (final String childRangeKey : childRangeKeys) {
				final String partitionedHashKey = getPartitionedHashKey(
						parentHashKey, childRangeKey);
				retVal.add(t1.load(cClass, partitionedHashKey, childRangeKey));
			}
		} else {
			for (final String childRangeKey : childRangeKeys) {
				final String partitionedHashKey = getPartitionedHashKey(
						parentHashKey, childRangeKey);
				retVal.add(repoTxManager.load(cClass, partitionedHashKey,
						childRangeKey, IsolationLevel.COMMITTED));
			}
		}

		return retVal;
	}

	/**
	 * With this implementation, there is no way to achieve a transactional read
	 * because the Query API is not offered and there is no way to identify the
	 * set of records to read without it since no such list is maintained in
	 * object state.
	 * 
	 * TODO: If query() is synchronous in nature, it may be necessary to use an
	 * executor service and join on all results returning.
	 */
	@Override
	public List<ChildTwo> loadAllChildren(final String parentHashKey) {
		final ArrayList<Future<PaginatedQueryList<ChildTwo>>> retValSource = new ArrayList<>();
		for (int ii = 0; ii < numParts; ii++) {
			// Construct the hash key outside the task so we have ii
			// embedded into a final field.
			final String partitionedHashKey = parentHashKey + ':' + ii;

			// Use the thread pool so all N work units are concurrent.
			retValSource.add(threadPool
					.submit(new Callable<PaginatedQueryList<ChildTwo>>() {
						@Override
						public PaginatedQueryList<ChildTwo> call() {
							final ChildTwo hashKey = new ChildTwo();
							hashKey.setUserId(partitionedHashKey);
							final DynamoDBQueryExpression<ChildTwo> query = new DynamoDBQueryExpression<ChildTwo>()
									.withHashKeyValues(hashKey)
									.withRangeKeyCondition(
											"id",
											new Condition()
													.withComparisonOperator(
															ComparisonOperator.LT)
													.withAttributeValueList(
															new AttributeValue(
																	"z")))
									.withConsistentRead(true);
							// LOG.info("Query: {}", query.toString());
							final PaginatedQueryList<ChildTwo> retVal = childQuery(query);
							retVal.loadAllResults();
							return retVal;
						}
					}));
		}

		// WAit until all Futures have resolved.
		return FluentIterable
				.from(Iterables
						.concat(FluentIterable
								.from(retValSource)
								.transform(
										new Function<Future<PaginatedQueryList<ChildTwo>>, PaginatedQueryList<ChildTwo>>() {
											@Override
											public PaginatedQueryList<ChildTwo> apply(
													final Future<PaginatedQueryList<ChildTwo>> source) {
												PaginatedQueryList<ChildTwo> retVal;
												try {
													retVal = source.get();
												} catch (final InterruptedException e) {
													throw new RuntimeException(
															e);
												} catch (final ExecutionException e) {
													throw new RuntimeException(
															e);
												}
												retVal.loadAllResults();
												return retVal;
											}
										}))).toImmutableList();
	}

	private static Function<ChildTwo, String> CHILD_TO_ID_FUNCTION = new Function<ChildTwo, String>() {
		@Override
		public String apply(final ChildTwo input) {
			return input.getId();
		}
	};

	@Override
	public List<String> getAllChildren(final String parentHashKey) {
		return convertToIds(loadAllChildren(parentHashKey));
	}

	@Override
	public void deleteChild(final String parentHashKey, final ChildTwo child) {
		// Read->Verify->Write->Commit transaction pattern as
		// alternative to Expected Check Constraints on DeleteItemRequest
		// low-level API call.
		final ChildTwo obj = loadChild(parentHashKey, child.getId());
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
	public void deleteChildren(final String parentHashKey,
			final Iterable<ChildTwo> childRangeKeys) {
		// Read->Verify->Write->Commit transaction pattern as
		// alternative to Expected Check Constraints on DeleteItemRequest
		// low-level API call.
		final List<ChildTwo> objList = loadChildren(
				parentHashKey,
				FluentIterable.from(childRangeKeys).transform(
						CHILD_TO_ID_FUNCTION));
		final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
		if (t1 != null) {
			for (final ChildTwo obj : objList) {
				t1.delete(obj);
			}
		} else {
			// TODO: See comment block in deleteChild(). Same applies
			// here too.
			for (final ChildTwo obj : objList) {
				mapper.delete(obj);
			}
		}
	}

	@Override
	public List<ChildTwo> deleteAllChildren(final String parentHashKey) {
		// Read->Verify->Write->Commit transaction pattern as
		// alternative to Expected Check Constraints on DeleteItemRequest
		// low-level API call.
		final List<ChildTwo> objList = loadAllChildren(parentHashKey);
		final Lvl6Transaction t1 = repoTxManager.getActiveTransaction();
		if (t1 != null) {
			for (final ChildTwo obj : objList) {
				t1.delete(obj);
			}
		} else {
			// TODO: See comment block in deleteChild(). Same applies
			// here too.
			for (final ChildTwo obj : objList) {
				mapper.delete(obj);
			}
		}

		return objList;
	}

	@Override
	public List<String> removeAllChildren(final String parentHashKey) {
		return convertToIds(deleteAllChildren(parentHashKey));
	}

	private String getPartitionedHashKey(final String parentHashKey,
			final String childRangeKey) {
		final int bucket = childRangeKey.hashCode() % numParts;
		return parentHashKey + ':' + bucket;
	}

	private void setPartitionedKey(final String parentHashKey,
			final ChildTwo obj) {
		String childRangeKey = obj.getId();
		if (childRangeKey == null) {
			childRangeKey = UUID.randomUUID().toString();
			obj.setId(childRangeKey);
		}

		final int bucket = childRangeKey.hashCode() % numParts;
		obj.setUserId(parentHashKey + ':' + bucket);
	}

	private List<String> convertToIds(final Iterable<ChildTwo> objIter) {
		final ArrayList<String> retVal = new ArrayList<String>();
		Iterables.addAll(retVal,
				FluentIterable.from(objIter).transform(CHILD_TO_ID_FUNCTION));
		return retVal;
	}
}