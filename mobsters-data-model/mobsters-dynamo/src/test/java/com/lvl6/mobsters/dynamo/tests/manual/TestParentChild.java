package com.lvl6.mobsters.dynamo.tests.manual;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lvl6.mobsters.dynamo.setup.Lvl6TxManager;
import com.lvl6.mobsters.dynamo.tests.manual.PartitionedHashKeyStrategy.ChildTwo;
import com.lvl6.mobsters.dynamo.tests.manual.PartitionedHashKeyStrategy.ParentTwo;
import com.lvl6.mobsters.dynamo.tests.manual.SingleHashKeyStrategy.ChildOne;
import com.lvl6.mobsters.dynamo.tests.manual.SingleHashKeyStrategy.ParentOne;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "classpath:spring-dynamo.xml", "classpath:spring-test-parentChild.xml"
})
public class TestParentChild
{
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(TestParentChild.class);

	private static final int NUM_PARENTS = 3; // 40;

	private static final int NUM_CHILDREN_PER_PARENT = 3; // 40;

	private static final int RUN_ITERS_PER_TASK = 10;

	private static final int[] READ_INDICES_ORDER = new int[] {
	    0, 2, 1
	};

	// 13, 9, 36, 15, 30, 4, 19, 22, 0, 23
	// };

	@Before
	public void createTestData()
	{
		variantOne.getRepository()
		    .emptyTables();
		doLoadData("Unpartitioned", variantOne);

		variantTwo.getRepository()
		    .emptyTables();
		doLoadData("Partitioned", variantTwo);
	}

	private void doLoadData( final String name,
	    final VariantStrategy<?, ? extends ChildDataAttrs> nextVariant )
	{
		System.out.println("Populating "
		    + name
		    + "'s test data set of "
		    + NUM_PARENTS
		    + " parents with "
		    + NUM_CHILDREN_PER_PARENT
		    + " children each.  This may take a while...");
		// txManager.beginTransaction();
		parentIds = new String[NUM_PARENTS];
		for (int ii = 0; ii < NUM_PARENTS; ii++) {
			parentIds[ii] = nextVariant.getNextParent(NUM_CHILDREN_PER_PARENT);
			for (int jj = 0; jj < NUM_CHILDREN_PER_PARENT; jj++) {
				final ChildDataAttrs nextChild = nextVariant.addNextChild();
				nextChild.setCombineStartTime(new Date());
				nextChild.setComplete((jj % 2) == 0);
				nextChild.setCurrentExp(24689);
				nextChild.setCurrentHealth(256);
				nextChild.setCurrentLvl(13);
				nextChild.setMonsterId((jj + 1) * 2);
				nextChild.setName("Phil the Monster");
				nextChild.setNumPieces(3);
				nextChild.setSourceOfPieces("Formatted:        'Wrong'    ; First:           'Last'     ;");
				nextChild.setTeamSlotNum(2);
				nextChild.setTradeInValue(9.999);
			}
			nextVariant.saveChildren();
		}

		// txManager.commit();
		System.out.println("Populated "
		    + parentIds.length
		    + " parents.");
	}

	// @After
	public void destroyTestData()
	{
		// txManager.rollback();
		variantOne.getRepository()
		    .emptyTables();
		variantTwo.getRepository()
		    .emptyTables();
	}

	@Test
	public void testSinglePartition()
	{
		final LoopingReader loaderOne =
		    new LoopingReader(txManager, variantOne.getRepository(), RUN_ITERS_PER_TASK,
		        parentIds, READ_INDICES_ORDER);

		final int requestCount;
		if ((RUN_ITERS_PER_TASK % 5) > 0) {
			requestCount = 25
			    * (1 + (RUN_ITERS_PER_TASK / 5))
			    * READ_INDICES_ORDER.length;
		} else {
			requestCount = 5
			    * RUN_ITERS_PER_TASK
			    * READ_INDICES_ORDER.length;
		}

		final long timers[] = new long[2];
		timers[0] = System.currentTimeMillis();
		loaderOne.run();
		timers[1] = System.currentTimeMillis();
		final long deltaOne = (timers[1] - timers[0]) / 1000;

		System.out.println("Time taken for one-key variant was "
		    + deltaOne
		    + " seconds to make "
		    + requestCount
		    + " readAll requests for "
		    + NUM_CHILDREN_PER_PARENT
		    + " children from each of "
		    + READ_INDICES_ORDER.length
		    + " distinct parents taken from a data set with "
		    + NUM_PARENTS
		    + " total possible parents.");
	}

