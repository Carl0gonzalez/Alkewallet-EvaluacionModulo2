package com.alkewallet.core;

import java.util.Scanner;

public class UI {

    // Ajusta según la altura de tu consola en Eclipse.
    private static final int ALTURA_PANTALLA = 20;

    // Líneas que ocupa el footer.
    private static final int ALTURA_FOOTER = 8;

    public static void limpiarPantalla() {
        // En Eclipse no hay clear real; simulamos con líneas en blanco.
        for (int i = 0; i < ALTURA_PANTALLA; i++) System.out.println();
    }

    public static void linea(int n) {
        System.out.println("═".repeat(Math.max(0, n)));
    }

    public static void header(String titulo) {
        linea(62);
        System.out.println("║" + centrar(titulo, 60) + "║");
        linea(62);
    }

    public static String centrar(String s, int width) {
        if (s == null) s = "";
        if (s.length() >= width) return s.substring(0, width);
        int left = (width - s.length()) / 2;
        int right = width - s.length() - left;
        return " ".repeat(left) + s + " ".repeat(right);
    }

    private static void imprimirLineasVacias(int n) {
        for (int i = 0; i < n; i++) System.out.println();
    }

    public static void pausa(Scanner sc) {
        System.out.print("\nPresiona Enter para continuar...");
        sc.nextLine();
    }

    public static void mostrarInicio() {
        System.out.println("┌────────────────────────────────────────────────────────────┐");
        System.out.println("│                     🧾 ALKE WALLET                         │");
        System.out.println("├────────────────────────────────────────────────────────────┤");
        System.out.println("│  1) Registrar usuario                                      │");
        System.out.println("│  2) Iniciar sesión                                         │");
        System.out.println("│  3) Salir                                                  │");
        System.out.println("└────────────────────────────────────────────────────────────┘");
        System.out.print("Opción: ");
    }

    // Footer fijo
    public static void dibujarFooter(double clp, double usd, double eur, double tasaUsd, double tasaEur) {
        System.out.println("┌────────────────────────────────────────────────────────────┐");
        System.out.println("│ SALDOS / TASAS                                             │");
        System.out.println("├────────────────────────────────────────────────────────────┤");

        // Formato es-CL
        String clpStr = TasaCambio.fmtClp(clp);
        String usdStr = TasaCambio.fmtUsd(usd);
        String eurStr = TasaCambio.fmtEur(eur);

        System.out.printf("│ CLP: %-53s│%n", clpStr);
        System.out.printf("│ USD: %-53s│%n", usdStr);
        System.out.printf("│ EUR: %-53s│%n", eurStr);

        System.out.println("├────────────────────────────────────────────────────────────┤");

        String tasaUsdStr = "$" + TasaCambio.fmtNumero(tasaUsd);
        String tasaEurStr = "$" + TasaCambio.fmtNumero(tasaEur);
        System.out.printf("│ 1 USD = %-12s CLP | 1 EUR = %-12s CLP                    │%n", tasaUsdStr, tasaEurStr);

        System.out.println("└────────────────────────────────────────────────────────────┘");
    }

    // Render sesión: arriba variable, abajo footer
    public static void pantallaLogueado(
            String user,
            String tituloArriba,
            String[] contenidoArriba,
            double clp, double usd, double eur,
            double tasaUsd, double tasaEur
    ) {
        limpiarPantalla();
        header("💳 ALKE WALLET — Sesión de " + user);

        System.out.println("┌────────────────────────────────────────────────────────────┐");
        System.out.println("│ " + String.format("%-58s", tituloArriba) + "│");
        System.out.println("└────────────────────────────────────────────────────────────┘");

        int lineasUsadas = 3;
        lineasUsadas += 3;

        for (String l : contenidoArriba) {
            System.out.println(l);
            lineasUsadas++;
        }

        int relleno = ALTURA_PANTALLA - lineasUsadas - ALTURA_FOOTER;
        if (relleno < 0) relleno = 0;
        imprimirLineasVacias(relleno);

        dibujarFooter(clp, usd, eur, tasaUsd, tasaEur);
    }

    public static void mostrarMenuSesionArriba(
            String user,
            double clp, double usd, double eur,
            double tasaUsd, double tasaEur
    ) {
        String[] menuArriba = new String[]{
                "┌────────────────────────────────────────────────────────────┐",
                "│ MENÚ                                                       │",
                "├────────────────────────────────────────────────────────────┤",
                "│  1) Ver saldo (elegir moneda)                              │",
                "│  2) Depositar                                              │",
                "│  3) Retirar                                                │",
                "│  4) Cambiar tasas                                          │",
                "│  5) Cerrar sesión                                          │",
                "└────────────────────────────────────────────────────────────┘",
                "Opción: "
        };

        pantallaLogueado(user, "MENÚ PRINCIPAL", menuArriba, clp, usd, eur, tasaUsd, tasaEur);
    }

    public static void errorEnSesion(
            String user,
            String mensaje,
            double clp, double usd, double eur,
            double tasaUsd, double tasaEur,
            Scanner sc
    ) {
        String[] arriba = new String[]{
                "┌────────────────────────────────────────────────────────────┐",
                "│ ERROR                                                      │",
                "├────────────────────────────────────────────────────────────┤",
                "│ " + String.format("%-58s", mensaje) + "│",
                "└────────────────────────────────────────────────────────────┘",
                "",
                "Presiona Enter para continuar..."
        };

        pantallaLogueado(user, "MENSAJE", arriba, clp, usd, eur, tasaUsd, tasaEur);
        sc.nextLine();
    }

    public static void mostrarMenuMoneda(String titulo) {
        System.out.println("\n┌────────────────────────────────────────────────────────────┐");
        System.out.println("│ " + String.format("%-58s", titulo) + "│");
        System.out.println("├────────────────────────────────────────────────────────────┤");
        System.out.println("│  1) CLP                                                   │");
        System.out.println("│  2) USD                                                   │");
        System.out.println("│  3) EUR                                                   │");
        System.out.println("│  4) Volver                                                │");
        System.out.println("└────────────────────────────────────────────────────────────┘");
        System.out.print("Opción: ");
    }

    public static void ok(String msg) {
        System.out.println("\n✅ " + msg);
    }

    public static void err(String msg) {
        System.out.println("\n❌ " + msg);
    }
}
