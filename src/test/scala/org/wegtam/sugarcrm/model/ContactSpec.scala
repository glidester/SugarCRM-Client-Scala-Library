package org.wegtam.sugarcrm.model

import org.scalatest.{Matchers, FunSpec}
import org.wegtam.sugarcrm.client.adt.NameValueList
import argonaut.{Parse, Json}
import org.wegtam.sugarcrm._

class ContactSpec extends FunSpec with Matchers {

  describe("Contact") {

    it("should read an existing contact") {

      val optCursor = for (json <- Parse.parseOption(jsonContact1)) yield json.cursor
      val result = Contact.parseContact(optCursor)

      result shouldBe 'isDefined
      result.get.id should equal (Some("3ebfca42-9229-379d-6bdc-54d9d15e344b"))
    }

    it("should convert the attributes to a NameValueList") {
      val contact = CRM_Contact(None, false, "2015-03-03 09:00:00", "Test User", "1", "2015-03-03 09:00:00",
        "Mr", "Tesssster", "Testing",
        "test@test.com", "Test2@Test2.com",
        "01783 456742", "0777 7348432",
        "A test", false)

      val nameValList = Contact.buildNameValueList(contact)

      /*nameValList.getSugarCompatibleMap.contains("id") should be (true)*/
      nameValList.getSugarCompatibleMap.contains("deleted") should be (true)
      nameValList.getSugarCompatibleMap.contains("date_entered") should be (true)
      nameValList.getSugarCompatibleMap.contains("created_by") should be (true)
      nameValList.getSugarCompatibleMap.contains("modified_user_id") should be (true)
      nameValList.getSugarCompatibleMap.contains("date_modified") should be (true)
      nameValList.getSugarCompatibleMap.contains("salutation") should be (true)
      nameValList.getSugarCompatibleMap.contains("first_name") should be (true)
      nameValList.getSugarCompatibleMap.contains("last_name") should be (true)
      nameValList.getSugarCompatibleMap.contains("email1") should be (true)
      nameValList.getSugarCompatibleMap.contains("email2") should be (true)
      nameValList.getSugarCompatibleMap.contains("phone_home") should be (true)
      nameValList.getSugarCompatibleMap.contains("phone_mobile") should be (true)
      nameValList.getSugarCompatibleMap.contains("description") should be (true)
      nameValList.getSugarCompatibleMap.contains("do_not_call") should be (true)

      //println(crmConnection.setEntry("Contacts", nameValList))
    }

    it("should be able to create, retrieve, and update") {
      val contact = CRM_Contact(None, false, "2015-03-03 09:00:00", "Test User", "1", "2015-03-03 09:00:00",
        "Mr", "Unit", s"Test_${java.util.UUID.randomUUID().toString}",
        "test@test.com", "Test2@Test2.com",
        "01783 456742", "0777 7348432",
        "A test", false)

      println(getTestCRMConnection.setEntry("Contacts", Contact.buildNameValueList(contact)))
    }
  }

