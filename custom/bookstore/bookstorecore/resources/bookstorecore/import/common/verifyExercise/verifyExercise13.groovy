import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
import de.hybris.platform.ruleengine.dao.impl.DefaultEngineRuleDao
import de.hybris.platform.ruleengineservices.enums.RuleStatus
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel
import de.hybris.platform.couponservices.dao.CouponDao;

import javassist.bytecode.stackmap.BasicBlock.Catch;
import groovy.json.JsonSlurper



def script = new GroovyScriptEngine( '.' ).with { loadScriptByName( '../../Logger.groovy' ) //for this to work, Logger.groovy should've already been put inside platform directory
}
this.metaClass.mixin script


/******************Verifying the coupon******************/

def multiCodeCouponCode = 'mcc1'
def codeSeparator = '-'
def couponPartCount = 4
def couponPartLength = 4
def numOfCoupons = 50
def restrictionCategoryCode = 'drama'

def coupon
try {
	coupon = couponDao.findMultiCodeCouponById(multiCodeCouponCode)
} catch (ModelNotFoundException ex) {
	addError("NOT OK: there is no multi code coupon with the identifier ${multiCodeCouponCode}")
}
if (coupon != null){
	addLog("OK: multi code coupon with ID ${multiCodeCouponCode} exists.")

	if (!coupon.getCodeGenerationConfiguration().getCodeSeparator().equals(codeSeparator)) {
		addError("NOT OK: code generation is wrong. Issue: wrong code separator. Should be ${codeSeparator}")
	}
	if (coupon.getCodeGenerationConfiguration().getCouponPartCount() != couponPartCount) {
		addError("NOT OK: code generation is wrong. Issue: wrong coupon part count. Should be ${couponPartCount}")
	}
	if (coupon.getCodeGenerationConfiguration().getCouponPartLength() != couponPartLength) {
		addError("NOT OK: code generation is wrong. Issue: wrong coupon part length. Should be ${couponPartLength}")
	}
	
	if (!hasError()) {
		addLog("OK: multi code coupon code generation is correct")
	}

	def numOfExistingVouchers = coupon.getCouponCodeNumber()
	if (numOfExistingVouchers < 1){
		addError("NOT OK: there are no coupon codes generated! Make sure that you have generated the coupons manually in the Backoffice!")
	}

	if (coupon.getActive() != null || coupon.getActive == true) {
		addLog("OK: coupon is active")
	} else {
		addError("NOT OK: coupon not active!")
	}

	if (!hasError()){
		addLog('OK: coupon is created correctly')
	}
} else{
	addError("NOT OK: no multi code coupon with the identifier ${multiCodeCouponCode}.")
}

/*****************Verifying the Promotion*****************/

def promotionCode = 'freebookCoupon01'
def promotionGroupID = 'bookstorePromoGrp'
def conditionTypeStringCoupon = 'y_qualifying_coupons'
def conditionTypeCategory = 'y_qualifying_categories'
def actionTypeString = 'y_free_gift'
def numberOfFreeGifts = 1


def sourceRule
try{
	sourceRule = ruleDao.findRuleByCode(promotionCode)
} catch (ModelNotFoundException e){
	addError("NOT OK: there's no promotion with the identifier ${promotionCode} in any promotion group! Remember to publish your rule manually in the Backoffice.")
}
if (sourceRule != null && sourceRule.class.simpleName == "PromotionSourceRuleModel"){
	addLog("OK: the promotion rule is created!")

	def promotionSourceRule = (PromotionSourceRuleModel) sourceRule

	if (sourceRule.website.identifier == promotionGroupID){
		addLog("OK: the promotion rule is in ${promotionGroupID}.")
	} else{
		addError("NOT OK: the promotion rule is not in ${promotionGroupID}! It's in ${sourceRule.website.identifier} instead!")
	}

	if (sourceRule.status == RuleStatus.PUBLISHED){
		addLog("OK: the promotion is successfully published.")
	} else{
		addError("NOT OK: the promotion is not published! It is ${sourceRule.status.code} right now.  Make sure that you have published your promotion rule in the Backoffice!")
	}


	def slurper = new JsonSlurper() //for parsing conditions and actions of a rule

	def conditions = sourceRule.conditions
	if (conditions != null){
		conditionsJson = slurper.parseText( conditions )

		if (conditionsJson.size == 0) {
			addError("NOT OK: no condition is defined for the promotion rule!")
		} else if (conditionsJson.size == 1) {
			addError("NOT OK: less conditions are defined for the promotion rule!")
		} else if (conditionsJson.size > 2){
			addError("NOT OK: too many conditions are set for the promotion rule! There should be only one condition.")
		} else {

			def checkCouponRule = false
			def checkQualifyingCategory = false
			for (conditionJson in conditionsJson) {
				def curConditionTypeString = conditionJson.definitionId.toString()
				if (curConditionTypeString == conditionTypeStringCoupon){
					addLog("OK: The condition set for the rule is 'Coupon code'.")
					checkCouponRule = true
				} else if (curConditionTypeString == conditionTypeCategory) {
					addLog("OK: The condition set for the rule is 'Qualifying Category'.")
					checkQualifyingCategory = true
				} else {
					addError("NOT OK: The condition ${curConditionTypeString}! set for the rule is wrong! Should be 'Coupon code' or 'Qualifying Category'")
				}
			}

			if (checkCouponRule && checkQualifyingCategory)	{
				addLog("OK: The conditions set for the rule are 'Coupon code' and 'Qualifying Category'.")
			} else {
				addError("NOT OK: The conditions set for the rule should be 'Coupon code' and 'Qualifying Category'")
			}
		}

		def actions = sourceRule.actions
		if (actions != null){
			actionsJson = slurper.parseText( actions )

			if (actionsJson.size == 0) {
				addError("NOT OK: no action is defined for the promotion rule!")
			} else if (actionsJson.size > 1){
				addError("NOT OK: too many actions are set for the promotion rule! There should be only one action.")
			}

			def actionJson = actionsJson.get(0)
			def curActionTypeString = actionJson.definitionId.toString()

			if (curActionTypeString == actionTypeString){
				addLog("OK: The action set for the rule is 'Free gift'.")

				if (actionJson.parameters.product.value != null &&
				actionJson.parameters.quantity.value == numberOfFreeGifts){
					addLog("OK: The action properties are set correctly.")
				} else{
					addError("NOT OK: The action doesn't have correct values! You should set only 1 free book as gift.")
				}
			} else{
				addError("NOT OK: The action set for the rule is not 'Free gift' but ${curActionTypeString}!")
			}

		} else {
			addError("NOT OK: no action is defined for the promotion rule!")
		}
	} else{
		addError("NOT OK: there's no promotion with the identifier ${promotionCode} in ${promotionGroupID}!")
	}
}
	/********************Print the Output*********************/
	printOutputLog()