/**
 * @license
 * Copyright 2018 The FOAM Authors. All Rights Reserved.
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package foam.nanos.logger;

import foam.dao.*;
import foam.core.*;
import foam.mlang.order.Comparator;
import foam.mlang.predicate.Predicate;
import java.util.concurrent.atomic.AtomicInteger;

public class LoggerDAO
  extends AbstractDAO
{
  protected int maxSize_;
  protected AtomicInteger index_;
  protected FObject[] loggers_;

  public LoggerDAO(X x, ClassInfo of ) {
    this(x, of, Integer.MAX_VALUE/2);
  }
  public LoggerDAO(X x, ClassInfo of, int max) {
    setX(x);
    setOf(of);
    index_ = new AtomicInteger(0);
    loggers_ = new FObject[max];
  }

  public FObject put_(X x, FObject obj) {
    if ( obj == null ) return obj;
    //find index to put value
    int index = index_.getAndAdd(1) % maxSize_;
    loggers_[index] = obj;
    return obj;
  }
  public Sink select_(X x, Sink sink, long skip, long limit, Comparator order, Predicate predicate) {
    sink = prepareSink(sink);
    Sink         decorated = decorateSink_(sink, skip, limit, order, predicate);
    Subscription sub       = new Subscription();
    int length = index_.get();
    length =  length > maxSize_ ? maxSize_ - 1 : length;
    for ( int i = 0 ; i <= length ; i++ ) {
      if ( sub.getDetached() ) break;
      decorated.put(loggers_[i], sub);
    }
    decorated.eof();
    return sink;
  }
  public FObject find_(X x, Object o) {
    throw new RuntimeException("LoggerDAO do not support find_");
  }
  public FObject remove_(X x, FObject obj) {
    throw new RuntimeException("LoggerDAO do not support remove_");
  }
}