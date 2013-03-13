package com.test.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * 
 */
@WebService(name = "CounterDelegate", targetNamespace = "http://webservice.test.com/")
public interface CounterDelegate {

	/**
	 * 
	 * @param arg1
	 * @param arg0
	 * @return returns int
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "add", targetNamespace = "http://webservice.test.com/", className = "com.test.webservice.Add")
	@ResponseWrapper(localName = "addResponse", targetNamespace = "http://webservice.test.com/", className = "com.test.webservice.AddResponse")
	public int add(@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") int arg1);

}