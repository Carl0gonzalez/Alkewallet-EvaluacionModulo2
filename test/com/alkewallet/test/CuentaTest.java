package com.alkewallet.test;

import com.alkewallet.core.Cuenta;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CuentaTest {

    private Cuenta cuenta;

    @Before
    public void setUp() {
        cuenta = new Cuenta(1, "Titular", 10000.0);
    }

    @Test
    public void depositarClpMontoPositivo() {
        assertTrue(cuenta.depositarClp(5000));
        assertEquals(15000.0, cuenta.getSaldoClp(), 0.0001);
    }

    @Test
    public void depositarClpMontoCeroRechaza() {
        assertFalse(cuenta.depositarClp(0));
        assertEquals(10000.0, cuenta.getSaldoClp(), 0.0001);
    }

    @Test
    public void retirarClpMontoValido() {
        assertTrue(cuenta.retirarClp(3000));
        assertEquals(7000.0, cuenta.getSaldoClp(), 0.0001);
    }

    @Test
    public void retirarClpSaldoInsuficienteRechaza() {
        assertFalse(cuenta.retirarClp(20000));
        assertEquals(10000.0, cuenta.getSaldoClp(), 0.0001);
    }
}
