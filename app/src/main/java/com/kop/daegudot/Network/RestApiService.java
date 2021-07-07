package com.kop.daegudot.Network;

import com.kop.daegudot.KakaoMap.Documents;
import com.kop.daegudot.Login.User;
import com.kop.daegudot.Network.Map.Place;
import com.kop.daegudot.Network.Map.PlaceGeo;
import com.kop.daegudot.Network.Schedule.MainSchedule;
import com.kop.daegudot.Network.Schedule.SubSchedule;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiService {

/*    @GET("repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> getObContributors(@Path("owner") String owner, @Path("repo") String repo);*/

    /* Login */
    @POST("/user/register")
    Observable<Long> registerUser(@Body User userRequest);

    @GET("/user/register/{email}")
    String checkEmailDup(@Path("email") String email);

    @GET("/user/register/{nickname}")
    Observable<List<User>> checkNickDup(@Path("email") String nickname);

    @POST("/user/login")
    Observable<User> requestLogin(@Body User userRequest);

    @GET("/places/list")
    Observable<List<Place>> getPlaceList();
    
    @PUT("/places/location")
    Observable<Long> updateLocation(@Body List<PlaceGeo> placeGeoList);
    
    /* get MapPoint by address */
    @GET("v2/local/search/address.json")
    Call<Documents> getSearchAddress(
            @Header("Authorization") String key,
            @Query("query") String query
    );
    
    /* get place by category */
    @GET("v2/local/search/category.json")
    Call<Documents> getPlacebyCategory(
            @Header("Authorization") String key,
            @Query("category_group_code") String category_group_code,
            @Query("x") String x,
            @Query("y") String y,
            @Query("radius") int radius,
            @Query("page") int page
    );
    
    @GET("v2/local/search/category.json")
    Call<Documents> getPlacebyCategoryRect(
            @Header("Authorization") String key,
            @Query("category_group_code") String category_group_code,
            @Query("rect") String coord
    );
    
    /* MainSchedule */
    
    /* make new main Schedule */
    @POST("/schedule/main/register")
    Observable<Long> saveMainSchedule(@Body MainSchedule mainSchedule);
    
    /* get all main schedules */
    @GET("/schedule/main/{userId}")
    Observable<List<MainSchedule>> getMainSchedule(@Path("userId") long userId);
    
    /* delete a main Schedule */
    @DELETE("/schedule/main/delete/{mainScheduleId}")
    Observable<Long> deleteMainSchedule(@Path("mainScheduleId") long mainScheduleId);
    
    /* update main Schedule */
    @PUT("/schedule/main/update/{mainscheduleId}")
    Observable<Long> updateMainSchedule(@Path("mainscheduleId") long mainScheduleId);
    
    /* SubSchedule */
    
    /* insert sub Schedule*/
    @POST("/schedule/sub/register")
    Observable<Long> saveSubSchedule(@Body SubSchedule subSchedule);
    
    /* get sub Schedule by main schedule */
    @GET("/schedule/sub/{mainscheduleId}")
    Observable<List<SubSchedule>> getSubscheduleList(@Path("mainscheduleId") long mainScheduleId);
    
    /* delete subschedule from main schedule */
    @DELETE("/schedule/sub/delete/{subscheduleId}")
    Observable<Long> deleteSubSchedule(@Path("subscheduleId") long subScheduleId);
    
    /* update Subschedule*/
    @PUT("/schedule/sub/update/{subscheduleId}")
    Observable<Long> updateSubSchedule(@Path("subscheduleId") long subscheduleId);
}
