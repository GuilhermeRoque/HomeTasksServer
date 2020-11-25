package controller;
import db.*;
import pojo.*;

import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.lang.reflect.Field;
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
                DAO dao = new DAO();
                User user = dao.find(User.class, log[0]);
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
            DAO dao = new DAO();
            Home home = dao.find(Home.class, new Integer(idHome));
            try {
                Field[] declaredFields = home.getClass().getDeclaredFields();
                Class<?> targetClass = Class.forName("pojo."+ entityClass);
                Object obj = null;
                for(Field f :declaredFields){
                    OneToMany oneToMany = f.getAnnotation(OneToMany.class);
                    ManyToMany manyToMany = f.getAnnotation(ManyToMany.class);
                    if(oneToMany != null){
                        Class<?> targetEntity = oneToMany.targetEntity();
                        if (targetClass.equals(targetEntity)){
                            obj = f.get(home);
                            break;
                        }
                    }
                    else if(manyToMany != null){
                        Class<?> targetEntity = manyToMany.targetEntity();
                        if (targetClass.equals(targetEntity)){
                            obj = f.get(home);
                            break;
                        }
                    }
                }
                return Response.ok(obj).build();
            }catch (Exception e){
                e.printStackTrace();
            }
            return Response.status(Response.Status.NOT_FOUND).entity("{\n" +
                    "    \"error\": \"Could not get the " +entityClass+ "\"\n" +
                    "}").build();
        }


        try {
            Class<?> aClass = Class.forName("pojo."+ entityClass);
            DAO dao = new DAO();
            Field[] declaredFields = aClass.getDeclaredFields();
            Object obj = null;
            for(Field f :declaredFields){
                if(f.getAnnotation(Id.class) != null){
                    Class<?> type = f.getType();
                    /*Primary keys are just Integer or String*/
                    if (type.equals(Integer.class)){
                        obj = dao.find(aClass,new Integer(id));
                    }
                    else {
                        obj = dao.find(aClass,id);
                    }
                    break;
                }
            }
            return Response.ok(obj).build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
        DAO dao = new DAO();
        if (dao.persist(obj)) {
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
        DAO dao = new DAO();
        User user = dao.find(User.class, userID);
        List<User> users = new ArrayList<>();
        users.add(user);
        newHome.setUsers(users);
        dao.persist(newHome);
        return Response.ok(newHome).build();
    }

    @Authorize
    @Path("/home")
    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response getHomesUser(@HeaderParam("Authorization") String bearerToken) {
        DAO dao = new DAO();
        String userID = JWT.getSubject(bearerToken);
        User user = dao.find(User.class, userID);
        List<Home> homes = user.getHomes();
        return Response.ok(homes).build();
    }

    @Authorize
    @Path("/task")
    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response getTasksHome(@HeaderParam("Authorization") String bearerToken) {
        DAO dao = new DAO();
        String userID = JWT.getSubject(bearerToken);
        User user = dao.find(User.class, userID);
        List<Home> homes = user.getHomes();
        return Response.ok(homes.get(0).getTasks()).build();
    }

}
