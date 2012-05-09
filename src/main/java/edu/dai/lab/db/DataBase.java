package edu.dai.lab.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import edu.dai.lab.db.IDataBase;

/**
 * @author <a href="http://es.linkedin.com/in/miguelcabo">Miguel Cabo Diez -
 *         DTIC</a> Email: <a
 *         href="mailto:miguelcabodiez@gmail.com">miguelcabodiez@gmail.com</a>
 * @version 2010.1010
 *          <p>
 * 
 *          Esta clase implementa los métodos necesarios para acceder a la base
 *          de datos.
 */
public class DataBase extends Thread implements IDataBase
{
	private static String USERNAME;
	private static String PASSWORD;
	private static String DBNAME;
	
	public static void init(String username, String password, String dbName) {
		USERNAME = username;
		PASSWORD = password;
		DBNAME = dbName;
	}
	
	private Connection conn = null;
	private ArrayList<HashMap<String, String>> estruc;
	private String consultaMysql;
	private String tabla;
	private ArrayList<String> setSt;
	private String parametro;

	/**
	 * <p>
	 * Constructor genérico de la clase; carga el Driver de Mysql e inicialiaza
	 * las variables.
	 * 
	 */
	public DataBase() {
		estruc = new ArrayList<HashMap<String, String>>();
		setSt = new ArrayList<String>();
		consultaMysql = null;
		tabla = null;
		parametro = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}

	}

	/**
	 * Se obtiene el número de ocurrencias de una consulta
	 * 
	 * @return un entero con el número de ocurrencias de la consulta.
	 */
	@Override
	public int getCountDB() {
		return this.dbCount(tabla, consultaMysql, parametro, setSt);
	}

	/**
	 * Se obtiene la existencia o no de una consulta.
	 * 
	 * @return booleano con valor 1 si existe la consulta y 0 en caso contrario.
	 */
	@Override
	public boolean getExistDB() {
		return this.dbExist(tabla, consultaMysql, parametro, setSt);
	}

	/**
	 * Se obtiene la consulta.
	 * 
	 * @return ArrayList de Hastables con el resultado de la consulta. En cada
	 *         Hastable se encuentran los atributos de la consulta.
	 */
	@Override
	public ArrayList<HashMap<String, String>> getSelectDB() {
		this.conexionDB();
		ResultSet rs = null;
		this.estruc.clear();
		rs = dbSelect(tabla, consultaMysql, parametro, setSt);
		try {
			ResultSetMetaData rm = rs.getMetaData();
			int numeroDeColumnas = rm.getColumnCount();
			// HashMap<String,String> tmpDic = new HashMap<String,String>();
			while (rs.next()) {
				HashMap<String, String> tmpDic = new HashMap<String, String>();
				for (int i = 1; i <= numeroDeColumnas; i++)
					tmpDic.put(rm.getColumnLabel(i),
							rs.getString(rm.getColumnLabel(i)));
				@SuppressWarnings("unused")
				boolean add = this.estruc.add(tmpDic);
			}
		} catch (SQLException e) {
		}

		this.closeDB();

		return this.estruc;
	}

	/**
	 * Inserta obtiene la consulta.
	 * 
	 * @return int Retorna el estado de la insercion de datos en la tabla, 1 si
	 *         todo ok, 0 si no se ha podido insertar.
	 */
	@Override
	public int setInsertDB() {
		return this.dbInsert(tabla, consultaMysql, setSt);
	}

	/**
	 * Actualiza la consulta.
	 * 
	 * @return int Retorna el estado de la actualización de datos en la tabla, 1
	 *         si todo ok, 0 si no se ha podido insertar.
	 */
	@Override
	public int setUpdateDB() {
		return dbUpdate(tabla, consultaMysql, parametro, setSt);
	}

	/**
	 * Fija el tipo de los datos del statement y el valor.
	 * 
	 * @param _tipo
	 *            Especificacion del tipo de dato: I -> Entero. S -> String. C
	 *            -> Char.
	 * @param _valor
	 *            Especificacion del valor del dato.
	 */
	@Override
	public void setDatoStatement(String _tipo, String _valor) {
		setSt.add(_tipo);
		setSt.add(_valor);
	}

	/**
	 * Fija el nombre de la tabla.
	 * 
	 * @param _tabla
	 *            Especificación del nombre de la tabla.
	 */
	@Override
	public void setTabla(String _tabla) {
		tabla = _tabla;
	}

	/**
	 * Fija la consulta Mysql.
	 * 
	 * @param _query
	 *            Especificacion de la consulta:
	 *            "WHERE id = ? or id = ? or id = ? order by id"
	 */
	@Override
	public void setQuery(String _query) {
		consultaMysql = _query;
	}

	/**
	 * Fija los campos de la consulta.
	 * 
	 * @param _values
	 *            Nombre de los campos separados por comas: "A, B, C,"
	 */
	@Override
	public void setValues(String _values) {
		parametro = _values;
	}

	/**
	 * Preparación de la conexión con la base de datos.
	 * 
	 */
	private void conexionDB() {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/" + DBNAME, USERNAME, PASSWORD);
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException on DataBase:conexionDB: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	/**
	 * Cierra la conexion con la base de datos.
	 * 
	 */
	private void closeDB() {
		try {
			conn.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		} finally {
			conn = null;
		}
	}

	/**
	 * Preparación del Statement genérico.
	 * 
	 * @param ps
	 *            Nombre del prepareStatement.
	 * @param _tipo
	 *            Tipo de dato del atributo de la tabla.
	 * @param __pos
	 *            Posicion que ocupa el atributo en la consulta.
	 * @param _valor
	 *            Dato que se le asigna al atributo de la tabla.
	 */
	private void set(PreparedStatement ps, char _tipo, int _pos, String _valor) {
		if (_tipo == 'S') {
			try {
				ps.setString(_pos, _valor);
			} catch (SQLException e) {
			}
		}
		if (_tipo == 'I') {
			try {
				ps.setInt(_pos, Integer.parseInt(_valor));
			} catch (SQLException e) {
			}
		}
		if (_tipo == 'C') {
			try {
				ps.setString(_pos, _valor);
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * Nos informa si existe o no una condicion. Retorna un valor booleano
	 * diciendo si el elemento existe o no.
	 * 
	 * @param _tabla
	 *            Nombre de la tabla.
	 * @param _sentencia
	 *            Formulacion de la sentencia.
	 * @param _parametro
	 *            Nombre del parametro.
	 * @param _set
	 *            Array de tipo-valor de los parametros para realizar el
	 *            prepareStatement.
	 * @return Retorna un valor booleano; 1 si existe, 0 si no existe.
	 */

	private boolean dbExist(String _tabla, String _sentencia,
			String _parametro, ArrayList<String> _set) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Statement st = null;
		boolean retorno = false;
		this.conexionDB();
		try {
			String tsql = "SELECT distinct count(" + _parametro
					+ ") as res FROM " + _tabla + " " + _sentencia;

			if (_set.isEmpty() == true) {
				st = conn.createStatement();
				rs = st.executeQuery(tsql);
			} else {
				int div = _set.size() >> 1;
				ps = conn.prepareStatement(tsql);
				for (int i = 0; i < div; i++) {
					this.set(ps, _set.get(i * 2).charAt(0), i + 1,
							_set.get(i * 2 + 1));
				}
				rs = ps.executeQuery();
			}
			if (rs != null) {
				rs.next();
				if (Integer.parseInt(rs.getString("res")) > 0)

					retorno = true;

			}
		} catch (SQLException ex) {
			System.out.println("SQLException on DataBase:dbExist: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		this.closeDB();
		return retorno;
	}

	/**
	 * Retorna el numero de filas de una tabla segun una condicion. Si la tabla
	 * o condicion esta vacia retorna 0.
	 * 
	 * @param _tabla
	 *            Nombre de la tabla.
	 * @param _sentencia
	 *            Formulacion de la sentencia.
	 * @param _parametro
	 *            Nombre del parametro.
	 * @param _set
	 *            Array de tipo-valor de los parametros para realizar el
	 *            prepareStatement.
	 * @return Retorna el numero de filas de la tabla en un Resulset, 0 si esta
	 *         vacia.
	 */
	private int dbCount(String _tabla, String _sentencia, String _parametro,
			ArrayList<String> _set) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Statement st = null;
		int retorno = 0;
		this.conexionDB();
		try {
			String tsql = "SELECT distinct count(" + _parametro
					+ ") as total FROM " + _tabla + " " + _sentencia;

			if (_set.isEmpty() == true) {
				st = conn.createStatement();
				rs = st.executeQuery(tsql);
			} else {
				int div = _set.size() >> 1;
				ps = conn.prepareStatement(tsql);
				for (int i = 0; i < div; i++) {
					this.set(ps, _set.get(i * 2).charAt(0), i + 1,
							_set.get(i * 2 + 1));
				}
				rs = ps.executeQuery();

			}
			if (rs != null) {
				rs.next();
				retorno = Integer.parseInt(rs.getString("total"));
			}
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		this.closeDB();
		return retorno;
	}

	/**
	 * Realiza una consulta Sql y la retorna mediante un Resulset. Si la tabla o
	 * condiciin esta vacia retorna null.
	 * 
	 * @param _tabla
	 *            Nombre de la tabla.
	 * @param _sentencia
	 *            Formulacion de la sentencia.
	 * @param _parametro
	 *            Nombre del parametro.
	 * @param _set
	 *            Array de tipo-valor de los parametros para realizar el
	 *            prepareStatement.
	 * @return Retorna el resutlado de la consulta en un resulset, null si esta
	 *         vacia.
	 */
	private ResultSet dbSelect(String _tabla, String _sentencia,
			String _parametros, ArrayList<String> _set) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			String tsql = "SELECT " + _parametros + " FROM " + _tabla + " "
					+ _sentencia;
			//System.out.println("SELECT: "+tsql);
			if (_set.isEmpty() == true) {
				st = conn.createStatement();
				rs = st.executeQuery(tsql);
			} else {
				int div = _set.size() >> 1;
				ps = conn.prepareStatement(tsql);
				for (int i = 0; i < div; i++) {
					this.set(ps, _set.get(i * 2).charAt(0), i + 1,
							_set.get(i * 2 + 1));
				}
				rs = ps.executeQuery();
			}

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return rs;
	}

	/**
	 * Inserta datos en una tabla Sql.
	 * 
	 * @param _tabla
	 *            Nombre de la tabla.
	 * @param _parametro
	 *            Nombre del parametro.
	 * @param _set
	 *            Array de tipo-valor de los parametros para realizar el
	 *            prepareStatement.
	 * @return Retorna el estado de la insercion de datos en la tabla, 1 si todo
	 *         ok, 0 si no se ha podido insertar.
	 */
	private int dbInsert(String _tabla, String _parametros,
			ArrayList<String> _set)
	{
		PreparedStatement ps = null;
		int res = 0;
		Statement st = null;
		this.conexionDB();
		try {
			String tsql = "INSERT INTO " + _tabla + " SET " + _parametros;
			
			//System.out.println("SQL QUERY: " + tsql);

			if (_set.isEmpty() == true) {
				st = conn.createStatement();
				res = st.executeUpdate(tsql);
			} else {
				int div = _set.size() >> 1;
				ps = conn.prepareStatement(tsql);
				for (int i = 0; i < div; i++) {
					this.set(ps, _set.get(i * 2).charAt(0), i + 1,
							_set.get(i * 2 + 1));
				}
				res = ps.executeUpdate();
			}

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		this.closeDB();
		return res;
	}

	/**
	 * Actualiza datos en una tabla Sql.
	 * 
	 * @param _tabla
	 *            Nombre de la tabla.
	 * @param _sentencia
	 *            Formulacion de la sentencia.
	 * @param _parametro
	 *            Nombre del parametro.
	 * @param _set
	 *            Array de tipo-valor de los parametros para realizar el
	 *            prepareStatement.
	 * @return Retorna el estado de la insercion de datos en la tabla, 1 si todo
	 *         ok, 0 si no se ha podido insertar.
	 */
	private int dbUpdate(String _tabla, String _sentencia, String _parametros,
			ArrayList<String> _set) {
		PreparedStatement ps = null;
		int res = 0;
		Statement st = null;
		this.conexionDB();
		try {
			String tsql = "UPDATE " + _tabla + " SET " + _parametros
					+ " WHERE " + _sentencia;
			
			//System.out.println("SQL QUERY: " + tsql);
			
			if (_set.isEmpty() == true) {
				st = conn.createStatement();
				res = st.executeUpdate(tsql);
			} else {
				int div = _set.size() >> 1;
				ps = conn.prepareStatement(tsql);
				for (int i = 0; i < div; i++) {
					this.set(ps, _set.get(i * 2).charAt(0), i + 1,
							_set.get(i * 2 + 1));
				}
				res = ps.executeUpdate();
			}

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		this.closeDB();
		return res;
	}
}
