package controller;

import media.JPAUtils;
import javax.ws.rs.core.Response;

public class JRUtils {

    static Response bad_request = Response.status(Response.Status.BAD_REQUEST).entity("{\n" +
            "    \"error\": \"Could not understand the request\"\n" +
            "}").build();
    static Response not_found = Response.status(Response.Status.NOT_FOUND).entity("{\n" +
            "    \"error\": \"Could understand find the requested entity \"\n" +
            "}").build();

    static Response internal_error = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\n" +
            "    \"error\": \"Internal error while processing the request\"\n" +
            "}").build();

    static public Response getEntity(String entityClass, String id){
        Object entity = JPAUtils.find(entityClass, id);
        return validateFind(entity);
    }

    static public Response getEntity(Class<?> entityClass, Object id){
        Object entity = JPAUtils.find(entityClass, id);
        return validateFind(entity);
    }

    static private Response validateFind(Object entity){
        if (entity != null){
            return Response.ok(entity).build();
        }
        return not_found;
    }

    public static Response getEntities(String ownerClass, String id, String targetClass){
        Object entity = JPAUtils.find(ownerClass, id);
        if (entity != null){
            Object fieldEntities = JPAUtils.findFieldEntities(entity, targetClass);
            return validateFind(fieldEntities);
        }
        return bad_request;
    }

}
