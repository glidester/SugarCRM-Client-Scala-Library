package org.wegtam.sugarcrm.client.adt

import org.scalatest.{Matchers, FunSpec}

import argonaut._, Argonaut._
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair

/**
 * Tests for the name value list.
 */
class NameValueListTest extends FunSpec with Matchers {
  describe("getSugarCompatibleMap") {
    describe("when executed on an empty name value list") {
      it("should create the correct map") {
        val list = new NameValueList
        val expected = Map[String, NameValuePair]()
        list.getSugarCompatibleMap should be(expected)
      }
    }
    describe("when executed on a non-empty name value list") {
      it("should create the correct map") {
        val list = new NameValueList
        list.foo = "bar"
        list.bar = "another attribute"
        list.is_fancy = false
        val expected = Map[String, NameValuePair](
          "foo" -> new BasicNameValuePair("foo", "bar"),
          "bar" -> new BasicNameValuePair("bar", "another attribute"),
          "is_fancy" -> new BasicNameValuePair("is_fancy", "false")
        )
        list.getSugarCompatibleMap should be(expected)
      }
    }
  }

  describe("asJSON") {
    describe("when executed on an empty name value list") {
      it("should be converted to json properly") {
        val list = new NameValueList()
        list.jencode.toString() should be("{\"name_value_list\":{}}")
      }
    }
    describe("when executed on a non-empty name value list") {
      it("should be converted to json properly") {
        val list = new NameValueList()
        list.user_default_team_id = null
        list.user_decimal_seperator = "."
        list.user_currency_name = "US Dollars"
        list.mobile_max_subpanel_entries = null
        list.user_currency_id = -99
        list.user_default_timeformat = "H:i"
        list.user_id = "37a15bc4-84cd-b256-990c-53130b3d239c"
        list.mobile_max_list_entries = null
        list.user_language = "en_us"
        list.user_default_dateformat = "d.m.Y"
        list.user_number_seperator = ","
        list.user_is_admin = false
        list.user_name = "fancy"
        val expectedJsonString = "{\"name_value_list\":{\"user_default_team_id\":{\"name\":\"user_default_team_id\",\"value\":null},\"user_decimal_seperator\":{\"name\":\"user_decimal_seperator\",\"value\":\".\"},\"user_currency_name\":{\"name\":\"user_currency_name\",\"value\":\"US Dollars\"},\"mobile_max_subpanel_entries\":{\"name\":\"mobile_max_subpanel_entries\",\"value\":null},\"user_currency_id\":{\"name\":\"user_currency_id\",\"value\":\"-99\"},\"user_default_timeformat\":{\"name\":\"user_default_timeformat\",\"value\":\"H:i\"},\"user_id\":{\"name\":\"user_id\",\"value\":\"37a15bc4-84cd-b256-990c-53130b3d239c\"},\"mobile_max_list_entries\":{\"name\":\"mobile_max_list_entries\",\"value\":null},\"user_language\":{\"name\":\"user_language\",\"value\":\"en_us\"},\"user_default_dateformat\":{\"name\":\"user_default_dateformat\",\"value\":\"d.m.Y\"},\"user_number_seperator\":{\"name\":\"user_number_seperator\",\"value\":\",\"},\"user_is_admin\":{\"name\":\"user_is_admin\",\"value\":false},\"user_name\":{\"name\":\"user_name\",\"value\":\"fancy\"}}}"
        NameValueList.cleanJson(list.jencode) should be(expectedJsonString)
      }
    }
  }
}
