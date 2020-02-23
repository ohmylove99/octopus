package org.octopus.api.ut;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.octopus.api.service.UserService;
import org.octopus.api.web.rest.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerUnitTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private ModelMapper mockMapper;

	@MockBean
	private UserService service;

	@Test
	public void health() throws Exception {
		mvc.perform(get("/api/user/health").contentType(MediaType.APPLICATION_JSON))//
				.andDo(print())//
				.andExpect(status().isOk())//
				.andExpect(jsonPath("status", is("UP")));
	}
}
