package org.octopus.api.exception;

public class EntityNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -3961367372795556895L;

	public EntityNotFoundException(Object clas, String identity) {
		super("Entity(" + clas + ") Identity(" + identity + ") not found");
	}
}