package org.octopus.api.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

public class MapperImpl<Destination, Source> implements IMapper<Destination, Source> {

	private ModelMapper modelMapper;

	public MapperImpl() {

	}

	public MapperImpl(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	/**
	 * <p>
	 * Note: outClass object must have default constructor with no arguments
	 * </p>
	 *
	 * @param <Destination> type of result object.
	 * @param <Source>      type of source object to map from.
	 * @param entity        entity that needs to be mapped.
	 * @param outClass      class of result object.
	 * @return new object of <code>outClass</code> type.
	 */
	@Override
	public Destination map(ModelMapper modelMapper, final Source in, Class<Destination> out) {
		return modelMapper.map(in, out);
	}

	/**
	 * Maps {@code source} to {@code destination}.
	 *
	 * @param source      object to map from
	 * @param destination object to map to
	 */
	@Override
	public Destination map(ModelMapper modelMapper, final Source in, Destination out) {
		modelMapper.map(in, out);
		return out;
	}

	/**
	 * <p>
	 * Note: outClass object must have default constructor with no arguments
	 * </p>
	 *
	 * @param entityList    list of entities that needs to be mapped
	 * @param outCLass      class of result list element
	 * @param <Destination> type of objects in result list
	 * @param <Source>      type of entity in <code>entityList</code>
	 * @return list of mapped object with <code><D></code> type.
	 */
	@Override
	public List<Destination> mapAll(ModelMapper modelMapper, final Collection<Source> in, Class<Destination> out) {
		return in.stream().map(entity -> map(modelMapper, entity, out)).collect(Collectors.toList());
	}

	/**
	 * <p>
	 * Note: outClass object must have default constructor with no arguments
	 * </p>
	 *
	 * @param entityList    list of entities that needs to be mapped
	 * @param outCLass      class of result list element
	 * @param <Destination> type of objects in result list
	 * @param <Source>      type of entity in <code>entityList</code>
	 * @return list of mapped object with <code><D></code> type.
	 */
	@Override
	public Page<Destination> mapPage(ModelMapper modelMapper, final Page<Source> in, Class<Destination> out) {
		Page<Destination> page = in.map(entity -> map(modelMapper, entity, out));
		return page;
	}

	@Override
	public Destination map(Source in, Class<Destination> out) {
		return map(modelMapper, in, out);
	}

	@Override
	public Destination map(Source source, Destination destination) {
		return map(modelMapper, source, destination);
	}

	@Override
	public List<Destination> mapAll(Collection<Source> in, Class<Destination> out) {
		return mapAll(modelMapper, in, out);
	}

	@Override
	public Page<Destination> mapPage(Page<Source> in, Class<Destination> out) {
		return mapPage(modelMapper, in, out);
	}
}
