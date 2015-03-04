package org.wegtam.sugarcrm.model

import argonaut.{Json, Cursor}
import org.wegtam.sugarcrm.client.adt.NameValueList

trait Contact {
  def id: Option[String]
  def deleted: Boolean
  def createdBy: String
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

case class CRM_Contact(id: Option[String], deleted: Boolean, createdBy: String, modifiedUserId: String, dateModified: String,
                       salutation: String, firstName: String, lastName: String,
                       email1: String, email2: String,
                       phoneHome: String, phoneMobile: String,
                       description: String, doNotCall: Boolean
                      ) extends Contact

object Contact {
  def parseJson(json: Json) = {
    Contact.parseContact(Some(json.cursor))
  }

  def parseContact(optCurso: Option[Cursor]): Option[Contact] = {
    if (optCurso.isEmpty)
      None
    else {
      val id = (optCurso.get --\ "id").get.focus.as[String].toOption

      val valueListName = for {
        cursor <- optCurso
        fields <- cursor.fields
      } yield {
        if (fields.contains("entry_list")) "entry_list" else "name_value_list"
      }

      val nameValueList = (optCurso.get --\ valueListName.getOrElse("name_value_list")).get

      /* For debugging
      getValue("deleted",nameValueList).get
      getValue("created_by",nameValueList).get
      getValue("modified_user_id",nameValueList).get

      getValue("salutation",nameValueList).get
      getValue("first_name",nameValueList).get
      getValue("last_name",nameValueList).get

      getValue("email1",nameValueList).get
      getValue("email2",nameValueList).get

      getValue("phone_home",nameValueList).get
      getValue("phone_mobile",nameValueList).get

      getValue("description",nameValueList).get
      getValue("do_not_call",nameValueList).get*/

      for {
        nameValueList <- (optCurso.get --\ valueListName.getOrElse("name_value_list"))

        deleted <- getValue("deleted",nameValueList)

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
        CRM_Contact(id,deleted.equals("1"),createdBy,modifiedUserId,dateModified,salutation,firstName,lastName,email1,email2,phoneHome,phoneMobile,
          description,doNotCall.equals("1"))
    }
  }

  def buildNameValueList(contact: Contact):NameValueList = {
    val values = new NameValueList()

    if (contact.id.isDefined) values.updateDynamic("id")(contact.id.get)
    values.updateDynamic("deleted")(if (contact.deleted) "1" else "0")
    values.updateDynamic("created_by")(contact.createdBy)
    values.updateDynamic("modified_user_id")(contact.modifiedUserId)
    values.updateDynamic("date_modified")(contact.dateModified)

    values.updateDynamic("salutation")(contact.salutation)
    values.updateDynamic("first_name")(contact.firstName)
    values.updateDynamic("last_name")(contact.lastName)

    values.updateDynamic("email1")(contact.email1)
    values.updateDynamic("email2")(contact.email2)

    values.updateDynamic("phone_home")(contact.phoneHome)
    values.updateDynamic("phone_mobile")(contact.phoneMobile)

    values.updateDynamic("description")(contact.description)
    values.updateDynamic("do_not_call")(if (contact.doNotCall) "1" else "0")

    values
  }
}