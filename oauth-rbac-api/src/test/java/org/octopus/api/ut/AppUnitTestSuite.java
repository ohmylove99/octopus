package org.octopus.api.ut;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AppServiceUnitTest.class, //
		UserServiceUnitTest.class, //
		AppControllerUnitTest.class, //
		UserControllerUnitTest.class, //
		RoleControllerUnitTest.class//
})
public class AppUnitTestSuite {
}
