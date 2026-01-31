package com.alkewallet.core;

import java.util.Scanner;

public class Principal {
    private static final Scanner sc = new Scanner(System.in);

    private static final GestorUsuarios gestor = new GestorUsuarios();
    private static final TasaCambio tc = new TasaCambio();

    private static Usuario usuarioActual;

    public static void main(String[] args) {
        UI.limpiarPantalla();
        UI.header("Bienvenido a Alke Wallet");

        boolean run = true;
        while (run) {
            UI.mostrarInicio();
            String op = sc.nextLine().trim();

            switch (op) {
                case "1" -> registrar();
                case "2" -> login();
                case "3" -> {
                    run = false;
                    System.out.println("\nHasta luego.\n");
                }
                default -> {
                    UI.err("Opción inválida. Presiona Enter para continuar.");
                    UI.pausa(sc);
                    UI.limpiarPantalla();
                    UI.header("Bienvenido a Alke Wallet");
                }
            }
        }
        sc.close();
    }

    private static void registrar() {
        UI.limpiarPantalla();
        UI.header("Registro");

        if (gestor.getCantidadUsuarios() >= 1) {
            UI.err("Solo se permite registrar 1 usuario en esta aplicación.");
            UI.pausa(sc);
            return;
        }

        System.out.print("Usuario: ");
        String user = sc.nextLine().trim();

        System.out.print("Clave (mínimo 4, solo letras/números): ");
        String pass = sc.nextLine();

        System.out.print("Confirmar clave: ");
        String pass2 = sc.nextLine();

        if (user.isBlank()) {
            UI.err("El usuario no puede estar vacío");
            UI.pausa(sc);
            return;
        }

        if (!Usuario.claveValida(pass)) {
            UI.err("Clave inválida. Mínimo 4, solo letras/números, y sin secuencias 1234/4321.");
            UI.pausa(sc);
            return;
        }

        if (!pass.equals(pass2)) {
            UI.err("Las claves no coinciden");
            UI.pausa(sc);
            return;
        }

        boolean ok = gestor.registrarUsuario(user, pass);
        if (ok) UI.ok("Usuario registrado. Ahora puedes iniciar sesión.");
        else UI.err("No se pudo registrar (datos inválidos o ya existe un usuario).");

        UI.pausa(sc);
    }

    private static void login() {
        UI.limpiarPantalla();
        UI.header("Login");

        System.out.print("Usuario: ");
        String user = sc.nextLine().trim();
        System.out.print("Clave: ");
        String pass = sc.nextLine();

        usuarioActual = gestor.autenticar(user, pass);
        if (usuarioActual == null) {
            UI.err("Credenciales incorrectas");
            UI.pausa(sc);
            return;
        }

        UI.ok("Login exitoso");
        UI.pausa(sc);

        pedirTasas();
        menuBilletera();
    }

    private static double saldoClp() {
        return usuarioActual.getCuenta().getSaldoClp();
    }

    private static double saldoUsd() {
        return tc.convertir(saldoClp(), Moneda.CLP, Moneda.USD);
    }

    private static double saldoEur() {
        return tc.convertir(saldoClp(), Moneda.CLP, Moneda.EUR);
    }

