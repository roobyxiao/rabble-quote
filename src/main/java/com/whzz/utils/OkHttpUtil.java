package com.whzz.utils;

import okhttp3.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class OkHttpUtil {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");

    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .connectTimeout(30 * 1000, TimeUnit.MILLISECONDS)
        .readTimeout(30 * 1000, TimeUnit.MILLISECONDS)
        .writeTimeout(30 * 1000, TimeUnit.MILLISECONDS)
        .cookieJar(new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }
            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(URLUtil.getDomainName(url.url()));
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        }).build();
    /**
     * get 请求
     * @param url  请求url地址
     * @return string
     * */
    public String doGet(String url) {
        return doGet(url, null, null);
    }
    /**
     * get 请求
     * @param url  请求url地址
     * @param params 请求参数 map
     * @return string
     * */
    public String doGet(String url, Map<String, String> params) {
        return doGet(url, params, null);
    }
    /**
     * get 请求
     * @param url  请求url地址
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     * */
    public String doGet(String url, String[] headers) {
        return doGet(url, null, headers);
    }
    /**
     * get 请求
     * @param url  请求url地址
     * @param params 请求参数 map
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     * */
    public String doGet(String url, Map<String, String> params, String[] headers) {
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.keySet().size() > 0) {
            boolean firstFlag = true;
            for (String key : params.keySet()) {
                if (firstFlag) {
                    sb.append("?").append(key).append("=").append(String.valueOf(params.get(key)));
                    firstFlag = false;
                } else {
                    sb.append("&").append(key).append("=").append(String.valueOf(params.get(key)));
                }
            }
        }
        Request.Builder builder = new Request.Builder();
        if (headers != null && headers.length > 0) {
            if (headers.length % 2 == 0) {
                for (int i = 0; i < headers.length; i = i + 2) {
                    builder.addHeader(headers[i], headers[i + 1]);
                }
            } else {
                //log.warn("headers"s length[{}] is error.", headers.length);
            }
        }

        //System.out.println(sb.toString());
        Request request = builder.url(sb.toString()).build();
        //log.info("do get request and url[{}]", sb.toString());
        return execute(request);
    }
    /**
     * post 请求
     * @param url  请求url地址
     * @param params 请求参数 map
     * @return string
     */
    public String doPost(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        //log.info("do post request and url[{}]", url);
        return execute(request);
    }

    public Call doPostCall(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        return okHttpClient.newCall(request);
    }

    /**
     * post 请求, 请求数据为 json 的字符串
     * @param url  请求url地址
     * @param json  请求数据, json 字符串
     * @return string
     */
    public String doPostJson(String url, String json) {
        //log.info("do post request and url[{}]", url);
        return exectePost(url, json, JSON);
    }
    /**
     * post 请求, 请求数据为 xml 的字符串
     * @param url  请求url地址
     * @param xml  请求数据, xml 字符串
     * @return string
     */
    public String doPostXml(String url, String xml) {
        //log.info("do post request and url[{}]", url);
        return exectePost(url, xml, XML);
    }
    private String exectePost(String url, String data, MediaType contentType) {
        RequestBody requestBody = RequestBody.create(contentType, data);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return execute(request);
    }
    private String execute(Request request) {
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            //log.error(ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return "";
    }
}
