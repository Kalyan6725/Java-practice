package org.northernarc.customerproduct.exceptions;

public class OrderNotFound extends RuntimeException{
	public OrderNotFound(String message) {
		super(message);
	}
}
