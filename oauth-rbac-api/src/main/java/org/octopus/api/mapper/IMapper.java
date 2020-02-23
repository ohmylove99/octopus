package org.octopus.api.mapper;

import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

public interface IMapper<Destination, Source> {
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
	public Destination map(final Source in, Class<Destination> out);

	/**
	 * Maps {@code source} to {@code destination}.
	 *
	 * @param source      object to map from
	 * @param destination object to map to
	 */
	public Destination map(final Source source, Destination destination);

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
	public List<Destination> mapAll(final Collection<Source> in, Class<Destination> out);

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
	public Page<Destination> mapPage(final Page<Source> in, Class<Destination> out);

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
	public Destination map(ModelMapper modelMapper, final Source in, Class<Destination> out);

	/**
	 * Maps {@code source} to {@code destination}.
	 *
	 * @param source      object to map from
	 * @param destination object to map to
	 */
	public Destination map(ModelMapper modelMapper, final Source source, Destination destination);

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
	public List<Destination> mapAll(ModelMapper modelMapper, final Collection<Source> in, Class<Destination> out);

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
	public Page<Destination> mapPage(ModelMapper modelMapper, final Page<Source> in, Class<Destination> out);
}
