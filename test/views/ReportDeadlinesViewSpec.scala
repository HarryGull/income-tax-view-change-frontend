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

package views

import assets.Messages.{ReportDeadlines => messages, Sidebar => sidebarMessages}
import assets.TestConstants.IncomeSourceDetails._
import assets.TestConstants.BusinessDetails._
import assets.TestConstants._
import config.FrontendAppConfig
import models._
import org.jsoup.Jsoup
import play.api.i18n.Messages.Implicits._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.TestSupport

class ReportDeadlinesViewSpec extends TestSupport {

  lazy val mockAppConfig = app.injector.instanceOf[FrontendAppConfig]

  val successModel = ReportDeadlinesModel(List(ReportDeadlineModel(
    start = "2017-1-1".toLocalDate,
    end = "2017-3-31".toLocalDate,
    due = "2017-4-5".toLocalDate,
    met = true
  )))
  val errorModel = ReportDeadlinesErrorModel(500,"ISE")


    "The ReportDeadlines view" should {
      lazy val businessIncomeSource = IncomeSourcesModel(
        List(
          BusinessIncomeModel(
            selfEmploymentId = testSelfEmploymentId,
            tradingName = testTradeName,
            cessationDate = None,
            accountingPeriod = testBusinessAccountingPeriod,
            reportDeadlines = successModel
          )
        ),
        None
      )

      lazy val page = views.html.report_deadlines(businessIncomeSource)(FakeRequest(), applicationMessages, mockAppConfig, testMtdItUser, serviceInfo)
      lazy val document = Jsoup.parse(contentAsString(page))

    s"have the title '${messages.title}'" in {
      document.title() shouldBe messages.title
    }

    s"have the an intro para '${messages.info}'" in {
      document.getElementById("obligation-intro").text() shouldBe messages.info
    }

    "have a table containing the obligations" should {

      "contain the heading for Report period" in {
        document.getElementById("bi-1-period-heading").text() shouldBe "Report period"
      }

      "contain the heading for Status" in {
        document.getElementById("bi-1-status-heading").text() shouldBe "Report due date"
      }

      "contain the first row and have the start date as '1 January 2017' and status 'Received'" in {
        document.getElementById("bi-1-ob-1-start").text() shouldBe "1 January 2017"
        document.getElementById("bi-1-ob-1-status").text() shouldBe "Received"
      }

      "not contain a second row" in {
        document.getElementById("bi-1-ob-2-status") shouldBe null
      }
    }

    "have sidebar section " in {
      document.getElementById("sidebar") shouldNot be(null)
    }

    "when only business obligations are returned" should {

      lazy val page = views.html.report_deadlines(businessIncomeSource)(FakeRequest(), applicationMessages, mockAppConfig, testMtdItUser, serviceInfo)
      lazy val document = Jsoup.parse(contentAsString(page))

      "contain a section for Business ReportDeadlines" in {
        document.getElementById("bi-1-section").text() shouldBe testTradeName
      }

      "not contain Property ReportDeadlines section" in {
        document.getElementById("pi-section") shouldBe null
      }
    }

    "when only property obligations are returned" should {

      lazy val propertyIncomeModel = IncomeSourcesModel(
        List.empty,
        Some(PropertyIncomeModel(
          accountingPeriod = AccountingPeriodModel("2017-04-06", "2018-04-05"),
          reportDeadlines  = successModel
        ))
      )

      lazy val page = views.html.report_deadlines(propertyIncomeModel)(FakeRequest(), applicationMessages, mockAppConfig, testMtdItUser, serviceInfo)
      lazy val document = Jsoup.parse(contentAsString(page))

      "contain a section for Property ReportDeadlines" in {
        document.getElementById("pi-section").text() shouldBe messages.propertyHeading
      }

      "not contain Business ReportDeadlines section" in {
        document.getElementById("bi-1-section") shouldBe null
      }
    }

    "when both Business and Property obligations are errored" should {

      lazy val bothIncomeSourcesReportsErrored = IncomeSourcesModel(
        List(
          BusinessIncomeModel(
            selfEmploymentId = testSelfEmploymentId,
            tradingName = testTradeName,
            cessationDate = None,
            accountingPeriod = testBusinessAccountingPeriod,
            reportDeadlines = errorModel
          )
        ),
        Some(PropertyIncomeModel(
          accountingPeriod = AccountingPeriodModel("2017-04-06", "2018-04-05"),
          reportDeadlines  = errorModel
        ))
      )

      lazy val page = views.html.report_deadlines(bothIncomeSourcesReportsErrored)(FakeRequest(), applicationMessages, mockAppConfig, testMtdItUser, serviceInfo)
      lazy val document = Jsoup.parse(contentAsString(page))

      "contains a no section Property ReportDeadlines" in {
        document.getElementById("pi-section") shouldBe null
      }

      "contains a no section Business ReportDeadlines" in {
        document.getElementById("bi-1-section") shouldBe null
      }

      "contains error content" which {

        s"has a paragraph with the message '${messages.Errors.p1}'" in {
          document.getElementById("p1").text() shouldBe messages.Errors.p1
        }

        s"has a second paragraph with the message '${messages.Errors.p2}'" in {
          document.getElementById("p2").text() shouldBe messages.Errors.p2
        }
      }
    }

    "when Business obligations are errored and there are no Property obligations" should {

      lazy val businessIncomeSourcesReportsErrored = IncomeSourcesModel(
        List(
          BusinessIncomeModel(
            selfEmploymentId = testSelfEmploymentId,
            tradingName = testTradeName,
            cessationDate = None,
            accountingPeriod = testBusinessAccountingPeriod,
            reportDeadlines = errorModel
          )
        ),
        None
      )

      lazy val page = views.html.report_deadlines(businessIncomeSourcesReportsErrored)(FakeRequest(), applicationMessages, mockAppConfig, testMtdItUser, serviceInfo)
      lazy val document = Jsoup.parse(contentAsString(page))

      "contain no section for Property ReportDeadlines" in {
        document.getElementById("pi-section") shouldBe null
      }

      "contains a section for Business ReportDeadlines" which {

        s"has the heading '$testTradeName'" in {
          document.getElementById("bi-1-section").text() shouldBe testTradeName
        }

        s"has a paragraph with the message '${messages.Errors.p1}'" in {
          document.getElementById("bi-1-p1").text() shouldBe messages.Errors.p1
        }

        s"has a second paragraph with the message '${messages.Errors.p2}'" in {
          document.getElementById("bi-1-p2").text() shouldBe messages.Errors.p2
        }
      }
    }

    "when Property obligations are errored and there are no Business obligations" should {

      lazy val propertyIncomeSourcesReportsErrored = IncomeSourcesModel(
        List(),
        Some(PropertyIncomeModel(
          accountingPeriod = AccountingPeriodModel("2017-04-06", "2018-04-05"),
          reportDeadlines  = errorModel
        ))
      )

      lazy val page = views.html.report_deadlines(propertyIncomeSourcesReportsErrored)(FakeRequest(), applicationMessages, mockAppConfig, testMtdItUser, serviceInfo)
      lazy val document = Jsoup.parse(contentAsString(page))

      "contain no section for Business ReportDeadlines" in {
        document.getElementById("bi-1-section") shouldBe null
      }

      "contains a section for Property ReportDeadlines" which {

        s"has the heading '${messages.propertyHeading}'" in {
          document.getElementById("pi-section").text() shouldBe messages.propertyHeading
        }

        s"has a paragraph with the message '${messages.Errors.p1}'" in {
          document.getElementById("pi-p1").text() shouldBe messages.Errors.p1
        }

        s"has a second paragraph with the message '${messages.Errors.p2}'" in {
          document.getElementById("pi-p2").text() shouldBe messages.Errors.p2
        }
      }
    }
  }
}