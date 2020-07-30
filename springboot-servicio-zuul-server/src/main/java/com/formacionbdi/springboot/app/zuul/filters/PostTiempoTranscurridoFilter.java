package com.formacionbdi.springboot.app.zuul.filters;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class PostTiempoTranscurridoFilter extends ZuulFilter {

	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PostTiempoTranscurridoFilter.class);
	
	@Override
	public boolean shouldFilter() {
		// validacion si se ejecuta si o no el filtro
		
		//true para que siempre se ejecute
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		
		LOGGER.info("Entrando a post filter");
		
		Long tiempoInicio = (Long) request.getAttribute("tiempoInicio");
		Long tiempoFinal = System.currentTimeMillis();
		Long tiemproTranscurrido = tiempoFinal - tiempoInicio;
		
		LOGGER.info(String.format("Tiempo transcurrido en segundos %s", 
				tiemproTranscurrido.doubleValue()/1000.00));
		request.setAttribute("tiempoInicio", tiempoInicio);
		
		LOGGER.info(String.format("Tiempo transcurrido en milesegundo %s", 
				tiemproTranscurrido));
		request.setAttribute("tiempoInicio", tiempoInicio);
		
		return null;
	}

	@Override
	public String filterType() {
		//este tipo devuelve "post" que es una palabra clave
		return "post";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
