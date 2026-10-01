package Proxy.Refactorizado;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// 1) Los roles posibles
enum Rol { ANALISTA, AGRICULTOR, VISITANTE }

// 2) Quién pregunta: nombre, rol y las muestras que son suyas
class Usuario {
    private final String nombre;
    private final Rol rol;
    private final Set<String> muestrasPropias = new HashSet<>();

    Usuario(String nombre, Rol rol, String... muestras) {
        this.nombre = nombre;
        this.rol = rol;
        for (String m : muestras) {
            muestrasPropias.add(m);
        }
    }

    String getNombre() { return nombre; }
    Rol getRol() { return rol; }
    boolean esPropietarioDe(String idMuestra) { return muestrasPropias.contains(idMuestra); }
}

// 3) El contrato común (Subject)
interface ServicioResultados {
    String consultarResultado(String idMuestra, Usuario solicitante);
}

// 4) El servicio real: hace la consulta, sin saber de seguridad ni de caché
class ServicioResultadosReal implements ServicioResultados {
    private final Map<String, String> resultados = new HashMap<>();

    ServicioResultadosReal() {
        System.out.println("  [BD] Abriendo conexión...");
        resultados.put("M-001", "Finca Uribe (Efren): pH=3.9 | MO=2.8%");
        resultados.put("M-002", "Finca Firme por la Patria (Juan): pH=4.7 | MO=3.4%");
        resultados.put("M-003", "Finca Sin Nada (Fabian): pH=3.0 | MO=3.3%");
    }

    @Override
    public String consultarResultado(String idMuestra, Usuario solicitante) {
        System.out.println("  [BD] Consulta costosa para " + idMuestra);
        return resultados.getOrDefault(idMuestra, "Muestra no encontrada");
    }
}

// 5) El proxy: controla el acceso antes de delegar
class ServicioResultadosProxy implements ServicioResultados {
    private ServicioResultados real;                       // empieza en null: se crea tarde
    private final Map<String, String> cache = new HashMap<>();

    @Override
    public String consultarResultado(String idMuestra, Usuario solicitante) {
        // a) validar
        if (!tieneAcceso(idMuestra, solicitante)) {
            System.out.println("  [AUDITORÍA] DENEGADO: " + solicitante.getNombre() + " -> " + idMuestra);
            throw new SecurityException(solicitante.getNombre() + " no puede ver la muestra " + idMuestra);
        }
        // b) dejar registro
        System.out.println("  [AUDITORÍA] PERMITIDO: " + solicitante.getNombre() + " -> " + idMuestra);

        // c) mirar el caché
        String guardado = cache.get(idMuestra);
        if (guardado != null) {
            System.out.println("  [CACHÉ] acierto");
            return guardado;
        }

        // d) crear el servicio real solo si hace falta
        if (real == null) {
            real = new ServicioResultadosReal();
        }

        // e) delegar y guardar el resultado
        String resultado = real.consultarResultado(idMuestra, solicitante);
        cache.put(idMuestra, resultado);
        return resultado;
    }

    private boolean tieneAcceso(String idMuestra, Usuario u) {
        if (u.getRol() == Rol.ANALISTA) {
            return true;
        }
        if (u.getRol() == Rol.AGRICULTOR) {
            return u.esPropietarioDe(idMuestra);
        }
        return false;   // VISITANTE
    }
}

// 6) El cliente: solo conoce la interfaz
public class ProxyRefactorizado {
    public static void main(String[] args) {
        ServicioResultados servicio = new ServicioResultadosProxy();

        Usuario efren   = new Usuario("Efren", Rol.AGRICULTOR, "M-001");
        Usuario juan    = new Usuario("Juan", Rol.AGRICULTOR, "M-002");
        Usuario analista = new Usuario("Analista", Rol.ANALISTA);
        Usuario anonimo = new Usuario("Anonimo", Rol.VISITANTE);

        System.out.println("Efren -> " + servicio.consultarResultado("M-001", efren));
        System.out.println("Efren otra vez -> " + servicio.consultarResultado("M-001", efren));
        System.out.println("Analista -> " + servicio.consultarResultado("M-002", analista));

        intentar(servicio, "M-002", anonimo);   // bloqueado
        intentar(servicio, "M-002", efren);     // bloqueado: no es su muestra
        intentar(servicio, "M-002", juan);      // permitido, y sale del caché
    }

    private static void intentar(ServicioResultados s, String id, Usuario u) {
        try {
            System.out.println(u.getNombre() + " -> " + s.consultarResultado(id, u));
        } catch (SecurityException e) {
            System.out.println("Acceso bloqueado: " + e.getMessage());
        }
    }
}