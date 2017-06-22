package com.example.bx.show;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by BX on 2017/6/22.
 */

public class WebService {
    private  static  String IP = "192.168.2.113";

    public static String executeHttpGet(String username, String password){
        HttpURLConnection conn = null;
        InputStream is = null;

        try{
            String path = "http://" + IP + "/HelloWorld/servlet/LoginServlet";
            path = path + "?username=" + username + "&password=" + password;

            conn = (HttpURLConnection)new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");

            if(conn.getResponseCode() == 200){
                is = conn.getInputStream();
                return parseInfo(is);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(conn != null){
                conn.disconnect();
            }
            if(is != null){
                try {
                    is.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private  static  String parseInfo(InputStream inStream) throws Exception{
        byte[] data = read(inStream);
        return new String(data, "UTF-8");
    }

    public  static byte[] read(InputStream inputStream) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
        return outputStream.toByteArray();
    }
}
