import my.bookstore.core.model.BookModel
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.interceptor.impl.InterceptorMapping
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

def script = new GroovyScriptEngine( '.' ).with {
    loadScriptByName( '../../Logger.groovy' ) //for this to work, Logger.groovy should've already been put inside platform directory
}
this.metaClass.mixin script

//NOTE: If we run scripts from ant there is no user session information
userService.setCurrentUser(userService.getAdminUser())


def checkUserGroup(def groupCode){
     final UserGroupModel group = userService.getUserGroupForUID(groupCode);
    if(group==null){
        addError('usergroup '+groupCode+'  not found!')
    }else{
        if(group.getUserPriceGroup()!=null && 'goldCustomerPriceList'.equals(group.getUserPriceGroup().getCode())){
            addLog('OK: goldCustomerPriceList Price Group is assigned to '+groupCode)
        }else{
            addError('NOT OK: goldCustomerPriceList Price Group is not assigned to '+groupCode)
        }
    }
}
def checkPriceRows(String bookCode){   
    CatalogModel cm=catalogService.getCatalogForId('bookstoreProductCatalog');
    if(cm==null)
    addError('NOT OK: bookstoreProductCatalog catalog not found!')
    else{
        BookModel book=(BookModel)productService.getProductForCode(cm.getActiveCatalogVersion(),bookCode)
        if(book==null){
            addError('NOT OK: Book with code:'+bookCode+' is not found!')
        }else{

            Collection<PriceRowModel>prices= book.getEurope1Prices();
            boolean found=false;
            for(PriceRowModel price: prices){
                if(price.getUg()!=null && "goldCustomerPriceList".equals(price.getUg().getCode())){
                    found=true;
                    break;
                }
            }
            if(found){
                addLog('OK: PriceRow for book with code: '+bookCode+' and for goldCustomerPriceList user group found')
            }else{
                addError('NOT OK: PriceRow for book with code: '+bookCode+' and for goldCustomerPriceList user group not found')
            }
       
        }
    }
}

def checkInterceptor(){
    if(goldCustomerPrepareInterceptorMapping instanceof InterceptorMapping){
        addLog('OK: Prepare Interceptor Mapping is ok')
        if(goldCustomerPrepareInterceptorMapping.getInterceptor()==null){
            addError('NOT OK: bookstorecore-spring.xml: goldCustomerPrepareInterceptorMapping doesnt have defined interceptor property')
        }else{
            if(goldCustomerPrepareInterceptorMapping.getInterceptor() instanceof PrepareInterceptor){
                addLog('OK: Defined interceptor implements PrepareInterceptor')
            }else{
                addError('NOT OK: Interceptor doesnt implement PrepareInterceptor interface')
            }
        }
    }else{
        addError('NOT OK: bookstorecore-spring.xml: goldCustomerPrepareInterceptorMapping has to be instance of InterceptorMapping class')
    }
  
}
checkUserGroup("goldcustomergroup")
checkPriceRows('9439522259')
checkPriceRows('6265644009')
checkPriceRows('8679478989')
checkInterceptor()
printOutputLog()