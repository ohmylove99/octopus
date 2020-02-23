package org.octopus.api.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * a custom favicon controller, that returns an empty response
 * when the browsers lookup they get a “404 Not Found” error
 * @author joshualeng
 */
@Controller
public class FaviconController {

	@GetMapping("favicon.ico")
	@ResponseBody
	void returnNoFavicon() {
	}
}
