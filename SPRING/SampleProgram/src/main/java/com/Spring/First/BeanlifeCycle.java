package com.Spring.First;
public class BeanlifeCycle {
private String message;


public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}


@Override
public String toString() {
	return "BeanlifeCycle [message=" + message + "]";
}


public BeanlifeCycle() {
	super();
	
}

public void init() {
	System.out.println("Spring is initialized ");
}

public void destroy() {
	System.out.println("Spring is destroyed");
}


}



