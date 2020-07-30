package com.formacionbdi.springboot.app.oauth.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.formacionbdi.springboot.app.commons.usuarios.models.entity.Usuario;
import com.formacionbdi.springboot.app.oauth.services.IUsuarioService;

import feign.FeignException;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

	@Autowired
	private IUsuarioService usuarioService;
	
	@Value("${config.security.oauth.cliente.id}")
	private String clientApp;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);

	/**
	 * obtener información del usuario authenticado
	 */
	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		
		if(authentication.getName().equals(clientApp)) {
			return;
		}
		
		UserDetails user = (UserDetails) authentication.getPrincipal();
		// se puede guardar auditoria del usuario cuando se logueo
		LOGGER.info("Success login: " + user.getUsername());
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		
		if(usuario.getIntentos()!=null && usuario.getIntentos()>0) {
			usuario.setIntentos(0);
			usuarioService.update(usuario, usuario.getId());
		}
	}

	/**
	 * obtengo el error de alguien que se quiso autenticar
	 */
	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		
		if(authentication.getName().equals(clientApp)) {
			return;
		}
		
		LOGGER.error("Error en el login: " + exception.getMessage());
		try {
			Usuario usuario = usuarioService.findByUsername(authentication.getName());
			if (usuario.getIntentos() == null) {
				usuario.setIntentos(0);
			}
			usuario.setIntentos(usuario.getIntentos() + 1);
			
			if(usuario.getIntentos()>=3) {
				LOGGER.info(String.format("El usuario %s supero la cantidad de intentos", 
						usuario.getUsername()));
				usuario.setEnabled(false);
			}
			
			usuarioService.update(usuario, usuario.getId());
			
		} catch (FeignException e) {
			LOGGER.error(String.format("el usuario %s no existe en el sistema", authentication.getName()));
		}

	}

}
