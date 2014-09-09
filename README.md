# SugarCRM Client Scala Library

[![Build Status](https://travis-ci.org/wegtam/SugarCRM-Client-Scala-Library.svg?branch=master)](https://travis-ci.org/wegtam/SugarCRM-Client-Scala-Library)
[![BSD License](http://img.shields.io/badge/license-bsd-green.svg)](http://opensource.org/licenses/BSD-3-Clause)

This library intends to provide a simple interface for the rest api of sugarcrm.

The development of the library was sponsored by the [Wegtam UG](http://www.wegtam.org).

The source of this library is released under the BSD license (see [LICENSE](LICENSE) for details).

## Requirements

* [Scala](http://scala-lang.org/) 2.10 or 2.11
* [Argonaut](http://argonaut.io/)
* [Apache Http Components](http://hc.apache.org)

See the [build.sbt](build.sbt) file for details.

## Usage

    val sugar = new Connection(new URL("http://crm.example.com/sugarcrm"), "user", "password")
    val entries = sugar.getEntries("Accounts", """accounts.name LIKE "%mining%"""")


