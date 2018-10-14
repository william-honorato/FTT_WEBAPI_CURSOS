package br.edu.cefsa.ftt.ec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.edu.cefsa.ftt.ec.model.Alunos;
import br.edu.cefsa.ftt.ec.model.Faltas;
import br.edu.cefsa.ftt.ec.model.Notas;
import br.edu.cefsa.ftt.util.DbUtil;

public class FaltasDao {

	private Connection connection;
	
	public FaltasDao() {
        connection = DbUtil.getConnection();
    }
	
	private long getLastId() {
		
		long id = 0;

		try {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT LAST_INSERT_ID() as ID;");

            if (rs.next()) {            	
            	id = rs.getLong("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return id;		
	}
		
	private long getNumFaltas(Long idAluno) {
		
		long numeroFaltas = 0;

		try {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT COUNT(1) as NumFaltas FROM Faltas WHERE IdAluno = " + idAluno);

            if (rs.next()) {            	
            	numeroFaltas = rs.getLong("NumFaltas");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return numeroFaltas;		
	}
			
    public void addFalta(Faltas falta) {
        
    	try {
    		
            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO Faltas (IdAluno, DataCadastro) VALUES (?, ?)");
            
            // Parameters start with 1
            preparedStatement.setLong(1, falta.getIdAluno());            
            preparedStatement.setDate(2, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
            
            preparedStatement.executeUpdate();
            falta.setIdFalta(getLastId());//Pega o id inserido
            falta.setNumeroFaltas(getNumFaltas(falta.getIdAluno()));

        } catch (SQLException e) {
            e.printStackTrace();
            
            throw new ArithmeticException("FaltasDao: addFalta: " + e.getMessage()); 
        }
    } //addFalta

    public void updateFaltas(Faltas falta) {
        try {        	
            PreparedStatement preparedStatement = connection
                    .prepareStatement("UPDATE Faltas SET IdAluno=?, "
                    		                          + "DataCadastro=? " 
                                               + "WHERE IdFalta=?");
            
            // Parameters start with 1
            preparedStatement.setLong(1, falta.getIdAluno());
            preparedStatement.setDate(2, new java.sql.Date(falta.getDataCadastro().getTime()));
            
            preparedStatement.setLong(3, falta.getIdFalta());
            
            preparedStatement.executeUpdate();
            falta.setNumeroFaltas(getNumFaltas(falta.getIdAluno()));
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } //updateNotas
    
    public void deleteFalta(long idFalta) {
        try {
            
        	PreparedStatement preparedStatement = connection
                    .prepareStatement("DELETE FROM Faltas WHERE idFalta=?");
            
            // Parameters start with 1
            preparedStatement.setLong(1, idFalta);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    } //deleteFalta
    
    public void deleteAllFaltas(long idAluno) {
        try {
            
        	PreparedStatement preparedStatement = connection
                    .prepareStatement("DELETE FROM Faltas WHERE idAluno=?");
            
            // Parameters start with 1
            preparedStatement.setLong(1, idAluno);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ArithmeticException("FaltasDao: deleteAllFaltas: " + e.getMessage()); 
        }
    } //deleteFalta
	
	public List<Faltas> getAllFaltas() {
        
    	List<Faltas> faltas = new ArrayList<Faltas>();
        
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Faltas");
            while (rs.next()) {
                
            	Faltas falta = new Faltas();
            	
            	falta.setIdFalta(rs.getLong("IdFalta"));
            	falta.setIdAluno(rs.getLong("IdAluno"));
            	falta.setDataCadastro(rs.getDate("DataCadastro"));            	
            	falta.setNumeroFaltas(getNumFaltas(falta.getIdAluno()));

            	faltas.add(falta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return faltas;
    } //getAllAlunos

    public List<Faltas> getFaltasAluno(long idAluno) {

    	List<Faltas> faltas = new ArrayList<Faltas>();
        
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Faltas WHERE IdAluno = " + idAluno);
            while (rs.next()) {
                
            	Faltas falta = new Faltas();
            	
            	falta.setIdFalta(rs.getLong("IdFalta"));
            	falta.setIdAluno(rs.getLong("IdAluno"));            	
            	falta.setNumeroFaltas(getNumFaltas(falta.getIdAluno()));            	
            	falta.setDataCadastro(rs.getDate("DataCadastro"));

            	faltas.add(falta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ArithmeticException("FaltasDao: getFaltasAluno: " + e.getMessage()); 
        }

        return faltas;
    } //getFaltasAluno    
    
    public Faltas getFaltaId(long id) {

    	Faltas faltas = new Faltas();
        
    	try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("SELECT * from Faltas WHERE IdFalta=?");
            
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {            	
            	faltas.setIdAluno(rs.getLong("IdAluno"));
            	faltas.setIdFalta(rs.getLong("IdFalta"));
            	faltas.setDataCadastro(rs.getDate("DataCadastro"));            	
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return faltas;
    }
}