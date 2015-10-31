package com.hall4.commons.db;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author <a href="http://es.linkedin.com/in/miguelcabo">Miguel Cabo Diez -
 *         DTIC</a> Email: <a
 *         href="mailto:miguelcabodiez@gmail.com">miguelcabodiez@gmail.com</a>
 * @version 2010.1010
 **/
public interface IDataBase {
	/**
	 * Se obtiene la existencia o no de una consulta.
	 * 
	 * @return booleano con valor 1 si existe la consulta y 0 en caso contrario.
	 */
	public boolean getExistDB();

	/**
	 * Se obtiene la consulta.
	 * 
	 * @return ArrayList de HastaMaps con el resultado de la consulta. En cada
	 *         HashMap se encuentran los atributos de la consulta.
	 */
	public ArrayList<HashMap<String, String>> getSelectDB();

	/**
	 * Se obtiene el número de ocurrencias de una consulta
	 * 
	 * @return un entero con el número de ocurrencias de la consulta.
	 */
	public int getCountDB();

	/**
	 * Se obtiene la consulta.
	 * 
	 * @return ArrayList de Hastables con el resultado de la consulta. En cada
	 *         Hastable se encuentran los atributos de la consulta.
	 */
	public int setInsertDB();

	/**
	 * Actualiza la consulta.
	 * 
	 * @return int Retorna el estado de la actualización de datos en la tabla, 1
	 *         si todo ok, 0 si no se ha podido insertar.
	 */
	public int setUpdateDB();

	/**
	 * Fija el tipo de los datos del statement y el valor.
	 * 
	 * @param _tipo
	 *            Especificacion del tipo de dato: I -> Entero. S -> String. C
	 *            -> Char.
	 * @param _valor
	 *            Especificacion del valor del dato.
	 */
	public void setDatoStatement(String _tipo, String _valor);

	/**
	 * Fija el nombre de la tabla.
	 * 
	 * @param _tabla
	 *            Especificación del nombre de la tabla.
	 */
	public void setTabla(String _tabla);

	/**
	 * Fija la consulta Mysql.
	 * 
	 * @param _query
	 *            Especificacion de la consulta:
	 *            "WHERE id = ? or id = ? or id = ? order by id"
	 */
	public void setQuery(String _query);

	/**
	 * Fija los campos de la consulta.
	 * 
	 * @param _values
	 *            Nombre de los campos separados por comas: "A, B, C,"
	 */
	public void setValues(String _values);
}
