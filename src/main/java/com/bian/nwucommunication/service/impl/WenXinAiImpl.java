package com.bian.nwucommunication.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bian.nwucommunication.common.errorcode.BaseErrorCode;
import com.bian.nwucommunication.common.execption.ServiceException;
import com.bian.nwucommunication.config.WenXinConfig;
import com.bian.nwucommunication.service.WenXinAi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WenXinAiImpl implements WenXinAi {

    public static final String ACCESS_TOKEN = "?access_token=";
    public static final Double TEMPERATURE = 0.95;
    public static final Double TOP_P = 0.8;
    public static final Double PENALTY_SCORE = 1.0;

    @Resource
    private WenXinConfig wenXinConfig;

    List<Map<String,String>> messages = new ArrayList<>();

    public static final String SummaryPrefix = "总结一下下面的内容：";

    /**
     * 非流式问答
     * @param content 文件内容
     * @return
     * @throws IOException
     */
    @Override
    public String getSummary(String content) {
        String responseStr = null;
        //先获取令牌然后才能访问api
        if (wenXinConfig.flushAccessToken() != null) {
            HashMap<String, String> user = new HashMap<>();
            user.put("role","user");
            user.put("content",SummaryPrefix + content);
            messages.add(user);
            String requestJson = constructRequestJson(TEMPERATURE,TOP_P,PENALTY_SCORE,false,messages);
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestJson);
            Request request = new Request.Builder()
                    .url(wenXinConfig.ERNIE_Bot_4_0_URL + ACCESS_TOKEN + wenXinConfig.flushAccessToken())
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

            try {
                String responseJson = HTTP_CLIENT.newCall(request).execute().body().string();
                JSONObject responseObject = JSON.parseObject(responseJson);
                System.out.println(responseObject);
                //将回复的内容添加到消息中
                HashMap<String, String> assistant = new HashMap<>();
                assistant.put("role","assistant");
                assistant.put("content",responseObject.getString("result"));
                responseStr = responseObject.getString("result");
                messages.add(assistant);
            } catch (IOException e) {
                log.error("调用文心一言失败,失败原因：{}",e.getMessage());
                throw new ServiceException(BaseErrorCode.REMOTE_ERROR);
            }
        }
        return responseStr;
    }


    /**
     * 构造请求的请求参数
     * @param temperature
     * @param topP
     * @param penaltyScore
     * @param messages
     * @return
     */
    public String constructRequestJson(Double temperature,
                                       Double topP,
                                       Double penaltyScore,
                                       boolean stream,
                                       List<Map<String, String>> messages) {
        Map<String,Object> request = new HashMap<>();
        request.put("temperature",temperature);
        request.put("top_p",topP);
        request.put("penalty_score",penaltyScore);
        request.put("stream",stream);
        request.put("messages",messages);
        return JSON.toJSONString(request);
    }
}
