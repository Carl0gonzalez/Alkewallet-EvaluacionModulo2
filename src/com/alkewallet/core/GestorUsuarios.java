package com.alkewallet.core;

/**
 * Repositorio en memoria de usuarios.
 * Regla: solo se permite registrar 1 usuario en toda la aplicación.
 */
public class GestorUsuarios {
    private final Usuario[] usuarios;
    private int cantidad;

    public GestorUsuarios() {
        this.usuarios = new Usuario[100];
        this.cantidad = 0;
    }

    public int getCantidadUsuarios() {
        return cantidad;
    }

    public Usuario buscarUsuario(String nombreUsuario) {
        for (int i = 0; i < cantidad; i++) {
            if (usuarios[i].getNombreUsuario().equals(nombreUsuario)) {
                return usuarios[i];
            }
        }
        return null;
    }

    public boolean registrarUsuario(String nombreUsuario, String clave) {
        if (nombreUsuario == null || nombreUsuario.isBlank()) return false;

        // Solo 1 usuario permitido
        if (cantidad >= 1) return false;

        if (!Usuario.claveValida(clave)) return false;
        if (buscarUsuario(nombreUsuario) != null) return false;

        usuarios[cantidad++] = new Usuario(nombreUsuario, clave);
        return true;
    }

    public Usuario autenticar(String nombreUsuario, String clave) {
        Usuario u = buscarUsuario(nombreUsuario);
        if (u == null) return null;
        return u.validarClave(clave) ? u : null;
    }
}
