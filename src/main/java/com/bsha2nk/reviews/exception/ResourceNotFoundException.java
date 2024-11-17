package com.bsha2nk.reviews.exception;

public class ResourceNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String errorMsg) {
		super(errorMsg);
	}
	
}