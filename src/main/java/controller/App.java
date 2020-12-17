package controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import media.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Path("/")
public class App extends BasicApp{

    /*
    /{class}?id=1    -> objeto de classe class identificado pelo ID
    /{class}?idHome=1    -> lista de objetos de classe class da Home identificada pelo ID
    /{class}?idHome=1    -> lista de objetos de classe class do User identificada pelo ID
    /picture/class?id=1    -> foto de objeto da class class identificado pelo ID
    */
    /*
    Home -> casas do usuário
    Invitation -> convites do usuario
    Payment -> pagamentos do usuario
    User -> usuarios da casa
    Task -> tarefas da casa
    Rules -> regras da casa
    Comment -> comentarios da tarefa
     */




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
        return Response.ok(Response.Status.BAD_REQUEST).entity("{\n" +
                " \"error\": \"Missing credentials\"\n" +
                "}").build();
    }


    @Path("/User")
    @POST
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response addUser(User newUser) {
        User user = DAO.find(User.class, newUser.getIdUser());
        if(user != null){
            return Response.status(Response.Status.CONFLICT).entity("{\n" +
                    "    \"error\": \"User already in use\"" + "\n" +
                    "}").build();
        }
        boolean persist = DAO.persist(newUser);
        if (!persist){
            return internal_error;
        }
        return Response.ok(newUser).build();
    }

    @Authorize
    @Path("/User")
    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response getUser(@QueryParam("id") String id,
                            @QueryParam("idHome") String idHome) {
        if(id != null && idHome==null) {
            return getEntity(User.class,id);
        }else if(id == null && idHome!=null){
            return getEntities(Home.class, idHome, User.class);
        }
        return bad_request;
    }

    /**
     * It is provided 3 ways to get an entity(s):
     * Get the specific entity passing the primary key of IT
     *   and the entity class simple name
     * Get the entities list from a specific entity passing the
     *   list entity class simple name, the owner class simple name and the primary key of the owner entity
     *
     * @param targetClass The object class
     * @param id The primary key field value
     * @param idHome The primary key of the Home entity
     * @param idUser The primary key of the User entity
     * @return A basic HTTP response in JSON format;
     */
    @Authorize
    @Path("/{entityClass}")
    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response handleGet(@QueryParam("id") String id,
                              @QueryParam("idHome") String idHome,
                              @QueryParam("idUser") String idUser,
                              @PathParam("entityClass") String targetClass) {

        if(id != null && idUser == null && idHome==null) {
            return getEntity(targetClass,id);
        }else if(id != null && idUser != null && idHome==null){
            return getEntities(User.class, idUser, targetClass);
        }else if(id == null && idUser == null && idHome != null){
            return getEntities(Home.class, idHome, targetClass);
        }
        return bad_request;
    }

    @Authorize
    @Path("/{entityClass}")
    @POST
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response addEntity(String json,
                              @PathParam("entityClass") String entityClass) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Class<?> targetClass = DAO.findTargetClass(entityClass);
            Object obj = objectMapper.readValue(json, targetClass);
            if (DAO.persist(obj)) {
                return Response.status(Response.Status.CREATED).entity(obj).build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return bad_request;

        }
        return internal_error;
    }

    @Authorize
    @Path("/Home")
    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response getHome(@QueryParam("id") String id,
                            @QueryParam("idUser") String idUser) {
        if(id != null && idUser==null) {
            return getEntity(Home.class,id);
        }else if(id == null && idUser!=null){
            return getEntities(User.class, idUser, Home.class);
        }
        return bad_request;
    }

    @Authorize
    @Path("/Home")
    @POST
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response addHome(Home newHome, @HeaderParam("Authorization") String bearerToken) {
        String userID = JWT.getSubject(bearerToken);
        User user = DAO.find(User.class, userID);
        List<User> users = new ArrayList<>();
        users.add(user);
        newHome.setUsers(users);
        boolean persist = DAO.persist(newHome);
        if (!persist){
            return internal_error;
        }
        return Response.ok(newHome).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/picture/{entityClass}")
    public Response getPicture(@PathParam("entityClass") String entityClass,
                               @QueryParam("id") String id){

        Object entity = DAO.find(entityClass, id);
        Object picture = DAO.getEntityFieldValue(entity, "picture");
        if (picture != null){
            return Response.ok(picture).build();
        }
        return internal_error;
    }

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/picture/{entityClass}")
    public Response getPicture(@PathParam("entityClass") String entityClass,
                               @QueryParam("id") String id,
                               byte[] picture){

        Object entity = DAO.find(entityClass, id);
        if (DAO.setEntityFieldValue(entity, "picture", picture)){
            if(DAO.persist(entity)){
                return Response.ok(entity).build();
            }
        }
        return internal_error;
    }
}
