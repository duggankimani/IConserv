package com.wira.pmgt.server.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse.Status;
import com.wira.pmgt.server.ServerConstants;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.helper.jbpm.JBPMHelper;
import com.wira.pmgt.server.helper.session.SessionHelper;
import com.wira.pmgt.server.rest.exception.WiraExceptionModel;
import com.wira.pmgt.server.rest.exception.WiraServiceException;
import com.wira.pmgt.server.rest.model.Request;
import com.wira.pmgt.server.rest.model.Response;
import com.wira.pmgt.server.rest.service.IncomingRequestService;
import com.wira.pmgt.server.rest.service.impl.IncomingRequestImpl;
import com.wira.pmgt.shared.model.HTUser;

@Path("/request/")
public class CommandBasedRestService{

	@GET
	@Path("/{name}")
	public String sayHello(@PathParam("name") String name){
		return "Hello "+name;
	}
	
	@POST
	@Path("/approval")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response execute(@Context HttpServletRequest httprequest, Request request){
		
		
		Response response = new Response();
		
		try{
			DB.beginTransaction();
			
			HTUser user = new HTUser();
			user.setUserId(request.getContext("ownerId").toString());
			httprequest.getSession().setAttribute(ServerConstants.USER,user);
			
			//check session
			SessionHelper.setHttpRequest(httprequest);

			IncomingRequestService service = new IncomingRequestImpl();
			
			service.executeClientRequest(request, response);
			
			DB.commitTransaction();			
		}catch(Exception e){
			DB.rollback();
			throw new WiraServiceException(WiraExceptionModel.getExceptionModel(e));
			
		}finally{
			
			DB.closeSession();
			SessionHelper.setHttpRequest(null);
			JBPMHelper.clearRequestData();
			
		}		
		
		return response;
	}
}
