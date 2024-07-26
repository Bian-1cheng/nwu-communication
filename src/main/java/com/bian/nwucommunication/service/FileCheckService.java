package com.bian.nwucommunication.service;


import java.io.File;

public interface FileCheckService {

    Double checkImgNsfw(String fileUrl,int retryTimes);

    Boolean checkVideoNsfw(File file,int retryTimes);

}
