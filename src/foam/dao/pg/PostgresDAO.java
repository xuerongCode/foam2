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

public class PostgresDAO
  extends AbstractJDBCDAO
{
  public PostgresDAO(X x, ClassInfo of) {
    super(x, of, (ConnectionPool) x.get("connectionPool"));
  }

  public FObject put_(X x, FObject obj) {
    Connection c = null;
    IndexedPreparedStatement stmt = null;
    ResultSet resultSet = null;

    try {
      c = connectionPool_.getConnection();
      StringBuilder builder = sb.get()
          .append("insert into ")
          .append(table_);

      buildFormattedColumnNames(obj, builder);
      builder.append(" values");
      buildFormattedColumnPlaceholders(obj, builder);
      builder.append(" on conflict (")
             .append(getPrimaryKey().createStatement())
             .append(") do update set");
      buildFormattedColumnNames(obj, builder);
      builder.append(" = ");
      buildFormattedColumnPlaceholders(obj, builder);

      stmt = new IndexedPreparedStatement(c.prepareStatement(builder.toString(),
          Statement.RETURN_GENERATED_KEYS));
      // set statement values twice: once for the insert and once for the update on conflict
      setStatementValues(stmt, obj);
      setStatementValues(stmt, obj);

      int inserted = stmt.executeUpdate();
      if ( inserted == 0 ) {
        throw new SQLException("Error performing put_ command");
      }

      // get auto-generated postgres keys
/*       resultSet = stmt.getGeneratedKeys();
      if ( resultSet.next() ) {
        obj.setProperty(getPrimaryKey().getName(), resultSet.getObject(1));
      } */

      return obj;
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    } finally {
      closeAllQuietly(resultSet, stmt, c);
    }
  }
}
