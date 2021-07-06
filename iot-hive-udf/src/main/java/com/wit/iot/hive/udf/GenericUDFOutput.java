package com.wit.iot.hive.udf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.UDFType;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde.Constants;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.apache.hadoop.io.IntWritable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * GenericUDFDBOutput is designed to output data directly from Hive to a JDBC
 * datastore. This UDF is useful for exporting small to medium summaries that
 * have a unique key.
 *
 * Due to the nature of hadoop, individual mappers, reducers or entire jobs can
 * fail. If a failure occurs a mapper or reducer may be retried. This UDF has no
 * way of detecting failures or rolling back a transaction. Consequently, you
 * should only only use this to export to a table with a unique key. The unique
 * key should safeguard against duplicate data.
 *
 * Use hive's ADD JAR feature to add your JDBC Driver to the distributed cache,
 * otherwise GenericUDFDBoutput will fail.
 */
@Description(name = "dboutput",
        value = "_FUNC_(jdbcstring,username,password,preparedstatement,[arguments])"
                + " - sends data to a jdbc driver",
        extended = "argument 0 is the JDBC connection string\n"
                + "argument 1 is the user name\n"
                + "argument 2 is the password\n"
                + "argument 3 is an SQL query to be used in the PreparedStatement\n"
                + "argument (4-n) The remaining arguments must be primitive and are "
                + "passed to the PreparedStatement object\n")
@UDFType(deterministic = false)
public class GenericUDFOutput extends GenericUDF {
    private static final Log LOG = LogFactory.getLog(GenericUDFOutput.class.getName());


    private transient ObjectInspector[] argumentOI;
    private transient Connection connection = null;
    private String url;
    private String user;
    private String pass;
    private final IntWritable result = new IntWritable(-1);

    /**
     * @param arguments
     * argument 0 is the JDBC connection string argument 1 is the user
     * name argument 2 is the password argument 3 is an SQL query to be
     * used in the PreparedStatement argument (4-n) The remaining
     * arguments must be primitive and are passed to the
     * PreparedStatement object
     */
    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        argumentOI = arguments;

        // 这里应该是自定义四个参数，connection url,username,password,query,column1[,columnn]*
        //参数0是要在PreparedStatement参数中使用的SQL查询(0)
        if (arguments[0].getCategory() == ObjectInspector.Category.PRIMITIVE) {
            PrimitiveObjectInspector poi = ((PrimitiveObjectInspector) arguments[0]);

            if (!(poi.getPrimitiveCategory() == PrimitiveObjectInspector.PrimitiveCategory.STRING)) {
                throw new UDFArgumentTypeException(0,
                        "The argument of function should be \""
                                + Constants.STRING_TYPE_NAME + "\", but \""
                                + arguments[0].getTypeName() + "\" is found");
            }
        }

        //其余参数必须是原始的，并传递给PreparedStatement对象(1...)
        for (int i = 1; i < arguments.length; i++) {
            if (arguments[i].getCategory() != ObjectInspector.Category.PRIMITIVE) {
                throw new UDFArgumentTypeException(i,
                        "The argument of function should be primative" + ", but \""
                                + arguments[i].getTypeName() + "\" is found");
            }
        }

        return PrimitiveObjectInspectorFactory.writableIntObjectInspector;
    }

    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        url = ((StringObjectInspector) argumentOI[0])
                .getPrimitiveJavaObject(arguments[0].get());
        user = ((StringObjectInspector) argumentOI[1])
                .getPrimitiveJavaObject(arguments[1].get());
        pass = ((StringObjectInspector) argumentOI[2])
                .getPrimitiveJavaObject(arguments[2].get());

        try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException ex) {
            LOG.error("Driver loading or connection issue", ex);
            result.set(2);
        }

        if (connection != null) {
            try {

                PreparedStatement ps = connection
                        .prepareStatement(((StringObjectInspector) argumentOI[3])
                                .getPrimitiveJavaObject(arguments[3].get()));
                for (int i = 4; i < arguments.length; ++i) {
                    PrimitiveObjectInspector poi = ((PrimitiveObjectInspector) argumentOI[i]);
                    ps.setObject(i - 3, poi.getPrimitiveJavaObject(arguments[i].get()));
                }
                ps.execute();
                ps.close();
                result.set(0);
            } catch (SQLException e) {
                LOG.error("Underlying SQL exception", e);
                result.set(1);
            } finally {
                try {
                    connection.close();
                } catch (Exception ex) {
                    LOG.error("Underlying SQL exception during close", ex);
                }
            }
        }

        return result;
    }

    @Override
    public String getDisplayString(String[] children) {
        StringBuilder sb = new StringBuilder();
        sb.append("dboutput(");
        if (children.length > 0) {
            sb.append(children[0]);
            for (int i = 1; i < children.length; i++) {
                sb.append(",");
                sb.append(children[i]);
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
