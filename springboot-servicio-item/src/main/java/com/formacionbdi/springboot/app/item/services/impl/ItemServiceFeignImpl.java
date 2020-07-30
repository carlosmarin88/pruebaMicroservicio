package com.formacionbdi.springboot.app.item.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formacionbdi.springboot.app.commons.models.entity.Producto;
import com.formacionbdi.springboot.app.item.clientes.ProductoClienteRest;
import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.services.IItemService;

@Service("itemServiceFeign")
public class ItemServiceFeignImpl implements IItemService {

	@Autowired
	private ProductoClienteRest clienteFeign;
	
	@Override
	public List<Item> findAll() {

		return clienteFeign.listar()
				.stream()
				.map(p -> new Item(p, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		Producto producto = clienteFeign.detalle(id);
		return new Item( producto, cantidad);
	}

	@Override
	public Producto save(Producto producto) {
		return clienteFeign.crear(producto);
	}

	@Override
	public Producto update(Producto producto, Long id) {
		
		return clienteFeign.editar(producto, id);
	}

	@Override
	public void deleteById(Long id) {
		clienteFeign.eliminar(id);
	}

}
