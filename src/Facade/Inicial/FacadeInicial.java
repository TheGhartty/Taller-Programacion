package facade.inicial;

import java.util.LinkedHashMap;
import java.util.Map;

// Subsistema 1: registra la muestra y devuelve su id
class RecepcionMuestras {
    private int contador = 0;

    String registrar(String agricultor, String cultivo) {
        contador++;
        String id = "M-00" + contador;
        System.out.println("  [Recepción] " + id + " (" + cultivo + ") de " + agricultor);
        return id;
    }
}

// Subsistema 2: mide pH y nutrientes
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

// Subsistema 3: arma el informe con los datos del laboratorio
class GeneradorInforme {
    String generar(String id, double ph, Map<String, String> nutrientes) {
        System.out.println("  [Informe] Generando informe de " + id);
        return "Informe " + id + " | pH=" + ph + " | " + nutrientes
                + " | Recomendación: encalar y fertilizar";
    }
}

// Subsistema 4: emite la factura (valor base + 19 % de IVA)
class Facturacion {
    double emitir(String agricultor, double valorBase) {
        double total = valorBase * 1.19;
        System.out.println("  [Facturación] Factura a " + agricultor + " por $" + total);
        return total;
    }
}

// Subsistema 5: avisa al agricultor
class NotificadorSMS {
    void enviar(String telefono, String mensaje) {
        System.out.println("  [SMS -> " + telefono + "] " + mensaje);
    }
}

public class FacadeInicial {
    public static void main(String[] args) {
        // El cliente tiene que crear y conocer las cinco clases
        RecepcionMuestras recepcion = new RecepcionMuestras();
        LaboratorioQuimico lab = new LaboratorioQuimico();
        GeneradorInforme generador = new GeneradorInforme();
        Facturacion facturacion = new Facturacion();
        NotificadorSMS sms = new NotificadorSMS();

        System.out.println("== App Web ==");
        flujoAppWeb(recepcion, lab, generador, facturacion, sms);

        System.out.println("\n== App Móvil ==");
        flujoAppMovil(recepcion, lab, generador, sms);
    }

    // Flujo completo, escrito a mano en el cliente
    static void flujoAppWeb(RecepcionMuestras r, LaboratorioQuimico l, GeneradorInforme g,
                            Facturacion f, NotificadorSMS s) {
        String id = r.registrar("Efren", "Maíz");
        double ph = l.medirPH(id);
        Map<String, String> nutrientes = l.medirNutrientes(id);
        String informe = g.generar(id, ph, nutrientes);
        f.emitir("Efren", 85000);
        s.enviar("3001112233", "Su informe " + id + " está listo");
        System.out.println(informe);
    }

    // DEFECTO: otra copia del flujo, y esta se olvidó de facturar
    static void flujoAppMovil(RecepcionMuestras r, LaboratorioQuimico l, GeneradorInforme g,
                              NotificadorSMS s) {
        String id = r.registrar("Juan", "Yuca");
        double ph = l.medirPH(id);
        String informe = g.generar(id, ph, l.medirNutrientes(id));
        s.enviar("3004445566", "Su informe " + id + " está listo");
        System.out.println(informe);
    }
}