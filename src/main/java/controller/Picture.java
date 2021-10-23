package controller;

import media.JPAUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/picture")
public class Picture {

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/picture/{entityClass}/{id}")
    public Response getPicture(@PathParam("entityClass") String entityClass,
                               @PathParam("id") String id){

        Object entity = JPAUtils.find(entityClass, id);
        Object picture = JPAUtils.getEntityFieldValue(entity, "picture");
        if (picture != null){
            return Response.ok(picture).build();
        }
        return JRUtils.internal_error;
    }

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/picture/{entityClass}/{id}")
    public Response getPicture(@PathParam("entityClass") String entityClass,
                               @PathParam("id") String id,
                               byte[] picture){

        Object entity = JPAUtils.find(entityClass, id);
        if (JPAUtils.setEntityFieldValue(entity, "picture", picture)){
            if(JPAUtils.persist(entity)){
                return Response.ok(entity).build();
            }
        }
        return JRUtils.internal_error;
    }


}

