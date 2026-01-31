package com.alkewallet.core;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Maneja tasas de cambio:
 * - tasaUsd: CLP por 1 USD
 * - tasaEur: CLP por 1 EUR
 */
public class TasaCambio {
    private double tasaUsd; // CLP por 1 USD
    private double tasaEur; // CLP por 1 EUR

    // Formateadores con Locale es-CL (coma decimal, punto de miles).
    private static final Locale LOCALE_CL = new Locale("es", "CL");
    private static final DecimalFormatSymbols DFS = new DecimalFormatSymbols(LOCALE_CL);
    private static final DecimalFormat DF_MONTO = new DecimalFormat("#,##0.00", DFS);

    public TasaCambio() {
        this.tasaUsd = 950.0;
        this.tasaEur = 1050.0;
    }

    public double getTasaUsd() {
        return tasaUsd;
    }

    public void setTasaUsd(double tasaUsd) {
        if (tasaUsd > 0) this.tasaUsd = tasaUsd;
    }

    public double getTasaEur() {
        return tasaEur;
    }

    public void setTasaEur(double tasaEur) {
        if (tasaEur > 0) this.tasaEur = tasaEur;
    }

    public double convertir(double monto, Moneda desde, Moneda hacia) {
        if (desde == null || hacia == null) {
            throw new IllegalArgumentException("Monedas no pueden ser null");
        }
        if (monto == 0) return 0;
        if (desde == hacia) return monto;

        // Primero llevamos a CLP
        double enClp;
        switch (desde) {
            case CLP:
                enClp = monto;
                break;
            case USD:
                enClp = monto * tasaUsd;
                break;
            case EUR:
                enClp = monto * tasaEur;
                break;
            default:
                throw new IllegalStateException("Moneda no soportada");
        }

        // Luego desde CLP al destino
        switch (hacia) {
            case CLP:
                return enClp;
            case USD:
                return enClp / tasaUsd;
            case EUR:
                return enClp / tasaEur;
            default:
                throw new IllegalStateException("Moneda no soportada");
        }
    }

    // Helpers de formato para UI (Locale es-CL)
    public static String fmtNumero(double valor) {
        synchronized (DF_MONTO) {
            return DF_MONTO.format(valor);
        }
    }

    public static String fmtClp(double clp) {
        return "$" + fmtNumero(clp);
    }

    public static String fmtUsd(double usd) {
        return "$" + fmtNumero(usd);
    }

    public static String fmtEur(double eur) {
        return "€" + fmtNumero(eur);
    }
}
