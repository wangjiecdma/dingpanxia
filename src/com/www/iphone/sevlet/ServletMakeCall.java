package com.www.iphone.sevlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.codegen.CompilerConstants;
import tools.FSHelp;
import tools.TTSHelp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@WebServlet(name = "ServletMakeCall")
public class ServletMakeCall extends HttpServlet {

    private static ArrayList<CallJob> callArray = new ArrayList<>();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("url :"+ request.getRequestURI());
        String method = request.getRequestURI();
        if (method.endsWith("makecall")){
            String number =  request.getParameter("number");
            String text = request.getParameter("text");
            System.out.println(" maek call number :"+number +"  text :"+text);
            java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            String fileName = "tts_" +format1.format(new Date())+".wav";
            File f = new File(fileName);
            String filePath =  f.getAbsolutePath();

            boolean result = TTSHelp.getInstance().getWavFromText(text,filePath);

            if (result){
                CallJob call = new CallJob();
                call.fileName = filePath;
                call.number = number;
                call.text = text;
                call.state = 0;
                callArray.add(call);
            }
        }else if(method.endsWith("getcall")){

            for (int i =0;i<callArray.size();i++){
                CallJob call = callArray.get(i);
                if (call.state ==0){
                    call.state = 1;
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("number",call.number);
                    jsonObject.put("fileName",call.fileName);

                    FSHelp.getInstance().autoCall(call.number,call.fileName);
                    response.getWriter().write(jsonObject.toJSONString());
                    return ;
                }
            }

        }
    }

    class CallJob {
        int state =0;
        //0 待处理，1正在拨号，2 拨号成功 ，3拨号失败
        String number;
        String text;
        String fileName;
    }
}
