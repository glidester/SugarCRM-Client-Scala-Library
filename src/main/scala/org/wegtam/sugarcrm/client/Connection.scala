package org.wegtam.sugarcrm.client

import argonaut._, Argonaut._
import scalaz._
import org.wegtam.utils.security.Hash
import java.net.URL

import org.apache.http.client.fluent.{Form, Request}
import org.wegtam.sugarcrm.client.adt.{Session, NameValueList, Error}

/**
 * This class connects to the sugarcrm rest interface and provides some simple operations.
 *
 * @param baseUrl The base url to the sugarcrm instance (e.g. http://crm.example.com/sugar).
 * @param username The username for the rest api.
 * @param userpass The password for the rest api user.
 */
class Connection(val baseUrl: URL, val username: String, val userpass: String) {
  // A list of default modules for the search.
  val DEFAULT_MODULES = List(
    "Accounts", "Bugs", "Calls", "Campaigns", "Cases", "Contacts", "Documents",
    "Emails", "Leads", "Meetings", "Notes", "Opportunities", "Project",
    "ProspectLists", "Prospects", "Tasks"
  )
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
    Session(id.as[String].getOr(""), response.fieldOrEmptyString("module_name").as[String].getOr(""), new NameValueList())
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
          .add("rest_data", arguments.nospaces)
          .build()
      ).execute().returnContent().asString()
    val result: String \/ Json = Parse.parse(response)
    if (result.isLeft) {
      val msg = ~result | ""
      throw new RuntimeException(s"Couldn't decode json: $msg")
    }
    val json = result | jEmptyObject
    val error: String \/ Error = Parse.decodeEither[Error](json.toString())
    if (error.isRight) {
      val details = error | None
      throw new RuntimeException(s"An error occured using session ${session.id}: $details")
    }
    json
  }

  /**
   * Checks if the session id is still valid.
   *
   * @return Returns `true` if the session is valid and `false` otherwise.
   */
  def checkSession(): Boolean = {
    val response = query("seamless_login", Json("session" := session.id))
    val value = response.jdecode[Int].getOr(0)
    if (value == 1)
      true
    else
      false
  }

  /**
   * Load a single bean from the api.
   *
   * @param moduleName The type of the bean (e.g. "Accounts").
   * @param id The id of the bean in the sugarcrm database.
   * @return
   */
  def getEntry(moduleName: String, id: String): Json = {
    val jsonLoadBean =
      Json("session" := session.id, "modulename" := moduleName, "id" := id)
    val response = query("get_entry", jsonLoadBean)
    val list = response.field("entry_list").get.array
    list.get(0)
  }

  /**
   * Search entries within the specified module.
   *
   * @param moduleName The name of the module (e.g. "Leads").
   * @param searchQuery An SQL search query (e.g. the options for `WHERE`).
   * @return
   */
  def getEntries(moduleName: String, searchQuery: String): Json = {
    val jsonSearch =
      Json("session" := session.id, "modulename" := moduleName, "query" := searchQuery, "deleted" := 0)
    val response = query("get_entry_list", jsonSearch)
    response
  }

  /**
   * Search entries across multiple modules.
   *
   * @param searchQuery A string to search for.
   * @param modules A list of modules which shall be searched. If left empty the default modules are searched.
   * @return
   */
  def search(searchQuery: String, maxResults: Int = 100, modules: List[String] = List()): Json = {
    val searchModules: List[String] = if (modules.isEmpty) DEFAULT_MODULES else modules
    val jsonSearch =
      Json(
        "session" := session.id,
        "search_string" := searchQuery,
        "modules" := searchModules,
        "offset" := 0,
        "max_results" := maxResults,
        "assigned_user_id" := jEmptyString,
        "select_fields" := jEmptyArray,
        "unified_search_only" := false,
        "favorites" := false
      )
    val response = query("search_by_module", jsonSearch)
    response
  }
}
