package com.yitaqi.test;

import com.yitaqi.ApmContext;
import com.yitaqi.collects.JdbcCollect;
import com.yitaqi.test.service.MockInstrumentation;
import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-19 16:56
 */
public class JdbcCollectTest {

    private ApmContext context;
    private MockInstrumentation instrumentation;
    private JdbcCollect collect;

    @Before
    public void init() {
        instrumentation = new MockInstrumentation();
        Properties properties = new Properties();
        context = new ApmContext(properties, instrumentation);
        collect = new JdbcCollect(instrumentation, context);
    }

    @Ignore
    @Test
    public void sqlTest() throws Exception {

        String className = "oracle.jdbc.driver.OracleDriver";
        byte[] bytes = collect.transform(JdbcCollect.class.getClassLoader(), className);
        ClassPool pool = new ClassPool();
        pool.insertClassPath(new ByteArrayClassPath(className, bytes));
        pool.get(className).toClass();
        Class.forName(className);
        Connection connection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:ORCL",
                "yitaqi",
                "yitaqi"
        );
        PreparedStatement preparedStatement = connection.prepareStatement("select * from dba_users");
        preparedStatement.executeQuery();
        preparedStatement.close();
        connection.close();
    }
}
