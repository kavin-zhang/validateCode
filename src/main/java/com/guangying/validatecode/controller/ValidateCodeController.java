package com.guangying.validatecode.controller;

import com.guangying.validatecode.service.ValidateService;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author 光影九曜 QQ: 150032099
 * @description [图片验证码接口]
 * @date 2020/3/20 10:34
 * @since 1.0
 */
@RestController
public class ValidateCodeController {

    private static final Logger LOG = LoggerFactory.getLogger(ValidateCodeController.class);

    @Autowired
    private ValidateService validateService;

    @RequestMapping("/validate/{type}/{width}/{height}/{count}")
    @ResponseBody
    public String validate(@PathVariable String type, @PathVariable short width, @PathVariable short height,
                         @PathVariable byte count) throws IOException, JSONException {
        return validate(type, width, height, count, 0);
    }

    /**
     * [获取gif验证码图片及验证码字符串]
     * @param type [图片类型]
     * @param width [图片宽度]
     * @param height [图片高度]
     * @param count [图片上显示的验证码个数]
     * @param gifDelayTime [图片播放一次所需时间, 默认300ms]
     * @return [json字符串]
     * @throws IOException
     * @throws JSONException
     */
    @RequestMapping("/validate/{type}/{width}/{height}/{count}/{gifDelayTime}")
    @ResponseBody
    public String validate(@PathVariable String type, @PathVariable short width, @PathVariable short height,
                           @PathVariable byte count, @PathVariable int gifDelayTime) throws IOException, JSONException {
        JSONObject json = new JSONObject();
        try {
            validateService.outImage(json, type, width, height, count, gifDelayTime);
            json.put("code", 0);
            json.put("msg", "");
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            LOG.error("生成图片异常", e);
            json.put("code", -1);
            json.put("msg", "生成图片异常");
        }

        return json.toString();
    }
}
