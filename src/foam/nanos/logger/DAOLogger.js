/**
 * @license
 * Copyright 2018 The FOAM Authors. All Rights Reserved.
 * http://www.apache.org/licenses/LICENSE-2.0
 */
foam.CLASS({
  package: 'foam.nanos.logger',
  name: 'DAOLogger',
  imports: [
    'user'
  ],
  javaImports: [
    'java.util.Date',
    'foam.core.*',
    'foam.dao.DAO'
  ],
  requires: [
    'foam.nanos.logger.Log'
  ],
  javaExtends: [ 'foam.nanos.logger.AbstractLogger' ],
  properties: [
    {
      class: 'String',
      name: 'logDAOKey',
      hidden: true,
      value: 'logDAO'
    },
    {
      class: 'foam.dao.DAOProperty',
      name: 'logDAO',
      hidden: true,
      factory: function() {
        return this.__context__[this.logDAOKey];
      },
      javaFactory: 'return (foam.dao.DAO)getX().get(getLogDAOKey());'
    },
  ],

  methods: [
    function normalizeDetail(e) {
      if ( foam.core.Exception.isInstance(e) ) {
        return '' + e + '\n' + '[name]: ' + e.name + '\n' + '[message]: ' + e.message + '\n';
      } else if ( e instanceof Error ) {
        return '' + e + '\n' + '[name]: ' + e.name + '\n' + '[message]: ' + e.message + '\n';
      } else {
        return '' + e + '\n';
      }
    },
    function createModel(type, detail) {
      return this.Log.create({
        time: new Date(),
        from: 'Web',
        user: (! this.user) ? '' : '' + this.user.firstName + ' ' + this.user.lastName,
        type: type,
        detail: detail
      })
    },
    {
      name: 'createModel',
      returns: 'foam.core.FObject',
      javaReturns: 'foam.core.FObject',
      args: [
        {
          name: 'type',
          javaType: 'String'
        },
        {
          name: 'detail',
          javaType: 'Object...'
        }
      ],
      code: function createModel(type, detail) {
        return this.Log.create({
          time: new Date(),
          from: 'Web',
          user: (! this.user) ? '' : '' + this.user.firstName + ' ' + this.user.lastName,
          type: type,
          detail: detail
        })
      },
      javaCode: `
      Date d = new Date();
      Log log = new Log();
      log.setTime(new Date());
      log.setFrom("BackEnd");
      log.setUser(System.getProperty("user.name"));
      log.setType(type);
      log.setDetail(combine(args));
      return log;
      `
    },
    function addToDAO(type, detail) {
      var m = this.createModel(type, detail);
      this.logDAO.put(m);
    },
    function outputLogger(type, array) {
      var ret = ''
      for (var i = 0 ; i < array.length ; i++ ) {
        ret = ret + this.normalizeDetail(array[i]);
      }
      this.addToDAO(type, ret);
    },
    {
      name: 'log',
      javaReturns: 'void',
      args: [
        {
          name: 'args',
          javaType: 'Object...'
        }
      ],
      code: function log() {
        this.outputLogger('log', Array.from(arguments));
      }
    }
  ]
})