    private static void pedirTasas() {
        String[] arriba = new String[]{
                "┌────────────────────────────────────────────────────────────┐",
                "│ CONFIGURAR TASAS (CLP por moneda)                          │",
                "├────────────────────────────────────────────────────────────┤",
                "│ Enter para mantener valores actuales                       │",
                "└────────────────────────────────────────────────────────────┘",
                ""
        };

        UI.pantallaLogueado(
                usuarioActual.getNombreUsuario(),
                "CONFIGURACIÓN",
                arriba,
                saldoClp(), saldoUsd(), saldoEur(),
                tc.getTasaUsd(), tc.getTasaEur()
        );

        System.out.print("Tasa USD (Enter para mantener " + TasaCambio.fmtNumero(tc.getTasaUsd()) + "): ");
        String inUsd = sc.nextLine().trim();
        if (!inUsd.isEmpty()) {
            try {
                tc.setTasaUsd(Double.parseDouble(inUsd));
            } catch (NumberFormatException e) {
                UI.errorEnSesion(
                        usuarioActual.getNombreUsuario(),
                        "Valor inválido para USD.",
                        saldoClp(), saldoUsd(), saldoEur(),
                        tc.getTasaUsd(), tc.getTasaEur(),
                        sc
                );
            }
        }

        UI.pantallaLogueado(
                usuarioActual.getNombreUsuario(),
                "CONFIGURACIÓN",
                arriba,
                saldoClp(), saldoUsd(), saldoEur(),
                tc.getTasaUsd(), tc.getTasaEur()
        );

        System.out.print("Tasa EUR (Enter para mantener " + TasaCambio.fmtNumero(tc.getTasaEur()) + "): ");
        String inEur = sc.nextLine().trim();
        if (!inEur.isEmpty()) {
            try {
                tc.setTasaEur(Double.parseDouble(inEur));
            } catch (NumberFormatException e) {
                UI.errorEnSesion(
                        usuarioActual.getNombreUsuario(),
                        "Valor inválido para EUR.",
                        saldoClp(), saldoUsd(), saldoEur(),
                        tc.getTasaUsd(), tc.getTasaEur(),
                        sc
                );
            }
        }

        UI.ok("Tasas configuradas");
        UI.pausa(sc);
    }

    private static void menuBilletera() {
        boolean sesion = true;

        while (sesion) {
            UI.mostrarMenuSesionArriba(
                    usuarioActual.getNombreUsuario(),
                    saldoClp(), saldoUsd(), saldoEur(),
                    tc.getTasaUsd(), tc.getTasaEur()
            );

            String op = sc.nextLine().trim();

            switch (op) {
                case "1" -> verSaldo();
                case "2" -> deposito();
                case "3" -> retiro();
                case "4" -> pedirTasas();
                case "5" -> {
                    sesion = false;
                    usuarioActual = null;
                    UI.ok("Sesión cerrada");
                    UI.pausa(sc);
                }
                default -> UI.errorEnSesion(
                        usuarioActual.getNombreUsuario(),
                        "Opción inválida. Presiona Enter para continuar.",
                        saldoClp(), saldoUsd(), saldoEur(),
                        tc.getTasaUsd(), tc.getTasaEur(),
                        sc
                );
            }
        }
    }

    private static Moneda elegirMoneda(String titulo) {
        while (true) {
            UI.mostrarMenuMoneda(titulo);
            String op = sc.nextLine().trim();
            return switch (op) {
                case "1" -> Moneda.CLP;
                case "2" -> Moneda.USD;
                case "3" -> Moneda.EUR;
                case "4" -> null;
                default -> {
                    UI.err("Opción inválida. Presiona Enter...");
                    UI.pausa(sc);
                    yield null;
                }
            };
        }
    }

    private static void verSaldo() {
        Moneda m = elegirMoneda("Elegir moneda para mostrar saldo");
        if (m == null) return;

        double valor = tc.convertir(saldoClp(), Moneda.CLP, m);

        String valStr = switch (m) {
            case CLP -> TasaCambio.fmtClp(valor);
            case USD -> TasaCambio.fmtUsd(valor);
            case EUR -> TasaCambio.fmtEur(valor);
        };

        String[] arriba = new String[]{
                "┌────────────────────────────────────────────────────────────┐",
                "│ SALDO                                                      │",
                "├────────────────────────────────────────────────────────────┤",
                "│ " + String.format("Saldo en %-3s: %-44s", m, valStr) + "│",
                "└────────────────────────────────────────────────────────────┘",
                "",
                "Presiona Enter para volver..."
        };

        UI.pantallaLogueado(
                usuarioActual.getNombreUsuario(),
                "VER SALDO",
                arriba,
                saldoClp(), saldoUsd(), saldoEur(),
                tc.getTasaUsd(), tc.getTasaEur()
        );

        sc.nextLine();
    }

