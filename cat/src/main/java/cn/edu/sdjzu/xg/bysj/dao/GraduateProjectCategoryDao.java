package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.GraduateProject;
import cn.edu.sdjzu.xg.bysj.domain.GraduateProjectCategory;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public final class GraduateProjectCategoryDao {
	//本类的一个对象引用，保存自身对象
	private static GraduateProjectCategoryDao graduateProjectCategoryDao = null;
	//私有的构造方法，防止其它类创建它的对象
	private GraduateProjectCategoryDao(){}
	//静态方法，返回本类的惟一对象
	public synchronized static GraduateProjectCategoryDao getInstance() {
		return graduateProjectCategoryDao == null ? new GraduateProjectCategoryDao()
				: graduateProjectCategoryDao;
	}

	private static Collection<GraduateProjectCategory> graduateProjectCategories;
	static {
		graduateProjectCategories = new TreeSet<GraduateProjectCategory>();
		GraduateProjectCategory design = new GraduateProjectCategory(1, "设计", "01", "");
		graduateProjectCategories.add(design);
		graduateProjectCategories.add(new GraduateProjectCategory(2, "论文", "02", ""));
	}

	public Set<GraduateProjectCategory> findAll() throws SQLException {
		//创建degrees集合对象
		Set<GraduateProjectCategory> graduateProjectCategories = new HashSet<GraduateProjectCategory>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		Statement statement = connection.createStatement();
		//创建结果集对象，并执行语句盒子对象的
		ResultSet resultSet = statement.executeQuery("select * from graduateprojectcategory");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			//以当前记录中的id,description,no,remarks值为参数，创建Degree对象
			GraduateProjectCategory graduateProjectCategory = new GraduateProjectCategory(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
			//向degrees集合中添加Degree对象
			graduateProjectCategories.add(graduateProjectCategory);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		return graduateProjectCategories;
	}
	public GraduateProjectCategory find(Integer id) throws SQLException{
		GraduateProjectCategory graduateProjectCategory = null;
		Connection connection = JdbcHelper.getConn();
		String findCategory_sql = "SELECT * FROM graduateprojectcategory WHERE id=?";
		//在该连接上创建预编译语句对象，参数为String类型的sql语句
		PreparedStatement preparedStatement = connection.prepareStatement(findCategory_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		//创建结果集对象，执行预编译语句对象
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			graduateProjectCategory = new GraduateProjectCategory(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return graduateProjectCategory;
	}
	public boolean update(GraduateProjectCategory graduateProjectCategory) throws ClassNotFoundException,SQLException{
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateCategory_sql = " update graduateprojectcategory set description=?,no=?,remarks=?where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateDegree_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,degree.getDescription());
		preparedStatement.setString(2,degree.getNo());
		preparedStatement.setString(3,degree.getRemarks());
		preparedStatement.setInt(4,degree.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}
	public boolean add(GraduateProjectCategory graduateProjectCategory) {
		return graduateProjectCategories.add(graduateProjectCategory);
	}

	public boolean delete(Integer id) {
		GraduateProjectCategory graduateProjectCategory = this.find(id);
		return this.delete(graduateProjectCategory);
	}

	public boolean delete(GraduateProjectCategory graduateProjectCategory) {
		return graduateProjectCategories.remove(graduateProjectCategory);
	}

	public static void main(String[] args) {
		GraduateProjectCategoryDao dao = new GraduateProjectCategoryDao();
		Collection<GraduateProjectCategory> graduateProjectCategories = dao
				.findAll();
	}
}
