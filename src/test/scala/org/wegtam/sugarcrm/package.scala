package org.wegtam

import org.wegtam.sugarcrm.client.Connection

package object sugarcrm {
  implicit val crmConnection = new Connection(new java.net.URL(sys.props("url").toString), sys.props("username").toString, sys.props("password").toString)

  def getTestCRMConnection = crmConnection
}
