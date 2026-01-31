package com.alkewallet.test;

import com.alkewallet.core.GestorUsuarios;
import com.alkewallet.core.Usuario;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GestorUsuariosTest {

    private GestorUsuarios gestor;

    @Before
    public void setUp() {
        gestor = new GestorUsuarios();
    }

    @Test
    public void registrarYAutenticar() {
        assertTrue(gestor.registrarUsuario("juan", "Ab12"));
        assertNotNull(gestor.autenticar("juan", "Ab12"));
    }

    @Test
    public void soloPermiteUnUsuarioRegistrado() {
        assertTrue(gestor.registrarUsuario("carlo", "aB12"));
        assertEquals(1, gestor.getCantidadUsuarios());

        assertFalse(gestor.registrarUsuario("maria", "xY34"));
        assertEquals(1, gestor.getCantidadUsuarios());

        Usuario u = gestor.buscarUsuario("carlo");
        assertNotNull(u);
        assertNull(gestor.buscarUsuario("maria"));
    }

    @Test
    public void noPermiteSecuenciaConsecutivaEnClave() {
        assertFalse(gestor.registrarUsuario("carlo", "xx1234yy"));
        assertEquals(0, gestor.getCantidadUsuarios());
    }

    @Test
    public void noPermiteCaracteresEspecialesEnClave() {
        assertFalse(gestor.registrarUsuario("maria", "ab#1"));
        assertEquals(0, gestor.getCantidadUsuarios());
    }
}
