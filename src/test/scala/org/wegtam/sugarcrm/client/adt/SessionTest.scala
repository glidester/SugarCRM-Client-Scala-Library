package org.wegtam.sugarcrm.client.adt

import org.scalatest.{Matchers, FunSpec}
import argonaut._, Argonaut._

/**
 * Tests for the sugarcrm api session.
 */
class SessionTest extends FunSpec with Matchers {
  describe("asJSON") {
    describe("when executed on an empty session") {
      it("should be converted to json properly") {
        val s = new Session("", "", new NameValueList())
        s.asJson.toString() should be ("{\"id\":\"\",\"module_name\":\"\",\"name_value_list\":{}}")
      }
    }
    describe("when executed on a non-empty session") {
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
        val s = new Session("4di2a22jph0iseetumill7r7s1", "Users", list)
        NameValueList.cleanJson(s.asJson) should be ("{\"id\":\"4di2a22jph0iseetumill7r7s1\",\"module_name\":\"Users\",\"name_value_list\":{\"user_default_team_id\":{\"name\":\"user_default_team_id\",\"value\":null},\"user_decimal_seperator\":{\"name\":\"user_decimal_seperator\",\"value\":\".\"},\"user_currency_name\":{\"name\":\"user_currency_name\",\"value\":\"US Dollars\"},\"mobile_max_subpanel_entries\":{\"name\":\"mobile_max_subpanel_entries\",\"value\":null},\"user_currency_id\":{\"name\":\"user_currency_id\",\"value\":\"-99\"},\"user_default_timeformat\":{\"name\":\"user_default_timeformat\",\"value\":\"H:i\"},\"user_id\":{\"name\":\"user_id\",\"value\":\"37a15bc4-84cd-b256-990c-53130b3d239c\"},\"mobile_max_list_entries\":{\"name\":\"mobile_max_list_entries\",\"value\":null},\"user_language\":{\"name\":\"user_language\",\"value\":\"en_us\"},\"user_default_dateformat\":{\"name\":\"user_default_dateformat\",\"value\":\"d.m.Y\"},\"user_number_seperator\":{\"name\":\"user_number_seperator\",\"value\":\",\"},\"user_is_admin\":{\"name\":\"user_is_admin\",\"value\":false},\"user_name\":{\"name\":\"user_name\",\"value\":\"fancy\"}}}")
      }
    }
  }
}
