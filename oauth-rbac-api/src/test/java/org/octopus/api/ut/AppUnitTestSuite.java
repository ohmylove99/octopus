package org.octopus.api.ut;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AppServiceUnitTest.class, // test case 1
		AppControllerUnitTest.class // test case 2
})
public class AppUnitTestSuite {
}
