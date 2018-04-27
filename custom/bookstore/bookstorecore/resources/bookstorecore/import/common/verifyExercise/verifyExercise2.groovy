import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.type.TypeService;
import my.bookstore.core.setup.CoreSystemSetup;
import java.util.Collection;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;


def typeService = spring.getBean("typeService");

def script = new GroovyScriptEngine( '.' ).with { loadScriptByName( '../../Logger.groovy' ) //for this to work, Logger.groovy should've already been put inside platform directory
}
this.metaClass.mixin script

def checkType(String typeCode, Map<String, String> expectedAttribQualifier2TypeMap ){
	addLog('\nChecking type:'+ typeCode);
  
    ComposedTypeModel type = null;
    try {
  	  type = typeService.getComposedTypeForCode(typeCode);
    } catch(UnknownIdentifierException e) {
       addError( ">>>>> type: " + typeCode + " does not exist! " );
       return;
    }
      
  	final Collection<AttributeDescriptorModel> actualTypeAttribs = type.getDeclaredattributedescriptors();
  
  	Map<String, String> actualAttribQualifier2TypeMap = new HashMap<>();
  	for (final AttributeDescriptorModel iAttrib : actualTypeAttribs )
	{
		final String attribQualifier = iAttrib.getQualifier();

		final TypeModel attribType = iAttrib.getAttributeType();
		final String attribTypeCode = attribType.getCode();
      
      	actualAttribQualifier2TypeMap.put( attribQualifier, attribTypeCode );
	}
  
  	Set<String> expectedAttribQualifierSet = expectedAttribQualifier2TypeMap.keySet();
    Set<String> actualAttribQualifierSet = actualAttribQualifier2TypeMap.keySet()

    for( String iExpectedQualifier : expectedAttribQualifierSet ) {
      if( actualAttribQualifierSet.contains( iExpectedQualifier ) ) {
        final String expectedType = expectedAttribQualifier2TypeMap.get( iExpectedQualifier );
        final String actualType = actualAttribQualifier2TypeMap.get(iExpectedQualifier);
        
        //remember, the return value from the "actual" map COULD be null, except that we did a "contains()" test.
        if( expectedType.equals( actualType )  ) {   
			addLog(" attribute: " + iExpectedQualifier + " - type: " + expectedType + " exists ...OK" );
        } else {
         	addError(">>>>> attribute '" + iExpectedQualifier + "' exists but wrong type\n      (is " + actualType + " but should be " + expectedType + ")." );
        }
      } else {
       	addError( ">>>>> Type " + typeCode + " is missing attribute with exact qualifier '" 
                 + iExpectedQualifier + "' \n      (could also be due to a relation involving " + typeCode + ")." );
      }
    }
}

def checkSuperType( String typeCode, String superTypeCode ) {
  
  
    ComposedTypeModel type = null;
    try{
    	type = typeService.getComposedTypeForCode(typeCode);
    } catch( UnknownIdentifierException e ) {
    	addError( ">>>>> type: " + typeCode + " does not exist!" );
        return;
    }
    Collection<ComposedTypeModel> superTypes = type.getAllSuperTypes();

    ComposedTypeModel superType = null;
    try {
  		superType = typeService.getComposedTypeForCode( superTypeCode );  
    } catch(UnknownIdentifierException e) {
    	addError( ">>>>> type: " + typeCode + " does not exist!" );
        return;
    }

  
    if( superTypes.contains(superType ) ) {
  		addLog( "\n" + typeCode + " extends " + superTypeCode + "....OK");
    } else {
        addError( "\n>>>>> " + typeCode + " DOES NOT extend " + superTypeCode  );
    }
}


