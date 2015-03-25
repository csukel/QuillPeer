package core.Server;

/**
 *
 * This class is used to store in its fields the API's location on the server side
 * Created on 08/02/2015.
 * @author Loucas Stylianou
 */
public class APIs {
    /**
     * The location of the loginAPI
     */
    public static final String loginAPI = "/auth/login";
    /**
     * The location of the logoutAPI
     */
    public static final String logoutAPI ="/auth/logout";
    /**
     * The location of the getPerson api
     */
    public static final String getPerson = "/api/getPerson";
    /**
     * The location of the getPeople api
     */
    public static final String getPeople = "/api/getPeople";
    /**
     * The location of the addFavourite api
     */
    public static final String addFavourite = "/api/addFavourite";
    /**
     * The location of the removeFavourite api
     */
    public static final String removeFavourite = "/api/removeFavourite";
    /**
     * The location of the uploadProfilPicture api
     */
    public static final String uploadProfilPicture = "/api/savePicture";
    /**
     * The location of the savePosition api
     */
    public static final String savePosition = "/api/savePosition";
    /**
     * The location of the getMapSitze api
     */
    public static final String getMapSize ="/api/getMapSize";
    /**
     * The location of the getRecommendation api
     */
    public static final String getRecommendation = "/api/getRecommendation";
    /**
     * The location of the getSuggestion api
     */
    public static final String getSuggestion = "/api/getSuggestion";

    /**
     * The default constructor is locked in order to disallow the instantiation of this class
     */
    private APIs(){}
}
