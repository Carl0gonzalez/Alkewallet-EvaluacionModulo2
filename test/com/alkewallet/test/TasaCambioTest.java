package com.alkewallet.test;

import com.alkewallet.core.Moneda;
import com.alkewallet.core.TasaCambio;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TasaCambioTest {

    private TasaCambio tc;

    @Before
    public void setUp() {
        tc = new TasaCambio();
        tc.setTasaUsd(1000.0);
        tc.setTasaEur(1100.0);
    }

    @Test
    public void convertirClpAUsd() {
        assertEquals(10.0, tc.convertir(10000.0, Moneda.CLP, Moneda.USD), 0.0001);
    }

    @Test
    public void convertirUsdAClp() {
        assertEquals(10000.0, tc.convertir(10.0, Moneda.USD, Moneda.CLP), 0.0001);
    }

    @Test
    public void convertirClpAEur() {
        assertEquals(10.0, tc.convertir(11000.0, Moneda.CLP, Moneda.EUR), 0.0001);
    }

    @Test
    public void convertirEurAClp() {
        assertEquals(11000.0, tc.convertir(10.0, Moneda.EUR, Moneda.CLP), 0.0001);
    }

    @Test
    public void formatoEsClUsaComaDecimal() {
        // No validamos el separador de miles, solo el decimal.
        assertEquals("1,50", TasaCambio.fmtNumero(1.5));
    }
}
