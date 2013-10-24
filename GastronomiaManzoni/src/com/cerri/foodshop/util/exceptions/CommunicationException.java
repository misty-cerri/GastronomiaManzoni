package com.cerri.foodshop.util.exceptions;

public class CommunicationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1776151583344139038L;

	public CommunicationException() {
		// TODO Auto-generated constructor stub
	}

	public CommunicationException(int responseStatusCode) {
		super("Server response sent http status: " + String.valueOf(responseStatusCode));
	}
	
	public CommunicationException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public CommunicationException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	public CommunicationException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

}
