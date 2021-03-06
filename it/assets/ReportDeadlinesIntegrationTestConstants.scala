/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package assets

import java.time.LocalDate

import models.reportDeadlines.{ReportDeadlineModel, ReportDeadlinesModel}
import play.api.libs.json.{JsValue, Json}
import utils.ImplicitDateFormatter._

object ReportDeadlinesIntegrationTestConstants {
  def successResponse(obligationsModel: ReportDeadlinesModel): JsValue = {
    Json.toJson(obligationsModel)
  }

  def emptyResponse(): JsValue = Json.arr()

  def failureResponse(code: String, reason: String): JsValue = Json.obj(
    "code" -> code,
    "reason" -> reason
  )

  val deadlineStart1 = "2017-01-01"
  val deadlineEnd1 = "2017-03-31"
  val deadlineStart2 = "2017-04-01"
  val deadlineEnd2 = "2017-06-30"
  val deadlineStart3 = "2016-06-01"
  val deadlineEnd3 = "2017-06-30"
  val deadlineStart4 = "2017-07-01"
  val deadlineEnd4 = "2017-09-30"
  val deadlineStart5 = "2017-10-01"
  val deadlineEnd5 = "2018-01-31"
  val deadlineStart6 = "2017-11-01"
  val deadlineEnd6 = "2018-02-01"

  val multipleReportDeadlinesDataSuccessModel = ReportDeadlinesModel(List(
    ReportDeadlineModel(
      start = deadlineStart1,
      end = deadlineEnd1,
      due = LocalDate.now().minusDays(128),
      periodKey = "periodKey"
    ), ReportDeadlineModel(
      start = deadlineStart2,
      end = deadlineEnd2,
      due = LocalDate.now().minusDays(36),
      periodKey = "periodKey"
    ), ReportDeadlineModel(
      start = deadlineStart3,
      end = deadlineEnd3,
      due = LocalDate.now().minusDays(36),
      periodKey = "periodKey"
    ), ReportDeadlineModel(
      start = deadlineStart4,
      end = deadlineEnd4,
      due = LocalDate.now().plusDays(30),
      periodKey = "periodKey"
    ),ReportDeadlineModel(
      start = deadlineStart5,
      end = deadlineEnd5,
      due = LocalDate.now().plusDays(146),
      periodKey = "periodKey"
    ), ReportDeadlineModel(
      start = deadlineStart6,
      end = deadlineEnd6,
      due = LocalDate.now().plusDays(174),
      periodKey = "periodKey"
    )
  ))

  val singleObligationStart = "2017-04-06"
  val singleObligationEnd = "2017-07-05"

  val singleObligationOverdueModel = ReportDeadlinesModel(List(
    ReportDeadlineModel(
      start = singleObligationStart,
      end = singleObligationEnd,
      due = LocalDate.now().minusDays(1),
      periodKey = "periodKey"
    )
  ))

  val singleObligationPlusYearOpenModel = ReportDeadlinesModel(List(
    ReportDeadlineModel(
      start = "2017-04-06",
      end = "2017-07-05",
      due = LocalDate.now().plusYears(1),
      periodKey = "periodKey"
    )
  ))

  val emptyModel = ReportDeadlinesModel(List())
}