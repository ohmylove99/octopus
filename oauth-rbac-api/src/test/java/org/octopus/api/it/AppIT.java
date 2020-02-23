package org.octopus.api.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ //
		AppControllerIT.class, //
		AppServiceIT.class, //
		AppRepositoryIT.class })
public class AppIT {

}
