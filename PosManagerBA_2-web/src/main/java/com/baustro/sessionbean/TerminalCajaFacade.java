/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.sessionbean;

import com.baustro.model.Comercio;
import com.baustro.model.EstadoEntity;
import com.baustro.model.Lote;
import com.baustro.model.TerminalCaja;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.baustro.service.AbstractFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.sql.DataSource;
//import com.jcraft.jsch.Channel;
//import com.jcraft.jsch.ChannelSftp;
//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.Session;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.primefaces.model.SortOrder;

/**
 *
 * @author ue01000632
 */
@Stateless
public class TerminalCajaFacade extends AbstractFacade<TerminalCaja> {

    @EJB
    private ComercioFacade ejbComercioFacade;

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TerminalCajaFacade() {
        super(TerminalCaja.class);
    }

    public TerminalCaja findTerminalCajaById(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TerminalCaja> cq = cb.createQuery(TerminalCaja.class);
        Root<TerminalCaja> from = cq.from(TerminalCaja.class);
        cq.select(from).where(cb.equal(from.get("id"), id));
        TypedQuery<TerminalCaja> q = em.createQuery(cq);
        TerminalCaja terminalcaja = q.getSingleResult();
        return terminalcaja;
    }

    public TerminalCaja findTerminalCajaByCodTerminal(Long id) {
        List<TerminalCaja> listaCaja = new ArrayList<TerminalCaja>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TerminalCaja> cq = cb.createQuery(TerminalCaja.class);

        Root<TerminalCaja> from = cq.from(TerminalCaja.class);

        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb.equal(from.get("codTerminal"), id));
        p.add(cb.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));

        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }

        listaCaja = getEntityManager().createQuery(cq).getResultList();

        if (!listaCaja.isEmpty()) {
            return listaCaja.get(listaCaja.size() - 1);
        } else {
            return null;
        }
    }

    public List<TerminalCaja> findAllNoNull() {
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<TerminalCaja> cq = cb0.createQuery(TerminalCaja.class);
        Root<TerminalCaja> from = cq.from(TerminalCaja.class);
        cq.select(from).where(cb0.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));
        TypedQuery<TerminalCaja> q = em.createQuery(cq);

        List<TerminalCaja> listaCaja = q.getResultList();

        return listaCaja;
    }

    public boolean findCajaByIdPinPad(Long idPinpad) {
        List<TerminalCaja> listaCaja = new ArrayList<TerminalCaja>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TerminalCaja> cq = cb.createQuery(TerminalCaja.class);
        Root<TerminalCaja> from = cq.from(TerminalCaja.class);
        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb.equal(from.get("pinpadPrincipal"), idPinpad));
        p.add(cb.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));
        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }
        listaCaja = getEntityManager().createQuery(cq).getResultList();

        if (!listaCaja.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public void generarArchivoCierre() {
        Connection conn = null;
        DataSource ds = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            InitialContext ic;
            File file = new File("SP.txt");
            PrintWriter out = new PrintWriter(file);
            ic = new InitialContext();
            ds = (DataSource) ic.lookup("java:jboss/datasources/POSMANAGER");
            conn = ds.getConnection();
            String temp = "";

            List<Comercio> listaComercio = ejbComercioFacade.findAllNoNull();
            for (Comercio comercio : listaComercio) {
                sp_obtenerRegistroCabecera(conn, ps, rs, temp, out, String.valueOf(comercio.getId()));
                sp_obtenerRegistroDetalle(conn, ps, rs, temp, out, String.valueOf(comercio.getId()));
                sp_obtenerRegistroTotales(conn, ps, rs, temp, out, String.valueOf(comercio.getId()));
            }
            out.close();

            subirArchivoBuzonSFTP(file);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(TerminalCajaFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void sp_obtenerRegistroCabecera(Connection conn, PreparedStatement ps, ResultSet rs, String temp, PrintWriter out, String idComercio) {
        try {
            String SPsql_cab = "exec sp_bpObtenerRegistroTransaccionesPosmanager_cabecera ?";
            try {
                ps = conn.prepareStatement(SPsql_cab);
            } catch (SQLException ex) {
                Logger.getLogger(TerminalCajaFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
            ps.setString(1, idComercio);
            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("Registro"));
                temp = temp + rs.getString("Registro") + "\n";
            }
            out.write(temp);
        } catch (SQLException ex) {
            Logger.getLogger(TerminalCajaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void sp_obtenerRegistroDetalle(Connection conn, PreparedStatement ps, ResultSet rs, String temp, PrintWriter out, String idComercio) {
        try {
            String SPsql_detalle = "exec sp_bpObtenerRegistroTransaccionesPosmanager_detalle ?";
            ps = conn.prepareStatement(SPsql_detalle);
            ps.setString(1, idComercio);
            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("Registro"));
                temp = temp + rs.getString("Registro") + "\n";
            }
            out.write(temp);

        } catch (SQLException ex) {
            Logger.getLogger(TerminalCajaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sp_obtenerRegistroTotales(Connection conn, PreparedStatement ps, ResultSet rs, String temp, PrintWriter out, String idComercio) {
        try {
            String SPsql_detalle = "exec sp_bpObtenerRegistroTransaccionesPosmanager_totales ?";
            ps = conn.prepareStatement(SPsql_detalle);
            ps.setString(1, idComercio);
            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("Registro"));
                temp = temp + rs.getString("Registro") + "\n";
            }
            out.write(temp);
        } catch (SQLException ex) {
            Logger.getLogger(TerminalCajaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getTerminalCajaTotalCount() {
        Number result = (Number) this.em.createNativeQuery("Select count(e.id) From terminalCaja e").getSingleResult();

        return result.intValue();
    }

    private void subirArchivoBuzonSFTP(File file) throws IOException {
//        Properties properties = new Properties();
//        properties.load(new FileInputStream(System.getProperty("jboss.server.config.dir") + "/propertiesPosmanager.properties"));
//        String SFTPHOST = properties.getProperty("SFTPHOST");
//        int SFTPPORT = Integer.parseInt(properties.getProperty("SFTPPORT"));
//        String SFTPUSER = properties.getProperty("SFTPUSER");
//        String SFTPPASS = properties.getProperty("SFTPPASS");
//        String SFTPWORKINGDIR = properties.getProperty("SFTPWORKINGDIR");
//
////        String SFTPHOST = "10.17.17.46";
////        int SFTPPORT = 22;
////        String SFTPUSER = "sochoa";
////        String SFTPPASS = "sochoa.123";
////        String SFTPWORKINGDIR = "/";
//        Session session = null;
//        Channel channel = null;
//        ChannelSftp channelSftp = null;
//
//        try {
//            JSch jsch = new JSch();
//            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
//            session.setPassword(SFTPPASS);
//            java.util.Properties config = new java.util.Properties();
//            config.put("StrictHostKeyChecking", "no");
//            session.setConfig(config);
//            session.connect();
//            channel = session.openChannel("sftp");
//            channel.connect();
//            channelSftp = (ChannelSftp) channel;
//            channelSftp.cd(SFTPWORKINGDIR);
////            File f = new File("D:\\SP.txt");
//            channelSftp.put(new FileInputStream(file), file.getName());
//            channel.disconnect();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

    public List<TerminalCaja> getTerminalCajaList(int start, int size,
            String sortField, SortOrder sortOrder) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TerminalCaja> q = cb.createQuery(TerminalCaja.class);
        Root<TerminalCaja> r = q.from(TerminalCaja.class);
        CriteriaQuery<TerminalCaja> select = q.select(r);
        if (sortField != null) {
            q.orderBy(sortOrder == SortOrder.DESCENDING
                    ? cb.asc(r.get(sortField)) : cb.desc(r.get(sortField)));
        }

        TypedQuery<TerminalCaja> query = em.createQuery(select);
        query.setFirstResult(start);
        query.setMaxResults(size);
        List<TerminalCaja> list = query.getResultList();

        return list;
    }
}
