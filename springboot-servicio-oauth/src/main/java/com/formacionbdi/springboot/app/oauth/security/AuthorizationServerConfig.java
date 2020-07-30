package com.formacionbdi.springboot.app.oauth.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@RefreshScope
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private InfoAdicionalToken infoAdicionalToken;
	
	/**
	 * dar seguridad a los endpoints
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()")
		.checkTokenAccess("isAuthenticated()");// permitir a todos
	}

	/**
	 * configurar los clientes que se conectan a la app
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//registramos todos los clientes frontend para el uso
		//es una caracteristica de oauth2
		clients.inMemory()
		.withClient(env.getProperty("config.security.oauth.cliente.id"))//credencial del cliente
		.secret(passwordEncoder.encode(env.getProperty("config.security.oauth.cliente.secret")))
		//configuro el alcance que va tener esa api que se conecta sobre nuestra aplicacion
		.scopes("read", "write")
		//el tipo de concepcion que va tener, como se va a obtener el token
		.authorizedGrantTypes("password", "refresh_token")
		.accessTokenValiditySeconds(3600)
		.refreshTokenValiditySeconds(3600);
/** forma de poder agregar otro cliente**/
//		.and()
//		.withClient("androidapp")//credencial del cliente
//		.secret(passwordEncoder.encode("secret"))
//		//configuro el alcance que va tener esa api que se conecta sobre nuestra aplicacion
//		.scopes("read", "write")
//		//el tipo de concepcion que va tener, como se va a obtener el token
//		.authorizedGrantTypes("password", "refresh_token")
//		.accessTokenValiditySeconds(3600)
//		.refreshTokenValiditySeconds(3600);//con credenciales user y pass
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain
		//importa en la cadena mantener el orden para agregar la info extra al token 
		.setTokenEnhancers(Arrays.asList(infoAdicionalToken, accessTokenConverter()));
		
		endpoints.authenticationManager(authenticationManager)
		//guardar y generar el token usando el tipo declarado ya configurado
		.tokenStore(tokenStore())
		//para que sea el tipo JWT
		.accessTokenConverter(accessTokenConverter())
		//adiciono la cadena
		.tokenEnhancer(tokenEnhancerChain);
		
	}
	
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(env.getProperty("config.security.oauth.jwt.key"));
		return tokenConverter;
	}
	
	
}
