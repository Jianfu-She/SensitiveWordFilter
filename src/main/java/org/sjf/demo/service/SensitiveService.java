package org.sjf.demo.service;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.sjf.demo.dto.TrieNode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by SJF on 2017/6/12.
 */
@Service
public class SensitiveService implements InitializingBean {

    // 敏感词替换符
    private static final String DEFAULT_REPLACEMENT = "*";

    // 黑名单-根节点
    private TrieNode rootNode = new TrieNode();
    // 白名单前缀-根节点
    private TrieNode rootNodePre = new TrieNode();
    // 白名单后缀-根节点
    private TrieNode rootNodePost = new TrieNode();


    // 判断是否是符号
    private boolean isSymbol(char c) {
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    // 去除特殊字符
    private String deleteSpecial(String str) {
        char[] chars = str.toCharArray();
        StringBuilder result = new StringBuilder();
        for (char c : chars) {
            if (!isSymbol(c))
                result.append(c);
        }
        return result.toString();
    }

    // 构造前缀树
    private void addWord(TrieNode rootNode, String text) {
        for (int i = 0; i < text.length(); i++) {
            Character c = text.charAt(i);
            // 过滤空格
            if (isSymbol(c))
                continue;
            TrieNode node = rootNode.getSubNode(c);
            if (node == null) {
                node = new TrieNode();
                rootNode.addSubNode(c, node);
            }
            rootNode = node;

            if (i == 0)
                rootNode.setKeywordStart(true);
            if (i == text.length() - 1)
                rootNode.setKeywordEnd(true);
        }
    }

    // 过滤敏感词
    public List<String> filter(String text) {

        List<String> list = new ArrayList<>();
        if (StringUtils.isBlank(text)) {
            list.set(0, text);
            return list;
        }


        StringBuilder result = new StringBuilder();
        int begin = 0; // 一直往后走不回头
        int position = 0; // 当前比较的位置
        int count = 0;
        TrieNode tempNode = rootNode;
        TrieNode tempNodePre = rootNodePre;
        TrieNode tempNodePost = rootNodePost;

        while (position < text.length()) {
            char c = text.charAt(position);
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            tempNode = tempNode.getSubNode(c);
            tempNodePre = tempNodePre == null ? rootNodePre.getSubNode(c) : tempNodePre.getSubNode(c);
            tempNodePost = tempNodePost == null ? rootNodePost.getSubNode(c) : tempNodePost.getSubNode(c);

            if (tempNode == null) {
                // 以begin开始的字符串不存在敏感词
                result.append(text.charAt(begin));
                // 跳到下一个字符开始测试
                position = begin + 1;
                begin = position;
                // 回到树初始节点
                tempNode = rootNode;
            } else if (tempNodePre != null && tempNodePre.isKeywordEnd()) {
                // 白名单前缀-end，黑名单的tmpNode回到根节点
                result.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
                tempNodePre = rootNodePre;
            } else if (tempNode.isKeywordEnd()) {
                // 发现敏感词，结合白名单后缀判断是否误判
                if (tempNodePost != null && tempNodePost.isKeywordStart()) {
                    int index = position;
                    while (++index < text.length()) {
                        char d = text.charAt(index);
                        if (isSymbol(d))
                            continue;
                        tempNodePost = tempNodePost.getSubNode(d);
                        if (tempNodePost == null) {
                            result.append(DEFAULT_REPLACEMENT);
                            position = position + 1;
                            list.add(count++, deleteSpecial(text.substring(begin, position)));
                            begin = position;
                            tempNode = rootNode;
                            break;
                        } else if (tempNodePost.isKeywordEnd()) {
                            while (begin <= index) {
                                result.append(text.charAt(begin++));
                            }
                            position = begin;
                            tempNode = rootNode;
                            tempNodePost = rootNodePost;
                            break;
                        }
                    }
                } else {
                    result.append(DEFAULT_REPLACEMENT);
                    position = position + 1;
                    list.add(count++, deleteSpecial(text.substring(begin, position)));
                    begin = position;
                    tempNode = rootNode;
                }
            } else {
                position++;
            }
        }
        result.append(text.substring(begin));
        list.add(count, result.toString());
        return list;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        InputStream isBlack = Thread.currentThread().getContextClassLoader().getResourceAsStream("dicts/blackList.txt");
        InputStream isWhitePre = Thread.currentThread().getContextClassLoader().getResourceAsStream("dicts/whiteListPre.txt");
        InputStream isWhitePos = Thread.currentThread().getContextClassLoader().getResourceAsStream("dicts/whiteListPost.txt");
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(isBlack, "UTF-8"));
        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(isWhitePre, "UTF-8"));
        BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(isWhitePos, "UTF-8"));
        String lineTxt;

        while ((lineTxt = bufferedReader1.readLine()) != null) {
            lineTxt = lineTxt.trim();
            addWord(rootNode, lineTxt);
        }
        while ((lineTxt = bufferedReader2.readLine()) != null) {
            lineTxt = lineTxt.trim();
            addWord(rootNodePre, lineTxt);
        }
        while ((lineTxt = bufferedReader3.readLine()) != null) {
            lineTxt = lineTxt.trim();
            addWord(rootNodePost, lineTxt);
        }

        bufferedReader1.close();
        bufferedReader2.close();
        bufferedReader3.close();
    }
}
