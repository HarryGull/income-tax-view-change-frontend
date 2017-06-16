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

package connectors

import mocks.MockHttp
import models.{ObligationsErrorModel, ErrorResponse, SuccessResponse}
import play.api.libs.json.Json
import play.mvc.Http.Status
import uk.gov.hmrc.play.http.{HeaderCarrier, HttpResponse}
import utils.TestSupport
import assets.TestConstants.Obligations._


class ObligationDataConnectorSpec extends TestSupport with MockHttp {

  implicit val hc = HeaderCarrier()

  val testNino = "AB123456C"
  val testSelfEmploymentId = "5318008"

  val successResponse = HttpResponse(Status.OK, Some(Json.toJson(obligationsDataResponse)))
  val successResponseBadJson = HttpResponse(Status.OK, responseJson = Some(Json.parse("{}")))
  val badResponse = HttpResponse(Status.BAD_REQUEST, responseString = Some("Error Message"))

  object TestObligationDataConnector extends ObligationDataConnector(mockHttpGet)

  "ObligationDataConnector.getObligationData" should {

    "return a SuccessResponse with JSON in case of sucess" in {
      setupMockHttpGet(TestObligationDataConnector.getObligationDataUrl(testNino, testSelfEmploymentId))(successResponse)
      val result = TestObligationDataConnector.getObligationData(testNino, testSelfEmploymentId)
      await(result) shouldBe obligationsDataResponse
    }

    "return ErrorResponse model in case of failure" in {
      setupMockHttpGet(TestObligationDataConnector.getObligationDataUrl(testNino, testSelfEmploymentId))(badResponse)
      val result = TestObligationDataConnector.getObligationData(testNino, testSelfEmploymentId)
      await(result) shouldBe ObligationsErrorModel(Status.BAD_REQUEST, "Error Message")
    }

    "return BusinessListError model when bad JSON is received" in {
      setupMockHttpGet(TestObligationDataConnector.getObligationDataUrl(testNino, testSelfEmploymentId))(successResponseBadJson)
      val result = TestObligationDataConnector.getObligationData(testNino, testSelfEmploymentId)
      await(result) shouldBe ObligationsErrorModel(Status.INTERNAL_SERVER_ERROR, "Json Validation Error. Parsing Obligation Data Response.")
    }
  }
}
