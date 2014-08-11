package com.lvl6.mobsters.tests.fixture.dummy;

import com.lvl6.mobsters.common.utils.IRunnableAction;

public interface IDummyService {

	IRunnableAction doSomethingForCaller(String id, int size, String title);

}