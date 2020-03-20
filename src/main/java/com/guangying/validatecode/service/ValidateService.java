package com.guangying.validatecode.service;

import com.github.botaruibo.xvcode.generator.Generator;
import com.github.botaruibo.xvcode.generator.GifVCGenerator;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Base64;

/**
 * @author 光影九曜	QQ:150032099
 * @ClassName:  PayValidateService
 * @Description: [支付验证码逻辑处理]   
 * @date:   2019年11月18日 下午3:01:12 
 * @version V1.0
 */
@Service
public class ValidateService {

    private static final Logger LOG = LoggerFactory.getLogger(ValidateService.class);

    static {
        try {
            modifyGifDelayTime(300);
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            LOG.error("设置git验证码播放时间异常", e);
        }
    }

    /**
     * [获取验证码]
     * @param json [json对象]
     * @param type [图片类型]
     * @param width [图片宽度]
     * @param height [图片高度]
     * @param count [图片上显示的验证码个数]
     * @param gifDelayTime [图片播放一次所需时间, 默认300ms]
     * @throws IOException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     */
    public void outImage(JSONObject json, String type, short width, short height,
                         byte count, int gifDelayTime) throws IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException, JSONException {
        if (gifDelayTime > 0) {
            modifyGifDelayTime(gifDelayTime);
        }
        //[创建VXcode对象]
        Generator g = new GifVCGenerator(width, height, count);
        //[图片输出到字节数组类中]
        ByteArrayOutputStream baos = new ByteArrayOutputStream(8096);
        g.write2out(baos).close();
        //[获取图片字节数组]
        byte[] imgBytes = baos.toByteArray();
        //[通过对象获取到验证码字符串]
        String validateCode = g.text();
        //[将图片进行base64编码]
        String imgBase64 = Base64.getEncoder().encodeToString(imgBytes);
        //[添加到json对象中]
        json.put("validateCode", validateCode);
        json.put("imgBase64", imgBase64);
    }

    /**
     * [设置gif播放时间]
     * @param gifDelayTime [单位ms]
     */
    private static void modifyGifDelayTime(int gifDelayTime) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
        Class<?> aClass = Class.forName("com.github.botaruibo.xvcode.generator.GifVCGenerator");
        Field f = aClass.getDeclaredField("gifDelayTime");
        f.setAccessible(true);
        f.setInt(null, gifDelayTime);
    }
}
