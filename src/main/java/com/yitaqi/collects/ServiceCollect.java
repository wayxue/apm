package com.yitaqi.collects;

import com.yitaqi.ApmContext;
import com.yitaqi.common.WildcardMatcher;
import com.yitaqi.interfaces.ICollect;
import com.yitaqi.model.ServiceStatistics;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;

import java.lang.instrument.Instrumentation;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-14 14:41
 */
public class ServiceCollect extends AbstractByteTransformCollect implements ICollect {

    public static ServiceCollect INSTANCE;
    private final ApmContext context;
    private WildcardMatcher includeMatcher;
    private WildcardMatcher excludeMatcher;

    private static final String beginSrc;
    private static final String endSrc;
    private static final String errorSrc;

    static {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("com.yitaqi.collects.ServiceCollect instance = ");
        stringBuilder.append("com.yitaqi.collects.ServiceCollect.INSTANCE;\r\n");
        stringBuilder.append("com.yitaqi.model.ServiceStatistics statistic =instance.begin(\"%s\",\"%s\");");
        beginSrc = stringBuilder.toString();
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append("instance.end(statistic);");
        endSrc = stringBuilder.toString();
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append("instance.error(statistic,e);");
        errorSrc = stringBuilder.toString();
    }


    public ServiceCollect(Instrumentation instrumentation, ApmContext context) {
        super(instrumentation);
        this.context = context;
        String include = context.getConfig("service.include");
        String exclude = context.getConfig("service.exclude");
        if (include != null) {
            includeMatcher = new WildcardMatcher(include);
        } else {
            System.err.println("[error]未配置 'service.include'参数无法监控service服务方法");
        }
        if (exclude != null) {
            excludeMatcher = new WildcardMatcher(exclude);
        }
        INSTANCE = this;
    }

    public ServiceStatistics begin(String className, String methodName) {

        long begin = System.currentTimeMillis();
        ServiceStatistics model = new ServiceStatistics();
        model.setBegin(begin);
        model.setServiceName(className);
        model.setSimpleName(className.substring(className.lastIndexOf("."), className.length()));
        model.setMethodName(methodName);
        model.setRecordTime(begin);
        model.setRecordModel("service");
        return model;
    }

    public void error(ServiceStatistics model, Throwable e) {

        model.setErrorMsg(e.getMessage());
        model.setErrorType(e.getClass().getSimpleName());
    }

    public void end(ServiceStatistics model) {
        long end = System.currentTimeMillis();
        model.setEnd(end);
        model.setUseTime(end - model.getBegin());
        context.submitCollectResult(model);
    }

    @Override
    public byte[] transform(ClassLoader loader, String className) throws Exception {

        if (includeMatcher == null) {
            return null;
        } else if (includeMatcher.matches(className)) {
            return null;
        } else if (excludeMatcher != null && excludeMatcher.matches(className)) {
            return null;
        }
        CtClass ctClass = toCtClass(loader, className);
        CtMethod[] methods = ctClass.getDeclaredMethods();
        AgentByteBuild agentByteBuild = new AgentByteBuild(className, loader, ctClass);
        for (CtMethod method : methods) {
            // 屏蔽非公共方法
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            // 屏蔽静态方法
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            // 屏蔽本地方法
            if (Modifier.isNative(method.getModifiers())) {
                continue;
            }
            AgentByteBuild.MethodSrcBuild methodSrcBuild = new AgentByteBuild.MethodSrcBuild();
            methodSrcBuild.setBeginSrc(String.format(beginSrc, className, method.getName())).setEndSrc(endSrc).setErrorSrc(errorSrc);
            agentByteBuild.updateMethod(method, methodSrcBuild);
        }
        return agentByteBuild.toBytecode();
    }
}
