
# 敏感词过滤 
基于Trie树的敏感词过滤程序，提供HTTP API访问，基本解决了传统方法在中文分词上的缺陷。

# API 
1. 过滤敏感词 
输入一段文本，返回敏感词及敏感词替换为*号后的文本
- Request: /sensitive/filter
- Request Method: GET
- Params: text(String, required)
- Example:
```
http://115.28.74.55/wordfilter/sensitive/filter?text=❤白色情人节❤看色❤情人❤文片之老司 机8❤与  机❤❤8❤尼玛的格尼玛莎拉蒂

{
    "errCode": 0,
    "errMsg": "ok",
    "content": {
        "Sensitive Words": "色情,机8,尼玛,",
        "Filtered Text": "❤白色情人节❤看*人❤文片之老司 机8❤与  *❤*的格尼玛莎拉蒂"
    }
}
```

2. 添加敏感词 
添加一组敏感词 
- Request: /sensitive/words
- Request Method: POST
- Params: words(String, required), list(String, required)
- Example:
``` 
// list在以下3项中选：blackList whiteListPre whiteListPost
// words以逗号分隔

http://115.28.74.55/wordfilter/sensitive/words?list=blackList&words=手枪,军火

{
    "errCode": 0,
    "errMsg": "ok",
    "content": null
}
```

3. 查看敏感词 
- Request: /sensitive/words
- Request Method: GET
- Params: list(String, required)
- Example:
``` 
// list在以下3项中选：blackList whiteListPre whiteListPost

http://115.28.74.55/wordfilter/sensitive/words?list=blackList
{
    "errCode": 0,
    "errMsg": "ok",
    "content": "色情,机8,尼玛,手枪,军火,"
}

http://115.28.74.55/wordfilter/sensitive/words?list=whiteListPre
{
    "errCode": 0,
    "errMsg": "ok",
    "content": "司机,"
}

http://115.28.74.55/wordfilter/sensitive/words?list=whiteListPost
{
    "errCode": 0,
    "errMsg": "ok",
    "content": "情人节,玛莎拉蒂,"
}
```

# 词库说明 
blackList：敏感词黑名单
whiteListPre：敏感词前缀白名单
whiteListPost：敏感词后缀白名单