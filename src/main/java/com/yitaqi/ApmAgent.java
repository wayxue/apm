package com.yitaqi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.Properties;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-14 10:53
 */
public class ApmAgent {

    public static void premain(String arg, Instrumentation instrumentation) {

        Properties properties = new Properties();
        try {
            // 装载配置文件
            if (arg != null && "".equals(arg.trim())) {
                properties.load(new ByteArrayInputStream(arg.replaceAll(",", "\n").getBytes()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        new ApmContext(properties, instrumentation);
    }
}
