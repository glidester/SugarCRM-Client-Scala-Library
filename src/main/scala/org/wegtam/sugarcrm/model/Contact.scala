package org.wegtam.sugarcrm.model

import argonaut.Cursor
import org.wegtam.sugarcrm.client.adt.NameValueList

trait Contact {
  def id: String
  def deleted: Boolean
  def createdBy: String
  def dateEntered: String
  def modifiedUserId: String
  def dateModified: String

  def salutation: String
  def firstName: String
  def lastName: String

  def email1: String
  def email2: String

  def phoneHome: String
  def phoneMobile: String

  def description: String
  def doNotCall: Boolean
}

case class CRM_Contact(id: String, deleted: Boolean, dateEntered: String, createdBy: String, modifiedUserId: String, dateModified: String,
                       salutation: String, firstName: String, lastName: String,
                       email1: String, email2: String,
                       phoneHome: String, phoneMobile: String,
                       description: String, doNotCall: Boolean
                      ) extends Contact

object Contact {
  def parseContact(optCurso: Option[Cursor]): Option[Contact] = {
    if (optCurso.isEmpty)
      None
    else {
      for {
        nameValueList <- (optCurso.get --\ "name_value_list")

        id <- getValue("id",nameValueList)
        deleted <- getValue("deleted",nameValueList)
        dateEntered <- getValue("date_entered",nameValueList)
        createdBy <- getValue("created_by",nameValueList)
        modifiedUserId <- getValue("modified_user_id",nameValueList)
        dateModified <- getValue("date_modified",nameValueList)

        salutation <- getValue("salutation",nameValueList)
        firstName <- getValue("first_name",nameValueList)
        lastName <- getValue("last_name",nameValueList)

        email1 <- getValue("email1",nameValueList)
        email2 <- getValue("email2",nameValueList)

        phoneHome <- getValue("phone_home",nameValueList)
        phoneMobile <- getValue("phone_mobile",nameValueList)

        description <- getValue("description",nameValueList)
        doNotCall <- getValue("do_not_call",nameValueList)
      } yield
        CRM_Contact(id,deleted.equals("1"),dateEntered,createdBy,modifiedUserId,dateModified,salutation,firstName,lastName,email1,email2,phoneHome,phoneMobile,
          description,doNotCall.equals("1"))
    }
  }

  def buildNameValueList(contact: Contact):NameValueList = {
    val values = new NameValueList()
    values.updateDynamic("id")(contact.id)
    values.updateDynamic("deleted")(if (contact.deleted) "1" else "0")
    values.updateDynamic("dateEntered")(contact.dateEntered)
    values.updateDynamic("createdBy")(contact.createdBy)
    values.updateDynamic("modifiedUserId")(contact.modifiedUserId)
    values.updateDynamic("dateModified")(contact.dateModified)

    values.updateDynamic("salutation")(contact.salutation)
    values.updateDynamic("firstName")(contact.firstName)
    values.updateDynamic("lastName")(contact.lastName)

    values.updateDynamic("email1")(contact.email1)
    values.updateDynamic("email2")(contact.email2)

    values.updateDynamic("phoneHome")(contact.phoneHome)
    values.updateDynamic("phoneMobile")(contact.phoneMobile)

    values.updateDynamic("description")(contact.description)
    values.updateDynamic("doNotCall")(if (contact.doNotCall) "1" else "0")

    values
  }
}