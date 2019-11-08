package com.kumoh.paylog2.util;

import android.graphics.Bitmap;
import android.util.Base64;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestHttpURLConnection {

    public JSONObject requestImageProcessing(String _url, Bitmap image) {
        // HttpURLConnection 참조 변수.
        HttpURLConnection conn = null;

        /**
         * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
         * */
        try{
            URL url = new URL(_url);
            conn = (HttpURLConnection) url.openConnection();

            // [2-1]. conn 옵션 설정.

            // URL 요청에 대한 메소드 설정 : POST.
            conn.setRequestMethod("POST");
            // Content type 설정. Request Body 전달시 json 으로 서버에 전달
            conn.setRequestProperty("Content-Type", "application/json");
            // Accept type 설정. Response Data 를 json 으로 서버로 받음
            conn.setRequestProperty("Accept", "application/json");
            // 컨트롤 캐쉬 설정(캐쉬를 유지할 필요가 없으므로 no-cache)
            conn.setRequestProperty("Cache-Control","no-cache");

            // OutputStream 으로 데이터를 넘겨주고,
            conn.setDoOutput(true);
            // InputStream 으로 데이터를 받겠다는 옵션
            conn.setDoInput(true);

            // body 에 보낼 json 객체 생성 및 초기화
            JSONObject job = new JSONObject();
            job.put("id", "1");
            job.put("image", getBase64String(image));

            long time = System.currentTimeMillis();
            SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String strDT = dayTime.format(new Date(time));
            System.out.println(strDT);


            // 혹시나 BufferedWriter랑 섞어서 사용하는거랑 OutputStream 그 자체로 사용하는거랑 속도 비교를 해봤는데
            // OutputStream만 쓴 경우가 더 빨랐다 왜지?? (좀더 공부 해봐야할듯)

            // [2-2]. parameter 전달 및 데이터 읽어오기.
            OutputStream os = conn.getOutputStream();
            os.write(job.toString().getBytes()); // 출력 스트림에 출력.
            os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
            os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.

            // [2-3]. 연결 요청 확인.
            // 실패 시 null을 리턴하고 메서드를 종료.
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            // [2-4]. 읽어온 결과물 리턴.
            // 요청한 URL의 출력물을 BufferedReader로 받는다.
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            // 출력물의 라인과 그 합에 대한 변수.
            String line;
            String page = "";

            // 라인을 받아와 합친다.
            while ((line = reader.readLine()) != null){
                page += line;

            }

            JSONObject receiptContext = new JSONObject(page);

            return receiptContext;

        } catch (MalformedURLException e) { // for URL.
            e.printStackTrace();
        } catch (IOException e) { // for openConnection().
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }

        return null;
    }

    public String getBase64String(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }
}
