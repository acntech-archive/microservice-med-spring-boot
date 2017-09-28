package no.acntech.converter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.ConfigurableConversionService;

@SuppressWarnings({"WeakerAccess"})
public abstract class AbstractConverter<S, T> implements Converter<S, T>, InitializingBean {

    protected ConfigurableConversionService conversionService;

    @Override
    public void afterPropertiesSet() throws Exception {
        conversionService.addConverter(this);
    }

    @Autowired
    public void setConversionService(ConfigurableConversionService conversionService) {
        this.conversionService = conversionService;
    }
}
