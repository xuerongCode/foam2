/**
 * @license
 * Copyright 2018 The FOAM Authors. All Rights Reserved.
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package foam.dao.pg;
import foam.core.ClassInfo;
import foam.core.FObject;
import foam.core.PropertyInfo;
import foam.core.X;
import java.sql.*;

public class SQLiteDAO
  extends AbstractJDBCDAO
{
  public SQLiteDAO(X x, ClassInfo of, ConnectionPool connectionPool) {
    super(x, of, connectionPool);
  }

  public FObject put_(X x, FObject obj) {
    Connection connection = null;
    IndexedPreparedStatement stmt = null;

    try {
      connection = connectionPool_.getConnection();
      StringBuilder builder = sb.get();
      builder.append("INSERT OR REPLACE INTO ");
      builder.append(table_);
      builder.append(" ");
      buildFormattedColumnNames(obj, builder);
      builder.append(" VALUES ");
      buildFormattedColumnPlaceholders(obj, builder);
      stmt = new IndexedPreparedStatement(connection.prepareStatement(builder.toString(),
      Statement.RETURN_GENERATED_KEYS));
      setStatementValues(stmt, obj);
      int inserted = stmt.executeUpdate();
      if ( inserted == 0 ) {
        throw new SQLException("Error performing put_ command");
      }
      return obj;
    } catch ( Throwable e ) {
      e.printStackTrace();
      return null;
    } finally {
      closeAllQuietly(null, stmt, connection);
    }
  }
}