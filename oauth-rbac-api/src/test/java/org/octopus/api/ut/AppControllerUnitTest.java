package org.octopus.api.ut;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.octopus.api.service.AppService;
import org.octopus.api.web.rest.AppController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(AppController.class)
public class AppControllerUnitTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private ModelMapper mockMapper;

	@MockBean
	private AppService service;

	@Test
	public void health() throws Exception {
		mvc.perform(get("/api/app/health").contentType(MediaType.APPLICATION_JSON))//
				.andDo(print())//
				.andExpect(status().isOk())//
				.andExpect(jsonPath("status", is("UP")));
	}
}
