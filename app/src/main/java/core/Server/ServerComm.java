package core.Server;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.http.AndroidHttpClient;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by loucas on 08/02/2015.
 */
public class ServerComm {
    //use a single httpClient instead of creating a new instance every time to store the laravel session
    private static DefaultHttpClient httpClient = new DefaultHttpClient();
    private ServerComm(){}

    //login to the server and return response
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

    //logout from the server and return response
    public static String logout(){
        String result = null;
        //HttpClient httpClient = new DefaultHttpClient();
/*
        HttpClient httpClient = AndroidHttpClient.newInstance("Android");
*/
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

    //post a request to the server to get a number of people
    public static String getPeople(String start,String size){
        //HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Server.getHost()+APIs.getPeople);
        JSONObject jsonObject = new JSONObject();
        String result=null;

        try {
            jsonObject.put("start", start);
            jsonObject.put("size",size);

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

    //return true if the device is connected to the internet
    public static boolean isNetworkConnected(Context context,Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo()!=null);
    }
}
