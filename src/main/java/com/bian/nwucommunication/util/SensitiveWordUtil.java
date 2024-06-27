package com.bian.nwucommunication.util;

import cn.hutool.core.util.StrUtil;
import com.bian.nwucommunication.dto.FileUploadDTO;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;

import java.util.List;

public class SensitiveWordUtil {

    public static String checkSensitiveWord(FileUploadDTO fileUploadDTO){
        List<String> sensitiveWord = SensitiveWordHelper.findAll(fileUploadDTO.toString());
        StringBuilder sensitiveWordStr = new StringBuilder(StrUtil.EMPTY);
        for(String item : sensitiveWord){
            sensitiveWordStr.append(item).append("、");
        }
        return sensitiveWordStr.toString();
    }
}
