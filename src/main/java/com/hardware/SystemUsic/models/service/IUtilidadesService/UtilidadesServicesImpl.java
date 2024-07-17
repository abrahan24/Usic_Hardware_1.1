package com.hardware.SystemUsic.models.service.IUtilidadesService;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class UtilidadesServicesImpl implements IUtilidadesServices{
    
    @Autowired
    private DataSource dataSource;

   @Override
    public byte[] generarReporte(String rutaJasper, Map<String, Object> parametros) {
        Connection conexionBD = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            // Obtener una conexión a la base de datos
            conexionBD = dataSource.getConnection();

            // Llenar el informe con datos y obtener los bytes
            JasperPrint jasperPrint = JasperFillManager.fillReport(rutaJasper, parametros, conexionBD);
            net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (SQLException | JRException e) {
            e.printStackTrace();
            // Manejar errores aquí
            return null;
        } finally {
            // Cerrar la conexión a la base de datos en el bloque finally para garantizar su
            // cierre
            if (conexionBD != null) {
                try {
                    conexionBD.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Manejar errores aquí
                }
            }
        }
    }

    @Override
    public ByteArrayOutputStream compilarAndExportarReporte(String nombreArchivo, Map<String, Object> params)
            throws IOException, JRException, SQLException {
        // Definir la ruta completa al archivo del reporte.
        Path rootPath = Paths.get("");
        Path rootAbsolutePath = rootPath.toAbsolutePath();
        String ruta = rootAbsolutePath.toString() + "/JaspertReport/" + nombreArchivo;

        // Crear el stream de salida que contendrá el reporte en formato PDF.
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        // Usar try-with-resources para asegurar el cierre automático de recursos.
        try (Connection con = dataSource.getConnection();
                FileInputStream reportStream = new FileInputStream(ruta)) {

            // Compilar el archivo JRXML para obtener el objeto JasperReport.
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Llenar el reporte compilado con datos de la base de datos y parámetros
            // proporcionados.
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);

            // Exportar el reporte lleno a un stream en formato PDF.
            JasperExportManager.exportReportToPdfStream(jasperPrint, stream);

            return stream;
        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado en la ruta: " + ruta);
            throw e;
        } catch (JRException e) {
            System.err.println("Error al compilar o llenar el reporte: " + e.getMessage());
            throw e;
        } catch (SQLException e) {
            System.err.println("Error al obtener la conexión de la base de datos: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public ByteArrayOutputStream Exportar_Reporte(String nombreArchivo)
            throws IOException, JRException, SQLException {
        Connection con = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Path rootPath = Paths.get("");
        Path rootAbsolutePath = rootPath.toAbsolutePath();
        String ruta = rootAbsolutePath.toString() + "/JaspertReport/" + nombreArchivo;

        try (FileInputStream reportStream = new FileInputStream(ruta)) {
            con = dataSource.getConnection();

            // Compilar el archivo jrxml a jasper
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Llenar el reporte sin parámetros
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JREmptyDataSource());

            // Exportar el reporte a un stream de PDF
            JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
        } catch (IOException | JRException | SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (con != null) {
                con.close();
            }
        }

        return stream;
    }

 
}
