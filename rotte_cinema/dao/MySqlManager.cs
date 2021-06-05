using MySql.Data.MySqlClient;
using System.Data;
using System.Xml;

namespace rotte_cinema.dao
{
	class MySqlManager
	{
		private static MySqlConnection connection;

		public static void setConnection()
		{
			string ip = string.Empty;
			string port = string.Empty;
			string database = string.Empty;
			string username = string.Empty;
			string password = string.Empty;

			XmlDocument xml = new XmlDocument();
			xml.Load(System.Environment.CurrentDirectory + @"\config.xml");
			XmlNodeList xmlList = xml.SelectNodes("/config");
			foreach (XmlNode xnl in xmlList)
			{
				ip = xnl["IP"].InnerText;
				port = xnl["PORT"].InnerText;
				database = xnl["DATABASE"].InnerText;
				username = xnl["USERNAME"].InnerText;
				password = xnl["PASSWORD"].InnerText;
			}

			/* xml에서 접속정보를 읽어와 DB 세팅 */

			connection = new MySqlConnection(string.Format("Server = {0}; port = {1}; database = {2}; uid = {3}; pwd = {4}", ip, port, database, username, password));

		}

		public static int Input(string sql)
		{
			using (MySqlCommand command = new MySqlCommand(sql, connection))
			{
				connection.Open();
				int result = command.ExecuteNonQuery(); /* ExecuteNonQuery 리턴값은 영향을 받은 행의 갯수 롤백이 발생한다면 -1을 리턴 */
				connection.Close();
				return result;
			}
		}

		public static int Delete(string sql)
		{
			using (MySqlCommand command = new MySqlCommand(sql, connection))
			{
				connection.Open();
				int result = command.ExecuteNonQuery(); /* ExecuteNonQuery 리턴값은 영향을 받은 행의 갯수 롤백이 발생한다면 -1을 리턴 */
				connection.Close();
				return result;
			}
		}


		public static int Insert(string sql)
		{
			using (MySqlCommand command = new MySqlCommand(sql, connection))
			{
				connection.Open();
				int result = command.ExecuteNonQuery(); /* ExecuteNonQuery 리턴값은 영향을 받은 행의 갯수 롤백이 발생한다면 -1을 리턴 */
				connection.Close();
				return result;
			}
		}


		public static int Update(string sql)
		{
			using (MySqlCommand command = new MySqlCommand(sql, connection))
			{
				connection.Open();
				int result = command.ExecuteNonQuery(); /* ExecuteNonQuery 리턴값은 영향을 받은 행의 갯수 롤백이 발생한다면 -1을 리턴 */
				connection.Close();
				return result;
			}
		}

		public static object Select(string sql)
		{
			using (MySqlCommand command = new MySqlCommand(sql, connection))
			{
				connection.Open();
				object result = command.ExecuteScalar(); /* ExecuteScalar 결과값 하나만을 object 형식으로 반환 */
				connection.Close();
				return result;
			}
		}

		public static DataTable AdapterRead(string sql)
		{
			DataTable dt = new DataTable();
			MySqlDataAdapter adapter = new MySqlDataAdapter(sql, connection); /* 데이터 테이블 형식으로 반환. */
			adapter.Fill(dt);
			return dt;
		}
	}
}
