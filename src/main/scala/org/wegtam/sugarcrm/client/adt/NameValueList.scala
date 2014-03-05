package org.wegtam.sugarcrm.client.adt

import scala.language.dynamics
import scala.collection.mutable

import argonaut._, Argonaut._
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair

/**
 * A mapper class for the `name_value_list` stuff returned by the sugarcrm api.
 */
class NameValueList extends Dynamic {
  val attributes = mutable.HashMap.empty[String, Any]

  /**
   * Convert the attribute map into a sugarcrm compatible one for the name value list.
   *
   * @return A sugarcrm rest api compatible map for mapping of "name_value_list".
   */
  def getSugarCompatibleMap: Map[String, NameValuePair] = for {
    (key, value) <- attributes.toMap[String, Any]
  } yield (key, new BasicNameValuePair(key, String.valueOf(value)))

  /**
   * Implementation for selection of dynamic attributes.
   *
   * @param name The name of the attribute.
   * @return The attribute's value or `None`.
   */
  def selectDynamic(name: String) = {
    if (attributes.contains(name))
      attributes.get(name)
    else
      None
  }

  /**
   * Implementation for setting and updating dynamic attributes.
   *
   * @param name The name of the attribute.
   * @param value The value of the attribute.
   * @return The set value.
   */
  def updateDynamic(name: String)(value: Any) = {
    attributes.put(name, value)
    attributes.get(name)
  }
}

/**
 * Codecs for the json conversion via argonaut.
 */
object NameValueList {
  // Encoding of name value pairs.
  // FIXME We shouldn't encode null and boolean values as strings (see `cleanJson`)!
  implicit def NameValuePairEncodeJson: EncodeJson[NameValuePair] =
    EncodeJson((nvp: NameValuePair) => ("value" := nvp.getValue) ->: ("name" := nvp.getName) ->: jEmptyObject)

  implicit def NameValueListEncodeJson: EncodeJson[NameValueList] =
    EncodeJson((nvl: NameValueList) => nvl.getSugarCompatibleMap.asJson)

  /**
   * This method is a workaround for our incomplete json codec.
   * It repairs the representation of null and boolean values as strings.
   *
   * @param json The json to be cleaned.
   * @return The cleaned json as string.
   */
  def cleanJson(json: Json): String = {
    val jsonString = json.toString()
      .replaceAll(":\"null\"", ":null")
      .replaceAll(":\"true\"", ":true")
      .replaceAll(":\"false\"", ":false")
    jsonString
  }
}