package core.Server;

/**
 * Created by loucas on 08/02/2015.
 */
public class APIs {
    public static final String loginAPI = "/auth/login";
    public static final String logoutAPI ="/auth/logout";
    public static final String getPerson = "/api/getPerson";
    public static final String getPeople = "/api/getPeople";
    public static final String addFavourite = "/api/addFavourite";
    public static final String removeFavourite = "/api/removeFavourite";
    public static final String uploadProfilPicture = "/api/savePicture";
    public static final String savePosition = "/api/savePosition";
    public static final String getMapSize ="/api/getMapSize";
    public static final String getRecommendation = "/api/getRecommendation";
    private APIs(){}
}
