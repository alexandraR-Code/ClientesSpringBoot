package com.krakedev.clientes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.krakedev.clientes.entidades.Cliente;
import com.krakedev.clientes.services.ServicioCliente;

// Pruebas de todo el proyecto en una sola clase, usando solo @Test.
// Cada prueba crea sus propios objetos, asi no dependen unas de otras.
public class ClientesTest {

	// =====================================================================
	// 1. ENTIDAD: Cliente
	// =====================================================================

	// El constructor vacio deja los tres datos en null
	@Test
	public void testClienteConstructorVacio() {
		Cliente cliente = new Cliente();

		assertNull(cliente.getCedula());
		assertNull(cliente.getNombre());
		assertNull(cliente.getApellido());
		assertNull(cliente.getEmail());
	}

	// El constructor inicializado guarda cada dato en su lugar
	@Test
	public void testClienteConstructorInicializado() {
		Cliente cliente = new Cliente("1001", "Ana", "Lopez", "alexa@movilis.ec");

		assertEquals("1001", cliente.getCedula());
		assertEquals("Ana", cliente.getNombre());
		assertEquals("Lopez", cliente.getApellido());
		assertEquals("alexa@movilis.ec", cliente.getEmail());
	}

	// Los setters cambian los datos y los getters los devuelven
	@Test
	public void testClienteSettersYGetters() {
		Cliente cliente = new Cliente();

		cliente.setCedula("2002");
		cliente.setNombre("Pedro");
		cliente.setApellido("Mora");

		assertEquals("2002", cliente.getCedula());
		assertEquals("Pedro", cliente.getNombre());
		assertEquals("Mora", cliente.getApellido());
	}

	// toString muestra los tres datos
	@Test
	public void testClienteToString() {
		Cliente cliente = new Cliente("1001", "Ana", "Lopez", "alexa@movilis.ec");

		assertEquals("Cliente [cedula=1001, nombre=Ana, apellido=Lopez, email=alexa@movilis.ec]", cliente.toString());
	}

	// =====================================================================
	// 2. SERVICIO: LISTAR
	// =====================================================================

	// Al inicio la lista esta vacia y no es null
	@Test
	public void testListarAlInicioEstaVacia() {
		ServicioCliente servicio = new ServicioCliente();

		assertNotNull(servicio.listar());
		assertTrue(servicio.listar().isEmpty());
	}

	// =====================================================================
	// 3. SERVICIO: CREAR
	// =====================================================================

	// Un cliente nuevo se agrega y se retorna
	@Test
	public void testCrearClienteNuevo() {
		ServicioCliente servicio = new ServicioCliente();
		Cliente ana = new Cliente("1001", "Ana", "Lopez", "maria@movilis.ec");

		Cliente resultado = servicio.crear(ana);

		assertSame(ana, resultado);
		assertEquals(1, servicio.listar().size());
	}

	// Dos clientes con cedulas distintas se agregan en orden
	@Test
	public void testCrearDosClientesDistintos() {
		ServicioCliente servicio = new ServicioCliente();
		Cliente ana = new Cliente("1001", "Ana", "Lopez", "lola@movilis.ec");
		Cliente luis = new Cliente("1002", "Luis", "Perez", "carlos@movilis.ec");

		servicio.crear(ana);
		servicio.crear(luis);

		assertEquals(2, servicio.listar().size());
		assertSame(ana, servicio.listar().get(0));
		assertSame(luis, servicio.listar().get(1));
	}

	// Con una cedula repetida retorna null y no se agrega
	@Test
	public void testCrearCedulaRepetida() {
		ServicioCliente servicio = new ServicioCliente();
		servicio.crear(new Cliente("1001", "Ana", "Lopez", "tatiana@movilis.ec"));

		Cliente resultado = servicio.crear(new Cliente("1001", "Otra", "Persona", "caro@movilis.ec"));

		assertNull(resultado);
		assertEquals(1, servicio.listar().size());
		assertEquals("Ana", servicio.buscarPorCedula("1001").getNombre());
	}

	// Despues de eliminar se puede volver a crear con la misma cedula
	@Test
	public void testCrearDespuesDeEliminar() {
		ServicioCliente servicio = new ServicioCliente();
		servicio.crear(new Cliente("1001", "Ana", "Lopez", "fernan@movilis.ec"));
		servicio.eliminar("1001");

		Cliente resultado = servicio.crear(new Cliente("1001", "Nueva", "Ana", "diana@movilis.ec"));

		assertNotNull(resultado);
		assertEquals(1, servicio.listar().size());
	}

