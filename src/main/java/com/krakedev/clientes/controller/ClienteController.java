package com.krakedev.clientes.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.clientes.entidades.Cliente;
import com.krakedev.clientes.services.ServicioCliente;

//@RestController: esta clase recibe peticiones HTTP y lo que retorna se envía directo como respuesta
@RestController

//@RequestMapping: ruta base; todas las rutas de esta clase empiezan con /clientes
@RequestMapping("/clientes")
public class ClienteController {

	// Atributo (composición): el controlador usa al servicio; final = no se puede
	// reasignar
	private final ServicioCliente servicioCliente = new ServicioCliente();

	// @PostMapping: mapea POST /clientes (crear)
	@PostMapping
	public Cliente crear(@RequestBody Cliente cliente) { // @RequestBody: convierte el JSON de la petición en un objeto
															// Cliente
		return servicioCliente.crear(cliente);
	}

	// @GetMapping: mapea GET /clientes (leer todos); la lista se devuelve como JSON
	@GetMapping
	public List<Cliente> listar() {
		return servicioCliente.listar();
	}

	// Variable de ruta: /clientes/1002 → cedula = "1002"
	@GetMapping("/{cedula}")
	public Cliente buscar(@PathVariable String cedula) { // @PathVariable: entrega el valor de {cedula} de la URL al
															// parámetro
		return servicioCliente.buscarPorCedula(cedula);
	}

	// @PutMapping: mapea PUT /clientes/{cedula} (actualizar)
	@PutMapping("/{cedula}")
	public Cliente actualizar(@PathVariable String cedula, @RequestBody Cliente clienteActualizado) {
		return servicioCliente.actualizar(cedula, clienteActualizado);
	}

	// @DeleteMapping: mapea DELETE /clientes/{cedula} (eliminar)
	@DeleteMapping("/{cedula}")
	public boolean eliminar(@PathVariable String cedula) {
		return servicioCliente.eliminar(cedula);
	}

}