  lazy val jsonContact1 = """{"id":"3ebfca42-9229-379d-6bdc-54d9d15e344b","module_name":"Contacts","name_value_list":{"phone_other":{"name":"phone_other","value":""},"email_addresses_non_primary":{"name":"email_addresses_non_primary","value":""},"email2":{"name":"email2","value":"wilkins.house@gmail.com"},"first_name":{"name":"first_name","value":"Tim"},"haz_tel_support_available_c":{"name":"haz_tel_support_available_c","value":""},"created_by":{"name":"created_by","value":"1"},"consumer_products_c":{"name":"consumer_products_c","value":"^HV3^,^PAZ^,^PA5^"},"name":{"name":"name","value":"Tim Wilkins"},"opportunity_role":{"name":"opportunity_role","value":""},"baz_trial_c":{"name":"baz_trial_c","value":false},"report_to_name":{"name":"report_to_name","value":""},"haz_online_services_expiry_c":{"name":"haz_online_services_expiry_c","value":"2014-09-16"},"c_accept_status_fields":{"name":"c_accept_status_fields","value":"                                                                                                                                                                                                                                                              "},"jjwg_maps_address_c":{"name":"jjwg_maps_address_c","value":""},"email":{"name":"email","value":""},"alt_address_street_3":{"name":"alt_address_street_3","value":""},"phone_fax":{"name":"phone_fax","value":""},"phone_work":{"name":"phone_work","value":""},"baz_telephone_support_c":{"name":"baz_telephone_support_c","value":"6_Hours"},"email_and_name1":{"name":"email_and_name1","value":"Mr. Tim Wilkins &lt;&gt;"},"full_name":{"name":"full_name","value":"Mr. Tim Wilkins"},"jjwg_maps_lat_c":{"name":"jjwg_maps_lat_c","value":"0.00000000"},"invalid_email":{"name":"invalid_email","value":"0"},"suite_start_c":{"name":"suite_start_c","value":"2014-11-05"},"description":{"name":"description","value":""},"haz_online_services_c":{"name":"haz_online_services_c","value":"EXPIRED"},"modified_by_name":{"name":"modified_by_name","value":"User Name Bitnami"},"alt_address_country":{"name":"alt_address_country","value":""},"modified_user_id":{"name":"modified_user_id","value":"1"},"alt_address_postalcode":{"name":"alt_address_postalcode","value":""},"email1":{"name":"email1","value":"glidester@gmail.com"},"email_opt_out":{"name":"email_opt_out","value":"0"},"reports_to_id":{"name":"reports_to_id","value":""},"m_accept_status_fields":{"name":"m_accept_status_fields","value":"                                                                                                                                                                                                                                                              "},"campaign_name":{"name":"campaign_name","value":""},"primary_address_state":{"name":"primary_address_state","value":""},"primary_address_country":{"name":"primary_address_country","value":""},"assistant":{"name":"assistant","value":""},"date_modified":{"name":"date_modified","value":"2015-02-27 16:20:17"},"baz_online_services_c":{"name":"baz_online_services_c","value":"SUITE"},"accept_status_name":{"name":"accept_status_name","value":""},"jjwg_maps_lng_c":{"name":"jjwg_maps_lng_c","value":"0.00000000"},"phone_home":{"name":"phone_home","value":""},"created_by_name":{"name":"created_by_name","value":"User Name Bitnami"},"jjwg_maps_geocode_status_c":{"name":"jjwg_maps_geocode_status_c","value":""},"alt_address_city":{"name":"alt_address_city","value":""},"opportunity_role_fields":{"name":"opportunity_role_fields","value":"                                                                                                                                                                                                                                                              "},"department":{"name":"department","value":""},"primary_address_city":{"name":"primary_address_city","value":""},"uuid_c":{"name":"uuid_c","value":"8944243b-3aab-4e65-b59b-9f5d2b7091ce"},"lead_source":{"name":"lead_source","value":""},"birthdate":{"name":"birthdate","value":false},"id":{"name":"id","value":"3ebfca42-9229-379d-6bdc-54d9d15e344b"},"phone_mobile":{"name":"phone_mobile","value":""},"primary_address_street_2":{"name":"primary_address_street_2","value":""},"last_name":{"name":"last_name","value":"Wilkins"},"baz_online_services_expiry_c":{"name":"baz_online_services_expiry_c","value":false},"alt_address_street":{"name":"alt_address_street","value":""},"alt_address_state":{"name":"alt_address_state","value":""},"alt_address_street_2":{"name":"alt_address_street_2","value":""},"salutation":{"name":"salutation","value":"Mr."},"do_not_call":{"name":"do_not_call","value":"0"},"assigned_user_name":{"name":"assigned_user_name","value":"User Name Bitnami"},"haz_telephone_support_c":{"name":"haz_telephone_support_c","value":""},"primary_address_postalcode":{"name":"primary_address_postalcode","value":""},"assigned_user_id":{"name":"assigned_user_id","value":"1"},"baz_telephone_support_expiry_c":{"name":"baz_telephone_support_expiry_c","value":"2015-05-11"},"primary_address_street_3":{"name":"primary_address_street_3","value":""},"business_products_c":{"name":"business_products_c","value":"^SUITE^,^BV3^"},"title":{"name":"title","value":"Mr"},"assistant_phone":{"name":"assistant_phone","value":""},"opportunity_role_id":{"name":"opportunity_role_id","value":""},"haz_telephone_support_expiry_c":{"name":"haz_telephone_support_expiry_c","value":false},"campaign_id":{"name":"campaign_id","value":""},"date_entered":{"name":"date_entered","value":"2015-02-10 09:37:18"},"accept_status_id":{"name":"accept_status_id","value":""},"baz_tel_support_available_c":{"name":"baz_tel_support_available_c","value":"0.00"},"sync_contact":{"name":"sync_contact","value":""},"primary_address_street":{"name":"primary_address_street","value":""},"deleted":{"name":"deleted","value":"0"},"haz_trial_c":{"name":"haz_trial_c","value":"2015-02-02"}}}"""
}
