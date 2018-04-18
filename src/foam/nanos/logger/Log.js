/**
 * @license
 * Copyright 2018 The FOAM Authors. All Rights Reserved.
 * http://www.apache.org/licenses/LICENSE-2.0
 */
foam.CLASS({
  package: 'foam.nanos.logger',
  name: 'Log',

  tableColumns: ['id', 'time', 'from', 'user', 'type', 'detail'],

  properties: [
    {
      class: 'Int',
      name: 'id',
      documentation: 'id for the log'
    },
    {
      class: 'DateTime',
      name: 'time',
      documentation: 'time of log',
      factory: function() {
        return new Date();
      },
      javaFactory: 'return new java.util.Date();'
    },
    {
      class: 'String',
      documentation: 'the place which log happen, FrontEnd or BackEnd',
      name: 'from'
    },
    {
      class: 'String',
      name: 'user',
      documentation: 'user who fire log',
      label: 'User ID'
    },
    {
      class: 'String',
      documentation: 'type of log',
      name: 'type'
    },
    {
      class: 'String',
      name: 'detail',
      documentation: 'detail of log',
      view: { class: 'foam.u2.tag.TextArea', rows: 10 }
    }
  ]
})