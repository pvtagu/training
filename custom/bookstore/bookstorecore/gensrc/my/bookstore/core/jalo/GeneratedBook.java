/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 18-apr-2018 16.49.50                        ---
 * ----------------------------------------------------------------
 */
package my.bookstore.core.jalo;

import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import my.bookstore.core.constants.BookstoreCoreConstants;
import my.bookstore.core.jalo.Rental;

/**
 * Generated class for type {@link de.hybris.platform.jalo.product.Product Book}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedBook extends Product
{
	/** Qualifier of the <code>Book.edition</code> attribute **/
	public static final String EDITION = "edition";
	/** Qualifier of the <code>Book.publication</code> attribute **/
	public static final String PUBLICATION = "publication";
	/** Qualifier of the <code>Book.rentals</code> attribute **/
	public static final String RENTALS = "rentals";
	/** Qualifier of the <code>Book.authors</code> attribute **/
	public static final String AUTHORS = "authors";
	/** Relation ordering override parameter constants for Book2AuthorRelation from ((bookstorecore))*/
	protected static String BOOK2AUTHORRELATION_SRC_ORDERED = "relation.Book2AuthorRelation.source.ordered";
	protected static String BOOK2AUTHORRELATION_TGT_ORDERED = "relation.Book2AuthorRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for Book2AuthorRelation from ((bookstorecore))*/
	protected static String BOOK2AUTHORRELATION_MARKMODIFIED = "relation.Book2AuthorRelation.markmodified";
	/**
	* {@link OneToManyHandler} for handling 1:n RENTALS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<Rental> RENTALSHANDLER = new OneToManyHandler<Rental>(
	BookstoreCoreConstants.TC.RENTAL,
	false,
	"book",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(Product.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(EDITION, AttributeMode.INITIAL);
		tmp.put(PUBLICATION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Book.authors</code> attribute.
	 * @return the authors
	 */
	public Collection<User> getAuthors(final SessionContext ctx)
	{
		final List<User> items = getLinkedItems( 
			ctx,
			true,
			BookstoreCoreConstants.Relations.BOOK2AUTHORRELATION,
			"User",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Book.authors</code> attribute.
	 * @return the authors
	 */
	public Collection<User> getAuthors()
	{
		return getAuthors( getSession().getSessionContext() );
	}
	
	public long getAuthorsCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			BookstoreCoreConstants.Relations.BOOK2AUTHORRELATION,
			"User",
			null
		);
	}
	
	public long getAuthorsCount()
	{
		return getAuthorsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Book.authors</code> attribute. 
	 * @param value the authors
	 */
	public void setAuthors(final SessionContext ctx, final Collection<User> value)
	{
		setLinkedItems( 
			ctx,
			true,
			BookstoreCoreConstants.Relations.BOOK2AUTHORRELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(BOOK2AUTHORRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Book.authors</code> attribute. 
	 * @param value the authors
	 */
	public void setAuthors(final Collection<User> value)
	{
		setAuthors( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to authors. 
	 * @param value the item to add to authors
	 */
	public void addToAuthors(final SessionContext ctx, final User value)
	{
		addLinkedItems( 
			ctx,
			true,
			BookstoreCoreConstants.Relations.BOOK2AUTHORRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(BOOK2AUTHORRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to authors. 
	 * @param value the item to add to authors
	 */
	public void addToAuthors(final User value)
	{
		addToAuthors( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from authors. 
	 * @param value the item to remove from authors
	 */
	public void removeFromAuthors(final SessionContext ctx, final User value)
	{
		removeLinkedItems( 
			ctx,
			true,
			BookstoreCoreConstants.Relations.BOOK2AUTHORRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(BOOK2AUTHORRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from authors. 
	 * @param value the item to remove from authors
	 */
	public void removeFromAuthors(final User value)
	{
		removeFromAuthors( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Book.edition</code> attribute.
	 * @return the edition - Name of book edition
	 */
	public String getEdition(final SessionContext ctx)
	{
		return (String)getProperty( ctx, EDITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Book.edition</code> attribute.
	 * @return the edition - Name of book edition
	 */
	public String getEdition()
	{
		return getEdition( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Book.edition</code> attribute. 
	 * @param value the edition - Name of book edition
	 */
	public void setEdition(final SessionContext ctx, final String value)
	{
		setProperty(ctx, EDITION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Book.edition</code> attribute. 
	 * @param value the edition - Name of book edition
	 */
	public void setEdition(final String value)
	{
		setEdition( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Book.publication</code> attribute.
	 * @return the publication - Year of book publication
	 */
	public Integer getPublication(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, PUBLICATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Book.publication</code> attribute.
	 * @return the publication - Year of book publication
	 */
	public Integer getPublication()
	{
		return getPublication( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Book.publication</code> attribute. 
	 * @return the publication - Year of book publication
	 */
	public int getPublicationAsPrimitive(final SessionContext ctx)
	{
		Integer value = getPublication( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Book.publication</code> attribute. 
	 * @return the publication - Year of book publication
	 */
	public int getPublicationAsPrimitive()
	{
		return getPublicationAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Book.publication</code> attribute. 
	 * @param value the publication - Year of book publication
	 */
	public void setPublication(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, PUBLICATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Book.publication</code> attribute. 
	 * @param value the publication - Year of book publication
	 */
	public void setPublication(final Integer value)
	{
		setPublication( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Book.publication</code> attribute. 
	 * @param value the publication - Year of book publication
	 */
	public void setPublication(final SessionContext ctx, final int value)
	{
		setPublication( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Book.publication</code> attribute. 
	 * @param value the publication - Year of book publication
	 */
	public void setPublication(final int value)
	{
		setPublication( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Book.rentals</code> attribute.
	 * @return the rentals
	 */
	public Collection<Rental> getRentals(final SessionContext ctx)
	{
		return RENTALSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Book.rentals</code> attribute.
	 * @return the rentals
	 */
	public Collection<Rental> getRentals()
	{
		return getRentals( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Book.rentals</code> attribute. 
	 * @param value the rentals
	 */
	public void setRentals(final SessionContext ctx, final Collection<Rental> value)
	{
		RENTALSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Book.rentals</code> attribute. 
	 * @param value the rentals
	 */
	public void setRentals(final Collection<Rental> value)
	{
		setRentals( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to rentals. 
	 * @param value the item to add to rentals
	 */
	public void addToRentals(final SessionContext ctx, final Rental value)
	{
		RENTALSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to rentals. 
	 * @param value the item to add to rentals
	 */
	public void addToRentals(final Rental value)
	{
		addToRentals( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from rentals. 
	 * @param value the item to remove from rentals
	 */
	public void removeFromRentals(final SessionContext ctx, final Rental value)
	{
		RENTALSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from rentals. 
	 * @param value the item to remove from rentals
	 */
	public void removeFromRentals(final Rental value)
	{
		removeFromRentals( getSession().getSessionContext(), value );
	}
	
}
