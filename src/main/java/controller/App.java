package controller;
import db.*;
import pojo.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


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
                DAO dao = new DAO();
                User user = dao.find(User.class, log[0]);
                dao.close();
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
        DAO dao = new DAO();
        User user = dao.find(User.class, idUser);
        user.setHomes(null);
        dao.close();
        return Response.ok(user).build();
    }

    @Path("/users")
    @POST
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response addUser(User newUser) {
        DAO dao = new DAO();
        User user = dao.find(User.class, newUser.getIdUser());
        if (user != null) {
            return Response.status(Response.Status.CONFLICT).entity("{\n" +
                    " \"error\": \"User already in use\"\n" +
                    "}").build();
        }
        boolean persist = dao.persist(newUser);
        dao.close();
        if (persist) {
            return Response.status(Response.Status.CREATED).entity(newUser).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\n" +
                "    \"error\": \"Could not persist the User\"\n" +
                "}").build();
    }

    @Authorize
    @Path("/home")
    @POST
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response addHome(Home newHome, @HeaderParam("Authorization") String bearerToken) {
        String userID = this.getSubject(bearerToken);
        DAO dao = new DAO();
        User user = dao.find(User.class, userID);
        List<User> users = new ArrayList<>();
        users.add(user);
        newHome.setUsers(users);
        dao.persist(newHome);
        dao.close();
        newHome.setUsers(null);
        return Response.ok(newHome).build();
    }

    @Path("/home")
    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response getHomesUser(@HeaderParam("Authorization") String bearerToken) {
        DAO dao = new DAO();
        String userID = this.getSubject(bearerToken);
        User user = dao.find(User.class, userID);
        List<Home> homes = user.getHomes();
        dao.close();
        homes.forEach(h->h.setUsers(null));
        return Response.ok(homes).build();
    }

    @Authorize
    @Path("/task")
    @POST
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response addTask(Task newTask) {
        return this.handlePost(newTask);
    }

    @Path("/task")
    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response getTasksHome(@HeaderParam("Authorization") String bearerToken) {
        DAO dao = new DAO();
        String userID = this.getSubject(bearerToken);
        User user = dao.find(User.class, userID);
        List<Home> homes = user.getHomes();
        dao.close();
        return Response.ok(homes.get(0).getTasks()).build();
    }

}
