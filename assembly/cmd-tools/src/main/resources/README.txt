	<!-- If we can standardize on a package space for local and global test 
		fixtures, then this file and spring-testdata.xml can be defined once and 
		only once. This would be the file that is reused for local fixtures. Local 
		fixtures are not meant for sharing and therefore not in the test module artifact 
		itself, but are at a standard location made canonical by that test module 
		artifact). TODAY: Shared fixtures are built from src/main/java and there 
		fore bundled in the test module artifact along with a context file (e.g. 
		spring-testdata.xml) that "activates" them. Local fixtures, in contrast, 
		are located under src/test/java and their context files are under src/test/resources. 
		TOMORROW: For the moment, these package namespaces overlap, which would have 
		to change. The local and shared distinction requires distinct page namespace 
		roots. Standardizing the namespaces would not change the fact that local 
		fixtures are built from src/test/java, but it would mean that the context 
		files that activate them would be standardized, shared with other projects, 
		and built into a test module project's artifact from its src/main/resources 
		directory. STRAWMAN for src/test/java testing infrastructure packages: com.lvl6.mobsters.*any 
		"real" package name* : Unit tests com.lvl6.mobsters.test.e2e : Root for local 
		integration tests com.lvl6.mobsters.test.local.fixture : Root for local fixtures 
		com.lvl6.mobsters.test.local.dummy : Root for local test dummies/fakes com.lvl6.mobsters.test.local.helper 
		: Root for local test helpers com.lvl6.mobsters.test.local.metatest : Root 
		for unit tests of local testing components com.lvl6.mobsters.test.shared.metatest 
		: Root for unit tests of shared components STRAWMAN for src/main/java testing 
		infrastructure packages: com.lvl6.mobsters.test.shared.fixture : Root for 
		shared fixtures com.lvl6.mobsters.test.shared.fixture : Root for shared test 
		dummies/fakes com.lvl6.mobsters.test.shared.fixture : Root for shared test 
		helpers -->
