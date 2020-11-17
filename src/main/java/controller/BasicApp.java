package controller;

import db.DAO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.ws.rs.core.Response;
import java.security.Key;
import java.util.Date;

public class BasicApp {
    final static Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Simply tries to persist the object and return a INTERNAL_SERVER_ERROR if it fails;
     * @param obj The received JSON object;
     * @return A basic HTTP response in JSON format;
     */
    protected Response handlePost(Object obj){
        if (DAO.persist(obj)) {
            return Response.status(Response.Status.CREATED).entity(obj).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\n" +
                "    \"error\": \"Could not persist the " +obj.getClass().toString()+ "\"\n" +
                "}").build();
    }

    /**
     * Simply tries to find the object and return a NOT_FOUND if it fails;
     * @param entityClass The object class
     * @param id The primary key field value
     * @return A basic HTTP response in JSON format;
     */
    protected Response handleGet(Class<?> entityClass, Object id){
        Object obj = DAO.find(entityClass, id);
        if (obj != null) {
            return Response.ok(obj).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("{\n" +
                "    \"error\": \"Could not find the " +entityClass.toString()+ "\"\n" +
                "}").build();
    }

    protected String buildToken(String subject){
        return Jwts.builder()
                .setIssuer("localhost:8080")
                .setSubject(subject)
                .setIssuedAt(new Date())
                .signWith(secretKey)
                .compact();

    }
}