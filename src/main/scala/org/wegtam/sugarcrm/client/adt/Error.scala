package org.wegtam.sugarcrm.client.adt

import argonaut._, Argonaut._

/**
 * A simple case class for api errors.
 */
case class Error(name: String, number: Int, description: String)

/**
 * Codecs for the json conversion via argonaut.
 */
object Error {
  implicit def ErrorDecodeJson: DecodeJson[Error] =
    jdecode3L(Error.apply)("name", "number", "description")

  implicit def ErrorEncodeJson: EncodeJson[Error] =
    jencode3L((e: Error) => (e.name, e.number, e.description))("name", "number", "description")
}
