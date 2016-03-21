package com.zhuang.itoronto;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by Zhuang on 2016-03-19.
 */
class ReadHttpGet extends AsyncTask<Object, Object, Object>

{
    public static interface HttpTaskHandler {
        void taskSuccessful(String json);

        void taskFailed();
    }

    HttpTaskHandler taskHandler;

    public void setTaskHandler(HttpTaskHandler taskHandler) {
        this.taskHandler = taskHandler;
    }

    public static String Inputstr2Str_Reader(InputStream in, String encode)
    {

        String str = "";
        try
        {
            if (encode == null || encode.equals(""))
            {
                // 默认以utf-8形式
                encode = "utf-8";
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));
            StringBuffer sb = new StringBuffer();

            while ((str = reader.readLine()) != null)
            {
                sb.append(str).append("\n");
            }
            return sb.toString();
        }
        catch (UnsupportedEncodingException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return str;
    }

    public String getData(String result)
    {
        String reqURL = result;
        try {
            //URL url = new URL(getResources().getString(R.string.Req_URL)+getString(R.string.Design_Total));
            URL url = new URL(reqURL);
            HttpURLConnection urlConn=(HttpURLConnection)url.openConnection();
            urlConn.setConnectTimeout(2000);
            //设置输入和输出流
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);

            int nReturn = urlConn.getResponseCode();
            if ( nReturn != 200)    //从Internet获取网页,发送请求,将网页以流的形式读回来
                throw new RuntimeException("请求url失败");

            InputStream is = urlConn.getInputStream();


            reqURL = Inputstr2Str_Reader(is, "GBK"); //文件流输入出文件用outStream.write
            //关闭连接
            urlConn.disconnect();

            //reqURL =  Inputstr2Str_Reader(is,"utf-8");
            //reqURL = is.toString();


            return reqURL;


        } catch (MalformedURLException e) {
            // URL is invalid
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
        }

        return null;
    }

    @Override
    protected Object doInBackground(Object... params) {

        return getData(params[0].toString());
     }



    @Override

    protected void onCancelled(Object result) {
        // TODO Auto-generated method stub
        super.onCancelled(result);
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);

       try {
            //JSONObject jsonObject = new JSONObject(result.toString()).getJSONObject("executionTime");
            JSONObject jsonObject = new JSONObject(result.toString());
            if(jsonObject!=null){
                taskHandler.taskSuccessful(result.toString());
            } else {
                taskHandler.taskFailed();
            }
            //获取某个对象的JSON数组
            //JSONArray jsonArray = jsonObject.getJSONArray("stationBeanList");

        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }



    @Override

    protected void onPreExecute() {

        // TODO Auto-generated method stub

        //super.onPreExecute();

        //Toast.makeText(getApplicationContext(), "开始HTTP GET请求", Toast.LENGTH_LONG).show();

    }



    @Override

    protected void onProgressUpdate(Object... values) {

        // TODO Auto-generated method stub

        super.onProgressUpdate(values);

    }



}
