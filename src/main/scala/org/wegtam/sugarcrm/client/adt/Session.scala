package org.wegtam.sugarcrm.client.adt

import argonaut._, Argonaut._

/**
 * A simple case class for the sugarcrm api session.
 */
case class Session(id: String, moduleName: String, values: NameValueList)

/**
 * Codecs for the json conversion via argonaut.
 */
object Session {
  implicit def SessionEncodeJson: EncodeJson[Session] =
    EncodeJson((s: Session) => ("name_value_list" := s.values) ->: ("module_name" := s.moduleName) ->: ("id" := s.id) ->: jEmptyObject)
}
