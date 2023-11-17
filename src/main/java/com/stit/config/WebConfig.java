package com.stit.config;

import com.stit.config.deser.MyLocalDateTimeDeserializer;
import com.stit.config.deser.MyLocalDateDeserializer;
import com.stit.config.deser.MyDateDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.util.UrlPathHelper;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.stit")
public class WebConfig implements WebMvcConfigurer {

    /*
	@Bean
	public MappingJackson2HttpMessageConverter MappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter jsonConverter
			= new MappingJackson2HttpMessageConverter();

		ObjectMapper mapper = jsonConverter.getObjectMapper();
		//mapper.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));

		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		JavaTimeModule javaTimeModule = new JavaTimeModule();
		// serializer
		//javaTimeModule.addSerializer(LocalDate.class,
		//	new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));

		// de-serializer
		//javaTimeModule.addDeserializer(LocalDate.class,
		//	new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormat)));
		//mapper.registerModule(javaTimeModule);
		System.out.println("mia ".repeat(10));

		//objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		//mapper.setTimeZone(TimeZone.getTimeZone("UTC"));  //default
		//objectMapper.setTimeZone(TimeZone.getDefault());
		// david for java.sql.Date -- ATTN
		// because DateFormat not used for java.sql.Date

		//SimpleModule myModule = new SimpleModule();
		//myModule.addSerializer(java.sql.Date.class, new DateSerializer());
		//objectMapper.registerModule(myModule);

		// circular
		//Hibernate5Module hibernate5Module = new Hibernate5Module();
		//objectMapper.registerModule(hibernate5Module);
		//-----------------------------------------------
		jsonConverter.setObjectMapper(mapper);
		jsonConverter.setPrettyPrint(true);

		return jsonConverter;
	}
     */
    /**
     * java 8 LocalDate, LocaleDateTime concren
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // Custom for LocalDate, LocalDateTime
        javaTimeModule.addDeserializer(LocalDate.class, new MyLocalDateDeserializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new MyLocalDateTimeDeserializer());

        // for java.util.Date
        // utc to tw
        javaTimeModule.addDeserializer(Date.class, new MyDateDeserializer());

        ObjectMapper mapper
                = Jackson2ObjectMapperBuilder
                        .json()
                        .modules(javaTimeModule, new Jdk8Module())
                        .build()
                        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // java.util.Date
        // output string using tw
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(dateFmt);

        MappingJackson2HttpMessageConverter conv = new MappingJackson2HttpMessageConverter(mapper);
        converters.add(conv);
    }

    //@Bean(name = "SpringSecurity")
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        return viewResolver;
    }

    /**
     * mapping /mia to mia.jsp, with controller class.
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/index.html");
        registry.addRedirectViewController("/nglogin", "/index.html");
        registry.addRedirectViewController("/login", "/index.html");

        // registry.addRedirectViewController("/ng", "/ng/index.html");
    }

    //@Override
    //public void addViewControllers(ViewControllerRegistry registry) {
    //  registry.addViewController("/mia").setViewName("mia");	
    //  registry.addRedirectViewController("/", "/index.html");
    //}
    // matrix variable
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }

    // CORS
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/**")
                .addResourceLocations("/");
    }

}
