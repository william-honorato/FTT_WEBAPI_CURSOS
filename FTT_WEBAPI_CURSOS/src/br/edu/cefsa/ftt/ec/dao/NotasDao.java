package br.edu.cefsa.ftt.ec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.edu.cefsa.ftt.ec.model.Alunos;
import br.edu.cefsa.ftt.ec.model.Faltas;
import br.edu.cefsa.ftt.ec.model.Notas;
import br.edu.cefsa.ftt.util.DbUtil;

public class NotasDao {
	
	private Connection connection;
	
	public NotasDao() {
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
			
    public void addNota(Notas nota) {
        
    	try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO Notas (IdAluno, Nota, DataCadastro) VALUES (?, ?, ?)");
            
            // Parameters start with 1
            preparedStatement.setLong(1, nota.getIdAluno());
            preparedStatement.setFloat(2, nota.getNota());
            preparedStatement.setDate(3, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
            
            preparedStatement.executeUpdate();
            nota.setIdNota(getLastId());

        } catch (SQLException e) {
            e.printStackTrace();
            
            throw new ArithmeticException("NotasDao: addNota: " + e.getMessage()); 
        }
    } //addFalta

    public void deleteNota(long idNota) {
        try {
            
        	PreparedStatement preparedStatement = connection
                    .prepareStatement("DELETE FROM Notas WHERE idFalta=?");
            
            // Parameters start with 1
            preparedStatement.setLong(1, idNota);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    } //deleteNota
    
    public void deleteAllNotas(long idAluno) {
        try {
            
        	PreparedStatement preparedStatement = connection
                    .prepareStatement("DELETE FROM Notas WHERE idAluno=?");
            
            // Parameters start with 1
            preparedStatement.setLong(1, idAluno);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
    public void updateNotas(Notas nota) {
        try {        	
            PreparedStatement preparedStatement = connection
                    .prepareStatement("UPDATE Notas SET IdAluno=?, " 
                    		                          + "Nota=? "
                                               + "WHERE IdNota=?");
            
            // Parameters start with 1
            preparedStatement.setLong(1, nota.getIdAluno());
            preparedStatement.setFloat(2, nota.getNota());
            
            preparedStatement.setLong(3, nota.getIdNota());
            
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    } //updateNotas
    
	public List<Notas> getAllNotas() {
        
    	List<Notas> notas = new ArrayList<Notas>();
        
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Notas");
            while (rs.next()) {
                
            	Notas nota = new Notas();
            	
            	nota.setIdNota(rs.getLong("IdNota"));
            	nota.setIdAluno(rs.getLong("IdAluno"));
            	nota.setNota(rs.getLong("Nota"));
            	nota.setDataCadastro(rs.getDate("DataCadastro"));

            	notas.add(nota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notas;
    } //getAllNotas

    public List<Notas> getNotasAluno(long idAluno) {

    	List<Notas> notas = new ArrayList<Notas>();
        
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Notas WHERE IdAluno = " + idAluno);
            while (rs.next()) {
                
            	Notas nota = new Notas();
            	
            	nota.setIdNota(rs.getLong("IdNota"));
            	nota.setIdAluno(rs.getLong("IdAluno"));
            	nota.setNota(rs.getLong("Nota"));
            	nota.setDataCadastro(rs.getDate("DataCadastro"));

            	notas.add(nota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notas;
    } //getNotaId
    
    public Notas getNotaId(long id) {

    	Notas nota = new Notas();
        
    	try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("SELECT * FROM Notas WHERE idNota=?");
            
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {            	
            	nota.setIdAluno(rs.getLong("IdAluno"));
            	nota.setIdNota(rs.getLong("idNota"));
            	nota.setDataCadastro(rs.getDate("DataCadastro"));            	
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nota;
    }
}
