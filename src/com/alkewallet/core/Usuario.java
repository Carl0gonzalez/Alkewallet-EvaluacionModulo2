package com.alkewallet.core;

/**
 * Usuario en memoria (sin BD).
 */
public class Usuario {
    private final String nombreUsuario;
    private final String clave;
    private final Cuenta cuenta;

    public Usuario(String nombreUsuario, String clave) {
        this.nombreUsuario = nombreUsuario;
        this.clave = clave;
        this.cuenta = new Cuenta();
        this.cuenta.setTitular(nombreUsuario);
        this.cuenta.setNumeroCuenta(generarNumeroCuenta());
        this.cuenta.setSaldoClp(0.0);
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getClave() {
        return clave;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public boolean validarClave(String claveIngresada) {
        return claveIngresada != null && clave.equals(claveIngresada);
    }

    private int generarNumeroCuenta() {
        return (int) (Math.random() * 900_000) + 100_000;
    }

    /** Solo letras y números. */
    public static boolean esAlfanumerica(String s) {
        if (s == null || s.isBlank()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isLetterOrDigit(s.charAt(i))) return false;
        }
        return true;
    }

    /** True si la clave contiene una subcadena de 4 dígitos consecutivos (asc/desc). */
    public static boolean contieneSecuenciaConsecutivaDe4Digitos(String clave) {
        if (clave == null) return false;

        for (int i = 0; i <= clave.length() - 4; i++) {
            char c0 = clave.charAt(i);
            char c1 = clave.charAt(i + 1);
            char c2 = clave.charAt(i + 2);
            char c3 = clave.charAt(i + 3);

            if (!Character.isDigit(c0) || !Character.isDigit(c1) ||
                !Character.isDigit(c2) || !Character.isDigit(c3)) {
                continue;
            }

            int d0 = c0 - '0';
            int d1 = c1 - '0';
            int d2 = c2 - '0';
            int d3 = c3 - '0';

            boolean asc = (d1 - d0 == 1) && (d2 - d1 == 1) && (d3 - d2 == 1);
            boolean desc = (d1 - d0 == -1) && (d2 - d1 == -1) && (d3 - d2 == -1);

            if (asc || desc) return true;
        }
        return false;
    }

    /** Regla final de clave. */
    public static boolean claveValida(String clave) {
        if (clave == null) return false;
        if (clave.length() < 4) return false;
        if (!esAlfanumerica(clave)) return false;
        return !contieneSecuenciaConsecutivaDe4Digitos(clave);
    }
}
