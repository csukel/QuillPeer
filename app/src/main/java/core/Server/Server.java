package core.Server;

/**
 * Created by loucas on 25/01/2015.
 * Singleton class for Server
 */
public class Server {
    private static Server instance;
    private static final String HOST="";
    private static final String PORT="";


    private Server(){}

    public static Server getInstance(){
        if (instance==null){
            instance = new Server();
        }
        return instance;
    }

    public static String getHost(){
        return HOST;
    }

    public static String getPort(){
        return PORT;
    }

}
