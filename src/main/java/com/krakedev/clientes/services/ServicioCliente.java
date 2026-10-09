package com.krakedev.clientes.services;

import java.util.ArrayList;
import java.util.List;

import com.krakedev.clientes.entidades.Cliente;

public class ServicioCliente {
	// Atributo
	private ArrayList<Cliente> clientes = new ArrayList<Cliente>();

	// BUSCAR
	public Cliente buscarPorCedula(String cedula) {
		for (Cliente c : clientes) {
			if (c.getCedula().equals(cedula)) {
				return c;
			}
		}
		return null;
	}

	// RETORNAR
	public Cliente crear(Cliente cliente) {
		Cliente existente = buscarPorCedula(cliente.getCedula());

		if (existente != null) {
			return null;
		} else {
			clientes.add(cliente);
			return cliente;
		}
	}

	// LISTAR
	public List<Cliente> listar() {
		return clientes;

	}

	// Actualizar
	public Cliente actualizar(String cedula, Cliente clienteActualizado) {
		Cliente cliente = buscarPorCedula(cedula);
		if (cliente != null) {
			cliente.setNombre(clienteActualizado.getNombre());
			cliente.setApellido(clienteActualizado.getApellido());
		}
		return cliente;
	}

	// Eliminar
	public boolean eliminar(String cedula) {
		Cliente cliente = buscarPorCedula(cedula);
		if (cliente != null) {
			clientes.remove(cliente);
			return true;
		} else {
			return false;
		}

	}

}
