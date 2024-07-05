package com.hardware.SystemUsic.models.service.IUtilidadesService;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
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
        Connection con = null;

        // return stream;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Path rootPath = Paths.get("");
        Path rootAbsolutPath = rootPath.toAbsolutePath();
        String ruta = rootAbsolutPath.toString() + "/Reportes/" + nombreArchivo;

        try (FileInputStream reportStream = new FileInputStream(ruta)) {
            con = dataSource.getConnection();

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
        } catch (IOException | JRException | SQLException e) {
            e.printStackTrace();
        }
        con.close();
        return stream;

    }

    @Override
    public ByteArrayOutputStream Exportar_Reporte(String nombreArchivo)
            throws IOException, JRException, SQLException {
        Connection con = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Path rootPath = Paths.get("");
        Path rootAbsolutePath = rootPath.toAbsolutePath();
        String ruta = rootAbsolutePath.toString() + "/Reportes/" + nombreArchivo;

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
