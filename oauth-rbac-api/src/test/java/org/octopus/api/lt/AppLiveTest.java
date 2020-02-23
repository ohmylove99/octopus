package org.octopus.api.lt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ //
		AppControllerLiveTest.class, //
		UserControllerLiveTest.class, //
		RoleControllerLiveTest.class, //
})
public class AppLiveTest {
}
