package core.Server;

/**
 * @author Loucas Stylianou
 * Created on 25/01/2015.
 * Singleton class for Server
 */
public class Server {
    /**
     * Server instance
     */
    private static Server instance;
    /**
     * The host domain name (ip address)
     */
    private static final String HOST="http://54.149.69.131";
    /**
     * Server port
     */
    private static final String PORT="";

    /**
     * Disallow instantiation of this class from more the 1 time
     */
    private Server(){}

    /**
     * Retrieve the single instance of this class
     * @return instance
     */
    public static Server getInstance(){
        if (instance==null){
            instance = new Server();
        }
        return instance;
    }

    /**
     * Retrieve the host domain name
     * @return host
     */
    public static String getHost(){
        return HOST;
    }

    /**
     * Retrieve the server's port number
     * @return PORT
     */
    public static String getPort(){
        return PORT;
    }

}
