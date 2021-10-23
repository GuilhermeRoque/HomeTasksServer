package controller;
import authentication.Authorize;
import authentication.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import media.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.Base64;

@Path("/")
public class App{

    @Path("login")
    @POST
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response login(@HeaderParam("Authorization") String credBase64) {
        if (credBase64 != null) {
            String basic = credBase64.substring("Basic".length()).trim();
            String credential = new String(Base64.getDecoder().decode(basic));
            String[] log = credential.split(":");
            if (log.length == 2) {
                User user = JPAUtils.find(User.class, log[0]);
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



    @Authorize
    @Path("{entityClass}/{id}/")
    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response handleGetUnique(@PathParam("entityClass") String targetClass,
                              @PathParam("id") String id) {
        return JRUtils.getEntity(targetClass,id);
    }


    @Authorize
    @Path("{ownerClass}/{id}/{targetClass}")
    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response handleGetOneToMany(@PathParam("ownerClass") String ownerClass,
                                       @PathParam("id") String id,
                                       @PathParam("targetClass") String targetClass){
        return JRUtils.getEntities(ownerClass,id,targetClass);
    }

    @Authorize
    @Path("{entityClass}")
    @POST
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response handlePost(String json,
                              @PathParam("entityClass") String entityClass) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Class<?> targetClass = JPAUtils.findTargetClass(entityClass);
            Object obj = objectMapper.readValue(json, targetClass);
            if (JPAUtils.persist(obj)) {
                return Response.status(Response.Status.CREATED).entity(obj).build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return JRUtils.bad_request;

        }
        return JRUtils.internal_error;
    }



//    @Authorize
//    @Path("/{entityClass}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    public Response handleGetQuery(@PathParam("entityClass") String targetClass,
//                              @QueryParam("idHome") String idHome,
//                              @QueryParam("idUser") String idUser) {
//
//        if(idUser != null && idHome==null){
//            return getEntities(User.class, idUser, targetClass);
//        }else if(id == null && idUser == null && idHome != null){
//            return getEntities(Home.class, idHome, targetClass);
//        }
//        return bad_request;
//    }


//    @Authorize
//    @Path("/Home")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    public Response getHome(@QueryParam("id") String id,
//                            @QueryParam("idUser") String idUser) {
//        if(id != null && idUser==null) {
//            return getEntity(Home.class,id);
//        }else if(id == null && idUser!=null){
//            return getEntities(User.class, idUser, Home.class);
//        }
//        return bad_request;
//    }

//    @Authorize
//    @Path("/Home")
//    @POST
//    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    public Response addHome(Home newHome, @HeaderParam("Authorization") String bearerToken) {
//        String userID = JWT.getSubject(bearerToken);
//        User user = JPAUtils.find(User.class, userID);
//        List<User> users = new ArrayList<>();
//        users.add(user);
//        newHome.setUsers(users);
//        boolean persist = JPAUtils.persist(newHome);
//        if (!persist){
//            return internal_error;
//        }
//        return Response.ok(newHome).build();
//    }


}

//    @Path("/User")
//    @POST
//    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    public Response addUser(User newUser) {
//        User user = JPAUtils.find(User.class, newUser.getIdUser());
//        if(user != null){
//            return Response.status(Response.Status.CONFLICT).entity("{\n" +
//                    "    \"error\": \"User already in use\"" + "\n" +
//                    "}").build();
//        }
//        boolean persist = JPAUtils.persist(newUser);
//        if (!persist){
//            return internal_error;
//        }
//        return Response.ok(newUser).build();
//    }
//
////    @Authorize
//    @Path("/User")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    public Response getUser(@QueryParam("id") String id,
//                            @QueryParam("idHome") String idHome) {
//        if(id != null && idHome==null) {
//            return getEntity(User.class,id);
//        }else if(id == null && idHome!=null){
//            return getEntities(Home.class, idHome, User.class);
//        }
//        return bad_request;