	// =====================================================================
	// 4. SERVICIO: BUSCAR
	// =====================================================================

	// Buscar por cedula devuelve el cliente que existe
	@Test
	public void testBuscarExistente() {
		ServicioCliente servicio = new ServicioCliente();
		Cliente ana = new Cliente("1001", "Ana", "Lopez", "Astrid@movilis.ec");
		Cliente luis = new Cliente("1002", "Luis", "Perez", "jesica@movilis.ec");
		servicio.crear(ana);
		servicio.crear(luis);

		assertSame(luis, servicio.buscarPorCedula("1002"));
	}

	// Una cedula que no existe devuelve null
	@Test
	public void testBuscarInexistente() {
		ServicioCliente servicio = new ServicioCliente();
		servicio.crear(new Cliente("1001", "Ana", "Lopez", "juli@movilis.ec"));

		assertNull(servicio.buscarPorCedula("9999"));
	}

	// Buscar en un servicio vacio devuelve null
	@Test
	public void testBuscarEnServicioVacio() {
		ServicioCliente servicio = new ServicioCliente();

		assertNull(servicio.buscarPorCedula("1001"));
	}

	// =====================================================================
	// 5. SERVICIO: ACTUALIZAR
	// =====================================================================

	// Actualizar cambia el nombre y el apellido
	@Test
	public void testActualizarExistente() {
		ServicioCliente servicio = new ServicioCliente();
		Cliente ana = new Cliente("1001", "Ana", "Lopez", "movilis@movilis.ec");
		servicio.crear(ana);

		Cliente resultado = servicio.actualizar("1001", new Cliente("0000", "Anita", "Gomez", "movilis@movilis.ec"));

		assertSame(ana, resultado);
		assertEquals("Anita", resultado.getNombre());
		assertEquals("Gomez", resultado.getApellido());
		assertEquals("movilis@movilis.ec", resultado.getEmail());
	}

	// Actualizar NO cambia la cedula (es la clave del cliente)
	@Test
	public void testActualizarNoCambiaLaCedula() {
		ServicioCliente servicio = new ServicioCliente();
		Cliente ana = new Cliente("1001", "Ana", "Lopez", "dario@movilis.ec");
		servicio.crear(ana);

		servicio.actualizar("1001", new Cliente("0000", "Anita", "Gomez", "amanda@movilis.ec"));

		assertEquals("1001", ana.getCedula());
		assertSame(ana, servicio.buscarPorCedula("1001"));
		assertNull(servicio.buscarPorCedula("0000"));
	}

	// Actualizar una cedula que no existe retorna null y no cambia nada
	@Test
	public void testActualizarInexistente() {
		ServicioCliente servicio = new ServicioCliente();
		Cliente ana = new Cliente("1001", "Ana", "Lopez", "ronald@movilis.ec");
		servicio.crear(ana);

		Cliente resultado = servicio.actualizar("9999", new Cliente("9999", "X", "Y", "laura@movilis.ec"));

		assertNull(resultado);
		assertEquals("Ana", ana.getNombre());
		assertEquals(1, servicio.listar().size());
	}

	// =====================================================================
	// 6. SERVICIO: ELIMINAR
	// =====================================================================

	// Eliminar un cliente que existe retorna true y lo quita de la lista
	@Test
	public void testEliminarExistente() {
		ServicioCliente servicio = new ServicioCliente();
		Cliente ana = new Cliente("1001", "Ana", "Lopez", "yenny@movilis.ec");
		Cliente luis = new Cliente("1002", "Luis", "Perez", "jess@movilis.ec");
		servicio.crear(ana);
		servicio.crear(luis);

		boolean resultado = servicio.eliminar("1001");

		assertTrue(resultado);
		assertEquals(1, servicio.listar().size());
		assertNull(servicio.buscarPorCedula("1001"));
		assertSame(luis, servicio.buscarPorCedula("1002"));
	}

	// Eliminar una cedula que no existe retorna false y no cambia la lista
	@Test
	public void testEliminarInexistente() {
		ServicioCliente servicio = new ServicioCliente();
		servicio.crear(new Cliente("1001", "Ana", "Lopez", "susana@movilis.ec"));

		assertFalse(servicio.eliminar("9999"));
		assertEquals(1, servicio.listar().size());
	}

