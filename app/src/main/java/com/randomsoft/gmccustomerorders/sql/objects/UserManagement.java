package com.randomsoft.gmccustomerorders.sql.objects;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.randomsoft.gmccustomerorders.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserManagement {

    public User user;
    public boolean isLoggedIn;

    private static String API_URL = "https://www.therandmcrafter.com/gmc/UserManagement/api/Management.php?apicall=";

    public boolean userLogin(String username, String password){
        boolean result = true;

        return result;
    }

    public void sessionLogOut(){
        isLoggedIn = false;
        user = null;
    }

    //Change values in user object then call this procedure to update in online database
    public void updateUserInformation(int userID){

    }

    //used for updating the user's password in the database
    public void changeUserPassword(int userID){

    }

    public void createNewUser(String username, String password, boolean isAdmin){

    }

    public void deleteUser(int userID){

    }

    public ArrayList<User> getUsers(){
        ArrayList<User> result = new ArrayList<User>();
        return result;
    }

    private class PerformNetworkRequest extends AsyncTask<Void,Void,String> {

        public static final int CODE_GET_REQUEST = 1024;
        public static final int CODE_POST_REQUEST = 1025;

        String url;

        HashMap<String, String> params;

        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode){
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //show a progress bar here
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            //hide progress bar here
            try{
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")){
                    Toast.makeText(MyApplication.getAppContext(),object.getString("message"),Toast.LENGTH_LONG);
                    //refresh the user list here
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids){
            RequestHandler requestHandler = new RequestHandler();
            if (requestCode == CODE_POST_REQUEST){
                return requestHandler.sendPostRequest(url, params);
            }
            if (requestCode == CODE_GET_REQUEST){
                return requestHandler.sendGetRequest(url);
            }

            return null;
        }
    }
}

//region notes
/*
    //inner class to perform network request extending an AsyncTask
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        //when the task started displaying a progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }


        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    //refreshing the herolist after every operation
                    //so we get an updated list
                    //we will create this method right now it is commented
                    //because we haven't created it yet
                    //refreshHeroList(object.getJSONArray("heroes"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //the network operation will be performed in background
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }
 */
//endregion