package tools;

import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.IEslEventListener;
import org.freeswitch.esl.client.internal.Context;
import org.freeswitch.esl.client.internal.IModEslApi;
import org.freeswitch.esl.client.transport.event.EslEvent;

import java.net.InetSocketAddress;

public class FSHelp {
    final Client inboudClient = new Client();
    private FSHelp(){
        try {
            inboudClient.connect(new InetSocketAddress("192.168.31.8", 8021), "ClueCon", 10);
            inboudClient.addEventListener(new IEslEventListener() {
                @Override
                public void onEslEvent(Context ctx, EslEvent event) {

                }
            });
            inboudClient.setEventSubscriptions(IModEslApi.EventFormat.PLAIN, "all");

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static FSHelp mHelp = null;
    public static FSHelp getInstance(){
        if (mHelp == null){
            mHelp = new FSHelp();
        }
        return mHelp;
    }

    public void autoCall(String number , String file ) {

        String arg = String.format("user/%s &playback(%s)",number,file);
        arg = arg.replace("\\","\\\\");
        System.out.println("call string :"+arg);
        inboudClient.sendApiCommand("originate",arg);
    }

}
