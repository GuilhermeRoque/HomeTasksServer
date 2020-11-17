package controller;
import db.*;
import pojo.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Base64;


@Path("/")
public class App extends BasicApp {

    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response login(@HeaderParam("Authorization") String credBase64) {
        if (credBase64 != null) {
            String basic = credBase64.substring("Basic".length()).trim();
            String credential = new String(Base64.getDecoder().decode(basic));
            String[] log = credential.split(":");
            if (log.length == 2) {
                User user = DAO.find(User.class, log[0]);
                if (user != null && user.getPassword().compareTo(log[1]) == 0) {
                    String token = this.buildToken(user.getIdUser());
                    return Response.status(Response.Status.OK).entity("{\"token\": \"" + token +"\" }").build();
                }
                return Response.status(Response.Status.UNAUTHORIZED).entity("{\n" +
                        " \"error\": \"Wrong login or password\"\n" +
                        "}").build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity("{\n" +
                    " \"error\": \"Invalid credentials\"\n" +
                    "}").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("{\n" +
                " \"error\": \"Missing credentials\"\n" +
                "}").build();
    }

    @Authorize
    @Path("/users/{idUser}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("idUser") String idUser) {
        return this.handleGet(User.class,idUser);
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
        return this.handlePost(newUser);
    }

    @Authorize
    @Path("/tasks/")
    @POST
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response addTask(Task newTask) {
        return this.handlePost(newTask);
    }

    @Authorize
    @Path("/tasks/{idTask}")
    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response getTask(@PathParam("idTask") String idTask) {
        return this.handleGet(Task.class,idTask);
    }

}
