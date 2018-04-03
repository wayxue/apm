package com.yitaqi;

import com.yitaqi.collects.JdbcCollect;
import com.yitaqi.collects.ServiceCollect;
import com.yitaqi.filter.JsonFilter;
import com.yitaqi.interfaces.ICollect;
import com.yitaqi.interfaces.IFilter;
import com.yitaqi.interfaces.IOutput;
import com.yitaqi.interfaces.Model;
import com.yitaqi.output.JulOutput;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-14 10:53
 */
public class ApmContext {

    private Properties properties;
    private Instrumentation instrumentation;
    private List<ICollect> collects = new ArrayList<ICollect>();
    private IFilter filter;
    private IOutput output;

    public ApmContext(Properties properties, Instrumentation instrumentation) {

        this.properties = properties;
        this.instrumentation = instrumentation;
        // 注册采集器
        collects.add(new ServiceCollect(instrumentation, this));
        collects.add(new JdbcCollect(instrumentation, this));
        // 注册过滤器
        filter = new JsonFilter();
        // 注册输出器
        output = new JulOutput();
    }

    public void submitCollectResult(Model model) {

        // TODO 基于后台线程执行
        // 将模型过滤
        Object json = filter.doFilter(model);
        output.output2Log(json);
    }

    public String getConfig(String key) {

        //TODO 这里需要读取配置文件
        return properties.getProperty(key);
    }
}
