package org.wegtam.sugarcrm.client

import argonaut._, Argonaut._
import org.wegtam.utils.security.Hash
import java.net.URL

import org.apache.http.client.fluent.{Form, Request}
import org.wegtam.sugarcrm.client.adt.{Session, NameValueList}

/**
 * This class connects to the sugarcrm rest interface and provides some simple operations.
 *
 * @param baseUrl The base url to the sugarcrm instance (e.g. http://crm.example.com/sugar).
 * @param username The username for the rest api.
 * @param userpass The password for the rest api user.
 */
class Connection(val baseUrl: URL, val username: String, val userpass: String) {
  // The base path to the rest api entry point.
  val REST_API_PATH = "/service/v4_1/rest.php"
  // Create the complete rest api url.
  val restApiUrl =
    if (baseUrl.getPort != -1)
      baseUrl.getProtocol + "://" + baseUrl.getHost + ":" + baseUrl.getPort + baseUrl.getPath + REST_API_PATH
    else
      baseUrl.getProtocol + "://" + baseUrl.getHost + baseUrl.getPath + REST_API_PATH
  // We need to store the session information.
  val session: Session = login()

  /**
   * Login via the rest api and return the session.
   *
   * @return A session returned from the api.
   */
  def login(): Session = {
    val jsonUserAuth: Json =
      Json("user_name" := username, "password" := Hash.MD5(userpass), "version" := "1.0")
    val jsonLogin: Json =
      Json("user_auth" := jsonUserAuth, "application_name" := "SugarCRM-Client Scala Library")
    val response = query("login", jsonLogin)
    val id = response.fieldOrNull("id")
    if (id == null)
      throw new RuntimeException("Got no session id from api!")
    Session(id.toString(), response.fieldOrEmptyString("module_name").toString(), new NameValueList())
  }

  /**
   * Query the sugarcrm rest api using the given method and arguments.
   *
   * @param method The name of the api method (e.g. "login").
   * @param arguments The arguments for the api call.
   * @return The json returned by the api.
   */
  def query(method: String, arguments: Json): Json = {
    val response = Request.Post(restApiUrl)
      .bodyForm(
        Form.form()
          .add("method", method)
          .add("input_type", "JSON")
          .add("response_type", "JSON")
          .add("rest_data", arguments.toString())
          .build()
      ).execute().returnContent().asString()
    val result: Option[Json] = Parse.parseOption(response)
    result.get
  }
}
