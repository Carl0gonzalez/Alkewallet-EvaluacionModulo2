package com.alkewallet.test;

import com.alkewallet.core.Usuario;
import org.junit.Test;

import static org.junit.Assert.*;

public class UsuarioTest {

    @Test
    public void claveValidaMinimo4Alfanumerica() {
        assertTrue(Usuario.claveValida("abcd"));
        assertTrue(Usuario.claveValida("Ab12"));
        assertTrue(Usuario.claveValida("a1B2"));
    }

    @Test
    public void claveInvalidaMenorA4() {
        assertFalse(Usuario.claveValida("a1B"));
        assertFalse(Usuario.claveValida("123"));
    }

    @Test
    public void claveInvalidaConCaracterEspecial() {
        assertFalse(Usuario.claveValida("ab#1"));
        assertFalse(Usuario.claveValida("12_3"));
        assertFalse(Usuario.claveValida("a b1"));
    }

    @Test
    public void claveInvalidaPorSecuenciaConsecutivaAscendente() {
        assertFalse(Usuario.claveValida("1234"));
        assertFalse(Usuario.claveValida("xx1234yy"));
        assertFalse(Usuario.claveValida("Abc0123"));
    }

    @Test
    public void claveInvalidaPorSecuenciaConsecutivaDescendente() {
        assertFalse(Usuario.claveValida("4321"));
        assertFalse(Usuario.claveValida("zz4321"));
        assertFalse(Usuario.claveValida("A9876b"));
    }

    @Test
    public void claveValidaSiNoHay4Consecutivos() {
        assertTrue(Usuario.claveValida("1245"));
        assertTrue(Usuario.claveValida("1111"));
        assertTrue(Usuario.claveValida("ab12cd34"));
    }
}
