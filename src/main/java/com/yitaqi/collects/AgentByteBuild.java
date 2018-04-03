package com.yitaqi.collects;

import javassist.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-14 16:35
 */
public class AgentByteBuild {

    private final String className;
    private final ClassLoader loader;
    private final CtClass ctClass;

    public AgentByteBuild(String className, ClassLoader loader, CtClass ctClass) {
        this.className = className;
        this.loader = loader;
        this.ctClass = ctClass;
    }

    public void updateMethod(CtMethod method, MethodSrcBuild build) throws CannotCompileException {

        String methodName = method.getName();
        // 复制方法
        CtMethod agentMethod = CtNewMethod.copy(method, methodName, ctClass, null);
        // 原方法重命名
        method.setName(methodName + "$agent");
        // 复制方法重置为代理
        agentMethod.setBody(build.buildSrc(agentMethod));
        // 将代理方法添加到类中
        ctClass.addMethod(agentMethod);

    }

    public byte[] toBytecode() throws IOException, CannotCompileException {

        return ctClass.toBytecode();
    }
    public static class MethodSrcBuild {

        private String beginSrc;
        private String endSrc;
        private String errorSrc;

        public MethodSrcBuild setBeginSrc(String beginSrc) {
            this.beginSrc = beginSrc;
            return this;
        }

        public MethodSrcBuild setEndSrc(String endSrc) {
            this.endSrc = endSrc;
            return this;
        }

        public MethodSrcBuild setErrorSrc(String errorSrc) {
            this.errorSrc = errorSrc;
            return this;
        }

        public String buildSrc(CtMethod method) {
            String result;
            try {
                String template = "void".equals(method.getReturnType().getName()) ? sourceVoid : source;
                String begin = beginSrc == null ? "" : beginSrc;
                String end = endSrc == null ? "" : endSrc;
                String error = errorSrc == null ? "" : errorSrc;
                result = String.format(template, begin, method.getName(), error, end);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
            return result;

        }

        final static String source = "{\n" +
                "            %s\n" +
                "            Object result = null;\n" +
                "            try {\n" +
                "                result = ($w)%s$agent($$);\n" +
                "            } catch (Throwable e) {\n" +
                "                %s\n" +
                "                throw e;\n" +
                "            } finally {\n" +
                "                %s\n" +
                "            }\n" +
                "            return ($r) result;" +
                "}\n";

        final static String sourceVoid = "{\n" +
                "            %s\n" +
                "            try {\n" +
                "                %s$agent($$);\n" +
                "            } catch (Throwable e) {\n" +
                "                %s\n" +
                "                throw e;\n" +
                "            } finally {\n" +
                "                %s\n" +
                "            }\n" +
                "}\n";
    }
}
