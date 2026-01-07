package com.management.venue.converters;

import java.util.Map;

public interface Converter<T,X> {
	//params is a field left to modify api response based on user's demands
	X convert(T source, X target, Map<String,Object> params);

}
