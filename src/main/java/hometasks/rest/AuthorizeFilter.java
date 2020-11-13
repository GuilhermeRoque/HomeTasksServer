package hometasks.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import static hometasks.rest.Controller.secretKey;


@Provider
@Authorize
@Priority(Priorities.AUTHENTICATION)
public class AuthorizeFilter implements ContainerRequestFilter {


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext
                .getHeaderString(HttpHeaders.AUTHORIZATION);
        try {
            String token = authorizationHeader.substring("Bearer".length()).trim();
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            requestContext
                    .abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("{\n" +
                            " \"error\": \""+e.toString()+"\"\n" +
                            "}")
                            .build());
        }

    }

}
