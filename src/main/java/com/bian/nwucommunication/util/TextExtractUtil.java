package com.bian.nwucommunication.util;

import com.bian.nwucommunication.common.execption.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class TextExtractUtil {

    public String fileExtractText(String filePath){
        String text = null;
        if(filePath.isEmpty()){
            return "";
        }
        if(filePath.toLowerCase().endsWith("doc")){
            try {
                InputStream file = new FileInputStream(new File(filePath));
                WordExtractor wordExtractor = new WordExtractor(file);
                text = wordExtractor.getText();
                file.close();
                wordExtractor.close();
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new ServiceException("doc文件解析失败");
            }
        }
        else if(filePath.toLowerCase().endsWith("docx")){
            try {
                OPCPackage opcPackage = POIXMLDocument.openPackage(filePath);
                POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
                text = extractor.getText();
                opcPackage.close();
                extractor.close();
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new ServiceException("docx文件解析失败");
            }
        }
        else if(filePath.toLowerCase().endsWith("pdf")){
            PDDocument pdDocument;
            try {
                InputStream file = new FileInputStream(new File(filePath));
                pdDocument = PDDocument.load(file);
                PDFTextStripper stripper = new PDFTextStripper();
                text = stripper.getText(pdDocument);
                file.close();
                pdDocument.close();
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new ServiceException("pdf文件解析失败");
            }
        }
        return text.substring(0,2000);
    }
}
