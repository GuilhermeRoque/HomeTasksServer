package controller;

import db.DAO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import pojo.User;

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
        DAO dao = new DAO();
        if (dao.persist(obj)) {
            dao.close();
            return Response.status(Response.Status.CREATED).entity(obj).build();
        }
        dao.close();
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
        DAO dao = new DAO();
        Object obj = dao.find(entityClass, id);
        dao.close();
        if (obj != null) {
//            for (Field declaredField : entityClass.getDeclaredFields()) {
//                Class<?> type = declaredField.getType();
//                boolean typeIsCollection = Collection.class.isAssignableFrom(type);
//                if (typeIsCollection){
//                    try {
//                        declaredField.set(obj, null);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
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

    protected String getSubject(String bearerToken){
        String token = bearerToken.substring("Bearer".length()).trim();
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }


    protected User getSubjectUser(String bearerToken){
        String token = bearerToken.substring("Bearer".length()).trim();
        String subject = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token).getBody().getSubject();
        DAO dao = new DAO();
        User user = dao.find(User.class, subject);
        user.setHomes(null);
        dao.close();
        return user;
    }


}