package com.logViz.dataservice.common.validation.validator;

import com.logViz.dataservice.common.validation.ValidDateRange;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.PropertyUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;
import java.util.Objects;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startDateFieldName;
    private String endDateFieldName;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startDateFieldName = constraintAnnotation.startDateFieldName();
        this.endDateFieldName = constraintAnnotation.endDateFieldName();
    }

    @SneakyThrows
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Date startDate = (Date) PropertyUtils.getProperty(value, startDateFieldName);
        Date endDate = (Date) PropertyUtils.getProperty(value, endDateFieldName);
        return Objects.nonNull(startDate) && Objects.nonNull(endDate) && endDate.after(startDate);
    }

}
