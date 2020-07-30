package com.formacionbdi.springboot.app.item.services.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.formacionbdi.springboot.app.commons.models.entity.Producto;
import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.services.IItemService;


@Service("itemService")
public class ItemServiceImpl implements IItemService {

	@Autowired
	RestTemplate clienteRest;
	
	@Override
	public List<Item> findAll() {
		
		List<Producto> productos = Arrays.asList( clienteRest
				.getForObject("http://servicio-productos/", 
						Producto[].class));
		
		return productos.stream().map(p -> new Item(p, 1))
				.collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		
		Map<String, String> pathVariable = new HashMap<String, String>();
		
		pathVariable.put("id", id.toString());
		
		Producto producto = clienteRest
				.getForObject("http://servicio-productos/{id}", 
						Producto.class, pathVariable);
		
		return new Item(producto, cantidad);
	}

	@Override
	public Producto save(Producto producto) {
		HttpEntity<Producto> body = new HttpEntity<Producto>(producto);
		ResponseEntity<Producto> responseProducto = clienteRest.exchange("http://servicio-productos/", HttpMethod.POST, body, Producto.class);
		return responseProducto.getBody();
	}

	@Override
	public Producto update(Producto producto, Long id) {
		
		Map<String, String> pathVariable = new HashMap<String, String>();
		pathVariable.put("id", id.toString());
		
		HttpEntity<Producto> body = new HttpEntity<Producto>(producto);
		ResponseEntity<Producto> responseProducto = clienteRest
				.exchange("http://servicio-productos/{id}", 
						HttpMethod.PUT, body, Producto.class,
						pathVariable);
		
		return responseProducto.getBody();
	}

	@Override
	public void deleteById(Long id) {
		Map<String, String> pathVariable = new HashMap<String, String>();
		pathVariable.put("id", id.toString());
		clienteRest.delete("http://servicio-productos/{id}",pathVariable);
	}
	
}
