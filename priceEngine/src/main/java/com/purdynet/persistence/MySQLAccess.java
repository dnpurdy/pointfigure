package com.purdynet.persistence;

import java.sql.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dnpurdy
 * Date: 8/26/13
 * Time: 7:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class MySQLAccess {
    private static final String CLASS_NAME="com.mysql.jdbc.Driver";

    private static final String HOST="localhost";
    private static final String DB="pf";
    private static final String USER="root";
    private static final String PASS="u704brrbudcn";

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private Connection connect() throws Exception
    {
        Connection conn = null;
        try
        {
            Class.forName(CLASS_NAME);
            return DriverManager.getConnection("jdbc:mysql://"+HOST+"/"+DB+"?user="+USER+"&password="+PASS);
        }
        catch(SQLException x)
        {
            throw new Exception("Failed to connect!", x);
        }
    }

    public ResultSet select(String query) throws Exception
    {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        try
        {
            connection = connect();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            return resultSet;
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            close(resultSet, statement, connection);
        }

    }

    private void writeMetaData(ResultSet resultSet) throws SQLException {
        //   Now get some metadata from the database
        // Result set get the result of the SQL query

        System.out.println("The columns in the table are: ");

        System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
        for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
            System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
        }
    }

    private void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);
            String user = resultSet.getString("myuser");
            String website = resultSet.getString("webpage");
            String summary = resultSet.getString("summary");
            Date date = resultSet.getDate("datum");
            String comment = resultSet.getString("comments");
            System.out.println("User: " + user);
            System.out.println("Website: " + website);
            System.out.println("Summary: " + summary);
            System.out.println("Date: " + date);
            System.out.println("Comment: " + comment);
        }
    }

    // You need to close the resultSet
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

    private void close(ResultSet rs, Statement s, Connection c)
    {
        try
        {
            if (rs != null)
            {
                rs.close();
            }
            if (s != null)
            {
                s.close();
            }
            if (c != null)
            {
                c.close();
            }
        }
        catch (Exception e)
        {
            //intentionally empty
        }
    }
 /*
 *
            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect
                    .prepareStatement("insert into  FEEDBACK.COMMENTS values (default, ?, ?, ?, ? , ?, ?)");
            // "myuser, webpage, datum, summary, COMMENTS from FEEDBACK.COMMENTS");
            // Parameters start with 1
            preparedStatement.setString(1, "Test");
            preparedStatement.setString(2, "TestEmail");
            preparedStatement.setString(3, "TestWebpage");
            preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
            preparedStatement.setString(5, "TestSummary");
            preparedStatement.setString(6, "TestComment");
            preparedStatement.executeUpdate();

            preparedStatement = connect
                    .prepareStatement("SELECT myuser, webpage, datum, summary, COMMENTS from FEEDBACK.COMMENTS");
            resultSet = preparedStatement.executeQuery();
            writeResultSet(resultSet);

            // Remove again the insert comment
            preparedStatement = connect
                    .prepareStatement("delete from FEEDBACK.COMMENTS where myuser= ? ; ");
            preparedStatement.setString(1, "Test");
            preparedStatement.executeUpdate();

            resultSet = statement
                    .executeQuery("select * from FEEDBACK.COMMENTS");
            writeMetaData(resultSet);
 * */
}