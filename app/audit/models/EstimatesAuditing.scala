/*
 * Copyright 2018 HM Revenue & Customs
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

package audit.models

import auth.MtdItUser
import models.calculation.CalcDisplayModel
import models.incomeSourceDetails.BusinessDetailsModel

object EstimatesAuditing {

  case class EstimatesAuditModel[A](user: MtdItUser[A], dataModel: CalcDisplayModel) extends AuditModel {

    val estimate: Option[BigDecimal] = dataModel.calcDataModel.flatMap { model =>
      model.eoyEstimate.flatMap { estimate =>
        Some(estimate.incomeTaxNicAmount)
      }
    }

    val estimateDetails: Seq[(String, String)] = estimate match {
      case Some(amount) => Seq(
        "annualEstimate" -> amount.toString,
        "currentEstimate" -> dataModel.calcDataModel.fold(dataModel.calcAmount)(_.totalIncomeTaxNicYtd).toString
      )
      case None => Seq(
        "currentEstimate" -> dataModel.calcDataModel.fold(dataModel.calcAmount)(_.totalIncomeTaxNicYtd).toString
      )
    }

    override val auditType: String = "estimatesPageView"
    override val transactionName: String = "estimates-page-view"

    //TODO: Auditing needs to be revisited for multiple businesses scenario - speak to Kris McLackland
    val business: Option[BusinessDetailsModel] = user.incomeSources.businesses.headOption
    override val detail: Seq[(String, String)] = Seq(
      "mtditid" -> user.mtditid,
      "nino" -> user.nino,
      "hasBusiness" -> user.incomeSources.hasBusinessIncome.toString,
      "hasProperty" -> user.incomeSources.hasPropertyIncome.toString,
      "bizAccPeriodStart" -> business.fold("-")(x => s"${x.accountingPeriod.start}"),
      "bizAccPeriodEnd" -> business.fold("-")(x => s"${x.accountingPeriod.end}"),
      "propAccPeriodStart" -> user.incomeSources.property.fold("-")(x => s"${x.accountingPeriod.start}"),
      "propAccPeriodEnd" -> user.incomeSources.property.fold("-")(x => s"${x.accountingPeriod.end}")
    ) ++ estimateDetails
  }
}
