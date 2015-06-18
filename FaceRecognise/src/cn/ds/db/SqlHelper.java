/**
 * Project             :FaceRecognise project
 * Comments            :操作数据库
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-3-22 | 创建 | jxm 
 */
package cn.ds.db;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class SqlHelper {
	// 定义变量
	private static Connection ct = null;
	private static PreparedStatement ps = null;
	private static ResultSet rs = null;

	// 连接数据库的参数
	private static String url = "";
	private static String username = "";
	private static String driver = "";
	private static String passwd = "";
	private static Properties pp = null;
	private static InputStream fis = null;

	// 加载驱动，只需要一次，用静态代码块
	static {
		try {
			pp = new Properties();
			// fis = SqlHelper.class.getClassLoader().getResourceAsStream(
			// "oracle.properties");
			fis = new FileInputStream("dat/oracle.properties");
			pp.load(fis);
			url = pp.getProperty("url");
			username = pp.getProperty("username");
			driver = pp.getProperty("driver");
			passwd = pp.getProperty("passwd");
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			fis = null;// 垃圾回收站来清理
		}

	}

	/**
	 * Description :得到连接
	 * 
	 * @return Connection
	 */
	public Connection getConnection() {
		try {
			ct = DriverManager.getConnection(url, username, passwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ct;
	}

	/**
	 * Description :关闭资源
	 */
	public void close(ResultSet rs, Statement ps, Connection ct) {
		// 关闭资源(先开后关)
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ps = null;
		}
		if (null != ct) {
			try {
				ct.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ct = null;
		}
	}

	/**
	 * Description :执行增，删，改操作
	 * 
	 * @param sql
	 *            :sql语句
	 * @param parameters
	 *            : 参数列表
	 * @return boolean:执行结果
	 */
	public boolean executeUpdate(String sql, String[] parameters) {
		boolean flag = false;
		try {
			// 取消自动提交功能
			// ct.setAutoCommit(false);

			ct = getConnection();
			ps = ct.prepareStatement(sql);

			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i + 1, parameters[i]);
				}

			}
			if (ps.executeUpdate() >= 1) {
				flag = true;
			}
			// 提交
			// ct.commit();
			// ct.setAutoCommit(true);// 恢复现场
		} catch (Exception e) {
			try {
				// 回滚
				// ct.rollback();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();// 开发阶段
			// 抛出异常
			// 可以处理，也可以不处理
			throw new RuntimeException(e.getMessage());
		} finally {
			close(rs, ps, ct);
		}
		return flag;
	}

	public void batchexecute(String sql, String[][] parameters) {
		try {

			ct = getConnection();
			ps = ct.prepareStatement(sql);

			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					String[] para = parameters[i]; 
					for(int j = 0;j < para.length;j++){
						ps.setString(j + 1, para[j]);	
					}
					ps.addBatch();
				}

			}
			ps.executeBatch();	

		} catch (Exception e) {
			try {
				// 回滚
				// ct.rollback();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();// 开发阶段
			// 抛出异常
			// 可以处理，也可以不处理
			throw new RuntimeException(e.getMessage());
		} finally {
			close(rs, ps, ct);
		}
	}

	/**
	 * Description :执行查询操作
	 * 
	 * @param sql
	 *            :sql语句
	 * @param parameters
	 *            : 参数列表
	 * @return ArrayList<Object[]>:返回查询结果
	 */
	public ArrayList<Object[]> queryExecute(String sql, String[] parameters) {
		ArrayList<Object[]> arrayList = new ArrayList<Object[]>();
		try {
			ct = getConnection();
			ps = ct.prepareStatement(sql);
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i + 1, parameters[i]);
				}
			}
			rs = ps.executeQuery();
			ResultSetMetaData resultSetMetaData = rs.getMetaData();
			int colum = resultSetMetaData.getColumnCount();
			while (rs.next()) {
				Object[] object = new Object[colum];
				for (int i = 0; i < colum; i++) {
					object[i] = rs.getObject(i + 1);
				}
				arrayList.add(object);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rs, ps, ct);
		}
		return arrayList;
	}

}