	// Eliminar dos veces el mismo cliente: la primera true y la segunda false
	@Test
	public void testEliminarDosVeces() {
		ServicioCliente servicio = new ServicioCliente();
		servicio.crear(new Cliente("1001", "Ana", "Lopez", "jse@movilis.ec"));

		assertTrue(servicio.eliminar("1001"));
		assertFalse(servicio.eliminar("1001"));
	}

	// =====================================================================
	// 7. SERVICIO: CRUD COMPLETO
	// =====================================================================

	// Crear, buscar, actualizar y eliminar en una sola prueba
	@Test
	public void testCrudCompleto() {
		ServicioCliente servicio = new ServicioCliente();
		Cliente ana = new Cliente("1001", "Ana", "Lopez", "sara@movilis.ec");

		assertSame(ana, servicio.crear(ana));
		assertSame(ana, servicio.buscarPorCedula("1001"));

		servicio.actualizar("1001", new Cliente("1001", "Ana Maria", "Lopez", "stefy@movilis.ec"));
		assertEquals("Ana Maria", servicio.buscarPorCedula("1001").getNombre());

		assertTrue(servicio.eliminar("1001"));
		assertTrue(servicio.listar().isEmpty());
	}

	// =====================================================================
	// 8. LIMITACIONES CONOCIDAS (documentan el comportamiento actual)
	// =====================================================================

	// crear(null) lanza NullPointerException
	@Test
	public void testLimitacionCrearNull() {
		ServicioCliente servicio = new ServicioCliente();

		assertThrows(NullPointerException.class, () -> servicio.crear(null));
	}

	// Con un cliente sin cedula en la lista, crear otro lanza NullPointerException
	@Test
	public void testLimitacionClienteSinCedula() {
		ServicioCliente servicio = new ServicioCliente();
		servicio.crear(new Cliente()); // el primero entra: la lista estaba vacia

		assertThrows(NullPointerException.class,
				() -> servicio.crear(new Cliente("1001", "Ana", "Lopez", "karola@movilis.ec")));
	}

	// =====================================================================
	// 9. CONTROLADOR: HolaController (llamado como Java normal)
	// =====================================================================

	// saludar() retorna el texto que se ve en el navegador en /hola
	@Test
	public void testHolaControllerSaludar() {
		HolaController controlador = new HolaController();

		assertEquals("Hola desde Spring Boot", controlador.saludar());
	}
	// ========================================================================
	// 10. testEmail
	// ========================================================================

	// Prueba de regresión: verifica que actualizar también cambia el email
	@Test
	public void testActualizarCambiarEmail() {
		ServicioCliente servicio = new ServicioCliente();
		servicio.crear(new Cliente("1001", "Ana", "Lopez", "ana@movilis.ec")); // email viejo

		Cliente resultado = servicio.actualizar("1001", new Cliente("0000", "Anita", "Gomez", "anita@movilis.ec")); // email
																													// nuevo

		assertEquals("anita@movilis.ec", resultado.getEmail());
	}

	// Prueba de creación: verifica que crear conserva el email del cliente
	@Test
	public void testCrearConservarEmail() {
		ServicioCliente servicio = new ServicioCliente();

		Cliente resultado = servicio.crear(new Cliente("1001", "Ana", "Lopez", "ana@movilis.ec"));

		assertEquals("ana@movilis.ec", resultado.getEmail());

	}

	// Prueba de búsqueda: verifica que buscarPorCedula devuelve el cliente con su
	// email
	@Test
	public void testBuscarDevuelveEmail() {
		ServicioCliente servicio = new ServicioCliente();
		servicio.crear(new Cliente("1021", "Ana", "Lopez", "ana@movilis.ec"));
		Cliente resultado = servicio.buscarPorCedula("1021");

		assertEquals("ana@movilis.ec", resultado.getEmail());

	}

	// Prueba de eliminación: verifica que eliminar borra a un cliente que tiene
	// email
	@Test
	public void testEliminarExistenteEmail() {
		ServicioCliente servicio = new ServicioCliente();
		servicio.crear(new Cliente("1001", "Ana", "Lopez", "jse@movilis.ec"));

		assertTrue(servicio.eliminar("1001"));
		assertFalse(servicio.eliminar("1001"));

	}
}