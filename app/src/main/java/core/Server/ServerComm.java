package core.Server;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.http.AndroidHttpClient;
import android.preference.Preference;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Loucas Stylianou
 * Created on 08/02/2015.
 * This class is created to capture the communication behaviour between client and server app
 */
public class ServerComm {
    /**
     * Use a single httpClient in order to keep alive the connection with the server(session)
     */
    private static DefaultHttpClient httpClient = new DefaultHttpClient();

    /**
     * Disallow instantiation of this class
     */
    private ServerComm(){}


    /**
     * Authenticate the user with the server and capture its response
     * @param paramUsername Username which is the email address
     * @param paramPassword Password
     * @return result Server's response
     */
    public static String login(String paramUsername,String paramPassword){
        String result = null;
        //HttpClient httpClient = new DefaultHttpClient();
        //HttpClient httpClient = AndroidHttpClient.newInstance("Android");
        // In a POST request, we don't pass the values in the URL.
        //Therefore we use only the web page URL as the parameter of the HttpPost argument
        HttpPost httpPost = new HttpPost(Server.getHost()+APIs.loginAPI);
        // Because we are not passing values over the URL, we should have a mechanism to pass the values that can be
        //uniquely separate by the other end.
        //To achieve that we use BasicNameValuePair
        //Things we need to pass with the POST request
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair("email", paramUsername);
        BasicNameValuePair passwordBasicNameValuePAir = new BasicNameValuePair("password", paramPassword);

        // We add the content that we want to pass with the POST request to as name-value pairs
        //Now we put those sending details to an ArrayList with type safe of NameValuePair
        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        nameValuePairList.add(passwordBasicNameValuePAir);

        try {
            // UrlEncodedFormEntity is an entity composed of a list of url-encoded pairs.
            //This is typically useful while sending an HTTP POST request.
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
            // setEntity() hands the entity (here it is urlEncodedFormEntity) to the request.
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                // HttpResponse is an interface just like HttpPost.
                //Therefore we can't initialize them
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity entity = httpResponse.getEntity();
                result = EntityUtils.toString(entity);

            } catch (ClientProtocolException cpe) {
                System.out.println("First Exception caz of HttpResponese :" + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out.println("Second Exception caz of HttpResponse :" + ioe);
                ioe.printStackTrace();
            }

        } catch (UnsupportedEncodingException uee) {
            System.out.println("An Exception given because of UrlEncodedFormEntity argument :" + uee);
            uee.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Logout from the server and capture the server's response
     * @return result Server's response
     */
    public static String logout(){
        String result = null;

        // In a POST request, we don't pass the values in the URL.
        //Therefore we use only the web page URL as the parameter of the HttpPost argument
        HttpGet httpGet = new HttpGet(Server.getHost()+APIs.logoutAPI);
        try {
            // HttpResponse is an interface just like HttpPost.
            //Therefore we can't initialize them
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);

        } catch (ClientProtocolException cpe) {
            System.out.println("First Exception caz of HttpResponese :" + cpe);
            cpe.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("Second Exception caz of HttpResponse :" + ioe);
            ioe.printStackTrace();
        }

        return result;
    }

    /**
     * Post a request to the server to get a number of people according to the give search criteria
     * @param start Start index
     * @param size The number of the expected results
     * @param searchQuery The search query given by the user if it exists
     * @return result Server
     */
    public static String getPeople(String start,String size,String searchQuery){
        //HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Server.getHost()+APIs.getPeople);
        JSONObject jsonObject = new JSONObject();
        String result=null;

        try {
            jsonObject.put("start", start);
            jsonObject.put("size",size);
            jsonObject.put("search",searchQuery);
            httpPost.setHeader("Content-type", "application/json");

            StringEntity se = new StringEntity(jsonObject.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);

            HttpResponse response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
        }catch(ClientProtocolException cex){
            cex.printStackTrace();
        }catch(IOException ioex){
            ioex.printStackTrace();
        }
        catch (JSONException jex){
            jex.printStackTrace();
        }
        return result;
    }


    /**
     * http post request to the server for getting suggested people according to the matching and searching criteria
     * @param start The start index
     * @param size The number of the expected results
     * @param searchQuery The search query given by the user if it exists
     * @return result Server's response
     */
    public static String getSuggestion(String start,String size,String searchQuery){
        //HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Server.getHost()+APIs.getSuggestion);
        JSONObject jsonObject = new JSONObject();
        String result=null;

        try {
            jsonObject.put("start", start);
            jsonObject.put("size",size);
            jsonObject.put("search",searchQuery);
            httpPost.setHeader("Content-type", "application/json");

            StringEntity se = new StringEntity(jsonObject.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);

            HttpResponse response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
        }catch(ClientProtocolException cex){
            cex.printStackTrace();
        }catch(IOException ioex){
            ioex.printStackTrace();
        }
        catch (JSONException jex){
            jex.printStackTrace();
        }
        return result;
    }

    /**
     * Add a participant to the favourites (this method communicates with the corresponding api of the server)
     * @param userId The user id
     * @return result Server's response
     */
    public static String addFavourite(String userId){
        HttpPost httpPost = new HttpPost(Server.getHost()+APIs.addFavourite);
        JSONObject jsonObject = new JSONObject();
        String result=null;

        try {
            jsonObject.put("person_id",Integer.valueOf(userId));
            httpPost.setHeader("Content-type", "application/json");

            StringEntity se = new StringEntity(jsonObject.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);

            HttpResponse response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
        }catch(ClientProtocolException cex){
            cex.printStackTrace();
        }catch(IOException ioex){
            ioex.printStackTrace();
        }
        catch (JSONException jex){
            jex.printStackTrace();
        }
        return result;
    }

    /**
     * Remove a participant from the favourites (this method communicates with the corresponding api of the server)
     * @param userId The user id
     * @return result Server's response
     */
    public static String removeFavourite(String userId){
        HttpPost httpPost = new HttpPost(Server.getHost()+APIs.removeFavourite);
        JSONObject jsonObject = new JSONObject();
        String result=null;

        try {
            jsonObject.put("person_id",Integer.valueOf(userId));

            httpPost.setHeader("Content-type", "application/json");

            StringEntity se = new StringEntity(jsonObject.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);

            HttpResponse response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
        }catch(ClientProtocolException cex){
            cex.printStackTrace();
        }catch(IOException ioex){
            ioex.printStackTrace();
        }
        catch (JSONException jex){
            jex.printStackTrace();
        }
        return result;
    }

    /**
     * Get a person (this method communicates with the corresponding api of the server)
     * @param userId The user id
     * @return result The server's response
     */
    public static String getPerson(String userId){
        HttpPost httpPost = new HttpPost(Server.getHost()+APIs.getPerson);
        JSONObject jsonObject = new JSONObject();
        String result=null;

        try {
            jsonObject.put("user_id",Integer.valueOf(userId));

            httpPost.setHeader("Content-type", "application/json");

            StringEntity se = new StringEntity(jsonObject.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);

            HttpResponse response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
        }catch(ClientProtocolException cex){
            cex.printStackTrace();
        }catch(IOException ioex){
            ioex.printStackTrace();
        }
        catch (JSONException jex){
            jex.printStackTrace();
        }
        return result;
    }

    /**
     * Send the average of 5 measurements collected fore each beacon (this method communicates with the corresponding api of the server)
     * @param jsonArray A json array containing the distance of the device from the beacons
     * @return result The server's response
     */
    public static String savePosition(JSONObject jsonArray){
        HttpPost httpPost = new HttpPost(Server.getHost()+APIs.savePosition);
        String result=null;

        try {

            httpPost.setHeader("Content-type", "application/json");

            StringEntity se = new StringEntity(jsonArray.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);

            HttpResponse response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
        }catch(ClientProtocolException cex){
            cex.printStackTrace();
        }catch(IOException ioex){
            ioex.printStackTrace();
        }
        return result;
    }

    /**
     * This method send a post request to the server to upload the profile image
     * @param picture The captured profile picture from the camera app
     * @return result The server's response to this request
     */
    public static String savePicture(String picture){

        HttpPost httpPost = new HttpPost(Server.getHost()+APIs.uploadProfilPicture);
        JSONObject jsonObject = new JSONObject();
        String result=null;

        try {
            jsonObject.put("picture",picture);

            httpPost.setHeader("Content-type", "application/json");

            StringEntity se = new StringEntity(jsonObject.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);

            HttpResponse response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
        }catch(ClientProtocolException cex){
            cex.printStackTrace();
        }catch(IOException ioex){
            ioex.printStackTrace();
        }
        catch (JSONException jex){
            jex.printStackTrace();
        }
        return result;
    }

    /**
     * GET method to get the max X and max Y coordinates
     * @return result The server's response related to this request
     */
    public static String getMapSize(){
        String result = null;

        //instantiate the http get request
        HttpGet httpGet = new HttpGet(Server.getHost()+APIs.getMapSize);
        try {
            // HttpResponse is an interface just like HttpPost.
            //Therefore we can't initialize them
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            //write the response in the result string
            result = EntityUtils.toString(entity);

        } catch (ClientProtocolException cpe) {
            System.out.println("First Exception caz of HttpResponese :" + cpe);
            cpe.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("Second Exception caz of HttpResponse :" + ioe);
            ioe.printStackTrace();
        }

        return result;
    }

    /**
     * Get the recommended people's data from the server
     * @return result The server's response related to this request
     */
    public static String getRecommendation(String size,JSONArray jsonArray){
        String result = null;

        //instantiate the http get request
        HttpPost httpPost = new HttpPost(Server.getHost()+APIs.getRecommendation);
        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("size",size);
            /*put json array which includes the qualifications that the user selects as filters for the map*/
            jsonObject.put("qualification",jsonArray);
            //jsonArray.put(jsonObject);
            httpPost.setHeader("Content-type", "application/json");

            StringEntity se = new StringEntity(jsonObject.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);
            // HttpResponse is an interface just like HttpPost.
            //Therefore we can't initialize them
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            //write the response in the result string
            result = EntityUtils.toString(entity);

        } catch (ClientProtocolException cpe) {
            System.out.println("First Exception caz of HttpResponese :" + cpe);
            cpe.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("Second Exception caz of HttpResponse :" + ioe);
            ioe.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * This method is used every time the client app is about to trigger an internet operation in order to check if the device
     * actually supports it, i.e if the device is connected to the internet
     * @param context The context within this method is called
     * @param activity The current running activity when this method in invoked
     * @return True if the device is connected to the internet, false otherwise
     */
    public static boolean isNetworkConnected(Context context,Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo()!=null);
    }
}
