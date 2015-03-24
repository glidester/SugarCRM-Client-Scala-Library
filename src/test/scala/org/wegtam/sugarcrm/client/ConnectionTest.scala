package org.wegtam.sugarcrm.client

import argonaut.{Cursor, DecodeJson, Json}
import org.scalatest.{Matchers, FunSpec}
import scala.language.postfixOps
import org.wegtam.sugarcrm.client.adt.NameValueList
import org.wegtam.sugarcrm.model._
import scala.util.{Success, Failure, Try}
import org.wegtam.sugarcrm._
import scala.util.Success
import scala.util.Failure
import scala.Some

//import org.wegtam.sugarcrm.client.Connection.StringValueItem


class ConnectionTest extends FunSpec with Matchers {

  describe("asJson") {
    /*it("should lookup a contact by UUID") {

      println(sys.props("url").toString + " " +  sys.props("password").toString)

      //val entry: Json  = sugar.getEntry("Contacts","3ebfca42-9229-379d-6bdc-54d9d15e344b")

      val values = new NameValueList()
      values.updateDynamic("first_name")("Created By New Webhook")
      values.updateDynamic("description")("Again")
      values.updateDynamic("id")("59fdcf2f-a316-d6fb-77d7-54f700405a4c")

      val newContact = getTestCRMConnection.setEntry("Contacts", values)

    }*/

    it("should lookup a contact by Email") {

      val values = new NameValueList()

      /*values.updateDynamic("module_id")("c142e7b6-50e6-50e4-3373-5501ae747ff2")
      values.updateDynamic("link_field_name")("contacts")
      values.updateDynamic("related_ids")("3ebfca42-9229-379d-6bdc-54d9d15e344b")*/

      println(getTestCRMConnection.setRelationship("accoz_sales", "4307096b-1a84-a1cd-1e0c-5506a720d5da","accoz_sales_contactscontacts_ida","1b1f9362-fd66-6463-266f-54f9778c075c", false))

      /*println(getTestCRMConnection.setRelationship("accoz_sales", values))*/
    }
  }
}
