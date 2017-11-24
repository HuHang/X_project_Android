package com.iftech.car.utils;


import android.util.Base64;
import android.util.Log;
import com.google.gson.JsonObject;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Tang Huo song on 2016/11/15.
 */
public class HttpConnect {
    /**
     * GET请求服务器
     * **/
    public static void getServer(final String url1, final String username, final String password, final HttpConnectCallBack httpCallBack ) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    //请求的URL
                    URL url = new URL(url1);
                    //打开连接
                    conn = (HttpURLConnection) url.openConnection();
                    //设置超时
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);
                    //设置请求方式为GET
                    conn.setRequestMethod("GET");
                    // base64认证响应头
                    conn.setRequestProperty("Authorization", base64Encode(username, password));
                    //获得返回状态码
                    int code = conn.getResponseCode();
                    //根据状态码作出相应判断
                    if (code == 200) {
                        //获得输入流
                        InputStream is = conn.getInputStream();
                        // 将结果转换成string类型
                        String result = streamToString(is);
                        httpCallBack.onFinish(result);
                    }else if(code == 400){
                        httpCallBack.onError("找不到数据:"+code);
                    }else if(code == 500){
                        httpCallBack.onError("服务器错误:"+code);
                    }else{
                        httpCallBack.onError("服务器连接错误:"+code);
                    }
                }catch (MalformedURLException e){
                    httpCallBack.onError("连接超时,请检查网络后重试！");
                    e.printStackTrace();
                }catch(ConnectTimeoutException e){
                    httpCallBack.onError("连接超时,请检查网络后重试！");
                    e.printStackTrace();
                }catch (SocketTimeoutException e){
                    httpCallBack.onError("服务器响应超时,请稍后重试！");
                }catch (Exception e) {
                    httpCallBack.onError("网络连接错误，请检查网络后重试！");
                    e.printStackTrace();
                }finally {
                    if(conn != null){
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * post请求服务器
     * */
    public static void postServer(final String url1, final String username, final String password , final HttpConnectCallBack httpCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(url1);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Authorization", base64Encode(username, password));
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    //设置请求头信息
                    conn.setDoOutput(true); //设置可以输出
                    OutputStream os = conn.getOutputStream(); //
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        String result = streamToString(is);
                        httpCallBack.onFinish(result);
                    }else if(code == 400){
                        httpCallBack.onError("找不到数据:"+code);
                    }else if(code == 500){
                        httpCallBack.onError("服务器错误:"+code);
                    }else{
                        httpCallBack.onError("服务器连接错误:"+code);
                    }
                }catch(ConnectTimeoutException e){
                    httpCallBack.onError("连接超时,请检查网络后重试！");
                    e.printStackTrace();
                }catch (MalformedURLException e){
                    httpCallBack.onError("连接超时,请检查网络后重试！");
                    e.printStackTrace();
                }catch (SocketTimeoutException e){
                    httpCallBack.onError("服务器响应超时,请稍后重试！");
                }catch (Exception e) {
                    httpCallBack.onError("网络连接错误，请检查网络后重试！");
                    e.printStackTrace();
                }finally {
                    if(conn != null){
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }


    /**
    *  post 请求服务器，json
    * */
    public static void postJsonServer(final String url1 , final String username, final String password, final JSONObject obj1 , final HttpConnectCallBack httpCallBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpConn= null;
                JSONObject obj = obj1;
                try {
                    String shopIds = obj.getString("shopIds");
                    shopIds = shopIds.replace("[","");
                    shopIds = shopIds.replace("]","");
                    String[] shopArray;
                    if(shopIds.contains(",")){
                        shopArray = shopIds.split(",");
                    }else{
                        shopArray = new String[1];
                        shopArray[0] = shopIds;
                    }
                    obj.remove("shopIds");
                    JSONArray array = new JSONArray();
                    for(int i = 0;i<shopArray.length; i++){
                        array.put(Integer.parseInt(shopArray[i]));
                    }
                    obj.put("shopIds",array);
                    Log.d("========",obj.toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }
                try{
                    // 建立连接
                    URL url = new URL(url1);
                    httpConn = (HttpURLConnection) url.openConnection();
                    // 设置参数
                    httpConn.setDoOutput(true); // 需要输出
                    httpConn.setDoInput(true); // 需要输入
                    httpConn.setUseCaches(false); // 不允许缓存
                    httpConn.setRequestMethod("POST"); // 设置POST方式连接
                    httpConn.setConnectTimeout(50000);// 设置连接超时
                    // 如果在建立连接之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。
                    httpConn.setReadTimeout(50000);// 设置读取超时
                    // 如果在数据可读取之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。

                    // 设置请求属性
                    httpConn.setRequestProperty("Content-Type", "application/json");
                    httpConn.setRequestProperty("Authorization", base64Encode(username,password));
                    httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    // httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                    httpConn.setRequestProperty("Charset", "UTF-8");
                    // 连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
                    httpConn.connect();
                    OutputStreamWriter osw = new OutputStreamWriter(httpConn.getOutputStream(), "utf-8");
                    osw.write(obj.toString());
                    osw.flush();
                    osw.close();
                    // 获得响应状态
                    int resultCode = httpConn.getResponseCode();
                    System.out.println("返回码：" + resultCode);
                    httpConn.getResponseMessage();
                    if (resultCode == 200) {
                        InputStream is = httpConn.getInputStream();
                        String result = streamToString(is);
                        httpCallBack.onFinish(result);
                    }else if(resultCode == 400){
                        httpCallBack.onError("找不到数据:"+resultCode);
                    }else if(resultCode == 500){
                        httpCallBack.onError("服务器错误:"+resultCode);
                    }else{
                        httpCallBack.onError("服务器连接错误:"+resultCode);
                    }
                }catch(ConnectTimeoutException e){
                    httpCallBack.onError("连接超时,请检查网络后重试！");
                    e.printStackTrace();
                }catch (MalformedURLException e){
                    httpCallBack.onError("连接超时,请检查网络后重试！");
                    e.printStackTrace();
                }catch (SocketTimeoutException e){
                    httpCallBack.onError("服务器响应超时,请稍后重试！");
                }catch (Exception e) {
                    httpCallBack.onError("网络连接错误，请检查网络后重试！");
                    e.printStackTrace();
                }finally {
                    if(httpConn != null){
                        httpConn.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 上传文件到服务器（post）
     * */
    public void uploadFiles(final String urlStr, final Map<String, String> textMap, final Map<String, String> fileMap,
                            final String username, final String password, final HttpConnectCallBack httpCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String res = "";
                HttpURLConnection conn = null;
                String BOUNDARY = "----WebKitFormBoundaryHZl2XBfUmE9FpFXc"; //boundary就是request头和上传文件内容的分隔符
                try {
                    URL url = new URL(urlStr);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(30000);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
                    conn.setRequestProperty("Authorization", base64Encode(username, password));
                    OutputStream out = new DataOutputStream(conn.getOutputStream());
                    // text
                    if (textMap != null) {
                        StringBuffer strBuf = new StringBuffer();
                        Iterator<Map.Entry<String, String>> iter = textMap.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry<String, String> entry = iter.next();
                            String inputName = entry.getKey();
                            String inputValue = entry.getValue();
                            if (inputValue == null) {
                                continue;
                            }
                            strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                            strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                            strBuf.append(inputValue);
                        }
                        out.write(strBuf.toString().getBytes());
                    }
                    // file
                    if (fileMap != null) {
                        Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry<String, String> entry = iter.next();
                            String inputName = entry.getKey();
                            String inputValue = entry.getValue();
                            if (inputValue == null) {
                                continue;
                            }
                            File file = new File(inputValue);
                            String filename = file.getName();
        //					MagicMatch match = Magic.getMagicMatch(file, false, true);
        //					String contentType = match.getMimeType();
                            String contentType = "multipart/form-data;charset=utf8";
                            StringBuffer strBuf = new StringBuffer();
                            strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                            strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
                            strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                            out.write(strBuf.toString().getBytes());
                            DataInputStream in = new DataInputStream(new FileInputStream(file));
                            int bytes = 0;
                            byte[] bufferOut = new byte[1024];
                            while ((bytes = in.read(bufferOut)) != -1) {
                                out.write(bufferOut, 0, bytes);
                            }
                            in.close();
                        }
                    }
                    byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
                    out.write(endData);
                    out.flush();
                    out.close();
                    // 读取返回数据
                    StringBuffer strBuf = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        strBuf.append(line).append("\n");
                    }
                    res = strBuf.toString();
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        httpCallBack.onFinish(res);
                    }else if(code == 400){
                        httpCallBack.onError("找不到数据:"+code);
                    }else if(code == 500){
                        httpCallBack.onError("服务器错误:"+code);
                    }else{
                        httpCallBack.onError("服务器连接错误:"+code);
                    }
                    reader.close();
                } catch (ConnectTimeoutException e1) {
                    httpCallBack.onError("连接超时,请检查网络后重试！");
                    e1.printStackTrace();
                } catch (MalformedURLException e2) {
                    httpCallBack.onError("连接超时,请检查网络后重试！");
                    e2.printStackTrace();
                } catch (SocketTimeoutException e3) {
                    httpCallBack.onError("服务器响应超时,请稍后重试！");
                    e3.printStackTrace();
                } catch (Exception e4) {
                    httpCallBack.onError("系统异常"+e4.getMessage());
                    e4.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     *  Base64验证状态头生成
     * */
    public static String base64Encode(String username, String password) throws UnsupportedEncodingException {
        String tok = username+":"+password;
        String hash = Base64.encodeToString(tok.getBytes("utf-8"), Base64.DEFAULT);
        return "Basic "+hash;
    }

    /**
     * 将字节流转换成字符串
     * */
    public static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface HttpConnectCallBack {
        // 网络请求成功
        void onFinish(String response);

        // 网络请求失败
        void onError(String response);
    }
}