using System;
using System.Collections.Generic;
using System.Text;
using MySql.Data.MySqlClient;
using System.Data;

namespace rotte_cinema
{
    class Manager
    {
        private static MySqlConnection connection = new MySqlConnection("Server = db.kumas.dev; port=3306; database=Cinema; uid=user; pwd=0000");

        //Command - 영향받은 행 개수 반환
        public int Input(string sql) {
            connection.Open();
            MySqlCommand input = new MySqlCommand(sql, connection);
            connection.Close();
            return input.ExecuteNonQuery();
        }

        public int Delete(string sql)
        {
            connection.Open();
            MySqlCommand delete = new MySqlCommand(sql, connection);
            connection.Close();
            return delete.ExecuteNonQuery();
        }


        public int Insert(string sql)
        {
            connection.Open();
            MySqlCommand insert = new MySqlCommand(sql, connection);
            connection.Close();
            return insert.ExecuteNonQuery();
        }


        public int Update(string sql)
        {
            connection.Open();
            MySqlCommand update = new MySqlCommand(sql, connection);
            connection.Close();
            return update.ExecuteNonQuery();
        }

        public int Select(string sql)
        {
            connection.Open();
            MySqlCommand select = new MySqlCommand(sql, connection);
            connection.Close();
            return select.ExecuteNonQuery();
        }

        public DataTable AdapterRead(string sql) {
            DataTable dt = new DataTable();
            connection.Open();
            MySqlDataAdapter adapter = new MySqlDataAdapter(sql, connection);
            adapter.Fill(dt);
            connection.Close();
            return dt;
        }


    }

}
