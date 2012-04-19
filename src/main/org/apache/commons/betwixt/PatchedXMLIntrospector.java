package org.apache.commons.betwixt;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

import org.apache.commons.betwixt.expression.MethodUpdater;
import org.apache.commons.betwixt.expression.PatchedMethodUpdater;
import org.apache.commons.betwixt.expression.Updater;

/**
 * Here we override betwixt XMLIntrospector so that we can replace any
 * MethodUpdater by our PatchedMethodUpdater that knows how to handle arrays of
 * primitive types (int, byte, char and so on ...)
 * 
 * @author dgrandemange
 * 
 */
public class PatchedXMLIntrospector extends XMLIntrospector {

	@Override
	public XMLBeanInfo introspect(BeanInfo beanInfo)
			throws IntrospectionException {
		XMLBeanInfo xmlBeanInfo = super.introspect(beanInfo);

		ElementDescriptor elementDescriptor = xmlBeanInfo
				.getElementDescriptor();
		patchElementDescriptor(elementDescriptor);

		return xmlBeanInfo;
	}

	/**
	 * @param elementDescriptor
	 */
	protected void patchElementDescriptor(ElementDescriptor elementDescriptor) {

		if (null == elementDescriptor) {
			return;
		}

		ElementDescriptor[] elementDescriptors = elementDescriptor
				.getElementDescriptors();

		if (0 == elementDescriptors.length) {
			Updater updater = elementDescriptor.getUpdater();
			if (updater instanceof MethodUpdater) {
				PatchedMethodUpdater patchUpdater = new PatchedMethodUpdater(
						((MethodUpdater) updater).getMethod());
				elementDescriptor.setUpdater(patchUpdater);
			}
		} else {
			for (ElementDescriptor child : elementDescriptors) {
				patchElementDescriptor(child);
			}
		}

	}

}
