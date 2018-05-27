package com.mytaxi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mytaxi.util.Constants;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 
 * Swagger Configuration
 *
 */
@Configuration
public class MytaxiAppSwaggerConfig
{

    @Bean
    public Docket docket()
    {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage(getClass().getPackage().getName()))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(generateApiInfo());
    }


    private ApiInfo generateApiInfo()
    {
        return new ApiInfoBuilder()
            .license(Constants.APP_LICENSE)
            .version(Constants.APP_VERSION)
            .termsOfServiceUrl(Constants.APP_TERMS_OF_SERVICE_URL)
            .title(Constants.APP_TITLE)
            .licenseUrl(Constants.APP_LICENSE_URL)
            .contact(new Contact(Constants.APP_CONTACT_NAME, null, Constants.APP_CONTACT_EMAIL))
            .description(Constants.APP_DESCRIPTION)
            .build();
    }

}
