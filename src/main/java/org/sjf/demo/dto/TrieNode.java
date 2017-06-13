package org.sjf.demo.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * 前缀树
 * Created by SJF on 2017/6/12.
 */
public class TrieNode {

    // 关键词的开始
    private boolean start = false;
    // 关键词的结束
    private boolean end = false;

    // 下一个节点，key表示该节点的字符，value表示该节点
    private Map<Character, TrieNode> subNode = new HashMap<>();

    // 添加下一个节点
    public void addSubNode(Character key, TrieNode node) {
        subNode.put(key, node);
    }

    // 获取下一个节点
    public TrieNode getSubNode(Character key) {
        return subNode.get(key);
    }

    public boolean isKeywordStart() {
        return start;
    }

    public boolean isKeywordEnd() {
        return end;
    }

    public void setKeywordStart(boolean start) {
        this.start = start;
    }

    public void setKeywordEnd(boolean end) {
        this.end = end;
    }
}
