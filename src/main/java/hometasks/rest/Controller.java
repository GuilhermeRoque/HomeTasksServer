package hometasks.rest;
import hometasks.db.*;
import hometasks.pojo.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;


@Path("/")
public class Controller {
    final static Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response login(@HeaderParam("Authorization") String credBase64) {
        if (credBase64 != null) {
            String basic = credBase64.split(" ")[1];
            String credential = new String(Base64.getDecoder().decode(basic));
            String[] log = credential.split(":");
            if (log.length == 2) {
                User user = DAO.find(User.class, log[0]);
                if (user != null && user.getPassword().compareTo(log[1]) == 0) {
                    String token = Jwts.builder()
                            .setIssuer("localhost:8080")
                            .setSubject(user.getIdUser())
                            .setExpiration(
                                    Date.from(
                                            LocalDateTime.now().plusMinutes(15L)
                                                    .atZone(ZoneId.systemDefault())
                                                    .toInstant()))
                            .setIssuedAt(new Date())
                            .signWith(secretKey)
                            .compact();
                    return Response.status(Response.Status.OK).entity("{\"token\": \"" + token +"\" }").build();
                }
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity("{\n" +
                    " \"error\": \"Invalid credentials\"\n" +
                    "}").build();

        }
        return Response.status(Response.Status.BAD_REQUEST).entity("{\n" +
                " \"error\": \"Missing credentials\"\n" +
                "}").build();
    }

    @Path("/users/{idUser}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchUsers(@PathParam("idUser") String idUser) {
        User user = DAO.find(User.class, idUser);
        if (user != null) {
            user.setPassword(null);
            return Response.ok(user).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }


    @Path("/users")
    @POST
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response addUser(User newUser) {
        if (DAO.find(User.class, newUser.getIdUser()) != null) {
            return Response.status(Response.Status.CONFLICT).entity("{\n" +
                    " \"error\": \"User already in use\"\n" +
                    "}").build();
        }
        if (DAO.persist(newUser)) {
            newUser.setPassword(null);
            return Response.status(Response.Status.CREATED).entity(newUser).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Path("/tasks/")
    @POST
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response addTask(Task newTask, @HeaderParam("token") String token) {
        User responsible = DAO.find(User.class, newTask.getIdOwner());
        if (responsible != null) {
            if(true){
            //if(responsible.getIdHome() == newTask.getIdHome()) {
                if (DAO.persist(newTask)) {
                    return Response.status(Response.Status.CREATED).entity(newTask).build();
                }
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity("{\n" +
                    "    \"error\": \"Owner of the task is not in the home of the task\"\n" +
                    "}").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("{\n" +
                "    \"error\": \"Owner of the task not found\"\n" +
                "}").build();
    }

}
