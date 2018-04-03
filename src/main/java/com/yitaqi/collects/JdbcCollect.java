package com.yitaqi.collects;

import com.yitaqi.ApmContext;
import com.yitaqi.interfaces.ICollect;
import com.yitaqi.model.JdbcStatistics;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-16 16:13
 */
public class JdbcCollect extends AbstractByteTransformCollect implements ICollect {

    public static JdbcCollect INSTANCE;
    private final ApmContext context;
    private String include;
    private String exclude;
    private final String agentClass = "oracle.jdbc.driver.OracleDriver";

    private static final String beginSrc;
    private static final String errorSrc;
    private static final String endSrc;

    static {
        beginSrc = "com.yitaqi.collects.JdbcCollect instance = com.yitaqi.collects.JdbcCollect.INSTANCE;";
        endSrc = "result = instance.proxyConnection((java.sql.Connection) result);";
        errorSrc = "";
    }

    public JdbcStatistics begin() {

        JdbcStatistics statistics = new JdbcStatistics();
        statistics.setRecordModel("jdbc");
        long begin = System.currentTimeMillis();
        statistics.setBegin(begin);
        statistics.setRecordTime(begin);
        return statistics;
    }

    public void error(JdbcStatistics statistics, Throwable e) {

        if (statistics == null) {
            return;
        }
        statistics.setErrorMsg(e.getMessage());
        statistics.setErrorType(e.getClass().getName());
        if (e instanceof InvocationTargetException) {
            statistics.setErrorMsg(((InvocationTargetException) e).getTargetException().getMessage());
            statistics.setErrorType(((InvocationTargetException) e).getTargetException().getClass().getName());
        }

    }

    public void end(JdbcStatistics statistics) {

        long end = System.currentTimeMillis();
        statistics.setEnd(end);
        statistics.setUseTime(end - statistics.getBegin());
        if (statistics.getJdbcUrl() != null) {
            statistics.setDataBaseName(getDbName(statistics.getJdbcUrl()));
        }
        context.submitCollectResult(statistics);
    }

    private static String getDbName(String url) {

        String dbName;
        if (url.contains("mysql")) {
            // TODO 未测试
            int index = url.indexOf("?");
            if (index != -1) {
                url = url.substring(0, index);
            }
            dbName = url.substring(url.lastIndexOf("/") + 1);
        } else {
            dbName = url.substring(url.lastIndexOf(":") + 1, url.length());
        }

        return dbName;
    }

    public JdbcCollect(Instrumentation instrumentation, ApmContext context) {

        super(instrumentation);
        this.context = context;
        INSTANCE = this;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className) throws Exception {
        if (agentClass.equals(className)) {
            CtClass ctClass = toCtClass(loader, agentClass);
            AgentByteBuild agentByteBuild = new AgentByteBuild(agentClass, loader, ctClass);
            CtMethod method = ctClass.getMethod("connect", "(Ljava/lang/String;Ljava/util/Properties;)Ljava/sql/Connection;");
            AgentByteBuild.MethodSrcBuild build = new AgentByteBuild.MethodSrcBuild();
            build.setBeginSrc(beginSrc);
            build.setErrorSrc(errorSrc);
            build.setEndSrc(endSrc);
            agentByteBuild.updateMethod(method, build);
            byte[] bytes = ctClass.toBytecode();
            File file = new File(System.getProperties().get("user.dir") + "/target/AgentTest.class");
            file.createNewFile();
            Files.write(file.toPath(), ctClass.toBytecode());
            return bytes;
        }
        return null;
    }

    public Connection proxyConnection(Connection connection) {

        Object result = Proxy.newProxyInstance(JdbcCollect.class.getClassLoader(), new Class[]{Connection.class},
                new ConnectionHandler(connection));
        return (Connection) result;
    }

    public PreparedStatement proxyPreparedStatement(PreparedStatement preparedStatement, JdbcStatistics statistics) {

        Object result = Proxy.newProxyInstance(JdbcCollect.class.getClassLoader(), new Class[]{PreparedStatement.class},
                new PreparedStatementHandler(preparedStatement, statistics));
        return (PreparedStatement) result;
    }

    public class ConnectionHandler implements InvocationHandler {

        private Connection connection;

        private final String[] agentMethod = {"prepareStatement"};

        public ConnectionHandler(Connection connection) {
            this.connection = connection;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            boolean isTargetMethod = false;
            for (String s : agentMethod) {
                if (s.equals(method.getName())) {
                    isTargetMethod = true;
                    break;
                }
            }
            Object result = null;
            JdbcStatistics statistics = null;
            try {
                if (isTargetMethod) {
                    statistics = JdbcCollect.this.begin();
                    statistics.setJdbcUrl(connection.getMetaData().getURL());
                    statistics.setSql((String) args[0]);
                }
                result = method.invoke(connection, args);
                if (isTargetMethod && result instanceof PreparedStatement) {
                    result = proxyPreparedStatement((PreparedStatement) result, statistics);
                }
            } catch (Throwable e) {
                JdbcCollect.this.error(statistics, e);
                JdbcCollect.this.end(statistics);
                throw e;
            }
            return result;
        }
    }

    public class PreparedStatementHandler implements InvocationHandler {

        private PreparedStatement preparedStatement;
        private JdbcStatistics statistics;
        private final String[] agentMethod = {"execute", "executeUpdate", "executeQuery"};

        public PreparedStatementHandler(PreparedStatement preparedStatement, JdbcStatistics statistics) {
            this.preparedStatement = preparedStatement;
            this.statistics = statistics;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            boolean isTargetMethod = false;
            for (String s : agentMethod) {
                if (s.equals(method.getName())) {
                    isTargetMethod = true;
                    break;
                }
            }

            Object result;
            try {
                result = method.invoke(preparedStatement, args);
                if (isTargetMethod) {
                    statistics.setPrepared(true);
                }
            } catch (Throwable e) {
                if (isTargetMethod) {
                    JdbcCollect.this.error(statistics, e);
                }
                throw e;
            } finally {
                if (isTargetMethod) {
                    JdbcCollect.this.end(statistics);
                }
            }

            return result;
        }
    }
}

