package com.example.penjahityogya.activities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

//    sessionManager = new SessionManager(NamaClass.this, SessionManager.SESSION_USER); //deklarasi sessionManager NB: SESSION_USER sesi untuk user, SESSION_JASA sesi untuk jasa
//
//    sessionManager.createPesanSession(variabel yang akan disimpan1, variabel yang akan disimpan2); //contoh save data ke sessionManager
//
//    HashMap<String, String> usersDetails = sessionManager.getUserDetailFromSession(); //deklarasi HashMap ambil data dari sessionManager
//
//    _username = usersDetails.get(SessionManager.KEY_USERNAME); // contoh ambil data dari HashMap
//
//    sessionManager.logoutUserFromSession(); //Hapus data yang ada di sessionManager (untuk Logout)


    SharedPreferences Session;
    SharedPreferences.Editor editorUser, editorJasa;
    Context context;

    public static final String SESSION_USER = "userLoginSession";
    public static final String SESSION_JASA = "jasaLoginSession";


    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_FULLNAME = "fullName";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_IDMITRA = "idMitra";
    public static final String KEY_IDUSER = "idUser";
    public static final String KEY_PHONENUMBER = "phoneNumber";

    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";

    public SessionManager(Context _context, String statusSession) {
        context = _context;
        Session = context.getSharedPreferences(statusSession, Context.MODE_PRIVATE);
        if(statusSession.equals("userLoginSession")) {
            editorUser = Session.edit();
        }else{
            editorJasa = Session.edit();
        }
    }

    public void createLoginSession( String password, String email, String idUser) {//String fullName, String username,, String phoneNumber
        editorUser.putBoolean(IS_LOGIN, true);

//        editorUser.putString(KEY_FULLNAME, fullName);
//        editorUser.putString(KEY_USERNAME, username);
        editorUser.putString(KEY_PASSWORD, password);
        editorUser.putString(KEY_EMAIL, email);
        editorUser.putString(KEY_IDUSER, idUser);
//        editorUser.putString(KEY_PHONENUMBER, phoneNumber);

        editorUser.commit();
    }

    public HashMap<String, String> getUserLoginFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

//        userData.put(KEY_FULLNAME, Session.getString(KEY_FULLNAME, null));
//        userData.put(KEY_USERNAME, Session.getString(KEY_USERNAME, null));
        userData.put(KEY_PASSWORD, Session.getString(KEY_PASSWORD, null));
        userData.put(KEY_EMAIL, Session.getString(KEY_EMAIL, null));
        userData.put(KEY_IDUSER, Session.getString(KEY_IDUSER, null));
//        userData.put(KEY_PHONENUMBER, Session.getString(KEY_PHONENUMBER, null));

        return userData;
    }

    public void createidMitraSession( String idMitra) {//String fullName, String username,, String phoneNumber
        editorUser.putBoolean(IS_LOGIN, true);

        editorUser.putString(KEY_IDMITRA, idMitra);

        editorUser.commit();
    }

    public HashMap<String, String> getidMitraFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_IDMITRA, Session.getString(KEY_IDMITRA, null));

        return userData;
    }

    public boolean checkLogin() {
        if (Session.getBoolean(IS_LOGIN, false)) {
            return true;
        } else
            return false;
    }

    public void logoutUserFromSession() {
        editorUser.clear();
        editorUser.commit();
    }

//    public void createPesanSession(String latitude, String longitude ){
//        editorUser.putBoolean(IS_LOGIN, true);
//
//        editorUser.putString(KEY_LATITUDE, latitude);
//        editorUser.putString(KEY_LONGITUDE, longitude);
//
//        editorUser.commit();
//    }
//
//    public HashMap<String, String> getUserPesanFromSession() {
//        HashMap<String, String> userData = new HashMap<String, String>();
//
//        userData.put(KEY_LATITUDE, Session.getString(KEY_LATITUDE, null));
//        userData.put(KEY_LONGITUDE, Session.getString(KEY_LONGITUDE, null));
//
//        return userData;
//    }

    //Jasa
    public static final String KEY_AMBILORDER = "ambilorder";
    public static final String KEY_NAMAJASA= "namajasa";

    public void createLoginJasaSession( String password, String email) {//String fullName, String username,, String phoneNumber
        editorJasa.putBoolean(IS_LOGIN, true);

//        editorUser.putString(KEY_FULLNAME, fullName);
//        editorUser.putString(KEY_USERNAME, username);
        editorJasa.putString(KEY_PASSWORD, password);
        editorJasa.putString(KEY_EMAIL, email);
//        editorUser.putString(KEY_PHONENUMBER, phoneNumber);

        editorJasa.commit();
    }

    public HashMap<String, String> getJasaLoginFromSession() {
        HashMap<String, String> jasaData = new HashMap<String, String>();

//        userData.put(KEY_FULLNAME, Session.getString(KEY_FULLNAME, null));
//        userData.put(KEY_USERNAME, Session.getString(KEY_USERNAME, null));
        jasaData.put(KEY_PASSWORD, Session.getString(KEY_PASSWORD, null));
        jasaData.put(KEY_EMAIL, Session.getString(KEY_EMAIL, null));
//        userData.put(KEY_PHONENUMBER, Session.getString(KEY_PHONENUMBER, null));

        return jasaData;
    }

    public void createOrderSession(String ambilOrder, String namaJasa){
        editorJasa.putBoolean(IS_LOGIN, true);

        editorJasa.putString(KEY_AMBILORDER, ambilOrder);
        editorJasa.putString(KEY_NAMAJASA, namaJasa);

        editorJasa.commit();
    }

    public HashMap<String, String> getOrderFromSession() {
        HashMap<String, String> jasaData = new HashMap<String, String>();

        jasaData.put(KEY_AMBILORDER, Session.getString(KEY_AMBILORDER, null));
        jasaData.put(KEY_NAMAJASA, Session.getString(KEY_NAMAJASA, null));

        return jasaData;
    }

    public void logoutJasaFromSession() {
        editorJasa.clear();
        editorJasa.commit();
    }
}
