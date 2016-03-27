package com.zhuang.itoronto.utilities;

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
public class ReadHttpGet extends AsyncTask<Object, Object, Object>

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
                // 榛樿浠tf-8褰㈠紡
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
            //璁剧疆杈撳叆鍜岃緭鍑烘祦
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);

            int nReturn = urlConn.getResponseCode();
            if ( nReturn != 200)    //浠嶪nternet鑾峰彇缃戦〉,鍙戦�佽姹�,灏嗙綉椤典互娴佺殑褰㈠紡璇诲洖鏉�
                throw new RuntimeException("璇锋眰url澶辫触");

            InputStream is = urlConn.getInputStream();


            reqURL = Inputstr2Str_Reader(is, "GBK"); //鏂囦欢娴佽緭鍏ュ嚭鏂囦欢鐢╫utStream.write
            //鍏抽棴杩炴帴
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
            //鑾峰彇鏌愪釜瀵硅薄鐨凧SON鏁扮粍
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

        //Toast.makeText(getApplicationContext(), "寮�濮婬TTP GET璇锋眰", Toast.LENGTH_LONG).show();

    }



    @Override

    protected void onProgressUpdate(Object... values) {

        // TODO Auto-generated method stub

        super.onProgressUpdate(values);

    }



}
