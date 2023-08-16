//package com.xiaobai.fast.quarkus.demo.resource;
//
//import com.xiaobai.fast.quarkus.core.exception.ServiceException;
//import com.xiaobai.fast.quarkus.core.ienum.ServiceCodeEnum;
//import com.xiaobai.fast.quarkus.core.util.JsonUtils;
//import com.xiaobai.fast.quarkus.demo.domain.Demo;
//import com.xiaobai.fast.quarkus.demo.domain.Person;
//import com.xiaobai.fast.quarkus.demo.interceptor.LogEvent;
//import com.xiaobai.fast.quarkus.demo.rest.DemoRestClient;
//import com.xiaobai.fast.quarkus.demo.rest.VoiceData;
//import io.agroal.api.AgroalDataSource;
//import io.quarkus.cache.CacheResult;
//import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
//import io.quarkus.redis.datasource.RedisDataSource;
//import io.quarkus.redis.datasource.hash.HashCommands;
//import io.quarkus.redis.datasource.value.ValueCommands;
//import io.quarkus.security.PermissionsAllowed;
//import io.vertx.mutiny.redis.client.Command;
//import io.vertx.mutiny.redis.client.Response;
//import jakarta.annotation.security.PermitAll;
//import jakarta.annotation.security.RolesAllowed;
//import jakarta.enterprise.context.Dependent;
//import jakarta.inject.Inject;
//import jakarta.persistence.EntityManager;
//import jakarta.validation.Valid;
//import jakarta.ws.rs.*;
//import jakarta.ws.rs.core.Context;
//import jakarta.ws.rs.core.MediaType;
//import jakarta.ws.rs.core.SecurityContext;
//import org.eclipse.microprofile.jwt.JsonWebToken;
//import org.eclipse.microprofile.rest.client.inject.RestClient;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.*;
//
///**
// * @author baijie <a href="mrwhite777@163.com"></a>
// * @date 2023/7/18
// * @since 1.0
// */
//@Path("/demo")
//public class DemoResource {
//
//    @Inject
//    @RestClient
//    @Dependent
//    DemoRestClient demoRestClient;
//
//
//    @GET
//    @Produces(value = MediaType.APPLICATION_JSON)
//    @LogEvent
//    public Response get(String a, String b){
//        return
//                Response.ok()
//                R.success(
//                "欢迎访问Fast-Quarkus应用，请访问API文档地址用于开发");
//    }
//
//    @POST
//    @Path("/add")
//    @Produces(value = MediaType.APPLICATION_JSON)
//    @Consumes(value = MediaType.APPLICATION_JSON)
//    public R<Demo> post(@Valid Demo demo){
//        return R.success(demo);
//    }
//
//    @GET
//    @Path("/exception")
//    @Produces(value = MediaType.APPLICATION_JSON)
//    public R<String> exception(){
//        throw new ServiceException(ServiceCodeEnum.ERROR.getMessage());
////        return R.success("Hello DemoResource GET");
//    }
//
//    @GET
//    @Path("/rest")
//    @Produces(value = MediaType.APPLICATION_JSON)
//    public R<List<VoiceData>> rest(){
//       return demoRestClient.getVoiceList();
//    }
//
//    @GET
//    @Path("permit-all")
//    @PermitAll
//    @Produces(MediaType.TEXT_PLAIN)
//    public String helloJwt(@Context SecurityContext context){
//        return getResponseString(context);
//    }
//
//    @Inject
//    AgroalDataSource dataSource;
//    @GET
//    @Path("jdbc")
//    @RolesAllowed({"Admin","User"})
//    @Produces(MediaType.APPLICATION_JSON)
//    public R jdbcQuery() throws SQLException {
//        Connection connection = dataSource.getConnection();
//        String sql = """
//                select * from demo
//                """;
//        PreparedStatement ps = connection.prepareStatement(sql);
//        ps.execute();
//        ResultSet resultSet = ps.getResultSet();
//       List<Map<String,Object>> data = new LinkedList<>();
//        while (resultSet.next()){
//            int id = resultSet.getInt("id");
//            String  username = resultSet.getString("username");
//            String password = resultSet.getString("user_password");
//            Map<String,Object> obj = new HashMap<>();
//            obj.put("id",id);
//            obj.put("username",username);
//            obj.put("password",password);
//            data.add(obj);
//        }
//        return R.success(data);
//    }
//
//    @Inject
//    EntityManager em;
//
//
//
//
//
//
//
//    @GET
//    @Path("/role")
//    @Produces(MediaType.TEXT_PLAIN)
//    @PermissionsAllowed(value = "access")
//    public String helloJwtRole(@Context SecurityContext ctx){
//        return getResponseString(ctx) + ", birthdate: " + jwt.getClaim("userInfo").toString();
//    }
//
//    @GET
//    @Path("/cache")
//    @Produces(MediaType.APPLICATION_JSON)
//    @PermitAll
//    @CacheResult(cacheName = "zhangsan")
//    public List<PanacheEntityBase> cacheResult(){
//        List<PanacheEntityBase> panacheEntityBases = Person.listAll();
//        return panacheEntityBases;
//    }
//
//    @Inject
//    RedisDataSource redisDataSource;
//    @GET
//    @Path("/redis/set")
//    @Produces(MediaType.TEXT_PLAIN)
//    @PermitAll
//    public String redisCacheSet(){
//        List<PanacheEntityBase> panacheEntityBases = Person.listAll();
//        ValueCommands<String, String> value = redisDataSource.value(String.class);
//        value.set("QuarkusCache",JsonUtils.object2Json(panacheEntityBases));
//        return "SUCCESS";
//    }
//    @GET
//    @Path("/redis/get")
//    @Produces(MediaType.TEXT_PLAIN)
//    @PermitAll
//    public String redisCacheGet(){
//
//        ValueCommands<String, String> value = redisDataSource.value(String.class);
//
//        return value.get("QuarkusCache");
//    }
//    @GET
//    @Path("/redis/hash/set")
//    @Produces(MediaType.TEXT_PLAIN)
//    @PermitAll
//    public String redisCacheHashSet(){
//        HashCommands<String, String, String> hash = redisDataSource.hash(String.class, String.class, String.class);
//        hash.hset("hashKey","name","张三");
//        return "SUCCESS";
//    }
//    @GET
//    @Path("/redis/hash/get")
//    @Produces(MediaType.TEXT_PLAIN)
//    @PermitAll
//    public String redisCacheHashGet(){
//
//        HashCommands<String, String, String> hash = redisDataSource.hash(String.class, String.class, String.class);
//        return hash.hget("hashKey", "name");
//    }
//
//    @GET
//    @Path("/redis/lua")
//    @Produces(MediaType.TEXT_PLAIN)
//    @PermitAll
//    public  Integer redisCacheLua(){
//        Response response = redisDataSource.execute(new Command(Command.EVAL.getDelegate()),"local exists = redis.call('exists', KEYS[1]); if (exists == 1) then return redis.call('incr', KEYS[1]); end return nil;" ,"1","testaaa");
//
//        return response.toInteger();
//    }
//
//    @Inject
//    JsonWebToken jwt;
//
//    private String getResponseString(SecurityContext ctx) {
//        String name;
//        if (ctx.getUserPrincipal() == null) {
//            name = "anonymous";
//        } else if (!ctx.getUserPrincipal().getName().equals(jwt.getName())) {
//            throw new InternalServerErrorException("Principal and JsonWebToken names do not match");
//        } else {
//            name = ctx.getUserPrincipal().getName();
//        }
//        return String.format("hello + %s,"
//                        + " isHttps: %s,"
//                        + " authScheme: %s,"
//                        + " hasJWT: %s",
//                name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt());
//    }
//    private boolean hasJwt() {
//        return jwt.getClaimNames() != null;
//    }
//}
