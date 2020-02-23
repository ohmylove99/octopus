package org.octopus.api.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ //
		AppRepositoryIT.class, //
		AppServiceIT.class, //
		AppControllerIT.class, //
		UserControllerIT.class, //
		RoleControllerIT.class })
public class AppIT {
}
