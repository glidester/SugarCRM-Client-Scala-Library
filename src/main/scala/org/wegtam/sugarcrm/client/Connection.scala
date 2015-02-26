package org.wegtam.sugarcrm.client

import argonaut._, argonaut.Argonaut._
import scalaz._
import org.wegtam.utils.security.Hash
import java.net.URL

import org.apache.http.client.fluent.{Form, Request}
import org.wegtam.sugarcrm.client.adt.{Session, NameValueList, Error}
import scala.Some
import scala.Tuple2

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
    val response = query("login", jsonLogin.nospaces)
    val id = response.fieldOrNull("id")
    if (id == null)
      throw new RuntimeException("Got no session id from api!")
    Session(id.as[String].getOr(""), response.fieldOrEmptyString("module_name").as[String].getOr(""), new NameValueList())
  }

  /**
   * Logout and disable the current session.
   * Attention! This means that you'll have no further use for the connection object and you have to create a new one!
   *
   */
  def logout(): Unit = {
    query("logout", jsonArgsSerializer(Json()))
  }

  private def jsonArgsSerializer(arguments: Json, moduleName: Option[String] = None): String = {
    val sessionPart = s"""{"session":"${session.id}""""
    val modulePart = if (moduleName.isDefined) { s""","module_name":"${moduleName.get}","""} else ""

    sessionPart + modulePart + arguments.nospaces.substring(1)
  }

  /**
   * Query the sugarcrm rest api using the given method and arguments.
   *
   * @param method The name of the api method (e.g. "login").
   * @param arguments The arguments for the api call.
   * @return The json returned by the api.
   */
  def query(method: String, arguments: String): Json = {

    println(s"arguments= $arguments")

    val response = Request.Post(restApiUrl)
      .bodyForm(
        Form.form()
          .add("method", method)
          .add("input_type", "JSON")
          .add("response_type", "JSON")
          .add("rest_data", arguments)
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
    val response = query("seamless_login", jsonArgsSerializer(Json()))
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
      Json("id" := id)
    val response = query("get_entry", jsonArgsSerializer(jsonLoadBean, Some(moduleName)))
    val list = response.field("entry_list").get.array
    list.get(0)
  }

  /**
   * Search entries within the specified module.
   *
   * @param moduleName The name of the module (e.g. "Leads").
   * @param searchQuery An SQL search query (e.g. the options for `WHERE`).
   * @param selectFields A List of field names to return (e.g. the options for `SELECT`).
   * @param orderBy Define the sort order (e.g. the options for `ORDER BY`).
   * @param offset The record offset from which to start.
   *@return
   */
  def getEntries(moduleName: String, searchQuery: String, selectFields: List[String] = List(), orderBy: String = "", offset: Int = 0): Json = {
    val jsonSearch =
      Json("query" := searchQuery, "select_fields" := selectFields, "deleted" := 0, "order_by":= orderBy, "offset" := offset)
    val response = query("get_entry_list", jsonArgsSerializer(jsonSearch, Some(moduleName)))
    response
  }

  case class NameValueList2(items: Seq[Tuple2[Tuple2[String,String],Tuple2[String,String]]])

  implicit def NameValueListEncodeJson: EncodeJson[NameValueList2] = {
    EncodeJson((p: NameValueList2) => {
      val jsonSeq =  p.items.foldLeft[Seq[Json]](Seq[Json]())((empty,item) => empty ++ Seq((item._2._1 := item._2._2) ->: (item._1._1 := item._1._2) ->: jEmptyObject))
      jArrayElemets(jsonSeq : _*)
    })
  }

  def setEntry2(moduleName: String, nameValList: Seq[Tuple2[String,String]]): Json = {

    val formattedNameValueList = nameValList.map( i => (("name",i._1),("value",i._2)))

    println(s"formattedNameValueList = $formattedNameValueList")

    val nameValueList = NameValueList2(formattedNameValueList)

    //val pp = nn.items.foldLeft[Json](jEmptyObject)((empty,item) => (item._1._1 := item._1._2) ->: (item._2._1 := item._2._2) ->: empty)*/

    val json = Json("name_value_list" := nameValueList)

    println(s"json = $json")

    /*val response = query("set_entry", jsonArgsSerializer(json, Some(moduleName)))
    response*/

    json
  }

  def setEntry(moduleName: String, nameValList: NameValueList): Json = {
    val json = Json("name_value_list" := nameValList)
    val response = query("set_entry", jsonArgsSerializer(json, Some(moduleName)))
    response
  }

  /**
   * Returns a list of available modules. The list can be filtered. Per default the filter is set
   * to `all`.
   *
   * @param filter Filter the modules. Possible values are `default`, `mobile` and `all`.
   * @return
   */
  def getModules(filter: String = "all"): Json = {
    val response = query("get_available_modules", jsonArgsSerializer(Json("filter" := filter)))
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
        "search_string" := searchQuery,
        "modules" := searchModules,
        "offset" := 0,
        "max_results" := maxResults,
        "assigned_user_id" := jEmptyString,
        "select_fields" := jEmptyArray,
        "unified_search_only" := false,
        "favorites" := false
      )
    val response = query("search_by_module", jsonArgsSerializer(jsonSearch))
    response
  }
}
