package com.wira.pmgt.server.rest.service.impl;

import javax.ws.rs.ext.ContextResolver;


import com.sun.jersey.api.json.JSONJAXBContext;
import com.wira.pmgt.server.executor.api.CommandContext;
import com.wira.pmgt.server.rest.exception.ExTrace;
import com.wira.pmgt.server.rest.exception.WiraExceptionModel;
import com.wira.pmgt.server.rest.model.BusinessKey;
import com.wira.pmgt.server.rest.model.Detail;
import com.wira.pmgt.server.rest.model.Request;
import com.wira.pmgt.server.rest.model.Response;

public class JAXBProviderImpl implements ContextResolver<JSONJAXBContext> {

	private JSONJAXBContext jsonJAXBContext;

	@Override
	public JSONJAXBContext getContext(Class<?> arg0) {


		if (!(arg0.equals(BusinessKey.class)
				|| arg0.equals(Request.class) || arg0.equals(Response.class)
				|| arg0.equals(Detail.class)
				|| arg0.equals(ExTrace.class) || arg0.equals(WiraExceptionModel.class)
				)) {
			return null;
		}

		if (jsonJAXBContext != null) {
			return jsonJAXBContext;
		}

		try {
			jsonJAXBContext = new JSONJAXBContext(BusinessKey.class,
					CommandContext.class, Request.class, Response.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return jsonJAXBContext;
	}
}
