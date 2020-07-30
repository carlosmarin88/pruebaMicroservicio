package com.formacionbdi.springboot.app.item.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.commons.models.entity.Producto;
import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.services.IItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RefreshScope
//lo tengo configurado en zuul como ruta de enlace
//@RequestMapping("/items")
public class ItemController {

	@Autowired
	@Qualifier("itemServiceFeign")
	private IItemService itemService;
	
	@Value("${configuracion.texto}")
	String configuracionTexto;
	
	@Autowired
	Environment env;
	
	@GetMapping("/")
	public List<Item> listar(){
		return itemService.findAll();
	}
	
	@HystrixCommand(fallbackMethod = "metodoAlternativo")
	@GetMapping("/ver/{id}/{cantidad}")
	public Item detalle(@PathVariable(name = "id") Long id,
			@PathVariable(name = "cantidad") Integer cantidad) {
		return itemService.findById(id, cantidad);
		
	}
	
	public Item metodoAlternativo(Long id, Integer cantidad) {
		Item item = new Item();
		Producto producto = new Producto();
		producto.setId(id);
		producto.setPrecio(500.00);
		producto.setNombre("Camara sony");
		item.setCantidad(cantidad);
		item.setProducto(producto);
		
		return item;
	}
	
	@GetMapping("/obtener-config")
	public ResponseEntity<?> obtenerConfiguracion() {
		Map<String, String> json = new HashMap<>();
		json.put("texto", configuracionTexto);
		
		if(env.getActiveProfiles().length > 0
				&& env.getActiveProfiles()[0].equals("dev")) {
			
			json.put("autor.nombre", env.getProperty("configuracion.autor.nombre"));
			json.put("autor.email", env.getProperty("configuracion.autor.email"));
		}
		
		return new ResponseEntity<>(json, HttpStatus.OK);
	}
	
	@PostMapping("/crear")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto  crear(@RequestBody Producto producto) {
		return itemService.save(producto);
	}
	
	@PutMapping("/editar/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto editar(@RequestBody Producto producto, 
			@PathVariable(required = true) Long id) {
		return itemService.update(producto, id);
	}
	
	@DeleteMapping("/eliminar/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable(required = true) Long id) {
		itemService.deleteById(id);
	}
	
	
}
