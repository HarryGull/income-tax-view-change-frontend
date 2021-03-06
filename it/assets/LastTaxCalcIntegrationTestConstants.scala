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

import assets.BaseIntegrationTestConstants.{testCalcId, testCalcId2}
import assets.CalcDataIntegrationTestConstants.{calculationDataSuccessModel, calculationDataSuccessWithEoYModel}
import enums.{Crystallised, Estimate}
import models.calculation.LastTaxCalculation

object LastTaxCalcIntegrationTestConstants {

  val crystallisedLastTaxCalcResponse =
    LastTaxCalculation(testCalcId,
      "2017-07-06T12:34:56.789Z",
      calculationDataSuccessWithEoYModel.totalIncomeTaxNicYtd,
      Crystallised
    )

  val crystallisedLastTaxCalcResponse2 =
    LastTaxCalculation(testCalcId2,
      "2017-07-06T12:34:56.789Z",
      calculationDataSuccessWithEoYModel.totalIncomeTaxNicYtd,
      Crystallised
    )

  val estimateLastTaxCalcResponse =
    LastTaxCalculation(testCalcId,
      "2017-07-06T12:34:56.789Z",
      calculationDataSuccessWithEoYModel.totalIncomeTaxNicYtd,
      Estimate
    )

  val estimateLastTaxCalcResponse2 =
    LastTaxCalculation(
      testCalcId2,
      "2017-07-06T12:34:56.789Z",
      calculationDataSuccessModel.totalIncomeTaxNicYtd,
      Estimate
    )

}
