package org.octopus.api.exception;

public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = -2661050013464669438L;

	public BusinessException(String msg) {
		super(msg);
	}
}