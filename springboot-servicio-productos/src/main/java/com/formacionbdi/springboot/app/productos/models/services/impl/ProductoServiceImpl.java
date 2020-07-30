package com.formacionbdi.springboot.app.productos.models.services.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacionbdi.springboot.app.productos.models.dao.ProductoDao;
import com.formacionbdi.springboot.app.commons.models.entity.Producto;
import com.formacionbdi.springboot.app.productos.models.services.IProductoService;

@Service("productoService")
@Transactional
public class ProductoServiceImpl implements IProductoService{

	@Autowired
	private ProductoDao productoDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Producto> findAll() {
		
		return productoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findById(Long id) {
		return productoDao.findById(id).orElseThrow();
	}

	@Override
	public Producto save(Producto producto) {
		return this.productoDao.save(producto);
	}

	@Override
	public void deleteById(Long id) {
		this.productoDao.deleteById(id);
	}

}
