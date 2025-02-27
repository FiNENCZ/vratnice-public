package cz.dp.vratnice_public.configuration;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.media.StringSchema;

@Configuration
public class SwaggerConfiguration {

    	@Bean
	public OperationCustomizer globalOperationCustomizer() {
		OperationCustomizer c = new OperationCustomizer() {
			@Override
			public Operation customize(Operation operation, HandlerMethod handlerMethod) {
				Parameter customHeaderVersion = new Parameter().in(ParameterIn.QUERY.toString()).name("lang")
						.schema(new StringSchema()).example("cs").required(false);
				operation.addParametersItem(customHeaderVersion);

				String beanName = handlerMethod.getBeanType().getSimpleName();
				operation.setOperationId(
						String.format("%s%s", handlerMethod.getMethod().getName(), beanName.replace("Controller", "")));

				return operation;
			}
		};
		return c;
	}


}
