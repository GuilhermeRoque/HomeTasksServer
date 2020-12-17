package controller;

import media.DAO;
import javax.ws.rs.core.Response;

public class BasicApp {

    Response bad_request = Response.status(Response.Status.BAD_REQUEST).entity("{\n" +
            "    \"error\": \"Could not understand the request\"\n" +
            "}").build();
    Response not_found = Response.status(Response.Status.NOT_FOUND).entity("{\n" +
            "    \"error\": \"Could understand find the requested entity \"\n" +
            "}").build();

    Response internal_error = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\n" +
            "    \"error\": \"Internal error while processing the request\"\n" +
            "}").build();

    public Response getEntity(String entityClass, String id){
        Object entity = DAO.find(entityClass, id);
        return validateFind(entity);
    }

    public Response getEntity(Class<?> entityClass, Object id){
        Object entity = DAO.find(entityClass, id);
        return validateFind(entity);
    }

    private Response validateFind(Object entity){
        if (entity != null){
            return Response.ok(entity).build();
        }
        return not_found;
    }

    public Response getEntities(Class<?> ownerClass,Object id, String entityClass ){
        Object entity = DAO.find(ownerClass, id);
        if (entity != null){
            Object fieldEntities = DAO.findFieldEntities(entity, entityClass);
            return validateFind(fieldEntities);
        }
        return bad_request;
    }

    public Response getEntities(Class<?> ownerClass,Object id, Class<?> entityClass ){
        Object entity = DAO.find(ownerClass, id);
        if (entity != null){
            Object fieldEntities = DAO.findFieldEntities(entity, entityClass);
            return validateFind(fieldEntities);
        }
        return bad_request;
    }
}
