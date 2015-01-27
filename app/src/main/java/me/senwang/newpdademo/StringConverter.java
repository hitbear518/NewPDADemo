package me.senwang.newpdademo;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by Wang Sen on 1/27/2015.
 * Last modified:
 * By:
 */
public class StringConverter implements Converter {

	@Override
	public Object fromBody(TypedInput body, Type type) throws ConversionException {
		String text = null;
		try {
			text = IOUtils.toString(body.in());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	@Override
	public TypedOutput toBody(Object object) {
		return null;
	}
}
