package com.alkewallet.core;

/**
 * Cuenta única del cliente.
 * Nota: el saldo se almacena internamente en CLP.
 */
public class Cuenta {
    private int numeroCuenta;
    private String titular;
    private double saldoClp;

    public Cuenta() {
        this.numeroCuenta = 0;
        this.titular = "";
        this.saldoClp = 0.0;
    }

    public Cuenta(int numeroCuenta, String titular, double saldoClp) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldoClp = saldoClp;
    }

    public int getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(int numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public double getSaldoClp() {
        return saldoClp;
    }

    public void setSaldoClp(double saldoClp) {
        this.saldoClp = saldoClp;
    }

    /** Deposita un monto en CLP. */
    public boolean depositarClp(double monto) {
        if (monto <= 0) return false;
        saldoClp += monto;
        return true;
    }

    /** Retira un monto en CLP. */
    public boolean retirarClp(double monto) {
        if (monto <= 0) return false;
        if (saldoClp < monto) return false;
        saldoClp -= monto;
        return true;
    }
}
