package com.cl.ysyd.common.utils;

import com.cl.ysyd.common.exception.BusiException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidatorUtil {
	private static Validator validator;
	
	static {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	public static void validateDto(Object obj, Class<?>... groups) {
		Set<ConstraintViolation<Object>> set = validator.validate(obj, groups);
		if(!set.isEmpty()) {
			StringBuilder msg = new StringBuilder();
			for(ConstraintViolation<Object> cv : set) {
				msg.append(cv.getMessage()).append(" ");
			}
			throw new BusiException(msg.toString());
		}
	}
}
