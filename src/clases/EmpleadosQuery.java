
package clases;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.math.BigDecimal;







public class EmpleadosQuery {

	private Connection myConn;
	
	public EmpleadosQuery() throws Exception {
		
            
            
            
            
            String user = "root";
		String password = "";
		String dburl = "jdbc:mysql://localhost:3306/trabajadoresBD";
		
		// connect to database
		myConn = DriverManager.getConnection(dburl, user, password);
		
		System.out.println("Conexión existosa a: " + dburl);
	}
        
        public void addEmpleado(Empleado nuevoEmpleado) throws Exception{
            PreparedStatement myStnt =null;
            try{
                myStnt = myConn.prepareStatement("insert into empleados (first_name,last_name,email,salary) " +
                        "values (?,? ,?,?");
                myStnt.setString(1,nuevoEmpleado.getFirstName());
                myStnt.setString(2,nuevoEmpleado.getLastName());
                myStnt.setString(3,nuevoEmpleado.getEmail());
                myStnt.setBigDecimal(4,nuevoEmpleado.getSalary());
                myStnt.executeUpdate();
            }finally{
                
            }
        }
        
        public void deleteEmployee(String email) throws SQLException {
		PreparedStatement myStmt = null;

		try {
			// prepare statement
			myStmt = myConn.prepareStatement("delete from employees where email=?");
			
			// set param
			myStmt.setString(1, email);
			
			// execute SQL
			myStmt.executeUpdate();			
		}
		finally {
			System.out.print("Todo bien");
		}
	}
	
	public List<Empleado> getAllEmployees() throws Exception {
		List<Empleado> list = new ArrayList<>();
		
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select * from empleados");
			
			while (myRs.next()) {
				Empleado tempEmployee = EmpleadoConvertido(myRs);
				list.add(tempEmployee);
			}

			return list;		
		}
		finally {
			close(myStmt, myRs);
		}
	}
        public void updateEmployee(Empleado theEmployee) throws SQLException {
		PreparedStatement myStmt = null;

		try {
			// prepare statement
			myStmt = myConn.prepareStatement("update employees"
					+ " set first_name=?, last_name=?, email=?, salary=?"
					+ " where email=?");
			
			// set params
			myStmt.setString(1, theEmployee.getFirstName());
			myStmt.setString(2, theEmployee.getLastName());
			myStmt.setString(3, theEmployee.getEmail());
			myStmt.setBigDecimal(4, theEmployee.getSalary());
			
			
			// execute SQL
			myStmt.executeUpdate();			
		}
		finally {
			System.out.print("Todo bien");
		}
		
	}
	
	public List<Empleado> searchEmployees(String lastName) throws Exception {
		List<Empleado> list = new ArrayList<>();

		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		try {
			lastName += "%";
			myStmt = myConn.prepareStatement("select * from employees where last_name like ?");
			
			myStmt.setString(1, lastName);
			
			myRs = myStmt.executeQuery();
			
			while (myRs.next()) {
				Empleado tempEmployee = EmpleadoConvertido(myRs);
				list.add(tempEmployee);
			}
			
			return list;
		}
		finally {
			close(myStmt, myRs);
		}
	}
	
	private Empleado EmpleadoConvertido(ResultSet myRs) throws SQLException {
		
		
		String lastName = myRs.getString("last_name");
		String firstName = myRs.getString("first_name");
		String email = myRs.getString("email");
		BigDecimal salary = myRs.getBigDecimal("salary");
		
		Empleado tempEmployee = new Empleado(lastName, firstName, email, salary);
		
		return tempEmployee;
	}

	
	private static void close(Connection myConn, Statement myStmt, ResultSet myRs)
			throws SQLException {

		if (myRs != null) {
			myRs.close();
		}

		if (myStmt != null) {
			
		}
		
		if (myConn != null) {
			myConn.close();
		}
	}

	private void close(Statement myStmt, ResultSet myRs) throws SQLException {
		close(null, myStmt, myRs);		
	}

	public static void main(String[] args) throws Exception {
		
		EmpleadosQuery emps = new EmpleadosQuery();
		System.out.println(emps.searchEmployees("thom"));

		System.out.println(emps.getAllEmployees());
	}
}
