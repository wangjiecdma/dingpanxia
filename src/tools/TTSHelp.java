package tools;

import com.alibaba.nls.client.AccessToken;
import com.alibaba.nls.client.protocol.NlsClient;
import com.alibaba.nls.client.protocol.OutputFormatEnum;
import com.alibaba.nls.client.protocol.SampleRateEnum;
import com.alibaba.nls.client.protocol.tts.SpeechSynthesizer;
import com.alibaba.nls.client.protocol.tts.SpeechSynthesizerListener;
import com.alibaba.nls.client.protocol.tts.SpeechSynthesizerResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class TTSHelp {

    String accessKeyId = "LTAIDzFXpQBoojj6";
    String accessKeySecret = "e2dn3yh4fLvX8BQcczspPZMbQFatBU";
    String appKey = "waVOAZyy8VcJ7l6s";
    private String mToken;
    NlsClient client;
    private  TTSHelp() {

        try {
            AccessToken accessToken = AccessToken.apply(accessKeyId, accessKeySecret);
            mToken = accessToken.getToken();
            long expireTime = accessToken.getExpireTime();

            System.out.println(" get token :"+accessToken);

            System.out.println("  expireTime :"+expireTime);
        }catch (Exception e){
            System.out.println("  Exception :"+e);

            e.printStackTrace();
            return ;
        }

        // Step0 创建NlsClient实例,应用全局创建一个即可,默认服务地址为阿里云线上服务地址
        client = new NlsClient(mToken);
    }

    private static TTSHelp  mHelp = null;

    public static TTSHelp getInstance(){
        if (mHelp == null){
            mHelp = new TTSHelp();
        }
        return mHelp;
    }


    private static SpeechSynthesizerListener getSynthesizerListener( String fileName) {
        SpeechSynthesizerListener listener = null;
        try {
            listener = new SpeechSynthesizerListener() {
                File f = new File(fileName);

                FileOutputStream fout = new FileOutputStream(f);
                // 语音合成结束
                @Override
                public void onComplete(SpeechSynthesizerResponse response) {
                    // 事件名称 SynthesisCompleted
                    System.out.println("name: " + response.getName() +
                            // 状态码 20000000 表示识别成功
                            ", status: " + response.getStatus() +
                            // 语音合成文件路径
                            ", output file :"+ f.getAbsolutePath()
                    );
                }
                // 语音合成的语音二进制数据
                @Override
                public void onMessage(ByteBuffer message) {
                    try {
                        byte[] bytesArray = new byte[message.remaining()];
                        message.get(bytesArray, 0, bytesArray.length);
                        System.out.println("write array:" + bytesArray.length);
                        fout.write(bytesArray);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listener;
    }

    public boolean getWavFromText( String text , String fileName) {
        SpeechSynthesizer synthesizer = null;
        try {
            // Step1 创建实例,建立连接
            synthesizer = new SpeechSynthesizer(client, getSynthesizerListener(fileName));
            synthesizer.setAppKey(appKey);
            // 设置返回音频的编码格式
            synthesizer.setFormat(OutputFormatEnum.WAV);
            // 设置返回音频的采样率
            synthesizer.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K);

            // 设置用于语音合成的文本
            synthesizer.setText(text);
            // Step2 此方法将以上参数设置序列化为json发送给服务端,并等待服务端确认
            synthesizer.start();
            // Step3 等待语音合成结束
            System.out.println("waitForComplete start");;

            synthesizer.waitForComplete();
            System.out.println("waitForComplete end");;
        } catch (Exception e) {
            System.out.println("process  error");
            System.err.println(e.getMessage());
            return false;
        } finally {
            // Step4 关闭连接
            if (null != synthesizer) {
                synthesizer.close();
            }
        }
        return true;
    }
    public void shutdown() {
        client.shutdown();
    }


    public static void main(String[] args) {
        TTSHelp demo = new TTSHelp();
        demo.getWavFromText("小冯您好，这里是盯盘侠给您来电，提醒您美股QQQ短时间内有较大涨幅，特此提醒。交易有风险，操作需谨慎，谢谢。","tts2.wav");
        demo.getWavFromText("小李您好，这里是盯盘侠给您来电，提醒您美股QQQ短时间内有较大涨幅，特此提醒。交易有风险，操作需谨慎，谢谢。","tts3.wav");

    }

}
