package controller;
import media.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Path("/")
public class App{

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
                    String token = JWT.buildToken(user.getIdUser());
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

    /**
     * Simply tries to find the object and return a NOT_FOUND if it fails;
     * @param entityClass The object class
     * @param id The primary key field value
     * @return A basic HTTP response in JSON format;
     */
    @Authorize
    @Path("/{entityClass}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObject(@PathParam("entityClass") String entityClass,
                              @HeaderParam("Authorization") String bearerToken,
                              @QueryParam("id") String id,
                              @QueryParam("idHome") String idHome){
        if(id == null){
            Home home = DAO.find(Home.class, new Integer(idHome));
            Object fieldEntities = DAO.findFieldEntities(home, entityClass);
            if (fieldEntities != null){
                return Response.ok(fieldEntities).build();
            }
        }else {
            Object entity = DAO.findEntity(entityClass, id);
            if (entity != null){
                return Response.ok(entity).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("{\n" +
                "    \"error\": \"Could not get the " +entityClass+ "\"\n" +
                "}").build();
    }


    /**
     * Simply tries to persist the object and return a INTERNAL_SERVER_ERROR if it fails;
     * @param obj The received JSON object;
     * @return A basic HTTP response in JSON format;
     */
    @Path("/{entityClass}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response postObject(@PathParam("entityClass") String entityClass, Object obj) {
        if (DAO.persist(obj)) {
            return Response.status(Response.Status.CREATED).entity(obj).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\n" +
                "    \"error\": \"Could not persist the " + obj.getClass().toString() + "\"\n" +
                "}").build();
    }

    @Authorize
    @Path("/home")
    @POST
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response addHome(Home newHome, @HeaderParam("Authorization") String bearerToken) {
        String userID = JWT.getSubject(bearerToken);
        User user = DAO.find(User.class, userID);
        List<User> users = new ArrayList<>();
        users.add(user);
        newHome.setUsers(users);
        DAO.persist(newHome);
        return Response.ok(newHome).build();
    }

    @Authorize
    @Path("/home")
    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response getHomesUser(@HeaderParam("Authorization") String bearerToken) {
        String userID = JWT.getSubject(bearerToken);
        User user = DAO.find(User.class, userID);
        List<Home> homes = user.getHomes();
        return Response.ok(homes).build();
    }

}
