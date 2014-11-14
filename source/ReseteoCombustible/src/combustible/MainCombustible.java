/*
 * Programa que se encarga de accesar a la base de datos, para reiniciar
 * contadores de combustible y generar el historial en caso de que no exista.
 */
package combustible;

import gob.sfa.database.PostgreSQL;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



/**
 *
 * @author Salvador Daniel Pelayo
 */

public class MainCombustible {
    
    private final PostgreSQL conexion;
    private ResultSet rs;
    private String db = "";
    private final Date date;
    private final Calendar calendar;
    private static final DateFormat FORMATO = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    
    public MainCombustible(String db){
        this.db = db;
        conexion = new PostgreSQL(this.db);
        date = new Date();
        calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek( Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek( 4 );
        calendar.setTime(date);
    }
    
    public static void main(String[] args) {
        MainCombustible mc = new MainCombustible("patrimonio");
        mc.init();
    }
    
    //Función que se encarga de reiniciar los registros y guardar los historicos.
    public void init(){
        try {
            //Calcular el número de resitros que no tienen consumo
            String query = "SELECT count(*) FROM combustible.consumo WHERE consumo=0";
            rs = conexion.openResultSet(query);
            rs.first();
            int count = rs.getInt("count");
            //Escoger los que no tienen consumo
            query = "SELECT id FROM combustible.consumo WHERE consumo=0 ORDER BY id";
            rs = conexion.openResultSet(query);
            int ids[] = new int[count];
            int i = 0;
            while(rs.next()) {
                ids[i] = rs.getInt("id");
                i++;
            }
            for(i=0; i<count; i++){
                DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                query = "INSERT INTO combustible.historicoconsumo VALUES (DEFAULT, '"+formato.format(date)+"',"+ids[i]+",0)";
                conexion.execute(query);
            }
            System.out.println(FORMATO.format(date)+"\t Aviso: "+count+" vehiculos no tenian consumo y se genero su registro en 0.");
            query = "UPDATE combustible.consumo SET consumo=0 WHERE consumo>0";
            int valor = conexion.execute(query);
            System.out.println(FORMATO.format(date)+"\t Aviso: Se reiniciaron los contadores de "+valor+" registros.");
        } catch (Exception ex) {
            System.out.println(FORMATO.format(date)+"\tError: Al intentar reiniciar registros. Mensaje: "+ex.getMessage());
        }
    }
}