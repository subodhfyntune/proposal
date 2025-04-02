package com.example.demo.exception;

public class ProposerDeletedAlready extends RuntimeException{

	public ProposerDeletedAlready(String message) {
		super(message);
	}
}
