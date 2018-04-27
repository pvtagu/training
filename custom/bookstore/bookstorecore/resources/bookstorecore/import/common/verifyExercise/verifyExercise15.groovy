import de.hybris.platform.processengine.model.ProcessTaskLogModel;

def script = new GroovyScriptEngine( '.' ).with { loadScriptByName( '../../Logger.groovy' ) //for this to work, Logger.groovy should've already been put inside platform directory
}
this.metaClass.mixin script

//NOTE: If we run scripts from ant there is no user session information
userService.setCurrentUser(userService.getAdminUser())

def checkClasses(String className, String parent){
	try{
		addLog('...Checking class:'+className)
		Class c=Class.forName(className);
		Class parentClass=Class.forName(parent);

		if(parentClass.isAssignableFrom(c)){
			addLog("OK: $className extends/implements $parent")
		}  else{
			addError("NOT OK: $className doesn\'t extend/implement $parent")
		}
	} catch(ClassNotFoundException ex){
		addError('Class not found:'+className)
	}
}

def checkBean() {
	try {
		updateCustomerPointsAction
		addLog("OK: updateCustomerPointsAction bean found")
	} catch (groovy.lang.MissingPropertyException ex) {
		addError("NOT OK: updateCustomerPointsAction bean not found")
	}
}


def checkBusinessProcess() {
	List<ProcessTaskLogModel> taskLogList=flexibleSearchService.search("select {ptl.PK} from {ProcessTaskLog as ptl} where {ptl.actionid} like 'updateCustomerPoints'").getResult();
	if(!taskLogList.isEmpty()){
		boolean found =false
		for (ProcessTaskLogModel model : taskLogList) {
			if (model.getProcess().getCode().contains("order-process")) {
				addLog("OK: updateCustomerPoints action found in order-process business process.")
				found = true;
				break;
			}
		}
		if (!found) {
			addError("NOT OK: updateCustomerPoints action wasn't found in order-process business process.")
		}

	}
	else{
		addError("NOT OK: BusinessProcess order-process is not found! Please run the business process before executing verification script.")
	}
}

checkClasses("my.bookstore.fulfilmentprocess.actions.order.UpdateCustomerPointsAction", "de.hybris.platform.processengine.action.AbstractProceduralAction")
checkBean();
checkBusinessProcess();
printOutputLog()