package fr.pantheonsorbonne.ufr27.miage.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {
	@Override
	public Response toResponse(Exception exception) {
		exception.printStackTrace();
		
		return Response.serverError().status(500, exception.getMessage()).build();
	}
}