    private static void deposito() {
        Moneda m = elegirMoneda("Elegir moneda de depósito");
        if (m == null) return;

        String[] arriba = new String[]{
                "┌────────────────────────────────────────────────────────────┐",
                "│ DEPÓSITO                                                   │",
                "├────────────────────────────────────────────────────────────┤",
                "│ Ingresa el monto a depositar                               │",
                "└────────────────────────────────────────────────────────────┘",
                ""
        };

        UI.pantallaLogueado(
                usuarioActual.getNombreUsuario(),
                "DEPÓSITO",
                arriba,
                saldoClp(), saldoUsd(), saldoEur(),
                tc.getTasaUsd(), tc.getTasaEur()
        );

        System.out.print("Monto en " + m + ": ");
        String in = sc.nextLine().trim();

        double monto;
        try {
            monto = Double.parseDouble(in);
        } catch (NumberFormatException e) {
            UI.errorEnSesion(usuarioActual.getNombreUsuario(), "Monto inválido.", saldoClp(), saldoUsd(), saldoEur(), tc.getTasaUsd(), tc.getTasaEur(), sc);
            return;
        }

        double clp = tc.convertir(monto, m, Moneda.CLP);
        boolean ok = usuarioActual.getCuenta().depositarClp(clp);

        if (!ok) {
            UI.errorEnSesion(usuarioActual.getNombreUsuario(), "No se pudo depositar (monto debe ser > 0).", saldoClp(), saldoUsd(), saldoEur(), tc.getTasaUsd(), tc.getTasaEur(), sc);
            return;
        }

        UI.pantallaLogueado(
                usuarioActual.getNombreUsuario(),
                "DEPÓSITO",
                new String[]{
                        "┌────────────────────────────────────────────────────────────┐",
                        "│ RESULTADO                                                  │",
                        "├────────────────────────────────────────────────────────────┤",
                        "│ Depósito realizado                                         │",
                        "└────────────────────────────────────────────────────────────┘",
                        "",
                        "Presiona Enter para volver..."
                },
                saldoClp(), saldoUsd(), saldoEur(),
                tc.getTasaUsd(), tc.getTasaEur()
        );
        sc.nextLine();
    }

    private static void retiro() {
        Moneda m = elegirMoneda("Elegir moneda de retiro");
        if (m == null) return;

        String[] arriba = new String[]{
                "┌────────────────────────────────────────────────────────────┐",
                "│ RETIRO                                                     │",
                "├────────────────────────────────────────────────────────────┤",
                "│ Ingresa el monto a retirar                                 │",
                "└────────────────────────────────────────────────────────────┘",
                ""
        };

        UI.pantallaLogueado(
                usuarioActual.getNombreUsuario(),
                "RETIRO",
                arriba,
                saldoClp(), saldoUsd(), saldoEur(),
                tc.getTasaUsd(), tc.getTasaEur()
        );

        System.out.print("Monto en " + m + ": ");
        String in = sc.nextLine().trim();

        double monto;
        try {
            monto = Double.parseDouble(in);
        } catch (NumberFormatException e) {
            UI.errorEnSesion(usuarioActual.getNombreUsuario(), "Monto inválido.", saldoClp(), saldoUsd(), saldoEur(), tc.getTasaUsd(), tc.getTasaEur(), sc);
            return;
        }

        double clp = tc.convertir(monto, m, Moneda.CLP);
        boolean ok = usuarioActual.getCuenta().retirarClp(clp);

        if (!ok) {
            UI.errorEnSesion(usuarioActual.getNombreUsuario(), "No se pudo retirar (saldo insuficiente o monto inválido).", saldoClp(), saldoUsd(), saldoEur(), tc.getTasaUsd(), tc.getTasaEur(), sc);
            return;
        }

        UI.pantallaLogueado(
                usuarioActual.getNombreUsuario(),
                "RETIRO",
                new String[]{
                        "┌────────────────────────────────────────────────────────────┐",
                        "│ RESULTADO                                                  │",
                        "├────────────────────────────────────────────────────────────┤",
                        "│ Retiro realizado                                           │",
                        "└────────────────────────────────────────────────────────────┘",
                        "",
                        "Presiona Enter para volver..."
                },
                saldoClp(), saldoUsd(), saldoEur(),
                tc.getTasaUsd(), tc.getTasaEur()
        );
        sc.nextLine();
    }
}
