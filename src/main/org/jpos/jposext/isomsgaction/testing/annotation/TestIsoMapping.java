package org.jpos.jposext.isomsgaction.testing.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to use when unit testing iso msg actions XML mappings
 * 
 * @author dgrandemange
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface TestIsoMapping {
	/**
	 * @return Directory location of mapping files 
	 */
	String mappingsDir();
	
	/**
	 * @return Id of mapping file to test
	 */
	String mappingId() default "*";
	
	/**
	 * @return Interactive indicator
	 */
	boolean interactive() default false;
}
