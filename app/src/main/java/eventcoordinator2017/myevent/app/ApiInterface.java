package eventcoordinator2017.myevent.app;


import java.util.List;

import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.Location;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.model.response.LoginResponse;
import eventcoordinator2017.myevent.model.response.ResultResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST(Endpoints.LOGIN)
    Call<LoginResponse> login(@Field(Constants.EMAIL) String username,
                              @Field(Constants.PASSWORD) String password);

    @FormUrlEncoded
    @POST(Endpoints.REGISTER)
    Call<ResultResponse> register(@Field(Constants.EMAIL) String username,
                                  @Field(Constants.PASSWORD) String password,
                                  @Field(Constants.FIRST_NAME) String firstName,
                                  @Field(Constants.LAST_NAME) String lastName,
                                  @Field(Constants.CONTACT) String contact,
                                  @Field(Constants.BIRTHDAY) String birthday,
                                  @Field(Constants.ADDRESS) String address
    );

    @FormUrlEncoded
    @POST(Endpoints.VERIFY)
    Call<LoginResponse> verify(@Field(Constants.USER_ID) String user_id,
                               @Field(Constants.VER_CODE) String verification_code);

    @FormUrlEncoded
    @POST(Endpoints.VERIFY_RESEND_EMAIL)
    Call<ResultResponse> verifyResendEmail(@Field(Constants.USER_ID) String user_id);


    @FormUrlEncoded
    @POST(Endpoints.GET_USER_EVENTS)
    Call<List<Event>> getUserEvents(@Field(Constants.USER_ID) int user_id);


    @FormUrlEncoded
    @POST(Endpoints.GET_PACKAGES)
    Call<List<Package>> getPackages(@Field("") String ah);

    @FormUrlEncoded
    @POST(Endpoints.GET_LOCATIONS)
    Call<List<Location>> getLocations(@Field("") String ah);




}
