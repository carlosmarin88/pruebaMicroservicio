package com.formacionbdi.springboot.app.zuul.filters;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class PreTiempoTranscurridoFilter extends ZuulFilter {

	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PreTiempoTranscurridoFilter.class);
	
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
		
		LOGGER.info(String.format("%s request enrutado a %s", 
				request.getMethod(),
				request.getRequestURL().toString()));
		
		Long tiempoInicio = System.currentTimeMillis();
		request.setAttribute("tiempoInicio", tiempoInicio);
		
		return null;
	}

	@Override
	public String filterType() {
		//este tipo devuelve "pre" que es una palabra clave
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
