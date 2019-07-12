package vc.thinker;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

import java.lang.reflect.WildcardType;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.classmate.TypeResolver;

import io.swagger.annotations.Api;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author YanZhuoMin
 *
 * @date 2019年7月12日
 */
@EnableSwagger2
@Configuration
public class ApiConfig {

	/**
	 * 通用错误
	 */
	public static final int COM_SERVICE_EXCEPTION = 101001;
	
	@Value("${swagger.show}")
	private boolean swaggerShow;
	
	@Bean
	  public Docket petApi() {
	    return new Docket(DocumentationType.SWAGGER_2).enable(swaggerShow)
	        .select()
	          .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
	         // .apis(RequestHandlerSelectors.basePackage("vc.thinker.sys.web.action"))
	          .paths(PathSelectors.any())
	          .build()
	        .pathMapping("/")
	        .directModelSubstitute(LocalDate.class, String.class)
	        .genericModelSubstitutes(ResponseEntity.class)
	        .alternateTypeRules(
	            newRule(typeResolver.resolve(DeferredResult.class,
	                typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
	                typeResolver.resolve(WildcardType.class)))
	        .useDefaultResponseMessages(false)
	        .globalResponseMessage(RequestMethod.POST,
	            newArrayList(new ResponseMessageBuilder()
	                .code(COM_SERVICE_EXCEPTION)
	                .message("通用业务异常")
	                .build()))
	        .globalResponseMessage(RequestMethod.DELETE,
		            newArrayList(new ResponseMessageBuilder()
		                .code(COM_SERVICE_EXCEPTION)
		                .message("通用业务异常")
		                .build()))
	        .securitySchemes(newArrayList(apiKey()))
//	        .securityContexts(newArrayList(securityContext()))
//	        .enableUrlTemplating(true)
	        .apiInfo(apiInfo())
//	        .globalOperationParameters(
//	            newArrayList(new ParameterBuilder()
//	                .name("someGlobalParameter")
//	                .description("Description of someGlobalParameter")
//	                .modelRef(new ModelRef("string"))
//	                .parameterType("query")
//	                .required(true)
//	                .build()))
//	        .tags(new Tag("Pet Service", "All apis relating to pets")) 
//	        .additionalModels(typeResolver.resolve(AdditionalModel.class)) 
	        ;
	  }
	   private ApiInfo apiInfo() {  
	        return new ApiInfoBuilder()  
	            .title("通用消息系统API")
	            .description("通用消息系统API")
	            .version("1.0.0")  
	            .termsOfServiceUrl("http://www.thinker.vc")
	            .build();
	    }  

	  @Autowired
	  private TypeResolver typeResolver;

	  private ApiKey apiKey() {
	    return new ApiKey("mykey", "api_key", "header");
	  }

//	  private SecurityContext securityContext() {
//	    return SecurityContext.builder()
//	        .securityReferences(defaultAuth())
//	        .forPaths(PathSelectors.regex("/api/*.*"))
//	        .build();
//	  }
//
//	  List<SecurityReference> defaultAuth() {
//	    AuthorizationScope authorizationScope
//	        = new AuthorizationScope("global", "accessEverything");
//	    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//	    authorizationScopes[0] = authorizationScope;
//	    return newArrayList(
//	        new SecurityReference("mykey", authorizationScopes));
//	  }
//
//	  @Bean
//	  SecurityConfiguration security() {
//	    return SecurityConfigurationBuilder.builder()
//	        .clientId("test-app-client-id")
//	        .clientSecret("test-app-client-secret")
//	        .realm("test-app-realm")
//	        .appName("test-app")
//	        .scopeSeparator(",")
//	        .additionalQueryStringParams(null)
//	        .useBasicAuthenticationWithAccessCodeGrant(false)
//	        .build();
//	  }

	 @Bean
	  UiConfiguration uiConfig() {
	    return UiConfigurationBuilder.builder()
	        .deepLinking(true)
	        .displayOperationId(false)
	        .defaultModelsExpandDepth(1)
	        .defaultModelExpandDepth(1)
	        .defaultModelRendering(ModelRendering.EXAMPLE)
	        .displayRequestDuration(false)
	        .docExpansion(DocExpansion.NONE)
	        .filter(false)
	        .maxDisplayedTags(null)
	        .operationsSorter(OperationsSorter.ALPHA)
	        .showExtensions(false)
	        .tagsSorter(TagsSorter.ALPHA)
	        .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
	        .validatorUrl(null)
	        .build();
	  }
}
