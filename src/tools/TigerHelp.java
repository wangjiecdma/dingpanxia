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
                .beginTime(date)
                .endTime("2019-12-13")
                .right("br")
                .buildJson();

        request.setBizContent(bizContent);
        TigerHttpResponse response = client.execute(request);

        return response.getData();
    }


    public String getKLineData_15(String symbol,String date){
        TigerHttpRequest request = new TigerHttpRequest(ApiServiceType.KLINE);


        String bizContent = QuoteParamBuilder.instance()
                .symbol(symbol)
                .market(Market.US)
                .period(KType.min15)
                .beginTime(date)
                .endTime("2019-12-13")
                .right("br")
                .buildJson();

        request.setBizContent(bizContent);
        TigerHttpResponse response = client.execute(request);

        return response.getData();
    }



    public void start(){
        thread.start();
    }


    Thread thread = new Thread(){

        private SymbolConfig  mList[] = new SymbolConfig[]{
                new SymbolConfig("QQQ",0.48f,0.48f,"纳指"),
                new SymbolConfig("AAPL",0.48f,0.48f,"苹果"),
                new SymbolConfig("AMZN",0.48f,0.48f,"亚马逊"),
                new SymbolConfig("MSFT",0.48f,0.48f,"微软"),
                new SymbolConfig("NFLX",0.48f,0.48f,"奈非"),
                new SymbolConfig("NVDA",0.48f,0.48f,"英伟达"),
                new SymbolConfig("TSLA",0.48f,0.48f,"特斯拉"),
                new SymbolConfig("BABA",0.48f,0.48f,"阿里巴巴"),
                new SymbolConfig("ADBE",0.48f,0.48f,"饿到北"),
                new SymbolConfig("JPM",0.48f,0.48f,"摩根大通"),
                new SymbolConfig("C",0.48f,0.48f,"花旗"),
                new SymbolConfig("BAC",0.48f,0.48f,"美国银行"),
                new SymbolConfig("MS",0.48f,0.48f,"摩根史丹利"),
                new SymbolConfig("MU",0.48f,0.48f,"镁光科技"),
                new SymbolConfig("INTC",0.48f,0.48f,"英特尔"),

        };


        @Override
        public void run() {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String date =  df.format(LocalDateTime.now());

            while (true) {

                for (int index = 0; index < mList.length; index++) {
                    SymbolConfig config = mList[index];
                    System.out.println("check symble :"+config.name);

                    String result = getKLineData_5(config.symbol, date);
                    JSONObject jsonObject = JSON.parseObject(result);
                    if (jsonObject.containsKey("items")) {
                        JSONArray items = jsonObject.getJSONArray("items");
                        System.out.println("result size  :" + items.size());

                        //五分钟只做突然的暴涨暴跌检测
                        PriceItem price = PriceItem.from(items.getJSONObject(items.size() - 1));
                        System.out.println("cehck price :"+price);
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

                            if (((flag == 3) || (flag == -3)) && (total > config.over_line_15)) {
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
                        Thread.sleep(5 * 1000);
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
