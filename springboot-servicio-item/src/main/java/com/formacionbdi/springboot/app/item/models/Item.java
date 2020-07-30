package com.formacionbdi.springboot.app.item.models;

import java.io.Serializable;

import com.formacionbdi.springboot.app.commons.models.entity.Producto;

public class Item implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7928633968469497443L;

	private Producto producto;
	
	private Integer cantidad;
	
	

	public Item() {
	}

	public Item(Producto producto, Integer cantidad) {
		super();
		this.producto = producto;
		this.cantidad = cantidad;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	public Double getTotal() {
		return producto.getPrecio() * cantidad.doubleValue();
	}

	
}