	@Test
	public void testFourPartitions()
	{
		final LoopingReader loader =
		    new LoopingReader(txManager, variantTwo.getRepository(), RUN_ITERS_PER_TASK,
		        parentIds, READ_INDICES_ORDER);

		final int requestCount;
		if ((RUN_ITERS_PER_TASK % 5) > 0) {
			requestCount = 25
			    * (1 + (RUN_ITERS_PER_TASK / 5))
			    * READ_INDICES_ORDER.length;
		} else {
			requestCount = 5
			    * RUN_ITERS_PER_TASK
			    * READ_INDICES_ORDER.length;
		}

		final long timers[] = new long[2];
		timers[0] = System.currentTimeMillis();
		loader.run();
		timers[1] = System.currentTimeMillis();
		final long deltaOne = (timers[1] - timers[0]) / 1000;

		System.out.println("Time taken for four-key variant was "
		    + deltaOne
		    + " seconds to make "
		    + requestCount
		    + " readAll requests for "
		    + NUM_CHILDREN_PER_PARENT
		    + " children from each of "
		    + READ_INDICES_ORDER.length
		    + " distinct parents taken from a data set with "
		    + NUM_PARENTS
		    + " total possible parents.");
	}

	static final class LoopingReader implements Runnable
	{
		private final int loopCount;
		private final String[] readIdsOrder;
		private final BaseParentChildRepository<?, ? extends ChildDataAttrs> repoStrategy;
		private final Lvl6TxManager txManager;

		LoopingReader( final Lvl6TxManager txManager,
		    final BaseParentChildRepository<?, ? extends ChildDataAttrs> repoStrategy,
		    final int loopCount, final String[] parentIds, final int[] readIndicesOrder )
		{
			this.txManager = txManager;
			this.repoStrategy = repoStrategy;
			this.loopCount = loopCount;

			System.out.println("Initializing worker.  Given "
			    + parentIds.length
			    + " parent ids.");

			int ii = 0;
			readIdsOrder = new String[readIndicesOrder.length];
			for (final int readIndex : readIndicesOrder) {
				readIdsOrder[ii++] = parentIds[readIndex];
			}
		}

		@Override
		public void run()
		{
			for (int ii = 0; ii < loopCount; ii += 5) {
				readFiveTimes();
				readFiveTimes();
				readFiveTimes();
				readFiveTimes();
				readFiveTimes();
			}
		}

		private void readFiveTimes()
		{
			// txManager.beginTransaction();
			for (final String element : readIdsOrder) {
				final List<? extends ChildDataAttrs> kidz =
				    repoStrategy.loadAllChildren(element);
				System.out.print(kidz.toString());
				System.out.println(" has "
				    + kidz.size()
				    + " inside for "
				    + element);
			}
			// txManager.rollback();

			// txManager.beginTransaction();
			for (final String element : readIdsOrder) {
				repoStrategy.loadAllChildren(element);
			}
			// txManager.rollback();

			// txManager.beginTransaction();
			for (final String element : readIdsOrder) {
				repoStrategy.loadAllChildren(element);
			}
			// txManager.rollback();

			// txManager.beginTransaction();
			for (final String element : readIdsOrder) {
				repoStrategy.loadAllChildren(element);
			}
			// txManager.rollback();

			// txManager.beginTransaction();
			for (final String element : readIdsOrder) {
				repoStrategy.loadAllChildren(element);
			}
			// txManager.rollback();
		}
	}

	@Autowired
	private Lvl6TxManager txManager;

	@Autowired
	@Qualifier("VariantOne")
	private VariantStrategy<ParentOne, ChildOne> variantOne;

	@Autowired
	@Qualifier("VariantTwo")
	private VariantStrategy<ParentTwo, ChildTwo> variantTwo;

	private String[] parentIds;
}
