package tools;
import com.tigerbrokers.stock.openapi.client.constant.ApiServiceType;
import com.tigerbrokers.stock.openapi.client.https.client.TigerHttpClient;
import com.tigerbrokers.stock.openapi.client.https.request.TigerHttpRequest;
import com.tigerbrokers.stock.openapi.client.https.response.TigerHttpResponse;
import com.tigerbrokers.stock.openapi.client.struct.enums.KType;
import com.tigerbrokers.stock.openapi.client.struct.enums.Market;
import com.tigerbrokers.stock.openapi.client.util.builder.QuoteParamBuilder;

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


    public String getKLineData(List<String> symbolList,String date){
        TigerHttpRequest request = new TigerHttpRequest(ApiServiceType.KLINE);


        String bizContent = QuoteParamBuilder.instance()
                .symbols(symbolList)
                .market(Market.US)
                .period(KType.min5)
                .beginTime(date)
                .endTime(date)
                .right("br")
                .buildJson();

        request.setBizContent(bizContent);
        TigerHttpResponse response = client.execute(request);

        return response.getData();
    }




}
