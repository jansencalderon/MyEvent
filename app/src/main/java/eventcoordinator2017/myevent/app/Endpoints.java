package eventcoordinator2017.myevent.app;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class Endpoints {

    public static final String _ID = "{id}/";
    public static final String BASE_URL = "http://eventcoordinator.000webhostapp.com";
    //public static final String BASE_URL = "http://127.0.0.1:8000";

    public static final String API_URL = BASE_URL + "/src/v1/";
    public static final String IMAGE_UPLOAD = BASE_URL + "/src/v1/";

    public static final String LOGIN = "loginUser";

    public static final String REGISTER = "registerUser";

    public static final String VERIFY = "verifyUser";
    public static final String VERIFY_RESEND_EMAIL ="resendEmail";


    public static final String GET_USER_EVENTS ="getAllUserEvents";
    public static final String GET_PACKAGES ="getPackages";
    public static final String GET_LOCATIONS ="getAllLocations";
    public static final String ADD_EVENT = "addEventApp";;

    public static final String SAVE_USER_TOKEN = "saveUserToken";
    public static final String DELETE_USER_TOKEN = "deleteUserToken";
    public static final String GET_ALL_EVENTS = "getAllEvents";
    public static final String GET_SINGLE_EVENT = "getEventById" ;
}
