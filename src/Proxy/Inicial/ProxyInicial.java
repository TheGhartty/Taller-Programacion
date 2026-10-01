package Proxy.Inicial;   // deja la línea de paquete que ya tengas en tu proyecto

import java.util.HashMap;
import java.util.Map;

class ServicioResultados {
    private final Map<String, String> resultados = new HashMap<>();

    ServicioResultados() {
        resultados.put("M-001", "Finca Uribe (Efren): pH=3.9 | MO=2.8%");
        resultados.put("M-002", "Finca Firme por la Patria (Juan): pH=4.7 | MO=3.4%");
        resultados.put("M-003", "Finca Sin Nada (Fabian): pH=3.0 | MO=3.3%");
    }

    String consultarResultado(String idMuestra) {
        System.out.println("  [BD] Consulta costosa para " + idMuestra);
        return resultados.getOrDefault(idMuestra, "Muestra no encontrada");
    }
}

public class ProxyInicial {
    public static void main(String[] args) {
        ServicioResultados servicio = new ServicioResultados();

        // DEFECTO 1: un anonimo lee la muestra de Juan y nadie lo impide
        System.out.println("Anonimo -> " + servicio.consultarResultado("M-002"));

        // DEFECTO 2: Efren consulta lo mismo dos veces y se repite la consulta costosa
        System.out.println("Efren -> " + servicio.consultarResultado("M-001"));
        System.out.println("Efren otra vez -> " + servicio.consultarResultado("M-001"));
    }
}