package com.randomsoft.gmccustomerorders.sql.objects;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



public class Constants {

    @StringDef({API_CALL.CREATEUSER,API_CALL.GETUSERS,API_CALL.UPDATEUSER,API_CALL.DELETEUSER,API_CALL.GETUSER, API_CALL.LOGIN})
    @Retention(RetentionPolicy.SOURCE)
    @interface API_CALL{
        public String CREATEUSER = "createuser";
        public String GETUSERS = "getusers";
        public String UPDATEUSER = "updateuser";
        public String DELETEUSER = "deleteuser";
        public String GETUSER = "getuser";
        public String LOGIN = "login";
    }

    @StringDef({PARAM.USERID, PARAM.USERNAME, PARAM.PASSWORD, PARAM.ISADMIN})
    @Retention(RetentionPolicy.SOURCE)
    @interface PARAM{
        public String USERID = "userid";
        public String USERNAME = "username";
        public String PASSWORD = "password";
        public String ISADMIN = "isadmin";
    }
}

/*
        public final static String API_CREATEUSER = "createuser";
        public final static String API_GETUSERS = "getusers";
        public final static String API_UPDATEUSER = "updateuser";
        public final static String API_DELETEUSER = "deleteuser";
        public final static String API_GETUSER = "getuser";
        public final static String API_LOGIN = "login";


        public final static String PARAM_USERID = "userid";
        public final static String PARAM_USERNAME = "username";
        public final static String PARAM_PASSWORD = "password";
        public final static String PARAM_ISADMIN = "isadmin";
 */