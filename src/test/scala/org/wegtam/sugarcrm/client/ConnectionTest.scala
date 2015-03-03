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
    it("should lookup a contact by UUID") {

      println(sys.props("url").toString + " " +  sys.props("password").toString)

      //val entry: Json  = sugar.getEntry("Contacts","3ebfca42-9229-379d-6bdc-54d9d15e344b")

      /*//Lookup by UUID
      val uuid = "8944243b-3aab-4e65-b59b-9f5d2b7091ce"
      val contactsWithUUID: Json = getTestCRMConnection.getEntries("Contacts", s"contacts.id IN (SELECT id_c FROM contacts_cstm WHERE uuid_c = '$uuid')",List())
      println(s"contactsWithUUID = $contactsWithUUID")*/

      /*
      //Lookup by email
      val emailAddress = "glidester@gmail.com"
      val contactsWithEmail: Json = getTestCRMConnection.getEntries("Contacts", s"contacts.id IN (SELECT eab.bean_id FROM email_addresses ea INNER JOIN email_addr_bean_rel eab ON eab.email_address_id=ea.id WHERE ea.email_address = '$emailAddress' AND eab.bean_module = 'Contacts' AND ea.deleted = 0 AND eab.deleted = 0)",List("id"))
      println(s"contactsWithEmail = $contactsWithEmail")
      */

      /*//create new contact
      val values = new NameValueList()
      values.updateDynamic("first_name")("Created By New Webhook")
      values.updateDynamic("last_name")("Again")
      values.updateDynamic("email1")("test@test.com")

      println(s"nameValList = ${values.getSugarCompatibleMap}")

      val newContact = getTestCRMConnection.setEntry("Contacts", values)
      println(s"newContact = $newContact")*/


      /*val newContact = getTestCRMConnection.setEntry2("Contacts", Seq(StringValueItem("first_name","Created By New Webhook"),StringValueItem("last_name","Created By New Webhook"),StringValueItem("email1","test@test.com")))
      println(s"newContact = $newContact")*/


      /*Logic.lookupContactByUUID(None) should be (List())
      Logic.lookupContactByUUID(Some("8944243b-3aab-4e65-b59b-9f5d2b7091ce")) should not be (List())*/

    }

    /*it("should lookup a contact by Email") {
      Logic.lookupContactByEmail(None) should be (List())
      Logic.lookupContactByEmail(Some("nospam@gmail.com")) should not be (List())

    }*/
  }
}




object Logic {

  def lookupContacts(emailAddress: String, uuid: Option[String] = None, firstName: Option[String] = None, lastName: Option[String] = None)(implicit crmConnection: Connection): Try[List[Contact]] = {
    val contactsByUUID = lookupContactByUUID(uuid)

    val matchingContacts = if (contactsByUUID.size > 0) contactsByUUID else lookupContactByEmail(Some(emailAddress))

    if (matchingContacts.size < 1) {
      val result = createNewContact("test@test.com",Some("Tester"),Some("Testing"))
      result match {
        case Success(v) => Success(List(v))
        case Failure(e) => Failure(e)
      }
    }
    else Success(matchingContacts)
  }

  def createNewContact(emailAddress: String, firstName: Option[String] = None, lastName: Option[String] = None, uuid: Option[String] = None)(implicit crmConnection: Connection): Try[Contact] = {
    val values = new NameValueList()

    values.updateDynamic("email1")(emailAddress)
    if (firstName.isDefined) values.updateDynamic("first_name")(firstName.get)
    if (lastName.isDefined) values.updateDynamic("last_name")(lastName.get)
    if (uuid.isDefined) values.updateDynamic("uuid_c")(uuid.get)

    val newJsonContact = crmConnection.setEntry("Contacts", values)

    val newContact = Contact.parseContact(Some(newJsonContact.cursor))

    if (newContact.isEmpty)
      Failure(new RuntimeException(s"Failed to create new CRM contact. Response was: $newJsonContact"))
    else
      Success(newContact.get)
  }

  def lookupContactByEmail(optEmailAddress: Option[String])(implicit crmConnection: Connection):List[Contact] = {
    if (optEmailAddress.isEmpty)
      List()
    else {
      val emailAddress = optEmailAddress.get
      val contactsJson: Json = crmConnection.getEntries("Contacts",
        s"contacts.id IN (SELECT eab.bean_id FROM email_addresses ea INNER JOIN email_addr_bean_rel eab ON eab.email_address_id=ea.id WHERE ea.email_address = '$emailAddress' AND eab.bean_module = 'Contacts' AND ea.deleted = 0 AND eab.deleted = 0)",
        List())

      val c = contactsJson.cursor

      val topField = (c --\ "entry_list")

      val contacts = if (topField.isEmpty)
        List()
      else
        parseArray(topField.get.downArray, Contact.parseContact)

      println(s">> $contacts")
      contacts
    }
  }

  def lookupContactByUUID(optUuid: Option[String])(implicit crmConnection: Connection):List[Contact] = {
    if (optUuid.isEmpty)
      List()
    else {
      val uuid = optUuid.get
      val contactsJson: Json = crmConnection.getEntries("Contacts",
        s"contacts.id IN (SELECT id_c FROM contacts_cstm WHERE uuid_c = '$uuid')",
        List())

      //println(contactsJson)

      val c = contactsJson.cursor



      val topField = (c --\ "entry_list")

      val contacts = if (topField.isEmpty)
        List()
      else
        parseArray(topField.get.downArray, Contact.parseContact)

      /*val contacts = for ( entryList <- (c --\ "entry_list") )
      yield parseArray(entryList.downArray, parseContact)*/


      println(s">> $contacts")
      contacts

      //println(parseArray((c --\ "entry_list"), parseContact))

     /*for {
        entryList <- (c --\ "entry_list")
      } yield {

       val pos1 = entryList.downArray
       val pos2 = pos1.get.right
       val pos3 = pos2.get.right

        println(s"entryList1 = ${pos1.get.focus}")
        println(s"entryList2 = ${pos2.get.focus}")
        println(s"entryList3 = ${pos3.get.focus}")

        //println(s"entryList3 = ${entryList.downArray.get.right.get.focus}")
        //println(s"entryList4 = ${entryList.downArray.get.focus}")
       //println(s"entryList5 = ${entryList.downArray.get.focus}")

      }



      val result = contactsJson.jdecode[List[CRM_Contact]]
      //println(result.toOption)

      Some(CRM_Contact("","","",""))*/
    }
  }
}