//This method can only see attributes DECLARED in a given type (i.e., to also see inherited attributes, you must 
//add lines to look into: type.getInheritedattributedescriptors()  )
def checkOptionalAttributes( String typeCode, String[] optionalAttribQualifierArry ) {
  
  	addLog('\nChecking ' + typeCode + ' for OPTIONAL attributes...' );
  	
  	List<String> optionalAttribQualifierList = Arrays.asList( optionalAttribQualifierArry );
    
    ComposedTypeModel type = null;
    try {
  	   type = typeService.getComposedTypeForCode(typeCode);
    } catch(UnknownIdentifierException e) {
       addError( ">>>>> type: " + typeCode + " does not exist! " );
       return;
    }
  	final Collection<AttributeDescriptorModel> declaredAttributes = type.getDeclaredattributedescriptors();

    //build Map of declared attribute qualifiers to optional (t/f)
  	Map<String, Boolean> declaredQualifier2OptionalMap = new HashMap<>();
	for( AttributeDescriptorModel iDeclaredAttrib : declaredAttributes ) {
      final String iQualifier = iDeclaredAttrib.getQualifier();
      declaredQualifier2OptionalMap.put( iQualifier, iDeclaredAttrib.getOptional() );
    }  
    
  
  	for( String iQualifier : optionalAttribQualifierArry ) {
      if( declaredQualifier2OptionalMap.containsKey( iQualifier ) ) {
  		Boolean attributeDeclaredOptional = declaredQualifier2OptionalMap.get( iQualifier );
      	if( attributeDeclaredOptional == null ) {
        	addError( ">>>>> " + typeCode + " attribute '" + iQualifier + "' might be declared for supertype or not declared at all." );
        } else {
          if( attributeDeclaredOptional.booleanValue() ) {
            addLog( " '" + iQualifier + "' was declared OPTIONAL ...OK" );
          } else {
        	addError( ">>>>> " + typeCode + " attribute '" + iQualifier + "' must be declared OPTIONAL but IS NOT." );
          }
        }
      } else {
      	addError( ">>>>> " + typeCode + " attribute '" + iQualifier + "' must be declared OPTIONAL but is NOT DECLARED in this type." );
      }
  	}
}


def checkIfClassExists( final String javaClassName ) {
  
  try {
 	Class modelClass = Class.forName( javaClassName ); 
  } catch (ClassNotFoundException e ) {
  	addError( ">>>>> " + javaClassName + " does not exist -- \n      (possibly no ...autogenerate='true'...)\n      (possibly misspelled type code or base package)" );
  }
  
  addLog("\n" + javaClassName + " exists. ....OK"); 

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
				addLog("Checking property $entry ....OK")
			}else{
				addError("Please set property $entry")
			}
		}
	}
}

addLog('\n* 1. Checking Hybris Types *')

Map<String, String> rentalTypeMap=new HashMap<>();
rentalTypeMap.put("rentalId","java.lang.Integer");
rentalTypeMap.put("startDate","java.util.Date");
rentalTypeMap.put("endDate","java.util.Date");
rentalTypeMap.put("customer","Customer");
rentalTypeMap.put("product","Product");
checkType("Rental",rentalTypeMap)

checkIfClassExists("my.bookstore.core.model.RentalModel");


Map<String, String> productTypeMap=new HashMap<>();
productTypeMap.put("language","java.lang.String");
productTypeMap.put("ISBN10","java.lang.String");
productTypeMap.put("ISBN13","java.lang.String");
productTypeMap.put("publisher","java.lang.String");
productTypeMap.put("publishedDate","java.util.Date");
productTypeMap.put("rentable","java.lang.Boolean");
productTypeMap.put("rewardPoints","java.lang.Integer");
checkType("Product",productTypeMap)

checkIfClassExists("de.hybris.platform.core.model.product.ProductModel");


Map<String, String> bookTypeMap=new HashMap<>();
bookTypeMap.put("edition","java.lang.String");
bookTypeMap.put("publication","java.lang.Integer");
bookTypeMap.put("authors","Book2AuthorRelationauthorsColl");
checkType("Book", bookTypeMap)

checkSuperType( "Book", "Product" );
checkIfClassExists( "my.bookstore.core.model.BookModel" );
checkOptionalAttributes( "Book", "edition", "publication", "authors" );


