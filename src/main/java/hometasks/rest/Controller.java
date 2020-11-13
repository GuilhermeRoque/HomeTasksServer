package hometasks.rest;
import hometasks.db.UserDAO;
import hometasks.pojo.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;


@Path("/")
public class Controller {
    final static Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final UserDAO userDAO = new UserDAO();


    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response login(@HeaderParam("Authorization") String credBase64) {
        if (credBase64 != null) {
            String basic = credBase64.split(" ")[1];
            String credential = new String(Base64.getDecoder().decode(basic));
            String[] log = credential.split(":");
            if (log.length == 2) {
                User user = this.userDAO.getUser(log[0]);
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
    public Response searchUsers(@PathParam("idUser") String idUser, @HeaderParam("token") String token) {
        User user = this.userDAO.getUser(idUser);
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
    public Response addUser(User newUser, @Context UriInfo uriInfo) {
        if (this.userDAO.getUser(newUser.getIdUser()) != null) {
            return Response.status(Response.Status.CONFLICT).entity("{\n" +
                    " \"error\": \"User already in use\"\n" +
                    "}").build();
        }
        if (this.userDAO.createUser(newUser)) {
            newUser.setPassword(null);
            return Response.status(Response.Status.CREATED).entity(newUser).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }




    //    @Path("/users/list/{idUser}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response searchUser(@PathParam("idUser") String idUser, @HeaderParam("token") String token) {
//        User user = this.userDAO.getUserToken(token);
//        if (user != null) {
//            List<User> users = this.userDAO.getUsersLikeID(idUser);
//            if (users != null) {
//                for (User u : users) {
//                    u.setPassword(null);
//                }
//                return Response.ok(users).build();
//
//            } else return Response.status(Response.Status.NOT_FOUND).entity("{\n" +
//                    "\"error\": \"None user\"\n" +
//                    "}").build();
//        }
//        return Response.status(Response.Status.UNAUTHORIZED).build();
//    }
//


//    @Authorize
//    @Path("/users/home")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response searchHomeId(@HeaderParam("token") String token) {
//        User user = this.userDAO.getUserToken(token);
//        if (user != null) {
//            int idHome = user.getIdHome();
//            if (idHome != 0) {
//                List<User> usersHome = this.userDAO.getUsersHome(idHome);
//                if (usersHome.size() > 0) {
//                    for (User u : usersHome) {
//                        u.setPassword(null);
//                    }
//                    return Response.ok(usersHome).build();
//                }
//                return Response.status(Response.Status.NOT_FOUND).entity("{\n" +
//                        "\"error\": \"None user\"\n" +
//                        "}").build();
//            }
//            return Response.status(Response.Status.NOT_FOUND).entity("{\n" +
//                    "    \"error\": \"User has no home\"\n" +
//                    "}").build();
//        }
//        return Response.status(Response.Status.UNAUTHORIZED).build();
//    }
//    @Authorize
//    @Path("/users")
//    @PUT
//    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    public Response updateUser(User updateUser, @Context UriInfo uriInfo, @HeaderParam("token") String token) {
//        if (token != null) {
//            User user = this.userDAO.getUserToken(token);
//            if (user != null) {
//                if (user.getIdUser().compareTo(updateUser.getIdUser()) == 0) {
//                    if (updateUser.getPassword() == null) {
//                        updateUser.setPassword(user.getPassword());
//                    }
//                    updateUser.setPoints(user.getPoints());
//                    if (this.userDAO.updateUser(updateUser) > 0) {
//                        return Response.status(Response.Status.NO_CONTENT).build();
//                    }
//                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//                }
//                return Response.status(Response.Status.FORBIDDEN).build();
//            }
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//        return Response.status(Response.Status.BAD_REQUEST).build();
//    }
//
//    @Authorize
//    @Path("/tasks/{state}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response searchTasks(@PathParam("state") String state, @HeaderParam("token") String token) {
//        if (token != null) {
//            User user = this.userDAO.getUserToken(token);
//            if (user != null) {
//                List<Task> tasks = this.taskDAO.getTasksUserState(user, state);
//                if (tasks.size() > 0) return Response.status(Response.Status.OK).entity(tasks).build();
//                return Response.status(Response.Status.NOT_FOUND).entity("{\n" +
//                        "    \"error\": \"None task\"\n" +
//                        "}").build();
//            }
//        }
//        return Response.status(Response.Status.UNAUTHORIZED).build();
//    }
//
//    @Path("/tasks")
//    @POST
//    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    public Response addTask(Task newTask, @Context UriInfo uriInfo, @HeaderParam("token") String token) {
//        if (token != null) {
//            User user = this.userDAO.getUserToken(token);
//            if (user != null) {
//                if (user.getIdHome() != 0) {
//                    newTask.setIdReporter(user.getIdUser());
//                    User responsible = this.userDAO.getUser(newTask.getIdOwner());
//                    if (responsible != null) {
//                        if(responsible.getIdHome() == user.getIdHome()) {
//                            int idTask = this.taskDAO.createTask(newTask);
//                            if (idTask > 0) {
//                                responsible.setPoints(0);
//                                this.userDAO.updateUser(responsible);
//                                newTask.setIdTask(idTask);
//                                return Response.status(Response.Status.CREATED).entity(newTask).build();
//                            }
//                            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//                        }
//                        return Response.status(Response.Status.NOT_FOUND).entity("{\n" +
//                                "    \"error\": \"Owner of the task is not in the home of the task\"\n" +
//                                "}").build();
//                    }
//                    return Response.status(Response.Status.NOT_FOUND).entity("{\n" +
//                            "    \"error\": \"Owner of the task not found\"\n" +
//                            "}").build();
//                }
//                return Response.status(Response.Status.NOT_FOUND).entity("{\n" +
//                        "    \"error\": \"User does not have home\"\n" +
//                        "}").build();
//            }
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//        return Response.status(Response.Status.BAD_REQUEST).build();
//    }
//
//    @Path("/tasks")
//    @PUT
//    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    public Response updateTask(Task updateTask, @Context UriInfo uriInfo, @HeaderParam("token") String token) {
//        if (token != null) {
//            User user = this.userDAO.getUserToken(token);
//            if (user != null) {
//                Task oldTask = taskDAO.getTask(updateTask.getIdTask());
//                if (oldTask != null) {
//                    if ((oldTask.getIdReporter().equals(user.getIdUser())) || (oldTask.getIdOwner().equals(user.getIdUser()))) {
//                        //update task values
//                        //TODO: user this when its to transfer task. it must be checked just here?
//                        String state = updateTask.getState();
//                        boolean transfer = updateTask.isAlternate();
//                        float value = updateTask.getValue();
//                        if(state.compareToIgnoreCase("revised") == 0){
//                            if ((transfer) && (value != 0)){
//                                String idDebtor = updateTask.getIdReporter();
//                                String idCreditor = updateTask.getIdOwner();
//                                Calendar c = Calendar.getInstance();
//                                Date date = c.getTime();
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                                String data = sdf.format(date);
//                                String description = "Payment for " + updateTask.getName();
//                                this.paymentDAO.createPayment(new Payment(
//                                        0,idDebtor, idCreditor, 0, value,data,description, false));
//                            }
//                            User userTask = this.userDAO.getUser(updateTask.getIdOwner());
//                            //TODO: points == revisedtasks ??
//                            userTask.setPoints(0);
//                            this.userDAO.updateUser(userTask);
//                        }
//                        if (this.taskDAO.updateTask(updateTask) > 0) {
//                            return Response.status(Response.Status.NO_CONTENT).build();
//                        }
//                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//                    }
//                    return Response.status(Response.Status.FORBIDDEN).entity("{\n" +
//                            "    \"error\": \"Only reporter or owner can change the task\"\n" +
//                            "}").build();
//                }
//                return Response.status(Response.Status.NOT_FOUND).build();
//            }
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//        return Response.status(Response.Status.BAD_REQUEST).build();
//    }
//
//    @Path("/comments/{idTask}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response searchComments(@PathParam("idTask") String idTask, @HeaderParam("token") String token) {
//        if (token != null) {
//            User user = this.userDAO.getUserToken(token);
//            if (user != null) {
//                Task task = this.taskDAO.getTask(Integer.parseInt(idTask));
//                if (task != null) {
//                    List<Comment> comments = this.commentDAO.getCommentTask(Integer.parseInt(idTask));
//                    if (comments.size() > 0) return Response.status(Response.Status.OK).entity(comments).build();
//                    return Response.status(Response.Status.NOT_FOUND).entity("{\n" +
//                            " \"error\": \"None comment\"\n" +
//                            "}").build();
//                }
//                return Response.status(Response.Status.NOT_FOUND).entity("{\n" +
//                        "\"error\": \"Task not found\"\n" +
//                        "}").build();
//            }
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//        return Response.status(Response.Status.BAD_REQUEST).build();
//    }
//
//    @Path("/comments")
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response addComment(Comment newComment, @HeaderParam("token") String token) {
//        if (token != null) {
//            User user = this.userDAO.getUserToken(token);
//            if (user != null) {
//                Task task = this.taskDAO.getTask(newComment.getIdTask());
//                if (task != null) {
//                    newComment.setIdUser(user.getIdUser());
//                    int idComment = this.commentDAO.createComment(newComment);
//                    if (idComment > 0) {
//                        newComment.setIdComment(idComment);
//                        return Response.status(Response.Status.CREATED).entity(newComment).build();
//                    }
//                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//                }
//                return Response.status(Response.Status.NOT_FOUND).entity("{\n" +
//                        "    \"error\": \"Task not found\"\n" +
//                        "}").build();
//            }
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//        return Response.status(Response.Status.BAD_REQUEST).build();
//
//    }
//
//    @Path("/home/list/{name}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response searchHome(@PathParam("name") String name, @HeaderParam("token") String token) {
//        User user = this.userDAO.getUserToken(token);
//        if (user != null) {
//            List<Home> homes = this.homeDAO.buscaCasasNome(name);
//            if (homes.size() == 0) {
//                homes = this.homeDAO.buscaCasasEndereco(name);
//            }
//            if (homes.size() > 0) {
//                return Response.ok(homes).build();
//
//            } else return Response.status(Response.Status.NOT_FOUND).build();
//        }
//        return Response.status(Response.Status.UNAUTHORIZED).build();
//    }
//
//    @Path("/home/{idCasa}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response searchHomeId(@PathParam("idCasa") String idCasa, @HeaderParam("token") String token) {
//        User userToken = this.userDAO.getUserToken(token);
//        if (userToken != null) {
//            Home home = this.homeDAO.getHome(Integer.parseInt(idCasa));
//            if (home != null) {
//                return Response.ok(home).build();
//            }
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//        return Response.status(Response.Status.UNAUTHORIZED).build();
//    }
//
//    @Path("/home")
//    @POST
//    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    public Response addHome(Home newHome, @Context UriInfo uriInfo, @HeaderParam("token") String token) {
//        User user = this.userDAO.getUserToken(token);
//        if (user != null) {
//            int idHome = this.homeDAO.createHome(newHome);
//            if (idHome > 0) {
//                newHome.setIdHome(idHome);
//                user.setIdHome(idHome);
//                user.setProfile("owner");
//                this.userDAO.updateUser(user);
//                return Response.status(Response.Status.CREATED).entity(newHome).build();
//            }
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//        }
//        return Response.status(Response.Status.UNAUTHORIZED).build();
//    }
//
//    @Path("/home")
//    @PUT
//    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    public Response updateHome(Home updateHome, @Context UriInfo uriInfo, @HeaderParam("token") String token) {
//        User user = userDAO.getUserToken(token);
//        if (user != null) {
//            int idCasa = updateHome.getIdHome();
//            Home oldHome = homeDAO.getHome(idCasa);
//            if (oldHome != null) {
//                int idCasaUserToken = user.getIdHome();
//                String profile = user.getProfile();
//                if ((idCasa == idCasaUserToken) && (profile.compareToIgnoreCase("owner") == 0)) {
////                    atualizaCamposCasa(updateHome, oldHome);
//                    if (homeDAO.updateHome(updateHome) > 0) {
//                        return Response.status(Response.Status.NO_CONTENT).build();
//                    }
//                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//                }
//                return Response.status(Response.Status.FORBIDDEN).build();
//            }
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//        return Response.status(Response.Status.UNAUTHORIZED).build();
//    }
//
//
//    @Path("/account")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response searchAccount(@HeaderParam("token") String token) {
//        User userToken = userDAO.getUserToken(token);
//        if (userToken != null) {
//            List<Payment> payments = paymentDAO.getPaymentUser(userToken.getIdUser());
//            if (payments.size() > 0) return Response.status(Response.Status.OK).entity(payments).build();
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//        return Response.status(Response.Status.UNAUTHORIZED).build();
//    }
//
//
//    @Path("/account")
//    @PUT
//    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    public Response updateAccount(Payment payment, @Context UriInfo uriInfo, @HeaderParam("token") String token) {
//        User user = this.userDAO.getUserToken(token);
//        if (user != null) {
//            int idPayment = payment.getIdPayment();
//            Payment oldPayment = this.paymentDAO.getPayment(idPayment);
//            if(oldPayment != null) {
//                //atualizaCamposPagamento(updatepag, oldPayment);
//                if (this.paymentDAO.updatePayment(payment) > 0) {
//                    return Response.status(Response.Status.NO_CONTENT).build();
//                }
//                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//            }
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//        return Response.status(Response.Status.UNAUTHORIZED).build();
//    }
//
//    @Path("/rules")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response searchRules(@HeaderParam("token") String token) {
//        User user = userDAO.getUserToken(token);
//        if (user != null) {
//            List<Rule> rules = ruleDAO.getRulesHome(user.getIdHome());
//            if (rules.size() > 0) return Response.status(Response.Status.OK).entity(rules).build();
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//        return Response.status(Response.Status.UNAUTHORIZED).build();
//    }
//
//    @Path("/rules")
//    @POST
//    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    public Response addRule(Rule newRule, @HeaderParam("token") String token) {
//        User userToken = this.userDAO.getUserToken(token);
//        if (userToken != null) {
//            int idCasa = userToken.getIdHome();
//            newRule.setState(userToken.getProfile().compareToIgnoreCase("owner") == 0);
//            newRule.setIdHome(idCasa);
//            newRule.setIdUser(userToken.getIdUser());
//            int idRule = this.ruleDAO.createRule(newRule);
//            if (idRule > 0) {
//                newRule.setIdRule(idRule);
//                return Response.status(Response.Status.CREATED).entity(newRule).build();
//            }
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//        }
//        return Response.status(Response.Status.UNAUTHORIZED).build();
//    }
//
//    @Path("/rules")
//    @PUT
//    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
//    public Response updateAccount(Rule rule, @HeaderParam("token") String token) {
//        User user = this.userDAO.getUserToken(token);
//        if (user != null) {
//            Rule oldRule = ruleDAO.getRule(rule.getIdRule());
//            if (oldRule != null) {
//                String profile = user.getProfile();
////                atualizaCamposRegra(rule, oldRule);
//                rule.setState(profile.compareToIgnoreCase("owner") == 0);
//                rule.setIdUser(user.getIdUser());
//                if (ruleDAO.updateRule(rule) > 0) {
//                    return Response.status(Response.Status.NO_CONTENT).build();
//                }
//                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//            }
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//        return Response.status(Response.Status.UNAUTHORIZED).build();
//    }
}
