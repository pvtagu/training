import javax.xml.xpath.*
import my.bookstore.core.setup.CoreSystemSetup;
import de.hybris.platform.btg.model.BTGConfigModel;
import java.util.Collection;
import de.hybris.platform.btg.enums.BTGRuleType;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.Map;



def script = new GroovyScriptEngine( '.' ).with {
    loadScriptByName( '../../Logger.groovy' ) //for this to work, Logger.groovy should've already been put inside platform directory
}
this.metaClass.mixin script

//NOTE: If we run scripts from ant there is no user session information
userService.setCurrentUser(userService.getAdminUser())

def checkClasses(String className, String parent){
    try{
        addLog('Checking class:'+className)
        Class c=Class.forName(className);
        Class parentClass=Class.forName(parent);

        if(parentClass.isAssignableFrom(c)){               
            addLog(className+' extends/implements '+parent+' ....OK')
        }  else{
            addError(className+' doesnt extend/implement '+parent+'  ....NOT OK')     
        }
    } catch(ClassNotFoundException ex){
        addError('Class not found:'+className)
    }
}

def checkLocalProperties(valuesMap){
    Properties properties = new Properties()
    InputStream inputStream = CoreSystemSetup.getClassLoader().getResourceAsStream("localization/bookstorecore-locales_en.properties")
    if(inputStream==null){
        addError('cannot find bookstorecore-locales_en.properties file')
    }
    else{
        properties.load(inputStream)
   
        for(entry in valuesMap){
            if(properties."$entry" !=null && properties."$entry" !=""){
                addLog('Checking property '+entry+' ....OK')
            }else{
                addError('Please set property '+entry)
            }
        }
    }
}

def checkBTGconfig(){
    BTGConfigModel btgConfig=btgConfigurationService.getConfig()
    Map<BTGRuleType,Collection<ComposedTypeModel>>maps=btgConfig.getOperandMapping();
    Collection<ComposedTypeModel> userRules=maps.get(BTGRuleType.USER);
    def found=false;
    for(ComposedTypeModel userRule:userRules){
        if("BTGCustomerRewardPointsOperand".equals(userRule.getCode())){
            found=true;
            break;
        }
    }
    if(found){
        addLog('BTGCustomerRewardPointsOperand is defined as Customer rule ....OK')
    }else{
        addError('BTGCustomerRewardPointsOperand is not defined as Customer Rule ...NOT OK')
    }
}
checkClasses("my.bookstore.core.model.BTGCustomerRewardPointsOperandModel","de.hybris.platform.btg.model.BTGAbstractCustomerOperandModel")

def propertiesMap=[
'type.BTGCustomerRewardPointsOperand.name',
]

checkLocalProperties(propertiesMap)

checkClasses("my.bookstore.core.btg.condition.valueprovider.CustomerRewardPointsValueProvider","de.hybris.platform.btg.condition.operand.OperandValueProvider")

checkBTGconfig()
printOutputLog()