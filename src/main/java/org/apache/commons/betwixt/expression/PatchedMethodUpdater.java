package org.apache.commons.betwixt.expression;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A method updater patched to handle arrays of primitive types
 * 
 * @author dgrandemange
 * 
 */
public class PatchedMethodUpdater extends MethodUpdater {

	private static final Log log = LogFactory
			.getLog(PatchedMethodUpdater.class);

	public PatchedMethodUpdater() {
		super();
	}

	public PatchedMethodUpdater(Method method) {
		super(method);
	}

	@Override
	public void update(Context context, Object newValue) {
		Object bean = context.getBean();
		if (bean != null) {
			if (newValue instanceof String) {
				// try to convert into primitive types
				if (log.isTraceEnabled()) {
					log.trace("Converting primitive to " + this.getValueType());
				}
				newValue = context.getObjectStringConverter().stringToObject(
						(String) newValue, this.getValueType(), context);
			}
			if (newValue != null) {
				// check that it is of the correct type
				/*
				 * if ( ! valueType.isAssignableFrom( newValue.getClass() ) ) {
				 * log.warn( "Cannot call setter method: " + method.getName() +
				 * " on bean: " + bean + " with type: " +
				 * bean.getClass().getName() +
				 * " as parameter should be of type: " + valueType.getName() +
				 * " but is: " + newValue.getClass().getName() ); return; }
				 */
			}
			// special case for collection objects into arrays
			if (newValue instanceof Collection && this.getValueType().isArray()) {
				Collection valuesAsCollection = (Collection) newValue;
				Class componentType = getValueType().getComponentType();
				if (componentType != null) {

					if (componentType.isPrimitive()) {
						newValue = getPrimitiveArrayFromObjectArray(
								componentType, valuesAsCollection.toArray());
					} else {
						Object[] valuesAsArray = (Object[]) Array.newInstance(
								componentType, valuesAsCollection.size());
						newValue = valuesAsCollection.toArray(valuesAsArray);
					}
				}
			}

			try {
				executeUpdate(context, bean, newValue);

			} catch (Exception e) {
				String valueTypeName = (newValue != null) ? newValue.getClass()
						.getName() : "null";
				log.warn("Cannot evaluate: " + this.toString() + " on bean: "
						+ bean + " of type: " + bean.getClass().getName()
						+ " with value: " + newValue + " of type: "
						+ valueTypeName);
				handleException(context, e);
			}
		}
	}

	protected Object getPrimitiveArrayFromObjectArray(Class componentType,
			Object[] val) {
		Object outputArray = null;

		if (componentType.isAssignableFrom(byte.class)) {
			byte[] array = new byte[val.length];
			for (int i = 0; i < val.length; i++) {
				array[i] = (Byte) val[i];
			}
			outputArray = array;
		} else if (componentType.isAssignableFrom(int.class)) {
			int[] array = new int[val.length];
			for (int i = 0; i < val.length; i++) {
				array[i] = (Integer) val[i];
			}
			outputArray = array;
		} else if (componentType.isAssignableFrom(float.class)) {
			float[] array = new float[val.length];
			for (int i = 0; i < val.length; i++) {
				array[i] = (Float) val[i];
			}
			outputArray = array;
		} else if (componentType.isAssignableFrom(double.class)) {
			double[] array = new double[val.length];
			for (int i = 0; i < val.length; i++) {
				array[i] = (Double) val[i];
			}
			outputArray = array;
		} else if (componentType.isAssignableFrom(boolean.class)) {
			boolean[] array = new boolean[val.length];
			for (int i = 0; i < val.length; i++) {
				array[i] = (Boolean) val[i];
			}
			outputArray = array;
		} else if (componentType.isAssignableFrom(short.class)) {
			short[] array = new short[val.length];
			for (int i = 0; i < val.length; i++) {
				array[i] = (Short) val[i];
			}
			outputArray = array;
		} else if (componentType.isAssignableFrom(long.class)) {
			long[] array = new long[val.length];
			for (int i = 0; i < val.length; i++) {
				array[i] = (Long) val[i];
			}
			outputArray = array;
		} else if (componentType.isAssignableFrom(char.class)) {
			char[] array = new char[val.length];
			for (int i = 0; i < val.length; i++) {
				array[i] = (Character) val[i];
			}
			outputArray = array;
		}

		if (outputArray == null) // not primitive type array
			outputArray = (Object) val;

		return outputArray;
	}

}
