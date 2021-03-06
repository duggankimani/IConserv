package com.wira.pmgt.server.rest.service.impl;

import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.wira.pmgt.server.executor.api.CommandContext;
import com.wira.pmgt.server.rest.exception.WiraExceptionModel;
import com.wira.pmgt.server.rest.model.Request;
import com.wira.pmgt.server.rest.model.Response;
import com.wira.pmgt.server.rest.service.OutgoingRequestService;

public class OutgoingRequestImpl implements OutgoingRequestService{

	static String serviceUri = null;
	
	static String propfile = "/general.properties";
	
	//String erpUrl = "http://ebusiness-duggansit.rhcloud.com/ebusiness/rest";
	
	static Logger logger = Logger.getLogger(OutgoingRequestImpl.class);
	
	static Properties properties = new Properties();
	
	static{
		try{
			InputStream is = OutgoingRequestImpl.class.getResourceAsStream(propfile);
			properties.load(is);
			String prop = properties.getProperty("ServiceUri");
			if(prop!=null){
				serviceUri = prop;
			}else{
				logger.warn("Property [ServiceUri] not found, using default value ["+serviceUri+"]");
			}
		}catch(Exception e){
//			logger.warn("Error Loading [erpUrl], using default value ["+erpUrl+"] :: Cause "+e.getMessage());
//			e.printStackTrace();
		}
	}
	
	/**
	 * Jersey Client
	 */
	private Client jclient;
	
	public OutgoingRequestImpl(){
		DefaultClientConfig config = new DefaultClientConfig(JAXBProviderImpl.class);
		jclient = Client.create(config);
		jclient.setConnectTimeout(15000);

	}
	
	@Override
	public Response executeCall(Request request) {
		String uri = null;
		
		Object URI = request.getContext("serviceURI");
		if(URI!=null && !URI.toString().isEmpty()){
			uri =URI.toString();
		}
		
		if(uri==null){			
			String log = "Trying ServiceUri Property from classpath:"+propfile+".. ";
			if(serviceUri==null){
				logger.info(log+" - NO VALUE FOUND");
			}else{
				uri=serviceUri;
				logger.info(log+" - "+uri);
			}
		}

		return executeCall(request, uri);
	}
	
	@Override
	public Response executeCall(Request request, String serviceURI) {
			
		logger.info("Submitting Request : "+request);

		if(serviceURI==null || serviceURI.isEmpty()){
			throw new IllegalArgumentException("REST URI cannot be null for rest service");
		}

		WebResource resource = jclient.resource(serviceURI);

		ClientResponse clientResponse = null;

		try {
			clientResponse = resource.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, request);
			
			// response.getHeaders()
		} catch (Exception e) {

			// server unavailable
			throw new RuntimeException(e);
		}

		if (!clientResponse.getClientResponseStatus().equals(
				ClientResponse.Status.OK)) {

			WiraExceptionModel model = clientResponse.getEntity(WiraExceptionModel.class);
			
			throw new RuntimeException(model.getCause());
		}

		Response response = clientResponse.getEntity(Response.class);
		
		return response;

	}
	
	
	public static void main(String[] args) {
		CommandContext ctx = new CommandContext();
		//ctx.setData("docType", DocType.LPO.name());
		ctx.setData("subject", "LPO/8023/12");
		ctx.setData("approvalStatus", "20/09/2013");
		
		Request request = new Request("WORKFLOWCALLOUTCOMMAND", null, ctx.getData());

//		//
//		 marshalXml(request);
		//
		Response response = new OutgoingRequestImpl().executeCall(request);
		
		assert request!=null;
	}

	public void executePostCall(String urlEncoding) {
		
	}

}
