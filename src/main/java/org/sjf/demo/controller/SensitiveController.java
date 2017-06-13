package org.sjf.demo.controller;

import org.sjf.demo.dto.JsonData;
import org.sjf.demo.service.SensitiveService;
import org.sjf.demo.service.WordEditService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by SJF on 2017/6/12.
 */
@RestController
@RequestMapping(value = "/sensitive")
public class SensitiveController {

    @Resource
    private SensitiveService sensitiveService;
    @Resource
    private WordEditService wordEditService;

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public JsonData filter(@RequestParam("text") String text) throws Exception {
        List<String> list = sensitiveService.filter(text);
        Map<String, String> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size() - 1; i++) {
            sb.append(list.get(i)).append(",");
        }
        map.put("Sensitive Words", sb.toString());
        map.put("Filtered Text", list.get(list.size() - 1));
        return new JsonData<>(0, "ok", map);
    }

    @RequestMapping(value = "/words", method = RequestMethod.POST)
    public JsonData addWordsList(@RequestParam("words") String word, @RequestParam("list") String listName) throws Exception {
        String[] words = word.split(",");
        for (String w : words) {
            wordEditService.addListWords(listName, w);
        }
        sensitiveService.afterPropertiesSet();
        return new JsonData<>(0, "ok", null);
    }

    @RequestMapping(value = "/words", method = RequestMethod.GET)
    public JsonData showWordsList(@RequestParam("list") String listName) throws Exception {
        return new JsonData<>(0, "ok", wordEditService.showListWords(listName));
    }
}
