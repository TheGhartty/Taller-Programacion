package Facade.Refactorizado;

import java.util.LinkedHashMap;
import java.util.Map;

// ---- Subsistemas: IGUALES a la versión inicial ----
class RecepcionMuestras {
    private int contador = 0;

    String registrar(String agricultor, String cultivo) {
        contador++;
        String id = "M-00" + contador;
        System.out.println("  [Recepción] " + id + " (" + cultivo + ") de " + agricultor);
        return id;
    }
}

class LaboratorioQuimico {
    double medirPH(String id) {
        System.out.println("  [Lab] Midiendo pH de " + id);
        return 5.8;
    }

    Map<String, String> medirNutrientes(String id) {
        System.out.println("  [Lab] Midiendo nutrientes de " + id);
        Map<String, String> nutrientes = new LinkedHashMap<>();
        nutrientes.put("N", "Bajo");
        nutrientes.put("P", "Medio");
        nutrientes.put("K", "Alto");
        return nutrientes;
    }
}

class GeneradorInforme {
    String generar(String id, double ph, Map<String, String> nutrientes) {
        System.out.println("  [Informe] Generando informe de " + id);
        return "Informe " + id + " | pH=" + ph + " | " + nutrientes
                + " | Recomendación: encalar y fertilizar";
    }
}

class Facturacion {
    double emitir(String agricultor, double valorBase) {
        double total = valorBase * 1.19;
        System.out.println("  [Facturación] Factura a " + agricultor + " por $" + total);
        return total;
    }
}

class NotificadorSMS {
    void enviar(String telefono, String mensaje) {
        System.out.println("  [SMS -> " + telefono + "] " + mensaje);
    }
}

// ---- la fachada ----
class AgroLabFacade {
    private static final double TARIFA_ANALISIS_COMPLETO = 85000;

    // La fachada es la que crea y guarda los cinco subsistemas
    private final RecepcionMuestras recepcion = new RecepcionMuestras();
    private final LaboratorioQuimico laboratorio = new LaboratorioQuimico();
    private final GeneradorInforme generador = new GeneradorInforme();
    private final Facturacion facturacion = new Facturacion();
    private final NotificadorSMS notificador = new NotificadorSMS();

    // Una sola llamada para todo el flujo
    public String solicitarAnalisisCompleto(String agricultor, String telefono, String cultivo) {
        String id = recepcion.registrar(agricultor, cultivo);
        double ph = laboratorio.medirPH(id);
        Map<String, String> nutrientes = laboratorio.medirNutrientes(id);
        String informe = generador.generar(id, ph, nutrientes);
        facturacion.emitir(agricultor, TARIFA_ANALISIS_COMPLETO);
        notificador.enviar(telefono, "Su informe " + id + " está listo");
        return informe;
    }

    // Una segunda operación más pequeña: solo pH
    public double solicitarMedicionRapidaPH(String agricultor, String cultivo) {
        String id = recepcion.registrar(agricultor, cultivo);
        return laboratorio.medirPH(id);
    }
}

// ---- El cliente: solo conoce la fachada ----
public class FacadeRefactorizado {
    public static void main(String[] args) {
        AgroLabFacade agrolab = new AgroLabFacade();

        System.out.println("== App Web ==");
        System.out.println(agrolab.solicitarAnalisisCompleto("Efren", "3001112233", "Maíz"));

        System.out.println("\n== App Móvil ==");
        System.out.println(agrolab.solicitarAnalisisCompleto("Juan", "3004445566", "Yuca"));

        System.out.println("\n== Medición rápida ==");
        System.out.println("pH = " + agrolab.solicitarMedicionRapidaPH("Fabian", "Arroz"));
    }
}