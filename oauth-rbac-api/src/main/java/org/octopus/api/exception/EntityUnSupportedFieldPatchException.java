package org.octopus.api.exception;

import java.util.Set;

public class EntityUnSupportedFieldPatchException extends RuntimeException {
	private static final long serialVersionUID = -1874514525401440931L;

	public EntityUnSupportedFieldPatchException(Set<String> keys) {
		super("Field " + keys.toString() + " update is not allow.");
	}
}