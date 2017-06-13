package org.sjf.demo.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


import java.io.*;
import java.net.URL;

/**
 * 添加敏感词
 * Created by SJF on 2017/6/13.
 */
@Service
public class WordEditService {

    public void addListWords(String filename, String content) throws Exception {
        Resource resource = new ClassPathResource("dicts/"  + filename + ".txt");
        File file = resource.getFile();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
        out.write("\r\n" + content);
        out.close();
    }

    public String showListWords(String filename) throws Exception {
        StringBuilder result = new StringBuilder();
        Resource resource = new ClassPathResource("dicts/"  + filename + ".txt");
        File file = resource.getFile();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        String lineTxt;
        while((lineTxt = br.readLine())!=null) {
            result.append(lineTxt).append(",");
        }
        br.close();
        return result.toString();
    }
}
