package org.octopus.api.ut;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.octopus.api.entity.UserEntity;

public class ModelMapperTest {
	@Test
	public void shouldMapHashMapToFoo() {
		HashMap<String, String> map = new HashMap<>();
		map.put("a", "aaaa");
		map.put("username", "bbbb");
		ModelMapper modelMapper = new ModelMapper();
		UserEntity foo = modelMapper.map(map, UserEntity.class);
		//modelMapper.getConfiguration().setFieldMatchingEnabled(true);
		modelMapper.getConfiguration().setFullTypeMatchingRequired(true);
		//modelMapper.getConfiguration().set
		assertEquals(foo.getUsername(), map.get("username"));
		// assertEquals(foo.getB(), map.get("b"));
	}
}
