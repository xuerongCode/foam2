/**
 * @license
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

foam.CLASS({
  package: 'foam.u2.md',
  name: 'DetailView',
  extends: 'foam.u2.DetailView',

  requires: [
    'foam.core.Property'
  ],

  exports: [
    'data'
  ],

  properties: [
    {
      name: 'data',
      postSet: function(old, nu) {
        if ( nu && nu.cls_ !== this.of ) this.of = nu.cls_;
      }
    },
    {
      name: 'of'
    },
    {
      name: 'properties',
      expression: function(of) {
        return of.getAxiomsByClass(foam.core.Property).filter(function(p) {
          return ! p.hidden;
        });
      }
    },
    {
      name: 'title',
      expression: function(of) {
        return of.model_.label;
      }
    },
    [ 'showTitle', true ],
    [ 'nodeName', 'div' ]
  ],

  methods: [
    function initE() {
      this.add(this.properties$);
    }
  ]
});
