package tools;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tigerbrokers.stock.openapi.client.constant.ApiServiceType;
import com.tigerbrokers.stock.openapi.client.https.client.TigerHttpClient;
import com.tigerbrokers.stock.openapi.client.https.request.TigerHttpRequest;
import com.tigerbrokers.stock.openapi.client.https.response.TigerHttpResponse;
import com.tigerbrokers.stock.openapi.client.struct.enums.KType;
import com.tigerbrokers.stock.openapi.client.struct.enums.Market;
import com.tigerbrokers.stock.openapi.client.util.builder.QuoteParamBuilder;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

import java.io.File;
import java.sql.Time;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TigerHelp {
    private  String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANPlSt4oQJAGyPMQ\n" +
            "MMauxW+CickFe0Qb/LgEhQ9tl1jUQgVV9H/yMCoq8MiufwVQKQX9USEzXrwGSWMR\n" +
            "yaYoAqp6s1vOAG0IMasvB66F1Oc9VzOdPdjuKvytNhu7ZUyMhcNBiuP2EDz+OnkZ\n" +
            "xze/IJY+SbsGO7BJbyNiXd2QukkLAgMBAAECgYBK1TqIlLygYNUNClW/hS+S/w38\n" +
            "IWR0HFI3BtmbJkACDgw26DFOElEyQzIAHagcTz/9roW9bO/zBWFbjIejl5PAzsaZ\n" +
            "fzzch8bCZJZzgr52V2vtuMgfM7WWV2eSUxJDUvjvVQLQrVceQH29zIbcGdBDgLKV\n" +
            "WcROA5WXrrs4EgJMgQJBAOzOOQGMdisdJA8iP3ask/UtyVsIMqz9kXZUE6KvnMFu\n" +
            "L/Xk20RgAoedA5jPQP//3VvydrRpUUzoC3rWNrdSaUsCQQDlEi7jyX/SPMxIiPRF\n" +
            "rRBa1x8xXaf7YCotcvleG/BDeVOWITWcIiLR41kvd09TBwv70A9vPGmVdRVmr1FQ\n" +
            "dYdBAkBme/2J4QtHL25qqkzDBH9oLa+mGQ8tdPQePNbeopf50sEo7ynCFrchj999\n" +
            "o7yizk4zu+SJwcawvQAy4kB96EmvAkEA1yDYgHyAWoVWpNhZ8AW0Uluto8L9O4r4\n" +
            "kW5k2wkJ/5Q198yAeH1mjg8hdv5ptOPtUTP5CtKFpiMrUiBcEWAPAQJAGUZRMJfv\n" +
            "Hx6imzNkaFI1i2Ncx+9/Uk9OVRQaSSxVYtf1jvyJaP+FwFY9fYwAB8YBluJX4ONY\n" +
            "gwERY1R9fX2j6A==\n" +
            "-----END PRIVATE KEY-----";

    private  TigerHttpClient client =null;



    private  TigerHelp(){
        client = new TigerHttpClient("https://openapi.itiger.com/gateway", "20150067", privateKey);
    }


    private static TigerHelp  mHelp = null;

    public  static  TigerHelp getInstance(){
        if (mHelp == null){
            mHelp = new TigerHelp();
        }
        return mHelp;
    }


    public String getKLineData_5(String symbol,String date){
        TigerHttpRequest request = new TigerHttpRequest(ApiServiceType.KLINE);


        String bizContent = QuoteParamBuilder.instance()
                .symbol(symbol)
                .market(Market.US)
                .period(KType.min5)
                .right("br")
                .limit(5)
                .buildJson();

        request.setBizContent(bizContent);
        TigerHttpResponse response = client.execute(request);

        return response.getData();
    }

    public String getKline10Day(String symbol){
        TigerHttpRequest request = new TigerHttpRequest(ApiServiceType.KLINE);

        String time = startTime(22);


        String bizContent = QuoteParamBuilder.instance()
                .symbol(symbol)
                .market(Market.US)
                .period(KType.day)
                .endTime("2019-12-13")
                .right("br")
                .limit(11)
                .buildJson();

        request.setBizContent(bizContent);
        TigerHttpResponse response = client.execute(request);

        return response.getData();
    }

    public float getMomentumValue(String symbol){
        String result = getKline10Day(symbol);
        JSONObject jsonObject =  JSON.parseObject(result);
        JSONArray array =  jsonObject.getJSONArray("items");
        float total = 0;
        float lastPrice=0,currentPrice=0;
        for (int i =1;i<array.size();i++){
            lastPrice  = array.getJSONObject(i-1).getFloat("close");
            currentPrice = array.getJSONObject(i).getFloatValue("close");
            int  volume = array.getJSONObject(i).getIntValue("volume");
            total +=  Math.abs(currentPrice - lastPrice)*volume;

        }
        float avg = total/ (array.size() -1);


        return getCurrentMomentum(symbol)/avg;
    }
    public float getCurrentMomentum(String symbol){

        try {
            Thread.sleep(10 * 1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        TigerHttpRequest request = new TigerHttpRequest(ApiServiceType.STOCK_DETAIL);


        String bizContent = QuoteParamBuilder.instance()
                .symbol(symbol)
                .market(Market.US)
                .buildJson();

        request.setBizContent(bizContent);
        TigerHttpResponse response = client.execute(request);

        JSONObject jsonObject  = JSON.parseObject(response.getData());
        System.out.println(response.getData());
        JSONArray array =  jsonObject.getJSONObject("data").getJSONArray("items");
        JSONObject item = array.getJSONObject(0);

        float volume = Math.abs( item.getFloatValue("latestPrice") - item.getFloatValue("preClose"))*
                item.getIntValue("volume");
        long time = item.getLongValue("timestamp");
        Date date = new Date(time);

        int hour = date.getHours();
        int min = date.getMinutes();
        int sec = date.getSeconds();

        if (hour>=22){
            hour -= 22;
        }else{
            hour += 2;
        }
        int timelen = hour*3600+min*60 +sec - 30*60;

        volume = volume *3600*6+30*60/timelen;

        return volume;

    }



    public String getKLineData_15(String symbol,String date){
        TigerHttpRequest request = new TigerHttpRequest(ApiServiceType.KLINE);


        String bizContent = QuoteParamBuilder.instance()
                .symbol(symbol)
                .market(Market.US)
                .period(KType.min15)
                .right("br")
                .limit(10)
                .buildJson();

        request.setBizContent(bizContent);
        TigerHttpResponse response = client.execute(request);

        return response.getData();
    }


    public String startTime(int beforDays){
        Date date = new Date();
        if (date.getHours() <5){
            beforDays +=1;
        }
        long time = date.getTime() - beforDays*24*60*60*1000;
        date = new Date(time);
        String value = String.format("%d-%02d-%02d 22:00:00",date.getYear()+1900,date.getMonth()+1,date.getDate());

        return value;
    }


    public void start(){
        thread.start();
    }


    Thread thread = new Thread(){

        private SymbolConfig  mList[] = new SymbolConfig[]{
                new SymbolConfig("QQQ",0.48f,0.99f,"纳指"),
                new SymbolConfig("AAPL",0.48f,0.99f,"苹果"),
                new SymbolConfig("AMZN",0.48f,0.99f,"亚马逊"),

        };


        @Override
        public void run() {


            System.out.println("start time :"+startTime(0));
            System.out.println("start time :"+startTime(1));

            String date  = startTime(0);


            String result =  getKline10Day("AAPL");
            System.out.println("10 day :"+result);

//            System.out.println("AAPL 动能 :"+getMomentumValue("AAPL"));
//            System.out.println("QQQ 动能 :"+getMomentumValue("QQQ"));
//            System.out.println("亚马逊 动能 :"+getMomentumValue("AMZN"));
//            System.out.println("JD 动能 :"+getMomentumValue("JD"));
//            System.out.println("AMD 动能 :"+getMomentumValue("AMD"));
//            System.out.println("镁光 动能 :"+getMomentumValue("MU"));
//            System.out.println("特斯拉 动能 :"+getMomentumValue("TLSA"));

            while (true) {

                Date time = new Date();
                System.out.println("current  time :"+ time );

                int min = time.getMinutes()%5;
                int sec = time.getSeconds();

                int wait = (60 - sec +10) + (4-min)*60;
                try{
                    Thread.sleep(wait*1000);
                }catch (Exception e){
                    e.printStackTrace();
                }


                System.out.println("now is a chance to get data :"+new Date());


                for (int index = 0; index < mList.length; index++) {
                    SymbolConfig config = mList[index];
                    String test = getKLineData_5(config.symbol, date);
                    System.out.println("check symble :"+config.name+" data:"+test);

                    JSONObject jsonObject = JSON.parseObject(test);
                    if (jsonObject.containsKey("items")) {
                        JSONArray items = jsonObject.getJSONArray("items");
                        System.out.println("result size  :" + items.size());
                        if (items.size()<6){
                            //开盘半小时不做任何提醒
                            break;
                        }
                        //五分钟只做突然的暴涨暴跌检测 //检车倒数第二个数据为完整的五分钟数据
                        PriceItem price = PriceItem.from(items.getJSONObject(items.size() - 2));
                        System.out.println("五分钟 cehck price :"+price.close);

                            PriceItem  pp = PriceItem.from(items.getJSONObject(items.size()-1));
                            Date priceTime =  new Date(pp.time);
                            System.out.println(" price time :"+priceTime.toString());


                        if (Math.abs(price.persent) >= config.over_line_5) {
                            //触发报警
                            String value = floatString(Math.abs( price.persent));//

                            String alert = "来电提醒，您关注的股票："+config.name + ",五分钟之内出现" + (price.persent > 0 ? "暴涨" : "暴跌") + "，幅度为百分之" +value;
                            //send call
                            alertCall(alert);

                            System.out.println("check alert :"+alert);

                        }else{
                            System.out.println("check normal");
                        }
                    }


                    result = getKLineData_15(config.symbol, date);
                    jsonObject = JSON.parseObject(result);
                    if (jsonObject.containsKey("items")) {
                        JSONArray items = jsonObject.getJSONArray("items");
                        System.out.println("result size  :" + items.size());

                        //15分钟趋势检测
                        if (items.size() >= 3) {
                            int flag = 0;
                            float total = 0;
                            for (int i = items.size() - 3; i < items.size(); i++) {
                                PriceItem price = PriceItem.from(items.getJSONObject(i));
                                if (price.persent > 0) {
                                    flag++;
                                } else {
                                    flag--;
                                }
                                total += price.persent;

                            }
                            System.out.println("cehck 15  :"+flag+ "   , "+total);

                            if (((flag == 3) || (flag == -3)) && (Math.abs(total) > config.over_line_15)) {
                                //三次检测方向一致，并且三次幅度相加超过设置的观测值。

                                String value = floatString(Math.abs(total));
                                String alert ="来电提醒，您关注的股票："+ config.name + ",四十五分钟之内趋势一致，" + (flag > 0 ? "涨幅" : "跌幅") + "为百分之" +value;
                                //send call
                                System.out.println("check alert :"+alert);
                                alertCall(alert);

                            }else {
                                System.out.println("check normal");
                            }

                        }

                    }

                    try {
                        Thread.sleep(30 * 1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }



            }




        }
    };

    private String floatString(float value){
        String str = String.valueOf(value);
        str =  str.substring(0,4).replace(".","点");
        str = str.replace("0","零").replace("1","一");
        str = str.replace("2","二").replace("3","三");
        str = str.replace("4","四").replace("5","五");
        str = str.replace("6","六").replace("7","七");
        str = str.replace("8","八").replace("9","九");


        return str;
    }


    public void alertCall(String alert ){
        String number =  "1006";
        String text = alert+", , , , , , , , , "+alert+", , , , , , , , , 谢谢您您。";
        System.out.println(" maek call number :"+number +"  text :"+text);
        java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        String fileName = "tts_" +format1.format(new Date())+".wav";
        File f = new File(fileName);
        String filePath =  f.getAbsolutePath();

        boolean result = TTSHelp.getInstance().getWavFromText(text,filePath);
        FSHelp.getInstance().autoCall(number,filePath);

    }

    public static void main(String[] args){
        TigerHelp.getInstance().start();

    }

    class SymbolConfig{
        String symbol;
        float  over_line_5;
        float  over_line_15;
        String name;
        public SymbolConfig(String s,float line5 ,float line15,String n){
            symbol = s;
            over_line_5 = line5;
            over_line_15 = line15;
            name = n;
        }
    }

    static class  PriceItem {
        float persent;
        float close;
        float high;
        float low;
        float open ;
        long  time;
        int volume ;

        public static PriceItem from(JSONObject obj){
            PriceItem item = new PriceItem();
            item.close = obj.getFloat("close");
            item.open = obj.getFloat("open");

            item.low  = obj.getFloat("low");
            item.high = obj.getFloat("high");
            item.time = obj.getLong("time");
            item.volume = obj.getInteger("volume");

            item.persent = (item.close-item.open)*100/item.open;

            return item;
        }

    }

}