Map<String, String> rewardStatusLevelConfigurationTypeMap=new HashMap<>();
rewardStatusLevelConfigurationTypeMap.put("threshold","java.lang.Integer");
rewardStatusLevelConfigurationTypeMap.put("image","Media");
rewardStatusLevelConfigurationTypeMap.put("rewardStatusLevel","RewardStatusLevel");
checkType("RewardStatusLevelConfiguration",rewardStatusLevelConfigurationTypeMap);

checkIfClassExists("my.bookstore.core.model.RewardStatusLevelConfigurationModel");


Map<String, String> customerTypeMap=new HashMap<>();
customerTypeMap.put("points","java.lang.Integer");
customerTypeMap.put("rewardLevelStartDate","java.util.Date");
customerTypeMap.put("expireDate","java.util.Date");
customerTypeMap.put("pointsToNextLevel","java.lang.Integer");
customerTypeMap.put("rewardStatusLevel","RewardStatusLevel");
customerTypeMap.put("rentals","Rental2CustomerRelationrentalsColl");
checkType("Customer",customerTypeMap)

checkOptionalAttributes( "Customer", "rewardStatusLevel");
checkIfClassExists("de.hybris.platform.core.model.user.CustomerModel")


Map<String, String> userTypeMap=new HashMap<>();
userTypeMap.put("books","Book2AuthorRelationbooksColl");
checkType("User", userTypeMap)

checkOptionalAttributes( "User", "books" );
checkIfClassExists("de.hybris.platform.core.model.user.UserModel");



def propertiesMap=[
	'type.Product.name',
	'type.Product.description',
	'type.Product.language.name',
	'type.Product.language.description',
	'type.Product.ISBN10.name',
	'type.Product.ISBN10.description',
	'type.Product.ISBN13.name',
	'type.Product.ISBN13.description',
	'type.Product.publisher.name',
	'type.Product.publisher.description',
	'type.Product.publishedDate.name',
	'type.Product.publishedDate.description',
	'type.Product.rentable.name',
	'type.Product.rentable.description',
	'type.Product.rewardPoints.name',
	'type.Product.rewardPoints.description',
	'type.Rental.customer.name',
	'type.Rental.customer.description',
	'type.Book.authors.name',
	'type.Book.authors.description',
	'type.User.books.name',
	'type.User.books.description',
	'type.Book.name',
	'type.Book.description',
	'type.Book.edition.name',
	'type.Book.edition.description',
	'type.Book.publication.name',
	'type.Book.publication.description',
	'type.Book.authors.name',
	'type.Book.authors.description',
	'type.Rental.name',
	'type.Rental.description',
	'type.Rental.rentalId.name',
	'type.Rental.rentalId.description',
	'type.Rental.startDate.name',
	'type.Rental.startDate.description',
	'type.Rental.product.name',
	'type.Rental.product.description',
	'type.Customer.name',
	'type.Customer.description',
	'type.Customer.points.name',
	'type.Customer.points.description',
	'type.Customer.rewardLevelStartDate.name',
	'type.Customer.rewardLevelStartDate.description',
	'type.Customer.expireDate.name',
	'type.Customer.expireDate.description',
	'type.Customer.pointsToNextLevel.name',
	'type.Customer.pointsToNextLevel.description',
	'type.Customer.rewardStatusLevel.name',
	'type.Customer.rewardStatusLevel.description',
	'type.RewardStatusLevelConfiguration.name',
	'type.RewardStatusLevelConfiguration.description',
	'type.RewardStatusLevelConfiguration.threshold.name',
	'type.RewardStatusLevelConfiguration.threshold.description',
	'type.RewardStatusLevelConfiguration.image.name',
	'type.RewardStatusLevelConfiguration.image.description',
	'type.RewardStatusLevelConfiguration.rewardStatusLevel.name',
	'type.RewardStatusLevelConfiguration.rewardStatusLevel.description'
]

addLog('\n* 2. Checking localization *\n')
checkLocalProperties(propertiesMap)

printOutputLog()
 