package com.duggan.workflow.server.rest.service.impl;

import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.jbpm.executor.api.CommandContext;

import com.duggan.workflow.server.rest.exception.WiraExceptionModel;
import com.duggan.workflow.server.rest.model.Request;
import com.duggan.workflow.server.rest.model.Response;
import com.duggan.workflow.server.rest.service.OutgoingRequestService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

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
		
		logger.info("Submitting Request : "+request);

		String uri = null;
		
		Object URI = request.getContext("ServiceUri");
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

		if(uri==null || uri.isEmpty()){
			throw new IllegalArgumentException("REST URI cannot be null for rest service");
		}

		WebResource resource = jclient.resource(uri);

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

}
