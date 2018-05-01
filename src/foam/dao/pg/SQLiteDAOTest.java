package foam.dao.pg;

import foam.core.X;
import foam.core.FObject;
import foam.core.ClassInfo;;
import net.nanopay.model.Institution;

public class SQLiteDAOTest { 
  public static SQLiteDAO dao;
  public static void connectToDataBase(X x, ClassInfo of, String url) {
    ConnectionPool connectionPool = new ConnectionPool();
    connectionPool.setDriver("org.sqlite.JDBC");
    connectionPool.setConnectionString(url);
    connectionPool.start();
    dao = new SQLiteDAO(x, of, connectionPool);

  }

  public static void addToDAO(FObject obj) {
    dao.put(obj);
  }
}