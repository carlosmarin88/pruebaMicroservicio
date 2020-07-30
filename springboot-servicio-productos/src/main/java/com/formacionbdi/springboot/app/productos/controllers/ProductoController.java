package com.formacionbdi.springboot.app.productos.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.commons.models.entity.Producto;
import com.formacionbdi.springboot.app.productos.models.services.IProductoService;

@RestController
//esta parte lo va llevar acabo zuul en el rutamiento
//@RequestMapping("/productos")
public class ProductoController {

	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private Environment env;
	
	@Value("${server.port}")
	private Integer port;
	
	@GetMapping("/")
	public List<Producto> listar(){
		return productoService.findAll().stream()
				.map(p->{
					//p.setPort(Integer.parseInt(env.getProperty("local.server.port")));
					p.setPort(port);
					return p;
				}).collect(Collectors.toList());
	}
	@GetMapping("/{id}")	
	public Producto detalle(@PathVariable Long id) throws Exception {
		Producto producto = productoService.findById(id);
		//producto.setPort(Integer.parseInt(env.getProperty("local.server.port")));
		producto.setPort(port);
		
		Thread.sleep(5000L);
		
		return producto;
	}
	
	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto crear(@RequestBody Producto producto) {
		return this.productoService.save(producto);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto editar(@RequestBody Producto producto, 
			@PathVariable(required = true) Long id) {
			
			Producto productoUpdate = this.productoService.findById(id);
			productoUpdate.setNombre(producto.getNombre());
			productoUpdate.setPrecio(producto.getPrecio());
			
		return this.productoService.save(productoUpdate);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		productoService.deleteById(id);
	}
}
