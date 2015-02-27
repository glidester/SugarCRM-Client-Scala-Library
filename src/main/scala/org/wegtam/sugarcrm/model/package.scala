package org.wegtam.sugarcrm

import argonaut.Cursor

/**
 * Created by tim on 27/02/15.
 */
package object model {
  def getValue(fieldName: String, cursor: Cursor): Option[String] = {
    for {
      fieldObject <- (cursor --\ fieldName)
      optFieldValue <- (fieldObject --\ "value")
      fieldValue <- optFieldValue.focus.as[String].toOption
    } yield {
      fieldValue
    }
  }

  def parseArray[T](optCurso: Option[Cursor], entityParser: (Option[Cursor]) => Option[T], entities: List[T] = List[T]()): List[T] = {
    if (optCurso.isEmpty)
      entities
    else {
      val nextEntity = entityParser(optCurso)
      val results = if (nextEntity.isDefined) List(nextEntity.get) ::: entities else entities
      parseArray(optCurso.get.right, entityParser,results)
    }
  }
}
